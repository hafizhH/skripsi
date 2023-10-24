package com.skripsi.app.unused;

import java.util.Arrays;
import java.util.List;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.HostBuilder;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.HarddriveStorage;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.vms.VmSimple;


public class App {
  public static void init() {
    // Enables just some level of logging.
    // Make sure to import org.cloudsimplus.util.Log;
    // Log.setLevel(ch.qos.logback.classic.Level.WARN);

    // Creates a CloudSimPlus object to initialize the simulation.
    CloudSimPlus simulation = new CloudSimPlus();

    // Creates a Broker that will act on behalf of a cloud user (customer).
    DatacenterBrokerSimple broker0 = new DatacenterBrokerSimple(simulation);

    // Host configuration
    long ram = 10000; // in Megabytes
    long bw = 100000; // in Megabits/s
    HarddriveStorage storage = new HarddriveStorage(100000); // in Megabytes
    List<Pe> peList = Arrays.asList((Pe) new PeSimple(20000));

    // Creates one host with a specific list of CPU cores (PEs).
    // Uses a PeProvisionerSimple by default to provision PEs for VMs
    // Uses ResourceProvisionerSimple by default for RAM and BW provisioning
    // Uses VmSchedulerSpaceShared by default for VM scheduling
    HostSimple host0 = new HostSimple(ram, bw, storage, peList);

    // Creates a Datacenter with a list of Hosts.
    // Uses a VmAllocationPolicySimple by default to allocate VMs
    DatacenterSimple dc0 = new DatacenterSimple(simulation, Arrays.asList(host0));

    // Creates one VM with one CPU core to run applications.
    // Uses a CloudletSchedulerTimeShared by default to schedule Cloudlets
    VmSimple vm0 = new VmSimple(1000, 1);
    vm0.setRam(1000).setBw(1000).setSize(1000);

    // Creates Cloudlets that represent applications to be run inside a VM.
    // It has a length of 1000 Million Instructions (MI) and requires 1 CPU core
    // UtilizationModel defining the Cloudlets use only 50% of any resource all the time
    UtilizationModelDynamic utilizationModel = new UtilizationModelDynamic(0.5);
    CloudletSimple cloudlet0 = new CloudletSimple(10000, 1, utilizationModel);
    CloudletSimple cloudlet1 = new CloudletSimple(10000, 1, utilizationModel);
    List<CloudletSimple> cloudletList = Arrays.asList(new CloudletSimple[] { cloudlet0, cloudlet1 });

    broker0.submitVmList(Arrays.asList(vm0));
    broker0.submitCloudletList(cloudletList);

    /*
     * Starts the simulation and waits all cloudlets to be executed, automatically stopping when there is no more events to process.
     */
    simulation.start();

    /*
     * Prints the results when the simulation is over
     * (you can use your own code here to print what you want from this cloudlet list).
     */
    new CloudletsTableBuilder(broker0.getCloudletFinishedList()).build();
  }

  public static void main(String[] args) {
    init();
  }
}
