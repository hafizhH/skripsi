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

public class WOA_DatacenterBroker extends DatacenterBrokerAbstract {
  private WOA woa;

  private int lastSelectedVmIndex;
  private int lastSelectedDcIndex;
  private int triedDatacenters;

  private Cloudlet currentCloudlet;
  private List<Vm> targetVmList;
  private int selectedVmIndex;

  public WOA_DatacenterBroker(CloudSimPlus sim, String name) {
    super(sim, name);
    this.lastSelectedVmIndex = -1;
    this.lastSelectedDcIndex = -1;
    this.selectedVmIndex = -1;
    this.woa = new WOA(50);
  }

  private void executeWOA(Cloudlet cl, int agentNum, Function<Integer, Double> fitnessFunction) {
    this.currentCloudlet = cl;
    woa.init(new double[]{ (double)this.targetVmList.size()-1 }, agentNum, fitnessFunction);
    woa.run();
    List<Map.Entry<Vector, Double>> results = woa.getResults();
    int selIndex = (int) Math.round(results.get(0).getKey().getElm(0));
    this.selectedVmIndex = selIndex;
    return;
  }

  private Double calculateFitness(Integer vmIndex) {
    Cloudlet clNew = currentCloudlet;
    List<Vm> vmList = this.targetVmList;
    vmIndex = Math.max(0, Math.min(vmList.size()-1, vmIndex));
    double makespan = 0.0;
    double compTimeSum = 0.0;
    double estNewCompTime = clNew.getTotalLength() / (vmList.get(vmIndex).getPesNumber() * vmList.get(vmIndex).getMips());
    compTimeSum += estNewCompTime;
    // makespan = Math.max(makespan, estNewCompTime);
    // for (int i = 0; i < vmList.size(); i++) {
    //   Vm vm = vmList.get(i);
    //   List<Cloudlet> cloudletList = vm.getCloudletScheduler().getCloudletList();
    //   for (int j = 0; j < cloudletList.size(); j++) {
    //     Cloudlet cl = cloudletList.get(j);
    //     double compTime = cl.getTotalLength() / (cl.getVm().getPesNumber() * cl.getVm().getMips());
    //     compTimeSum += compTime;
    //     makespan = Math.max(makespan, compTime);
    //   }
    // }
    List<Cloudlet> cloudlets = getCloudletCreatedList();
    double[] vmCompTimeSum = new double[vmList.size()];
    vmCompTimeSum[vmIndex] += estNewCompTime;
    for (int i = 0; i < cloudlets.size(); i++) {
      Cloudlet cl = cloudlets.get(i);
      double compTime = cl.getTotalLength() / (cl.getVm().getPesNumber() * cl.getVm().getMips());
      int index = vmList.indexOf(cl.getVm());
      vmCompTimeSum[index] += compTime;
      compTimeSum += compTime;
      // makespan = Math.max(makespan, compTime);
    }
    double vmUtilSum = 0.0;
    for (int i = 0; i < vmList.size(); i++) {
      makespan = Math.max(makespan, vmCompTimeSum[i]);
    }
    for (int i = 0; i < vmList.size(); i++) {
      vmUtilSum += vmCompTimeSum[i] / makespan;
    }
    // double meanUtilization = compTimeSum / (makespan * vmList.size());
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

  // @Override
  // protected Vm defaultVmMapper(Cloudlet cloudlet) {
  //   if (cloudlet.isBoundToVm()) {
  //     return cloudlet.getVm();
  //   } else if (this.getVmExecList().isEmpty()) {
  //     return Vm.NULL;
  //   } else {
  //     this.lastSelectedVmIndex = ++this.lastSelectedVmIndex % this.getVmExecList().size();
  //     return this.getVmFromCreatedList(this.lastSelectedVmIndex);
  //   }
  // }

  @Override
  protected Vm defaultVmMapper(Cloudlet cloudlet) {
    if (cloudlet.isBoundToVm()) {
      return cloudlet.getVm();
    } else if (this.getVmExecList().isEmpty()) {
      return Vm.NULL;
    } else {
      this.targetVmList = getVmCreatedList();
      double startRealtime = TimeUtil.currentTimeSecs();
      executeWOA(cloudlet, 10, this::calculateFitness);
      double execRealtime = TimeUtil.currentTimeSecs() - startRealtime;
      cloudlet.setSubmissionDelay(cloudlet.getSubmissionDelay() + execRealtime);
      return this.targetVmList.get(this.selectedVmIndex);
      // return this.getVmFromCreatedList(this.selectedVmIndex);
    }
  }
}

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