package com.skripsi.app.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.HarddriveStorage;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

public class Environment {
  private int cloneNum;
  private Host[][] hosts;
  private Vm[][] vmList;
  private Cloudlet[][] cloudletList;

  public Environment(int cloneNum) {
    this.cloneNum = cloneNum;
    hosts = new Host[cloneNum][];
    vmList = new Vm[cloneNum][];
    cloudletList = new Cloudlet[cloneNum][];

    // initRandomVms(cloneNum, 5, 1, 2, 1000, 5000, 1000);
    // initRandomCloudlets(cloneNum, 10, 1000, 5000, 100, 1.0);
  }

  public void initResources(int[] cloudletConfig, double cloudletDelay, int[][] vmConfig) {
    initHosts(cloneNum, 4);
    initCloudlets(cloudletConfig, cloudletDelay);
    initVms(vmConfig);
  }

  public Host[] getHosts(int cloneIndex) {
    return hosts[cloneIndex];
  }

  public Vm[] getVmList(int cloneIndex) {
    return vmList[cloneIndex];
  }

  public Cloudlet[] getCloudlet(int cloneIndex) {
    return cloudletList[cloneIndex];
  }

  public List<Pe> initPes(int num, long mips) {
    List<Pe> peList = new ArrayList<Pe>();
    for (int i = 0; i < num; i++) {
      peList.add((Pe) new PeSimple(mips));
    }
    return peList;
  }
  
  public void initHosts(int cloneNum, int num) {
    long ram = 100000; // in Megabytes
    long bw = 1000000; // in Megabits/s
    long peMips = 30000;
    
    for (int k = 0; k < cloneNum; k++) {
      hosts[k] = new Host[num];
      for (int i = 0; i < num; i++) {
        HarddriveStorage storage = new HarddriveStorage(100000); // in Megabytes
        List<Pe> peList = initPes(10, peMips);
        hosts[k][i] = new HostSimple(ram, bw, storage, peList);
      }
    }
  }

  public void initCloudlets(int[] cloudletConfig, double cloudletDelay) {
    for (int k = 0; k < cloneNum; k++) {
      cloudletList[k] = new Cloudlet[cloudletConfig.length];
    }
    for (int i = 0; i < cloudletConfig.length; i++) {
      long numInstructions = cloudletConfig[i];
      for (int k = 0; k < cloneNum; k++) {
        UtilizationModelDynamic utilizationModel = new UtilizationModelDynamic(0.1);
        cloudletList[k][i] = new CloudletSimple(numInstructions, 1, utilizationModel);
        cloudletList[k][i].setSubmissionDelay(cloudletDelay*i);
      }
    }
  }

  public void initVms(int[][] vmConfig) {
    for (int k = 0; k < cloneNum; k++) {
      vmList[k] = new Vm[vmConfig.length];
    }
    for (int i = 0; i < vmConfig.length; i++) {
      int numPe = vmConfig[i][0];
      double mips = vmConfig[i][1];
      int ram = vmConfig[i][2];
      int bw = vmConfig[i][3];
      for (int k = 0; k < cloneNum; k++) {
        vmList[k][i] = new VmSimple(mips, numPe);
        vmList[k][i].setRam(ram).setBw(bw).setSize(1000);
      }
    }
  }

  public void initRandomVms(int cloneNum, int num, int minPe, int maxPe, int minMips, int maxMips, int incMips) {
    Random rand = new Random();
    for (int k = 0; k < cloneNum; k++) {
      vmList[k] = new Vm[num];
    }
    for (int i = 0; i < num; i++) {
      int numPe = rand.nextInt(minPe, maxPe);
      double mips = (double) (rand.nextInt(minMips, maxMips)/incMips) * incMips;
      for (int k = 0; k < cloneNum; k++) {  
        vmList[k][i] = new VmSimple(mips, numPe);
        vmList[k][i].setRam(1000).setBw(1000).setSize(1000);
      }
    }
  }

  public void initRandomCloudlets(int cloneNum, int num, long minInst, long maxInst, long incInst, double interval) {
    Random rand = new Random();
    
    for (int k = 0; k < cloneNum; k++) {
      cloudletList[k] = new Cloudlet[num];
    }
    for (int i = 0; i < num; i++) {
      long numInstructions = (rand.nextLong(minInst, maxInst) / incInst) * incInst;
      for (int k = 0; k < cloneNum; k++) {
        UtilizationModelDynamic utilizationModel = new UtilizationModelDynamic(0.1);
        cloudletList[k][i] = new CloudletSimple(numInstructions, 1, utilizationModel);
        cloudletList[k][i].setSubmissionDelay(interval*i);
      }
    }
  }
}
