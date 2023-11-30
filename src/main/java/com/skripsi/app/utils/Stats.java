package com.skripsi.app.utils;

public class Stats {
  private String name;
  private double[] data;

  private Double min;
  private Double max;

  private Double mean;
  private Double varianceS;
  private Double varianceP;
  private Double stdevS;
  private Double stdevP;

  public Stats(String name, double[] data) {
    this.name = name;
    this.data = data;
    this.mean = null;
    this.varianceS = null;
    this.varianceP = null;
    this.stdevS = null;
    this.stdevP = null;
  }

  public String getName() {
    return this.name;
  }

  public double[] calcQuartile() {
    if (this.min != null && this.max != null) {
      return new double[]{this.min, this.max};
    }
      
    double min = Double.MAX_VALUE;
    double max = 0.0;
    for (int i = 0; i < data.length; i++) {
      min = Math.min(min, data[i]);
      max = Math.max(max, data[i]);
    }
    this.min = min;
    this.max = max;
    return new double[]{this.min, this.max};
  }

  public double calcMean() {
    if (this.mean != null) {
      return this.mean;
    }
    double sum = 0;
    for (int i = 0; i < data.length; i++) {
      sum += data[i];
    }
    this.mean = sum / data.length;
    return this.mean;
  }
  
  public double calcVarianceS() {
    if (this.varianceS != null) {
      return this.varianceS;
    }
    double mean = calcMean();
    double sum = 0;
    for (int i = 0; i < data.length; i++) {
      sum += (data[i] - mean)*(data[i] - mean);
    }
    this.varianceS = sum / (data.length - 1);
    return this.varianceS;
  }

  public double calcVarianceP() {
    if (this.varianceP != null) {
      return this.varianceP;
    }
    double mean = calcMean();
    double sum = 0;
    for (int i = 0; i < data.length; i++) {
      sum += (data[i] - mean)*(data[i] - mean);
    }
    this.varianceP = sum / data.length;
    return this.varianceP;
  }

  public double calcStdevS() {
    if (this.stdevS != null) {
      return this.stdevS;
    }
    this.stdevS = Math.sqrt(calcVarianceS());
    return this.stdevS;
  }

  public double calcStdevP() {
    if (this.stdevP != null) {
      return this.stdevP;
    }
    this.stdevP = Math.sqrt(calcVarianceP());
    return this.stdevP;
  }

  public double[] getData() {
    return data;
  }

  public void setData(double[] data) {
    this.data = data;
  }

  public Double getMin() {
    return min;
  }

  public Double getMax() {
    return max;
  }

  public Double getMean() {
    return mean;
  }

  public Double getVarianceS() {
    return varianceS;
  }

  public Double getVarianceP() {
    return varianceP;
  }

  public Double getStdevS() {
    return stdevS;
  }

  public Double getStdevP() {
    return stdevP;
  }

  // public double maxNormalize(double x, double min, double max) {
  //   return (max - x)/(max - min);
  // }

  // public double minNormalize(double x, double min, double max) {
  //   return (x - min)/(max - min);
  // }
}
