package com.skripsi.app.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.skripsi.app.tables.SimulationPerformancesTableBuilder;
import com.skripsi.app.tables.ScenarioPerformanceStatsTableBuilder;
import com.skripsi.app.utils.Stats;

public class ScenarioPerformance {
  private List<SimulationPerformance> performances;

  private String name;
  private Stats doi;
  private Stats makespan;
  private Stats avgResponseTime;
  private Stats meanUtilization;
  private Stats avgIterationCount;

  public ScenarioPerformance(String name) {
    this.name = name;
    this.performances = new ArrayList<SimulationPerformance>();
  }

  public void addPerformance(SimulationPerformance perf) {
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
    new ScenarioPerformanceStatsTableBuilder(name + " Performance Stats", Arrays.asList(new Stats[]{doi, makespan, avgResponseTime, meanUtilization, avgIterationCount})).build();
  }

  public static void printComparisonStats(ScenarioPerformance[] perfStats, String sceneName) {
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
    new ScenarioPerformanceStatsTableBuilder("DOI Comparison", doiStats).build();
    new ScenarioPerformanceStatsTableBuilder("Makespan Comparison", makespanStats).build();
    new ScenarioPerformanceStatsTableBuilder("Avg. Response Time Comparison", avgResponseTimeStats).build();
    new ScenarioPerformanceStatsTableBuilder("Mean Utilization Comparison", meanUtilizationStats).build();
    new ScenarioPerformanceStatsTableBuilder("Avg. Iteration Utilization Comparison", avgIterationCountStats).build();
    
    new ScenarioPerformanceStatsTableBuilder("DOI", doiStats, "src/main/java/com/skripsi/app/output/"+sceneName).build();
    new ScenarioPerformanceStatsTableBuilder("Makespan", makespanStats, "src/main/java/com/skripsi/app/output/"+sceneName).build();
    new ScenarioPerformanceStatsTableBuilder("Avg. Response Time", avgResponseTimeStats, "src/main/java/com/skripsi/app/output/"+sceneName).build();
    new ScenarioPerformanceStatsTableBuilder("Mean Utilization", meanUtilizationStats, "src/main/java/com/skripsi/app/output/"+sceneName).build();
    new ScenarioPerformanceStatsTableBuilder("Avg. Iteration Utilization", avgIterationCountStats, "src/main/java/com/skripsi/app/output/"+sceneName).build();
  }

  public void outputTrialPerformanceResultToCSV(String sceneName) {
    new SimulationPerformancesTableBuilder(name, this.getPerformanceList(), "src/main/java/com/skripsi/app/output/"+sceneName).build();
  }

  public static ScenarioPerformance merge(String newName, ScenarioPerformance[] tp) {
    List<SimulationPerformance> merged = new ArrayList<SimulationPerformance>();
    for (int i = 0; i < tp.length; i++) {
      List<SimulationPerformance> perfs = tp[i].getPerformanceList();
      for (int j = 0; j < perfs.size(); j++) {
        merged.add(perfs.get(j));
      }
    }
    ScenarioPerformance result = new ScenarioPerformance(newName);
    result.setPerformanceList(merged);
    return result;
  }

  public List<SimulationPerformance> getPerformanceList() {
    return this.performances;
  }
  
  public void setPerformanceList(List<SimulationPerformance> performances) {
    this.performances = performances;
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
