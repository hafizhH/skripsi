package com.skripsi.app.models;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.skripsi.app.utils.Vector;

public class MWOA extends WOA {
  private List<Vector> solutionHistory;
  private int allocatedAgentNum;
  private int prevSolutionNum;

  public MWOA(int maxIteration, int agentNum, int allocatedAgentNum, int prevSolutionNum) {
    super(maxIteration, agentNum);
    this.allocatedAgentNum = allocatedAgentNum;
    this.prevSolutionNum = prevSolutionNum;
  }

  public void init(double[] searchDimensions, Function<Integer, Double> fitness, List<Vector> solutionHistory) { 
    this.solutionHistory = solutionHistory;
    super.init(searchDimensions, fitness);
  }

  private int weightedRandomChoice(double[] p) {
    Random rand = new Random();
    double[] prefSum = new double[p.length];
    for (int i = 0; i < p.length; i++) {
      if (i > 0) {
        prefSum[i] = prefSum[i-1] + p[i];
      } else {
        prefSum[i] = p[i];
      }
    }
    double randNum = rand.nextDouble(prefSum[p.length-1]);
    int choice = 0;
    for (int i = 0; i < p.length; i++) {
      if (p[i] > randNum) {
        choice = i - 1;
        if (choice < 0)
          choice = 0;
        break;
      }
    }
    return choice;
  }

  @Override
  public void populationInitialization() {
    if (solutionHistory.size() == 0) {
      super.populationInitialization();
    } else {
      weightedRandomPopulationInit(prevSolutionNum, allocatedAgentNum, 0.0);
    }
  }

  public void weightedRandomPopulationInit(int m, int k, double r) {
    m = Math.min(m, solutionHistory.size());
    double[] prob = new double[m];
    double sum = m*(m+1)/2;
    for (int i = 0; i < m; i++) {
      prob[m-i-1] = (i+1)/sum;
    }
    Random rand = new Random();
    for (int i = 0; i < k; i++) {
      this.initialAgents[i] = new Vector(searchDimensions.length);
      this.agents[i] = new Vector(searchDimensions.length);
      int selectedPrevSolutionIndex = weightedRandomChoice(prob);
      Vector selectedPrevSolution = solutionHistory.get(solutionHistory.size() - selectedPrevSolutionIndex - 1);
      for (int j = 0; j < searchDimensions.length; j++) {
        Double randInit = selectedPrevSolution.getElm(j) + (rand.nextDouble()*r*2.0 - r);
        this.initialAgents[i].setElm(j, randInit);
        this.agents[i].setElm(j, randInit);
      }
    }
    for (int i = k; i < agentNum; i++) {
      this.initialAgents[i] = new Vector(searchDimensions.length);
      this.agents[i] = new Vector(searchDimensions.length);
      for (int j = 0; j < searchDimensions.length; j++) {
        double interval = searchDimensions[j] / (agentNum-k-1);
        double initPos =  Math.max(0, Math.min(searchDimensions[j],(double) Math.round((i-k)*interval)));
        this.initialAgents[i].setElm(j, initPos);
        this.agents[i].setElm(j, initPos);
      }
    }
    return;
  }
}