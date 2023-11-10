package com.skripsi.app.woa;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.cloudsimplus.brokers.DatacenterBrokerAbstract;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
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

  public WOA_Broker(CloudSimPlus sim, String name, int maxIteration, int agentNum) {
    super(sim, name);
    this.lastSelectedVmIndex = -1;
    this.lastSelectedDcIndex = -1;
    this.selectedVmIndex = -1;
    this.woa = new WOA(maxIteration, agentNum);
  }

  private void executeWOA(Cloudlet cl, Function<Integer, Double> fitnessFunction) {
    this.currentCloudlet = cl;
    woa.init(new double[]{ (double)this.targetVmList.size()-1 }, fitnessFunction);
    woa.run();
    List<Map.Entry<Vector, Double>> results = woa.getResults();
    int selIndex = (int) Math.round(results.get(0).getKey().getElm(0));
    this.selectedVmIndex = selIndex;
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
    double fitness = meanUtilization / makespan;
    return fitness;
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
}