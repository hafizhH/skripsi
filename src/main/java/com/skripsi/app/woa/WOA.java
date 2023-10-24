package com.skripsi.app.woa;

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
  private Vector[] initialAgents;
  private Vector[] agents;
  private double[] agentFitness;
  private Function<Integer, Double> fitness;

  private double[] searchDimensions;
  private int agentNum;
  private int bestAgentIdx;
  private double bestScore;
  private int maxIteration;

  public WOA(int maxIteration) {
    this.maxIteration = maxIteration;
  }

  public void init(double[] searchDimensions, int agentNum, Function<Integer, Double> fitness) {
    this.fitness = fitness;
    this.searchDimensions = searchDimensions;
    this.agentNum = agentNum;
    this.initialAgents = new Vector[agentNum];
    this.agents = new Vector[agentNum];
    this.agentFitness = new double[agentNum];
    this.bestAgentIdx = 0;
    this.bestScore = 0.0;
    Random rand = new Random();
    // Random init
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
    for (int i = 0; i < maxIteration; i++) {
      for (int j = 0; j < agentNum; j++) {
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
        // for (int k = 0; k < searchDimensions.length; k++) {
        //   a.setElm(k, rand.nextDouble() * aMagMax - 1.0);
        // }
        
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

      int newBestAgentIdx = this.bestAgentIdx;
      double newBestScore = this.bestScore;
      for (int j = 0; j < agentNum; j++) {
        for (int k = 0; k < searchDimensions.length; k++) {
          agents[j].setElm(k, Math.max(0, Math.min(searchDimensions[k], agents[j].getElm(k))));
        }
        agentFitness[j] = fitness.apply((Integer) ((Number) Math.round(agents[j].getElm(0))).intValue());
        if (agentFitness[j] >= newBestScore) {
          newBestScore = agentFitness[j];
          newBestAgentIdx = j;
        }
      }
      this.bestAgentIdx = newBestAgentIdx;
      this.bestScore = newBestScore;
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
    Vector D = Vector.abs(Vector.subt(Xbest, X));
    X1 = Vector.add(Vector.mul(D, (Double) Math.pow(Math.E, b*l) * Math.cos(2*Math.PI*l)), Xbest);
    return X1;
  }
}
