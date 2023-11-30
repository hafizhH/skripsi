package com.skripsi.app.evaluation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.skripsi.app.tables.PerformanceTableBuilder;
import com.skripsi.app.tables.ScenarioStatsTableBuilder;
import com.skripsi.app.utils.Stats;

public class TrialPerformances {
  private List<Performance> performances;

  private String name;
  private Stats doi;
  private Stats makespan;
  private Stats avgResponseTime;
  private Stats meanUtilization;
  private Stats avgIterationCount;

  public TrialPerformances(String name) {
    this.name = name;
    this.performances = new ArrayList<Performance>();
  }

  public void addPerformance(Performance perf) {
    this.performances.add(perf);
  }

  public void processStats() {
    double[] doiData = new double[performances.size()];
    double[] makespanData = new double[performances.size()];
    double[] avgResponseTimeData = new double[performances.size()];
    double[] meanUtilizationData = new double[performances.size()];
    double[] avgIterationCountData = new double[performances.size()];
    for (int i = 0; i < performances.size(); i++) {
      doiData[i] = performances.get(i).getDoi();
      makespanData[i] = performances.get(i).getMakespan();
      avgResponseTimeData[i] = performances.get(i).getAvgResponseTime();
      meanUtilizationData[i] = performances.get(i).getMeanUtilization();
      avgIterationCountData[i] = performances.get(i).getAvgIterationCount();
    }
    this.doi = new Stats(this.getName(), doiData);
    this.makespan = new Stats(this.getName(), makespanData);
    this.avgResponseTime = new Stats(this.getName(), avgResponseTimeData);
    this.meanUtilization = new Stats(this.getName(), meanUtilizationData);
    this.avgIterationCount = new Stats(this.getName(), avgIterationCountData);
  }

  public void printStats() {
    processStats();
    new ScenarioStatsTableBuilder(name + " Performance Stats", Arrays.asList(new Stats[]{doi, makespan, avgResponseTime, meanUtilization, avgIterationCount})).build();
  }

  public static void printComparisonStats(TrialPerformances[] perfStats, String sceneName) {
    List<Stats> doiStats = new ArrayList<Stats>();
    List<Stats> makespanStats = new ArrayList<Stats>();
    List<Stats> avgResponseTimeStats = new ArrayList<Stats>();
    List<Stats> meanUtilizationStats = new ArrayList<Stats>();
    List<Stats> avgIterationCountStats = new ArrayList<Stats>();
    for (int i = 0; i < perfStats.length; i++) {
      perfStats[i].processStats();
      doiStats.add(perfStats[i].getDoi());
      makespanStats.add(perfStats[i].getMakespan());
      avgResponseTimeStats.add(perfStats[i].getAvgResponseTime());
      meanUtilizationStats.add(perfStats[i].getMeanUtilization());
      avgIterationCountStats.add(perfStats[i].getAvgIterationCount());
    }
    new ScenarioStatsTableBuilder("DOI Comparison", doiStats).build();
    new ScenarioStatsTableBuilder("Makespan Comparison", makespanStats).build();
    new ScenarioStatsTableBuilder("Avg. Response Time Comparison", avgResponseTimeStats).build();
    new ScenarioStatsTableBuilder("Mean Utilization Comparison", meanUtilizationStats).build();
    new ScenarioStatsTableBuilder("Avg. Iteration Utilization Comparison", avgIterationCountStats).build();
    
    new ScenarioStatsTableBuilder("DOI", doiStats, "src/main/java/com/skripsi/app/output/"+sceneName).build();
    new ScenarioStatsTableBuilder("Makespan", makespanStats, "src/main/java/com/skripsi/app/output/"+sceneName).build();
    new ScenarioStatsTableBuilder("Avg. Response Time", avgResponseTimeStats, "src/main/java/com/skripsi/app/output/"+sceneName).build();
    new ScenarioStatsTableBuilder("Mean Utilization", meanUtilizationStats, "src/main/java/com/skripsi/app/output/"+sceneName).build();
    new ScenarioStatsTableBuilder("Avg. Iteration Utilization", avgIterationCountStats, "src/main/java/com/skripsi/app/output/"+sceneName).build();
  }

  public void outputTrialPerformanceResultToCSV(String sceneName) {
    new PerformanceTableBuilder(name, this.getPerformanceList(), "src/main/java/com/skripsi/app/output/"+sceneName).build();
  }

  public List<Performance> getPerformanceList() {
    return this.performances;
  }
  
  public void setPerformanceList(List<Performance> performances) {
    this.performances = performances;
  }

  public static TrialPerformances merge(String newName, TrialPerformances[] tp) {
    List<Performance> merged = new ArrayList<Performance>();
    for (int i = 0; i < tp.length; i++) {
      List<Performance> perfs = tp[i].getPerformanceList();
      for (int j = 0; j < perfs.size(); j++) {
        merged.add(perfs.get(j));
      }
    }
    TrialPerformances result = new TrialPerformances(newName);
    result.setPerformanceList(merged);
    return result;
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

  public Stats getAvgIterationCount() {
    return avgIterationCount;
  }
}
