package com.skripsi.app.utils;

public class Misc {
  public static double maxNormalize(double x, double min, double max) {
    return (max - x)/(max - min);
  }

  public static double minNormalize(double x, double min, double max) {
    return (x - min)/(max - min);
  }

  public static double mean(double[] arr) {
    double sum = 0;
    for (int i = 0; i < arr.length; i++) {
      sum += arr[i];
    }
    double mean = sum / arr.length;
    return mean;
  }
  
  public static double variance_s(double[] arr) {
    double mean = mean(arr);
    double sum = 0;
    for (int i = 0; i < arr.length; i++) {
      sum += (arr[i] - mean)*(arr[i] - mean);
    }
    double variance = sum / (arr.length - 1);
    return variance;
  }

  public static double variance_p(double[] arr) {
    double mean = mean(arr);
    double sum = 0;
    for (int i = 0; i < arr.length; i++) {
      sum += (arr[i] - mean)*(arr[i] - mean);
    }
    double variance = sum / arr.length;
    return variance;
  }

  public static double stddev_s(double[] arr) {
    return Math.sqrt(variance_s(arr));
  }

  public static double stddev_p(double[] arr) {
    return Math.sqrt(variance_p(arr));
  }

}
