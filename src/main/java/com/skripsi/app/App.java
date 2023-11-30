package com.skripsi.app;

import com.skripsi.app.evaluation.PerformanceEvaluation;
import com.skripsi.app.simulation.Scenario;

public class App {
  public static void runScenarios() {
    PerformanceEvaluation perfEval = new PerformanceEvaluation();
    Scenario[] scenarios = new Scenario[5];
    for (int i = 0; i < 5; i++) {
      scenarios[i] = new Scenario("scene"+(i+1), String.format("scene%s.txt", i+1));
      scenarios[i].runSimulation();
      perfEval.addScene(scenarios[i]);
    }
    perfEval.evaluate();
  }

  public static void main(String[] args) {
    runScenarios();
  }
}