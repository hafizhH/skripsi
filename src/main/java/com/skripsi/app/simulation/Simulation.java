package com.skripsi.app.simulation;

import java.util.Arrays;

import org.cloudsimplus.allocationpolicies.VmAllocationPolicySimple;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.util.Log;
import org.cloudsimplus.vms.Vm;

import com.skripsi.app.evaluation.Performance;
import com.skripsi.app.models.MWOA_Broker;
import com.skripsi.app.models.SA_Broker;
import com.skripsi.app.models.WOA_Broker;

public class Simulation {

  public static Performance WOA(Environment environment, int envIndex, int maxIteration, int agentNum) {
    Log.setLevel(ch.qos.logback.classic.Level.WARN);
    CloudSimPlus simulation = new CloudSimPlus();
    Host[] hosts = environment.getHosts(envIndex);
    Vm[] vmList = environment.getVms(envIndex);
    Cloudlet[] cloudletList = environment.getCloudlets(envIndex);

    WOA_Broker broker = new WOA_Broker(simulation, "WOA_Broker", maxIteration, agentNum);
    VmAllocationPolicySimple allocationPolicy = new VmAllocationPolicySimple();
    DatacenterSimple datacenter = new DatacenterSimple(simulation, Arrays.asList(hosts), allocationPolicy);

    broker.submitVmList(Arrays.asList(vmList));
    broker.submitCloudletList(Arrays.asList(cloudletList));

    simulation.start();

    // new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();
    Performance perf = new Performance(broker.getName(), broker.getCloudletFinishedList(), broker.getVmCreatedList(), broker.getBestFitnessHistory());
    for (int i = 0; i < perf.getExecTime().length; i++) {
      System.out.print(perf.getExecTime()[i] + " ");
    }
    System.out.println();
    // perf.printEvaluation();
    // perf.plotFitnessGraph(9);
    return perf;
  }

  public static Performance MWOA(Environment environment, int envIndex, int maxIteration, int agentNum, int allocatedAgentNum, int prevSolutionNum) {
    Log.setLevel(ch.qos.logback.classic.Level.WARN);
    CloudSimPlus simulation = new CloudSimPlus();
    Host[] hosts = environment.getHosts(envIndex);
    Vm[] vmList = environment.getVms(envIndex);
    Cloudlet[] cloudletList = environment.getCloudlets(envIndex);

    WOA_Broker broker = new MWOA_Broker(simulation, "MWOA_Broker", maxIteration, agentNum, allocatedAgentNum, prevSolutionNum);
    VmAllocationPolicySimple allocationPolicy = new VmAllocationPolicySimple();
    DatacenterSimple datacenter = new DatacenterSimple(simulation, Arrays.asList(hosts), allocationPolicy);
    
    broker.submitVmList(Arrays.asList(vmList));
    broker.submitCloudletList(Arrays.asList(cloudletList));

    simulation.start();

    // new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();
    Performance perf = new Performance(broker.getName(), broker.getCloudletFinishedList(), broker.getVmCreatedList(), broker.getBestFitnessHistory());
    for (int i = 0; i < perf.getExecTime().length; i++) {
      System.out.print(perf.getExecTime()[i] + " ");
    }
    System.out.println();
    // perf.printEvaluation();
    // perf.plotFitnessGraph(9);
    return perf;
  }

  public static Performance SA(Environment environment, int envIndex) {
    Log.setLevel(ch.qos.logback.classic.Level.WARN);
    CloudSimPlus simulation = new CloudSimPlus();
    Host[] hosts = environment.getHosts(envIndex);
    Vm[] vmList = environment.getVms(envIndex);
    Cloudlet[] cloudletList = environment.getCloudlets(envIndex);

    SA_Broker broker = new SA_Broker(simulation, "SA_Broker", 100.0, 0.95, 0.1);
    VmAllocationPolicySimple allocationPolicy = new VmAllocationPolicySimple();
    DatacenterSimple datacenter = new DatacenterSimple(simulation, Arrays.asList(hosts), allocationPolicy);
    
    broker.submitVmList(Arrays.asList(vmList));
    broker.submitCloudletList(Arrays.asList(cloudletList));

    simulation.start();
    
    // new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();
    Performance perf = new Performance(broker.getName(), broker.getCloudletFinishedList(), broker.getVmCreatedList());
    // perf.printEvaluation();
    return perf;
  }

  public static Performance RR(Environment environment, int envIndex) {
    Log.setLevel(ch.qos.logback.classic.Level.WARN);
    CloudSimPlus simulation = new CloudSimPlus();
    Host[] hosts = environment.getHosts(envIndex);
    Vm[] vmList = environment.getVms(envIndex);
    Cloudlet[] cloudletList = environment.getCloudlets(envIndex);

    DatacenterBrokerSimple broker = new DatacenterBrokerSimple(simulation);
    VmAllocationPolicySimple allocationPolicy = new VmAllocationPolicySimple();
    DatacenterSimple datacenter = new DatacenterSimple(simulation, Arrays.asList(hosts), allocationPolicy);

    broker.submitVmList(Arrays.asList(vmList));
    broker.submitCloudletList(Arrays.asList(cloudletList));

    simulation.start();
    
    // new CloudletsTableBuilder(broker.getCloudletFinishedList()).build();
    Performance perf = new Performance(broker.getName(), broker.getCloudletFinishedList(), broker.getVmCreatedList());
    // perf.printEvaluation();
    return perf;
  }
}