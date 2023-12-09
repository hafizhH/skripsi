package com.skripsi.app.simulation;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import com.skripsi.app.evaluation.SimulationPerformance;
import com.skripsi.app.evaluation.ScenarioPerformance;

public class Scenario {
  private String sceneName;
  private int repetitionNum;
  private int modelNum;
  private Environment environment;

  private ScenarioPerformance trialPerformancesWOA;
  private ScenarioPerformance trialPerformancesMWOA;
  private ScenarioPerformance trialPerformancesSA;
  // private PerformanceStats trialPerformancesRR;

  private int maxIteration;
  private int agentNum;
  private int allocatedAgentNum;
  private int prevSolutionNum;

  private int cloudletNum;
  private double cloudletDelay;
  private int[] cloudletConfig;
  private int vmNum;
  private int[][] vmConfig;

  public Scenario(String sceneName, String sceneFileName) {
    this.modelNum = 3;
    this.sceneName = sceneName;
    loadSceneFile(sceneFileName);
  }

  public void loadSceneFile(String sceneFileName) {
    Path path = Paths.get("src/main/java/com/skripsi/app/config", sceneFileName);
    File file = new File(path.toUri());
    try {
      Scanner scanner = new Scanner(file);
      repetitionNum = scanner.nextInt();
      maxIteration = scanner.nextInt();
      agentNum = scanner.nextInt();
      allocatedAgentNum = scanner.nextInt();
      prevSolutionNum = scanner.nextInt();

      cloudletNum = scanner.nextInt();
      cloudletDelay = scanner.nextDouble();
      cloudletConfig = new int[cloudletNum];
      for (int i = 0; i < cloudletNum; i++) {
        cloudletConfig[i] = scanner.nextInt();
      }

      vmNum = scanner.nextInt();
      vmConfig = new int[vmNum][4];
      for (int i = 0; i < vmNum; i++) {
        for (int j = 0; j < 4; j++) {
          vmConfig[i][j] = scanner.nextInt();
        }
      }
      scanner.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void runSimulation() {
    environment = new Environment(repetitionNum * modelNum);
    environment.initResources(cloudletConfig, cloudletDelay, vmConfig);

    trialPerformancesWOA = new ScenarioPerformance("WOA");
    trialPerformancesMWOA = new ScenarioPerformance("Modified WOA");
    trialPerformancesSA = new ScenarioPerformance("SA");

    System.out.println("Started " + sceneName);
    for (int i = 0; i < repetitionNum; i++) {
      // System.out.println("\nTrial " + (i+1));
      // System.out.println("\nStarted " + trialPerformancesWOA.getName() + " Trial " + i);
      SimulationPerformance perfWOA = Simulation.WOA(environment, i, i*modelNum, maxIteration, agentNum);
      // System.out.println("\nStarted " + trialPerformancesMWOA.getName() + " Trial " + i);
      SimulationPerformance perfMWOA = Simulation.MWOA(environment, i, i*modelNum + 1, maxIteration, agentNum, allocatedAgentNum, prevSolutionNum);
      // System.out.println("\nStarted " + trialPerformancesSA.getName() + " Trial " + i);
      SimulationPerformance perfSA = Simulation.SA(environment, i, i*modelNum + 2);
      
      perfWOA.setName(perfWOA.getName() + " Trial " + (i+1));
      perfMWOA.setName(perfMWOA.getName() + " Trial " + (i+1));
      perfSA.setName(perfSA.getName() + " Trial " + (i+1));
      trialPerformancesWOA.addPerformance(perfWOA);
      trialPerformancesMWOA.addPerformance(perfMWOA);
      trialPerformancesSA.addPerformance(perfSA);
    }
    ScenarioPerformance.printComparisonStats(new ScenarioPerformance[]{trialPerformancesWOA, trialPerformancesMWOA, trialPerformancesSA}, sceneName);

    trialPerformancesWOA.outputTrialPerformanceResult(sceneName);
    trialPerformancesMWOA.outputTrialPerformanceResult(sceneName);
    trialPerformancesSA.outputTrialPerformanceResult(sceneName);
  }

  public ScenarioPerformance[] getModelTrialPerformances() {
    return new ScenarioPerformance[]{trialPerformancesWOA, trialPerformancesMWOA, trialPerformancesSA};
  }
}