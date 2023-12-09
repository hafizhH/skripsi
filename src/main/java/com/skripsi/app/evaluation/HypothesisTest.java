package com.skripsi.app.evaluation;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

public class HypothesisTest {
  private String name;
  private double[] sampleA;
  private double[] sampleB;
  private double significance;
  private boolean isLeftTailed;

  private double testScore;
  private double pValue;
  private double criticalValue;

  private boolean isH0Rejected;

  public HypothesisTest(String name, double[] sampleA, double[] sampleB, double significance, boolean isLeftTailed) {
    this.name = name;
    this.sampleA = sampleA;
    this.sampleB = sampleB;
    this.significance = significance;
    this.isLeftTailed = isLeftTailed;
    test();
  }

  public void test() {
    this.testScore = new TTest().pairedT(sampleA, sampleB);
    this.pValue = new TTest().pairedTTest(sampleA, sampleB);
    this.criticalValue = new TDistribution(sampleA.length - 1).inverseCumulativeProbability(1 - this.significance);

    if (isLeftTailed) {
      this.criticalValue = -this.criticalValue;
      this.isH0Rejected = (testScore < criticalValue) ? true : false;
    } else {
      this.isH0Rejected = (testScore > criticalValue) ? true : false;
    }
  }

  public double[] getSampleA() {
    return sampleA;
  }

  public double[] getSampleB() {
    return sampleB;
  }

  public double getSignificance() {
    return significance;
  }

  public boolean isLeftTailed() {
    return isLeftTailed;
  }

  public double getTestScore() {
    return testScore;
  }

  public double getpValue() {
    return pValue;
  }

  public double getCriticalValue() {
    return criticalValue;
  }

  public boolean isH0Rejected() {
    return isH0Rejected;
  }

  public String getName() {
    return name;
  }
}