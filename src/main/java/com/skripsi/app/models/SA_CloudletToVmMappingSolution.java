package com.skripsi.app.models;

import java.util.List;
import java.util.function.BiFunction;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.heuristics.CloudletToVmMappingSolution;
import org.cloudsimplus.vms.Vm;

public class SA_CloudletToVmMappingSolution extends CloudletToVmMappingSolution {
  private BiFunction<Vm, List<Cloudlet>, Double> fitnessFunction;
  private CloudletToVmMappingSolution ctvms;

  public SA_CloudletToVmMappingSolution(CloudletToVmMappingSolution ctvms) {
    super(ctvms.getHeuristic());
    this.ctvms = ctvms;
  }

  public double getCost(BiFunction<Vm, List<Cloudlet>, Double> fitnessFunction) {
    return super.getCost();
  }
  
  @Override
  public double getVmCost(Vm vm, List<Cloudlet> cloudlets) {
    return fitnessFunction.apply(vm, cloudlets);
  }
}
