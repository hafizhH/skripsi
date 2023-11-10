package com.skripsi.app.simulation;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Scenario {
  private int repetitionNum;
  private int modelNum;
  private Environment environment;

  private PerformanceStats perfStatsWOA;
  private PerformanceStats perfStatsMWOA;
  private PerformanceStats perfStatsRR;

  private int maxIteration;
  private int agentNum;
  private int allocatedAgentNum;
  private int prevSolutionNum;

  private int cloudletNum;
  private double cloudletDelay;
  private int[] cloudletConfig;
  private int vmNum;
  private int[][] vmConfig;

  public Scenario(String sceneFileName) {
    this.modelNum = 3;
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

    perfStatsWOA = new PerformanceStats("WOA");
    perfStatsMWOA = new PerformanceStats("Modified WOA");
    perfStatsRR = new PerformanceStats("Round-Robin");

    for (int i = 0; i < repetitionNum; i++) {
      // System.out.println("\nTrial " + (i+1));
      Performance perfWOA = Simulation.WOA(environment, i*modelNum, maxIteration, agentNum);
      Performance perfMWOA = Simulation.MWOA(environment, i*modelNum + 1, maxIteration, agentNum, allocatedAgentNum, prevSolutionNum);
      Performance perfRR = Simulation.RR(environment, i*modelNum + 2);
      perfStatsWOA.addTrialPerformance(perfWOA);
      perfStatsMWOA.addTrialPerformance(perfMWOA);
      perfStatsRR.addTrialPerformance(perfRR);
      // new PerformanceCompare(perfWOA, perfMWOA).printComparison();
    }

    perfStatsWOA.printStats();
    perfStatsMWOA.printStats();
    perfStatsRR.printStats();
  }
}