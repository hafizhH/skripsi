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

import com.skripsi.app.woa.MWOA_Broker;
import com.skripsi.app.woa.WOA_Broker;

public class Simulation {

  public static Performance WOA(Environment environment, int envIndex, int maxIteration, int agentNum) {
    Log.setLevel(ch.qos.logback.classic.Level.OFF);
    CloudSimPlus simulation = new CloudSimPlus();
    Host[] hosts = environment.getHosts(envIndex);
    Vm[] vmList = environment.getVmList(envIndex);
    Cloudlet[] cloudletList = environment.getCloudlet(envIndex);

    WOA_Broker broker = new WOA_Broker(simulation, "WOA_DC", maxIteration, agentNum);
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

  public static Performance MWOA(Environment environment, int envIndex, int maxIteration, int agentNum, int allocatedAgentNum, int prevSolutionNum) {
    Log.setLevel(ch.qos.logback.classic.Level.OFF);
    CloudSimPlus simulation = new CloudSimPlus();
    Host[] hosts = environment.getHosts(envIndex);
    Vm[] vmList = environment.getVmList(envIndex);
    Cloudlet[] cloudletList = environment.getCloudlet(envIndex);

    WOA_Broker broker = new MWOA_Broker(simulation, "MWOA_DC", maxIteration, agentNum, allocatedAgentNum, prevSolutionNum);
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
    Vm[] vmList = environment.getVmList(envIndex);
    Cloudlet[] cloudletList = environment.getCloudlet(envIndex);

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