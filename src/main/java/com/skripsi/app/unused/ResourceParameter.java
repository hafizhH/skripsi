package com.skripsi.app.unused;

import org.cloudsimplus.vms.Vm;

public class ResourceParameter {
  private Vm vm;
  public double totalExecTime;

  public long availableCpu;
  public long totalCpu;
  public double cpuUtils;

  public long availableBw;
  public long totalBw;
  public double bwUtils;

  public long availableRam;
  public long totalRam;
  public double ramUtils;

  public ResourceParameter(Vm vm) {
    this.vm = vm;
    updateParams();
  }

  public void updateParams() {
    totalExecTime = vm.getTotalExecutionTime();

    availableCpu = vm.getProcessor().getAvailableResource();
    totalCpu = vm.getProcessor().getCapacity();
    cpuUtils = vm.getCpuPercentUtilization();

    availableBw = vm.getBw().getAvailableResource();
    totalBw = vm.getBw().getCapacity();
    bwUtils = vm.getBw().getPercentUtilization();

    availableRam = vm.getRam().getAvailableResource();
    totalRam = vm.getRam().getCapacity();
    ramUtils = vm.getRam().getPercentUtilization();
  }

}