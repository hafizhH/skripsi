package com.skripsi.app.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.cloudsimplus.brokers.DatacenterBrokerAbstract;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletExecution;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.core.ExecDelayable;
import org.cloudsimplus.core.events.SimEvent;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.schedulers.cloudlet.CloudletScheduler;
import org.cloudsimplus.util.TimeUtil;
import org.cloudsimplus.vms.Vm;

import com.skripsi.app.utils.Vector;

public class WOA_Broker extends DatacenterBrokerAbstract {
  private WOA woa;

  private int lastSelectedVmIndex;
  private int lastSelectedDcIndex;
  private int triedDatacenters;

  protected Cloudlet currentCloudlet;
  protected List<Vm> targetVmList;
  protected int selectedVmIndex;
  protected List<List<Double>> bestFitnessHistory;

  public WOA_Broker(CloudSimPlus sim, String name, int maxIteration, int agentNum) {
    super(sim, name);
    this.lastSelectedVmIndex = -1;
    this.lastSelectedDcIndex = -1;
    this.selectedVmIndex = -1;
    this.woa = new WOA(maxIteration, agentNum);
    this.bestFitnessHistory = new ArrayList<List<Double>>();
  }

  private void executeWOA(Cloudlet cl, Function<Integer, Double> fitnessFunction) {
    // System.out.println("\nStarting cloudlet " + cl.getId());
    this.currentCloudlet = cl;
    woa.init(new double[]{ (double)this.targetVmList.size()-1 }, fitnessFunction);
    woa.run();
    List<Map.Entry<Vector, Double>> results = woa.getResults();
    int selIndex = (int) Math.round(results.get(0).getKey().getElm(0));
    this.selectedVmIndex = selIndex;
    this.bestFitnessHistory.add(woa.getBestFitnessHistory());

    // System.out.print("Cloudlet " + cl.getId() + "\t: ");
    // for (int i = 0; i < results.size(); i++) {
    //   System.out.print(String.format("(%2.2f,%2.2f) ", results.get(i).getKey().getElm(0), results.get(i).getValue()));
    // }
    return;
  }

  public Double calculateFitness(Integer vmIndex) {
    Cloudlet clNew = currentCloudlet;
    List<Vm> vmList = this.targetVmList;
    double makespan = 0.0;
    double compTimeSum = 0.0;
    List<Cloudlet> cloudlets = getCloudletSubmittedList();
    double[] vmCompTimeSum = new double[vmList.size()];
    
    // Calculate VM Completion Time
    for (int i = 0; i < cloudlets.size(); i++) {
      Cloudlet cl = cloudlets.get(i);
      if (!cl.isBoundToVm()) {
        continue;
      }
      double compTime = cl.getTotalLength() / (cl.getVm().getPesNumber() * cl.getVm().getMips());
      int index = vmList.indexOf(cl.getVm());
      vmCompTimeSum[index] += compTime;
      compTimeSum += compTime;
    }

    // Check Load Threshold
    double compTimeMean = compTimeSum / vmList.size();
    double varSum = 0.0;
    for (int i = 0; i < vmList.size(); i++) {
      varSum += (vmCompTimeSum[i] - compTimeMean)*(vmCompTimeSum[i] - compTimeMean);
    }
    double compTimeStdev = Math.sqrt(varSum / vmList.size());
    vmIndex = Math.max(0, Math.min(vmList.size()-1, vmIndex));
    if (vmCompTimeSum[vmIndex] > compTimeMean + compTimeStdev) {
      return 0.0;
    }

    // Calculate Agent Fitness
    double estNewCompTime = clNew.getTotalLength() / (vmList.get(vmIndex).getPesNumber() * vmList.get(vmIndex).getMips());
    compTimeSum += estNewCompTime;
    vmCompTimeSum[vmIndex] += estNewCompTime;
    double vmUtilSum = 0.0;
    for (int i = 0; i < vmList.size(); i++) {
      makespan = Math.max(makespan, vmCompTimeSum[i]);
    }
    for (int i = 0; i < vmList.size(); i++) {
      vmUtilSum += vmCompTimeSum[i] / makespan;
    }
    double meanUtilization = vmUtilSum / vmList.size();
    double utilDiffSum = 0.0;
    for (int i = 0; i < vmList.size(); i++) {
      utilDiffSum += ((vmCompTimeSum[i] / makespan) - meanUtilization) * ((vmCompTimeSum[i] / makespan) - meanUtilization);
    }
    double doi = Math.sqrt(utilDiffSum / vmList.size());
    double fitness = 1 / (doi * makespan);
    // double fitness = meanUtilization / makespan;
    return fitness;
  }

  public List<List<Double>> getBestFitnessHistory() {
    return this.bestFitnessHistory;
  }
  

  @Override
  protected Datacenter defaultDatacenterMapper(Datacenter lastDatacenter, Vm vm) {
    if (this.getDatacenterList().isEmpty()) {
      throw new IllegalStateException("You don't have any Datacenter created.");
    } else if (this.getDatacenterList().size() == 1) {
      return (Datacenter) this.getDatacenterList().get(0);
    } else if (lastDatacenter != Datacenter.NULL) {
      return this.nextDatacenter(lastDatacenter);
    } else if (this.triedDatacenters >= this.getDatacenterList().size()) {
      return Datacenter.NULL;
    } else {
      ++this.triedDatacenters;
      return (Datacenter) this.getDatacenterList().get(++this.lastSelectedDcIndex % this.getDatacenterList().size());
    }
  }

  private Datacenter nextDatacenter(Datacenter lastDatacenter) {
    if (this.lastSelectedDcIndex == -1) {
      this.lastSelectedDcIndex = this.getDatacenterList().indexOf(lastDatacenter);
    }
    return lastDatacenter;
  }

  @Override
  protected Vm defaultVmMapper(Cloudlet cloudlet) {
    if (cloudlet.isBoundToVm()) {
      return cloudlet.getVm();
    } else if (this.getVmExecList().isEmpty()) {
      return Vm.NULL;
    } else {
      this.targetVmList = getVmCreatedList();
      double startRealtime = TimeUtil.currentTimeSecs();
      executeWOA(cloudlet, this::calculateFitness);
      double execRealtime = TimeUtil.currentTimeSecs() - startRealtime;
      cloudlet.setSubmissionDelay(cloudlet.getSubmissionDelay() + execRealtime);
      return this.targetVmList.get(this.selectedVmIndex);
    }
  }

  // DEBUG
  // @Override
  // public void processEvent(SimEvent evt) {
  //   super.processEvent(evt);
  //   if (evt.getTag() == 14) {
  //     System.out.print(this.getName() + "\t: ");
  //     int[] vmTaskCount = new int[this.getVmsNumber()];
  //     Iterator<Cloudlet> it = this.getCloudletCreatedList().iterator();
  //     while (it.hasNext()) {
  //       vmTaskCount[(int)it.next().getVm().getId()]++;
  //     }
  //     for (int i = 0; i < vmTaskCount.length; i++) {
  //       System.out.print(vmTaskCount[i] + " ");
  //     }
  //     // vmTaskCount.stream().forEach(cl -> System.out.print(cl.getVm().getId() + " "));
  //     System.out.println();
  //   }
  // }
}