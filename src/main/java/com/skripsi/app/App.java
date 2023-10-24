package com.skripsi.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.cloudsimplus.allocationpolicies.VmAllocationPolicySimple;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.HarddriveStorage;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.util.Log;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

import com.skripsi.app.utils.Performance;
import com.skripsi.app.woa.WOA_DatacenterBroker;


public class App {

  public static List<Pe> initPes(int num, long mips) {
    List<Pe> peList = new ArrayList<Pe>();
    for (int i = 0; i < num; i++) {
      peList.add((Pe) new PeSimple(mips));
    }
    return peList;
  }
  
  public static void initHosts(int num) {
    // Host configuration
    long ram = 100000; // in Megabytes
    long bw = 1000000; // in Megabits/s
    long peMips = 30000;
    
    // HostSimple[] hostList = new HostSimple[num];
    hosts = new Host[num];
    hosts2 = new Host[num];
    for (int i = 0; i < num; i++) {
      // Creates one host with a specific list of CPU cores (PEs).
      // Uses a PeProvisionerSimple by default to provision PEs for VMs
      // Uses ResourceProvisionerSimple by default for RAM and BW provisioning
      // Uses VmSchedulerSpaceShared by default for VM scheduling
      HarddriveStorage storage = new HarddriveStorage(100000); // in Megabytes
      HarddriveStorage storage2 = new HarddriveStorage(100000); // in Megabytes
      // List<Pe> peList = Arrays.asList((Pe) new PeSimple(peMips), (Pe) new PeSimple(peMips), (Pe) new PeSimple(peMips), (Pe) new PeSimple(peMips), (Pe) new PeSimple(peMips));
      // List<Pe> peList2 = Arrays.asList((Pe) new PeSimple(peMips), (Pe) new PeSimple(peMips), (Pe) new PeSimple(peMips), (Pe) new PeSimple(peMips), (Pe) new PeSimple(peMips));
      List<Pe> peList = initPes(10, peMips);
      List<Pe> peList2 = initPes(10, peMips);
      hosts[i] = new HostSimple(ram, bw, storage, peList);
      hosts2[i] = new HostSimple(ram, bw, storage2, peList2);
    }
  }

  public static void initVms(int num, int minPe, int maxPe, int minMips, int maxMips, int incMips) {
    Random rand = new Random();
    vmList = new Vm[num];
    vmList2 = new Vm[num];
    for (int i = 0; i < num; i++) {
      int numPe = rand.nextInt(minPe, maxPe);
      double mips = (double) (rand.nextInt(minMips, maxMips)/incMips) * incMips;
      vmList[i] = new VmSimple(mips, numPe);
      vmList[i].setRam(1000).setBw(1000).setSize(1000);
      vmList2[i] = new VmSimple(mips, numPe);
      vmList2[i].setRam(1000).setBw(1000).setSize(1000);
    }
  }

  public static void initCloudlets(int num, long minInst, long maxInst, long incInst) {
    Random rand = new Random();
    cloudletList = new Cloudlet[num];
    cloudletList2 = new Cloudlet[num];
    for (int i = 0; i < num; i++) {
      long numInstructions = (rand.nextLong(minInst, maxInst) / incInst) * incInst;
      UtilizationModelDynamic utilizationModel = new UtilizationModelDynamic(0.1);
      cloudletList[i] = new CloudletSimple(numInstructions, 1, utilizationModel);
      UtilizationModelDynamic utilizationModel2 = new UtilizationModelDynamic(0.1);
      cloudletList2[i] = new CloudletSimple(numInstructions, 1, utilizationModel2);
    }
  }

  private static Host[] hosts;
  private static Vm[] vmList;
  private static Cloudlet[] cloudletList;

  private static Host[] hosts2;
  private static Vm[] vmList2;
  private static Cloudlet[] cloudletList2;

  public static void createEnvironment() {
    initHosts(4);
    initVms(20, 1, 2, 1000, 5000, 1000);
    initCloudlets(100, 1000, 5000, 100);
  }
  
  public static Performance simWOA() {
    Log.setLevel(ch.qos.logback.classic.Level.OFF);
    CloudSimPlus simulation = new CloudSimPlus();

    WOA_DatacenterBroker broker = new WOA_DatacenterBroker(simulation, "WOA_DC");
    VmAllocationPolicySimple allocationPolicy = new VmAllocationPolicySimple();
    DatacenterSimple datacenter = new DatacenterSimple(simulation, Arrays.asList(hosts), allocationPolicy);

    broker.submitVmList(Arrays.asList(vmList));
    for (int i = 0; i < cloudletList.length; i++) {
      cloudletList[i].setSubmissionDelay(1.0*i - 1.0);
      broker.submitCloudlet(cloudletList[i]);
    }

    simulation.start();

    new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();
    Performance perf =  new Performance(broker.getCloudletFinishedList(), broker.getVmCreatedList());
    perf.printEvaluation();
    return perf;
  }

  public static Performance simRR() {
    Log.setLevel(ch.qos.logback.classic.Level.WARN);
    CloudSimPlus simulation = new CloudSimPlus();

    DatacenterBrokerSimple broker = new DatacenterBrokerSimple(simulation);
    VmAllocationPolicySimple allocationPolicy = new VmAllocationPolicySimple();
    DatacenterSimple datacenter = new DatacenterSimple(simulation, Arrays.asList(hosts2), allocationPolicy);

    broker.submitVmList(Arrays.asList(vmList2));
    for (int i = 0; i < cloudletList2.length; i++) {
      cloudletList2[i].setSubmissionDelay(1.0*i - 1.0);
      broker.submitCloudlet(cloudletList2[i]);
    }

    simulation.start();
    
    new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();
    Performance perf =  new Performance(broker.getCloudletFinishedList(), broker.getVmCreatedList());
    perf.printEvaluation();
    return perf;
  }

  public static void main(String[] args) {
    createEnvironment();
    Performance perfRR = simRR();
    Performance perfWOA = simWOA();
    Performance.compare(perfWOA, perfRR);
  }
}
