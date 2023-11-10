package com.skripsi.app.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cloudsimplus.builders.tables.CsvTable;

import com.skripsi.app.utils.Stats;
import com.skripsi.app.utils.StatsTableBuilder;

public class PerformanceStats {
  List<Performance> trialPerformance;
  
  private String name;
  private Stats doi;
  private Stats makespan;
  private Stats avgResponseTime;
  private Stats meanUtilization;

  public PerformanceStats(String name) {
    this.name = name;
    this.trialPerformance = new ArrayList<Performance>();
  }

  public void addTrialPerformance(Performance perf) {
    this.trialPerformance.add(perf);
  }

  public void processStats() {
    double[] doiData = new double[trialPerformance.size()];
    double[] makespanData = new double[trialPerformance.size()];
    double[] avgResponseTimeData = new double[trialPerformance.size()];
    double[] meanUtilizationData = new double[trialPerformance.size()];
    for (int i = 0; i < trialPerformance.size(); i++) {
      doiData[i] = trialPerformance.get(i).getDoi();
      makespanData[i] = trialPerformance.get(i).getMakespan();
      avgResponseTimeData[i] = trialPerformance.get(i).getAvgResponseTime();
      meanUtilizationData[i] = trialPerformance.get(i).getMeanUtilization();
    }
    this.doi = new Stats(" DOI", doiData);
    this.makespan = new Stats(" Makespan", makespanData);
    this.avgResponseTime = new Stats(" Avg. Response Time", avgResponseTimeData);
    this.meanUtilization = new Stats(" Mean Utilization", meanUtilizationData);
  }

  public void printStats() {
    processStats();
    // System.out.println();
    // System.out.println(name + " Performance Stats");
    // System.out.println("- Makespan");
    // printMetricStats(makespan);
    // System.out.println("- Avg. Response Time");
    // printMetricStats(avgResponseTime);
    // System.out.println("- Mean Utilization");
    // printMetricStats(meanUtilization);
    // System.out.println("- DOI");
    // printMetricStats(doi);
    new StatsTableBuilder(name + " Performance Stats", Arrays.asList(new Stats[]{doi, makespan, avgResponseTime, meanUtilization})).build();
  }

  private void printMetricStats(Stats metric) {
    double[] q = metric.calcQuartile();
    System.out.print("Min : " + q[0] + ", ");
    System.out.print("Max : " + q[1] + ", ");
    System.out.print("Mean : " + metric.calcMean() + ", ");
    System.out.println("Stdev : " + metric.calcStdevS());
    // System.out.println("Variance : " + metric.calcVarianceP());
  }

  public List<Performance> getTrialPerformance() {
    return trialPerformance;
  }

  public String getName() {
    return name;
  }

  public Stats getDoi() {
    return doi;
  }

  public Stats getMakespan() {
    return makespan;
  }

  public Stats getAvgResponseTime() {
    return avgResponseTime;
  }

  public Stats getMeanUtilization() {
    return meanUtilization;
  }
}
