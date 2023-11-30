package com.skripsi.app.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.skripsi.app.utils.Vector;

public class WOA {
  protected Vector[] initialAgents;
  protected Vector[] agents;
  protected double[] agentFitness;
  protected Function<Integer, Double> fitness;
  protected List<Double> bestFitnessHistory;
  protected double[] searchDimensions;
  protected int agentNum;
  protected int bestAgentIdx;
  protected double bestFitness;
  protected int maxIteration;
  protected int convergenceIterationNum;
  protected double convergenceEpsilon;

  public WOA(int maxIteration, int agentNum) {
    this.maxIteration = maxIteration;
    this.agentNum = agentNum;
    this.convergenceIterationNum = 0;
    this.convergenceEpsilon = Double.MIN_VALUE;
  }

  public void init(double[] searchDimensions, Function<Integer, Double> fitness) {
    this.fitness = fitness;
    this.searchDimensions = searchDimensions;
    this.initialAgents = new Vector[agentNum];
    this.agents = new Vector[agentNum];
    this.agentFitness = new double[agentNum];
    this.bestAgentIdx = 0;
    this.bestFitness = 0.0;
    this.bestFitnessHistory = new ArrayList<Double>();
    populationInitialization();
  }

  public void populationInitialization() {
    Random rand = new Random();
    for (int i = 0; i < agentNum; i++) {
      this.initialAgents[i] = new Vector(searchDimensions.length);
      this.agents[i] = new Vector(searchDimensions.length);
      for (int j = 0; j < searchDimensions.length; j++) {
        Double randInit = rand.nextDouble() * searchDimensions[j];
        this.initialAgents[i].setElm(j, randInit);
        this.agents[i].setElm(j, randInit);
      }
    }
  }
  
  public void run() {
    int constantFitnessIterCount = 0;
    for (int i = 0; i < maxIteration; i++) {
      for (int j = 0; j < agentNum; j++) {
        if (i > 0 && j == bestAgentIdx) {
          continue;
        }
        Vector currentAgent = agents[j];
        Vector bestAgent = agents[bestAgentIdx];
        Random rand = new Random();
        Vector randomAgent = agents[rand.nextInt(agentNum)];
        Vector r = new Vector(searchDimensions.length);
        for (int k = 0; k < searchDimensions.length; k++) {
          r.setElm(k, rand.nextDouble());
        }
        
        double aVal = 2.0-(2.0*((double)i)/((double)maxIteration));
        Vector a = new Vector(searchDimensions.length, aVal);
        
        Vector A = Vector.subt(Vector.elmMul(Vector.mul(a, 2.0), r), a);
        Vector C = Vector.mul(r, 2.0);
        double b = 0.5;
        double l = rand.nextDouble() * 2.0 - 1.0;
        double p = rand.nextDouble();

        if (p < 0.5) {
          double AMag = Vector.magnitude(A);
          if (AMag < 1) {
            agents[j] = shrinkingEncircling(currentAgent, bestAgent, C, A);
          } else {
            agents[j] = exploration(currentAgent, randomAgent, C, A);
          }
        } else {
          agents[j] = spiral(currentAgent, bestAgent, C, A, b, l);
        }
      }

      // System.out.print(String.format("Iteration %d\t: ", i));

      int newBestAgentIdx = this.bestAgentIdx;
      double newBestFitness = this.bestFitness;
      for (int j = 0; j < agentNum; j++) {
        for (int k = 0; k < searchDimensions.length; k++) {
          agents[j].setElm(k, Math.max(0, Math.min(searchDimensions[k], agents[j].getElm(k))));
        }
        agentFitness[j] = fitness.apply((Integer) ((Number) Math.round(agents[j].getElm(0))).intValue());
        if (agentFitness[j] >= newBestFitness) {
          newBestFitness = agentFitness[j];
          newBestAgentIdx = j;
        }
        // System.out.print(String.format("(%2.2f,%2.8f) ", agents[j].getElm(0), agentFitness[j]));
      }

      // System.out.print(String.format("[%d : (%2.2f,%2.8f)] ", newBestAgentIdx, agents[bestAgentIdx].getElm(0), agentFitness[bestAgentIdx]));
      // System.out.println();
      int prevBestAgentIdx = this.bestAgentIdx;
      this.bestAgentIdx = newBestAgentIdx;
      this.bestFitness = newBestFitness;
      this.bestFitnessHistory.add(bestFitness);

      if (bestFitnessHistory.size() < 2 || convergenceIterationNum == 0) {
        continue;
      }
      // if (Math.abs(bestFitness - bestFitnessHistory.get(bestFitnessHistory.size()-2)) < convergenceEpsilon) {
      if (this.bestAgentIdx == prevBestAgentIdx) {
        constantFitnessIterCount++;
      } else {
        constantFitnessIterCount = 0;
      }
      if (constantFitnessIterCount >= convergenceIterationNum) {
        break;
      }
    }
  }

  public List<Map.Entry<Vector, Double>> getResults() {
    Map<Vector, Double> hm = new HashMap<Vector, Double>();
    for (int i = 0; i < agentNum; i++) {
      hm.put(agents[i], agentFitness[i]);
    }
    List<Map.Entry<Vector, Double>> entryList = new ArrayList<Map.Entry<Vector, Double>>(hm.entrySet());
    Collections.sort(entryList, Comparator.comparingDouble(Map.Entry::getValue));
    return entryList;
  }

  public List<Double> getBestFitnessHistory() {
    return this.bestFitnessHistory;
  }

  private Vector exploration(Vector X, Vector Xrand, Vector C, Vector A) {
    Vector X1 = new Vector(searchDimensions.length);
    Vector D = Vector.abs(Vector.subt(Vector.elmMul(C, Xrand), X));
    X1 = Vector.subt(Xrand, Vector.elmMul(A, D));
    return X1;
  }

  private Vector shrinkingEncircling(Vector X, Vector Xbest, Vector C, Vector A) {
    Vector X1 = new Vector(searchDimensions.length);
    Vector D = Vector.abs(Vector.subt(Vector.elmMul(C, Xbest), X));
    X1 = Vector.subt(Xbest, Vector.elmMul(A, D));
    return X1;
  }

  private Vector spiral(Vector X, Vector Xbest, Vector C, Vector A, Double b, Double l) {
    Vector X1 = new Vector(searchDimensions.length);
    Vector sub = Vector.subt(Xbest, X);
    Vector D = Vector.abs(sub);
    X1 = Vector.add(Vector.mul(D, (Double) Math.pow(Math.E, b*l) * Math.cos(2*Math.PI*l)), Xbest);
    return X1;
  }
}
