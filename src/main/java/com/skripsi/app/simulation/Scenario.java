package com.skripsi.app.simulation;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import com.skripsi.app.evaluation.Performance;
import com.skripsi.app.evaluation.TrialPerformances;
import com.skripsi.app.tables.PerformanceTableBuilder;

public class Scenario {
  private String sceneName;
  private int repetitionNum;
  private int modelNum;
  private Environment environment;

  private TrialPerformances trialPerformancesWOA;
  private TrialPerformances trialPerformancesMWOA;
  private TrialPerformances trialPerformancesSA;
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

    trialPerformancesWOA = new TrialPerformances("WOA");
    trialPerformancesMWOA = new TrialPerformances("Modified WOA");
    trialPerformancesSA = new TrialPerformances("SA");
    // trialPerformancesRR = new PerformanceStats("Round-Robin");

    for (int i = 0; i < repetitionNum; i++) {
      // System.out.println("\nTrial " + (i+1));
      System.out.println("\nStarted " + trialPerformancesWOA.getName() + " Trial " + i);
      Performance perfWOA = Simulation.WOA(environment, i*modelNum, maxIteration, agentNum);
      System.out.println("\nStarted " + trialPerformancesMWOA.getName() + " Trial " + i);
      Performance perfMWOA = Simulation.MWOA(environment, i*modelNum + 1, maxIteration, agentNum, allocatedAgentNum, prevSolutionNum);
      System.out.println("\nStarted " + trialPerformancesSA.getName() + " Trial " + i);
      Performance perfSA = Simulation.SA(environment, i*modelNum + 2);
      // Performance perfRR = Simulation.RR(environment, i*modelNum + 2);
      perfWOA.setName(perfWOA.getName() + " Trial " + (i+1));
      perfMWOA.setName(perfMWOA.getName() + " Trial " + (i+1));
      perfSA.setName(perfSA.getName() + " Trial " + (i+1));
      trialPerformancesWOA.addPerformance(perfWOA);
      trialPerformancesMWOA.addPerformance(perfMWOA);
      trialPerformancesSA.addPerformance(perfSA);
      // trialPerformancesRR.addPerformance(perfRR);
      // new PerformanceCompare(perfWOA, perfMWOA).printComparison();
    }

    // trialPerformancesWOA.printStats();
    // trialPerformancesMWOA.printStats();
    // trialPerformancesSA.printStats();
    // trialPerformancesRR.printStats();
    TrialPerformances.printComparisonStats(new TrialPerformances[]{trialPerformancesWOA, trialPerformancesMWOA, trialPerformancesSA}, sceneName);
    
    Path path1 = Paths.get("src/main/java/com/skripsi/app/output/"+sceneName, "woa-performance.csv");
    Path path2 = Paths.get("src/main/java/com/skripsi/app/output/"+sceneName, "mwoa-performance.csv");
    Path path3 = Paths.get("src/main/java/com/skripsi/app/output/"+sceneName, "sa-performance.csv");

    trialPerformancesWOA.outputTrialPerformanceResultToCSV(sceneName);
    trialPerformancesMWOA.outputTrialPerformanceResultToCSV(sceneName);
    trialPerformancesSA.outputTrialPerformanceResultToCSV(sceneName);
  }

  public TrialPerformances[] getModelTrialPerformances() {
    return new TrialPerformances[]{trialPerformancesWOA, trialPerformancesMWOA, trialPerformancesSA};
  }
}