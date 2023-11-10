package com.skripsi.app;

import com.skripsi.app.simulation.Scenario;

public class App {
  public static void main(String[] args) {
    Scenario scenario = new Scenario("scene1.txt");
    scenario.runSimulation();
  }
}