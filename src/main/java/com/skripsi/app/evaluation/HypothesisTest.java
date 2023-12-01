package com.skripsi.app.evaluation;

import org.apache.commons.math3.stat.inference.TTest;

public class HypothesisTest {
  private String name;
  private double[] sampleA;
  private double[] sampleB;
  private double significance;
  private boolean isOneTailed;

  private double testScore;
  private double pValue;
  private boolean isH0Rejected;
  private boolean isH0Rejected2;

  public HypothesisTest(String name, double[] sampleA, double[] sampleB, double significance, boolean isOneTailed) {
    this.name = name;
    this.sampleA = sampleA;
    this.sampleB = sampleB;
    this.significance = significance;
    this.isOneTailed = isOneTailed;
    test();
  }

  public void test() {
    this.testScore = new TTest().pairedT(sampleA, sampleB);
    this.pValue = new TTest().pairedTTest(sampleA, sampleB);

    if (isOneTailed) {
      this.pValue = pValue / 2.0;
      this.isH0Rejected2 = new TTest().pairedTTest(sampleA, sampleB, significance * 2.0);
    } else {
      this.isH0Rejected2 = new TTest().pairedTTest(sampleA, sampleB, significance);
    }

    if (this.pValue < significance) {
      this.isH0Rejected = true;
    } else {
      this.isH0Rejected = false;
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

  public boolean isOneTailed() {
    return isOneTailed;
  }

  public double getTestScore() {
    return testScore;
  }

  public double getpValue() {
    return pValue;
  }

  public boolean isH0Rejected() {
    return isH0Rejected;
  }

  public boolean isH0Rejected2() {
    return isH0Rejected2;
  }

  public String getName() {
    return name;
  }
}