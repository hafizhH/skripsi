package com.skripsi.app.models;

import java.util.Iterator;
import java.util.List;

import org.cloudsimplus.brokers.DatacenterBrokerHeuristic;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.core.events.SimEvent;
import org.cloudsimplus.distributions.UniformDistr;
import org.cloudsimplus.vms.Vm;

public class SA_Broker extends DatacenterBrokerHeuristic {
  SA sa;

  public SA_Broker(CloudSimPlus sim, String name, double initialTemperature, double coolingRate, double coldTemperature) {
    super(sim);
    this.setName(name);
    this.sa = new SA(initialTemperature, new UniformDistr(), this::calculateFitness);
    sa.setCoolingRate(coolingRate);
    sa.setColdTemperature(coldTemperature);
    this.setHeuristic(sa);
  }

  public Double calculateFitness(Vm vm, List<Cloudlet> cloudlets) {
    int vmIndex = (int) vm.getId();
    List<Vm> vmList = this.getVmCreatedList();
    double makespan = 0.0;
    double compTimeSum = 0.0;
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
    return fitness;
  }

  @Override
  public Vm defaultVmMapper(Cloudlet cloudlet) {
    cloudlet.setSubmissionDelay(cloudlet.getSubmissionDelay() + (sa.getSolveTime() / getCloudletSubmittedList().size()));
    return super.defaultVmMapper(cloudlet);
  }

  // DEBUG
  @Override
  public void processEvent(SimEvent evt) {
    super.processEvent(evt);
    if (evt.getTag() == 14) {
      System.out.print("\n" + this.getName() + "\t: ");
      int[] vmTaskCount = new int[this.getVmsNumber()];
      Iterator<Cloudlet> it = this.getCloudletCreatedList().iterator();
      while (it.hasNext()) {
        vmTaskCount[(int)it.next().getVm().getId()]++;
      }
      for (int i = 0; i < vmTaskCount.length; i++) {
        System.out.print(vmTaskCount[i] + " ");
      }
      // vmTaskCount.stream().forEach(cl -> System.out.print(cl.getVm().getId() + " "));
      System.out.println();
    }
  }
}
