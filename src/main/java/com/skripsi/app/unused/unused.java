package com.skripsi.app.unused;

public class unused {
  // public Double calculateFitness(Integer vmIndex) {
  //   Cloudlet clNew = currentCloudlet;
  //   List<Vm> vmList = this.targetVmList;
  //   vmIndex = Math.max(0, Math.min(vmList.size()-1, vmIndex));
  //   double makespan = 0.0;
  //   double compTimeSum = 0.0;
  //   double estNewCompTime = clNew.getTotalLength() / (vmList.get(vmIndex).getPesNumber() * vmList.get(vmIndex).getMips());
  //   compTimeSum += estNewCompTime;
  //   // makespan = Math.max(makespan, estNewCompTime);
  //   // for (int i = 0; i < vmList.size(); i++) {
  //   //   Vm vm = vmList.get(i);
  //   //   List<Cloudlet> cloudletList = vm.getCloudletScheduler().getCloudletList();
  //   //   for (int j = 0; j < cloudletList.size(); j++) {
  //   //     Cloudlet cl = cloudletList.get(j);
  //   //     double compTime = cl.getTotalLength() / (cl.getVm().getPesNumber() * cl.getVm().getMips());
  //   //     compTimeSum += compTime;
  //   //     makespan = Math.max(makespan, compTime);
  //   //   }
  //   // }
  //   List<Cloudlet> cloudlets = getCloudletSubmittedList();
  //   double[] vmCompTimeSum = new double[vmList.size()];
  //   vmCompTimeSum[vmIndex] += estNewCompTime;
  //   for (int i = 0; i < cloudlets.size(); i++) {
  //     Cloudlet cl = cloudlets.get(i);
  //     if (!cl.isBoundToVm()) {
  //       continue;
  //     }
  //     double compTime = cl.getTotalLength() / (cl.getVm().getPesNumber() * cl.getVm().getMips());
  //     int index = vmList.indexOf(cl.getVm());
  //     vmCompTimeSum[index] += compTime;
  //     compTimeSum += compTime;
  //     // makespan = Math.max(makespan, compTime);
  //   }
  //   double vmUtilSum = 0.0;
  //   for (int i = 0; i < vmList.size(); i++) {
  //     makespan = Math.max(makespan, vmCompTimeSum[i]);
  //   }
  //   for (int i = 0; i < vmList.size(); i++) {
  //     vmUtilSum += vmCompTimeSum[i] / makespan;
  //   }
  //   // double meanUtilization = compTimeSum / (makespan * vmList.size());
  //   double meanUtilization = vmUtilSum / vmList.size();
  //   double fitness = meanUtilization / makespan;
  //   return fitness;
  // }

  // private Double calculateFitness2(Integer vmIndex) {
  //   Cloudlet clNew = currentCloudlet;
  //   List<Vm> vmList = this.targetVmList;
  //   vmIndex = Math.max(0, Math.min(vmList.size()-1, vmIndex));
  //   double makespan = 0.0;
  //   double compTimeSum = 0.0;
  //   double estNewCompTime = clNew.getTotalLength() / (vmList.get(vmIndex).getPesNumber() * vmList.get(vmIndex).getMips());
  //   compTimeSum += estNewCompTime;
  //   makespan = Math.max(makespan, estNewCompTime);
  //   // for (int i = 0; i < vmList.size(); i++) {
  //   //   Vm vm = vmList.get(i);
  //   //   List<Cloudlet> cloudletList = vm.getCloudletScheduler().getCloudletList();
  //   //   for (int j = 0; j < cloudletList.size(); j++) {
  //   //     Cloudlet cl = cloudletList.get(j);
  //   //     double compTime = cl.getTotalLength() / (cl.getVm().getPesNumber() * cl.getVm().getMips());
  //   //     compTimeSum += compTime;
  //   //     makespan = Math.max(makespan, compTime);
  //   //   }
  //   // }
  //   List<Cloudlet> cloudlets = getCloudletSubmittedList();
  //   double[] vmCompTimeSum = new double[vmList.size()];
  //   vmCompTimeSum[vmIndex] += estNewCompTime;
  //   for (int i = 0; i < cloudlets.size(); i++) {
  //     Cloudlet cl = cloudlets.get(i);
  //     if (!cl.isBoundToVm()) {
  //       continue;
  //     }
  //     double compTime = cl.getTotalLength() / (cl.getVm().getPesNumber() * cl.getVm().getMips());
  //     int index = vmList.indexOf(cl.getVm());
  //     vmCompTimeSum[index] += compTime;
  //     compTimeSum += compTime;
  //     makespan = Math.max(makespan, compTime);
  //   }
  //   double vmUtilSum = 0.0;
  //   // for (int i = 0; i < vmList.size(); i++) {
  //   //   makespan = Math.max(makespan, vmCompTimeSum[i]);
  //   // }
  //   for (int i = 0; i < vmList.size(); i++) {
  //     vmUtilSum += vmCompTimeSum[i] / makespan;
  //   }
  //   // double meanUtilization = compTimeSum / (makespan * vmList.size());
  //   double meanUtilization = vmUtilSum / vmList.size();
  //   // double fitness = meanUtilization / makespan;
  //   double fitness = meanUtilization / makespan;
  //   return fitness;
  // }

  // private Double calculateFitness3(Integer vmIndex) {
  //   Cloudlet clNew = currentCloudlet;
  //   List<Vm> vmList = this.targetVmList;
  //   vmIndex = Math.max(0, Math.min(vmList.size()-1, vmIndex));
  //   double makespan = 0.0;
  //   double compTimeSum = 0.0;
  //   double estNewCompTime = clNew.getTotalLength() / (vmList.get(vmIndex).getPesNumber() * vmList.get(vmIndex).getMips());
  //   compTimeSum += estNewCompTime;
  //   // makespan = Math.max(makespan, estNewCompTime);
  //   // for (int i = 0; i < vmList.size(); i++) {
  //   //   Vm vm = vmList.get(i);
  //   //   List<Cloudlet> cloudletList = vm.getCloudletScheduler().getCloudletList();
  //   //   for (int j = 0; j < cloudletList.size(); j++) {
  //   //     Cloudlet cl = cloudletList.get(j);
  //   //     double compTime = cl.getTotalLength() / (cl.getVm().getPesNumber() * cl.getVm().getMips());
  //   //     compTimeSum += compTime;
  //   //     makespan = Math.max(makespan, compTime);
  //   //   }
  //   // }
  //   List<Cloudlet> cloudlets = getCloudletSubmittedList();
  //   double[] vmCompTimeSum = new double[vmList.size()];
  //   double[] firstStart = new double[vmList.size()];
  //   double[] lastFinish = new double[vmList.size()];
  //   Arrays.fill(firstStart, Double.MAX_VALUE);
  //   vmCompTimeSum[vmIndex] += estNewCompTime;
  //   firstStart[vmIndex] = Math.min(firstStart[vmIndex], clNew.getSubmissionDelay());
  //   lastFinish[vmIndex] = Math.max(lastFinish[vmIndex], clNew.getSubmissionDelay() + estNewCompTime);
    
  //   for (int i = 0; i < cloudlets.size(); i++) {
  //     Cloudlet cl = cloudlets.get(i);
  //     if (!cl.isBoundToVm()) {
  //       continue;
  //     }
  //     double compTime = cl.getTotalLength() / (cl.getVm().getPesNumber() * cl.getVm().getMips());
  //     int index = vmList.indexOf(cl.getVm());
  //     vmCompTimeSum[index] += compTime;
  //     compTimeSum += compTime;
  //     firstStart[index] = Math.min(firstStart[index], cl.getSubmissionDelay());
  //     lastFinish[index] = Math.max(lastFinish[index], cl.getSubmissionDelay() + compTime);
  //     // makespan = Math.max(makespan, compTime);
  //   }
  //   for (int i = 0; i < vmList.size(); i++) {
  //     if (firstStart[i] == Double.MAX_VALUE) {
  //       firstStart[i] = 0.0;
  //     }
  //     makespan = Math.max(makespan, lastFinish[i] - firstStart[i]);
  //   }
  //   double vmUtilSum = 0.0;
  //   for (int i = 0; i < vmList.size(); i++) {
  //     vmUtilSum += vmCompTimeSum[i] / makespan;
  //   }
  //   // double meanUtilization = compTimeSum / (makespan * vmList.size());
  //   double meanUtilization = vmUtilSum / vmList.size();
  //   double fitness = meanUtilization / makespan;
  //   return fitness;
  // }

  
  // @Override
  // public void startInternal() {
  //   super.startInternal();
  //   this.schedule(1.0, 100);
  // }

  // @Override
  // public void processEvent(SimEvent evt) {
  //   if (!processLoadBalancerEvents(evt)) {
  //     super.processEvent(evt);
  //   }
  // }

  // private boolean processLoadBalancerEvents(SimEvent evt) {
  //   boolean var10000;
  //   switch (evt.getTag()) {
  //     case 100:
  //       var10000 = this.processRoutineHealthCheck();
  //       break;
  //     default:
  //       var10000 = false;
  //   }
  //   return var10000;
  // }

  // private boolean processRoutineHealthCheck() {
  //   // List<Vm> vmList = getVmCreatedList();
  //   // double[] load = calculateLoad(vmList);

  //   // double mean = Misc.mean(load), stddev = Misc.stddev_p(load);
  //   // double upperThresh = mean + stddev, lowerThresh = mean - stddev;
  //   // List<Vm> underloadVmList = new ArrayList<>();
  //   // List<Vm> overloadVmList = new ArrayList<>();
  //   // for (int i = 0; i < vmList.size(); i++) {
  //   //   if (load[i] > upperThresh)
  //   //     overloadVmList.add(vmList.get(i));
  //   //   else if (load[i] < lowerThresh)
  //   //     underloadVmList.add(vmList.get(i));
  //   // }
  //   // if (overloadVmList.size() > 0)
  //   //   executeTaskMigration(100, overloadVmList, load);
  //   // return true;
  // }

  // private double[] calculateVMsLoad(List<Vm> vmList) {
  //   double[] load = new double[vmList.size()];
  //   for (int i = 0; i < vmList.size(); i++) {
  //     List<Cloudlet> cloudletList = vmList.get(i).getCloudletScheduler().getCloudletList();
  //     long lengthSum = 0;
  //     for (int j = 0; j < cloudletList.size(); j++) {
  //       lengthSum += cloudletList.get(j).getTotalLength();
  //     }
  //     load[i] = lengthSum / vmList.get(i).getTotalMipsCapacity();
  //   }
  //   return load;
  // }

  // private double calculateDOI(List<Vm> vmList) {
  //   double[] load = calculateVMsLoad(vmList);
  //   double totalLoad = 0.0;
  //   for (int i = 0; i < vmList.size(); i++) {
  //     totalLoad += load[i];
  //   }
  //   double meanLoad = totalLoad / vmList.size();
  //   double[] processTime = new double[vmList.size()];
  //   double totalProcessTime = 0.0;
  //   for (int i = 0; i < vmList.size(); i++) {
  //     processTime[i] = load[i] / meanLoad;
  //     totalProcessTime += processTime[i];
  //   }
  //   double processTimeDiffSum = 0.0;
  //   for (int i = 0; i < processTime.length; i++) {
  //     processTimeDiffSum += processTime[i] - totalProcessTime;
  //   }
  //   return Math.sqrt(processTimeDiffSum / vmList.size());
  // }

  // private double[] calculateResourceUtilization(List<Vm> vmList) {
  //   ResourceParameter[] parameters = new ResourceParameter[vmList.size()];
  //   double minCpu = Double.POSITIVE_INFINITY, maxCpu = 0;
  //   long minBw = Long.MAX_VALUE, maxBw = 0, minRam = Long.MAX_VALUE, maxRam = 0;
  //   double wCpu = 0.4, wBw = 0.2, wRam = 0.4;

  //   for (int i = 0; i < vmList.size(); i++) {
  //     Vm vm = vmList.get(i);
  //     parameters[i] = new ResourceParameter(vm);
  //     minCpu = Math.min(minCpu, parameters[i].cpuUtils);
  //     maxCpu = Math.max(maxCpu, parameters[i].cpuUtils);
  //     minBw = Math.min(minBw, parameters[i].availableBw);
  //     maxBw = Math.max(maxBw, parameters[i].availableBw);
  //     minRam = Math.min(minRam, parameters[i].availableRam);
  //     maxRam = Math.max(maxRam, parameters[i].availableRam);
  //   }

  //   // double sumCpu = 0, sumBw = 0, sumRam = 0;
  //   double[] load = new double[vmList.size()];
  //   for (int i = 0; i < vmList.size(); i++) {
  //     Vm vm = vmList.get(i);
  //     parameters[i] = new ResourceParameter(vm);
  //     double normCpu = Misc.minNormalize(parameters[i].cpuUtils, minCpu, maxCpu);
  //     double normBw = Misc.minNormalize(parameters[i].availableBw, minBw, maxBw);
  //     double normRam = Misc.minNormalize(parameters[i].availableRam, minRam, maxRam);
  //     load[i] = (wCpu * normCpu) + (wBw * normBw) + (wRam * normRam);
  //     // sumCpu += normCpu;
  //     // sumBw += normBw;
  //     // sumRam += normRam;
  //   }
  //   return load;
  // }

  // private void bindCloudlets(List<? extends Cloudlet> cloudlets) {
  //   executeWOA(cloudlets.size());
  //   for (int i = 0; i < cloudlets.size(); i++) {
  //     Vm vm = getVmFromCreatedList(mapping[i]);
  //     bindCloudletToVm(cloudlets.get(i), vm);
  //   }
  // }

  // private void getResourceParameter(){
  // List<? extends Vm> vmList = getVmCreatedList();

  // }

  // @Override
  // public DatacenterBroker submitCloudletList(List<? extends Cloudlet> cloudlets) {
  //   bindCloudlets(cloudlets);
  //   return super.submitCloudletList(cloudlets);
  // }

// class FitnessFunction {
//   // Function<Integer, Function<Integer, UnaryOperator<Integer>>> fit = a -> b -> c -> ;
//   Function fitFunc;

//   public FitnessFunction(Function func) {
//     this.fitFunc = func;
//   }

//   public double evaluate(Vector X) {
    
//     return params[(int) Math.round(X.getElm(0))];
//   }
// }
}
