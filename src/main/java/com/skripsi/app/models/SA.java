package com.skripsi.app.models;

import org.cloudsimplus.heuristics.CloudletToVmMappingSimulatedAnnealing;
import org.cloudsimplus.heuristics.CloudletToVmMappingSolution;
import org.cloudsimplus.vms.Vm;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.distributions.ContinuousDistribution;

public class SA extends CloudletToVmMappingSimulatedAnnealing {
  protected BiFunction<Vm, List<Cloudlet>, Double> fitnessFunction;

  public SA(double initialTemperature, ContinuousDistribution random, BiFunction<Vm, List<Cloudlet>, Double> fitnessFunction) {
    super(initialTemperature, random);
    this.fitnessFunction = fitnessFunction;
  }

  @Override
  public double getAcceptanceProbability() {
    SA_CloudletToVmMappingSolution bestSolution = new SA_CloudletToVmMappingSolution(this.getBestSolutionSoFar());
    SA_CloudletToVmMappingSolution neighborSolution = new SA_CloudletToVmMappingSolution(this.getNeighborSolution());
    return Math.exp((bestSolution.getCost(fitnessFunction) - neighborSolution.getCost(fitnessFunction)) / (1.0 * this.getCurrentTemperature()));
  }
}
