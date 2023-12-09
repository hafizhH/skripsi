package com.skripsi.app.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.skripsi.app.tables.SimulationPerformancesTableBuilder;
import com.skripsi.app.tables.TexTable;
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

  private boolean includeIteration;

  public ScenarioPerformance(String name) {
    this.name = name;
    this.performances = new ArrayList<SimulationPerformance>();
    this.includeIteration = true;
  }

  public void addPerformance(SimulationPerformance perf) {
    this.performances.add(perf);
    if (!perf.isIncludeIteration()) {
      this.includeIteration = false;
    }
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
      if (this.includeIteration) {
        avgIterationCountData[i] = performances.get(i).getAvgIterationCount();
      }
    }
    this.doi = new Stats(this.getName(), doiData);
    this.makespan = new Stats(this.getName(), makespanData);
    this.avgResponseTime = new Stats(this.getName(), avgResponseTimeData);
    this.meanUtilization = new Stats(this.getName(), meanUtilizationData);
    if (this.includeIteration) {
      this.avgIterationCount = new Stats(this.getName(), avgIterationCountData);
    }
  }

  public void printStats() {
    processStats();
    List<Stats> statList = new ArrayList<Stats>();
    statList = Arrays.asList((this.includeIteration) ? new Stats[]{doi, makespan, avgResponseTime, meanUtilization, avgIterationCount} : new Stats[]{doi, makespan, avgResponseTime, meanUtilization});
    new ScenarioPerformanceStatsTableBuilder(name + " Performance Stats", statList).build();
  }

  public void outputTrialPerformanceResult(String sceneName) {
    new SimulationPerformancesTableBuilder(name, this.getPerformanceList(), "src/main/java/com/skripsi/app/output/"+sceneName, new TexTable(), this.includeIteration).build();
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
      if (perfStats[i].isIncludeIteration()) {
        avgIterationCountStats.add(perfStats[i].getAvgIterationCount());
      }
    }
    new ScenarioPerformanceStatsTableBuilder("DOI Comparison", doiStats).build();
    new ScenarioPerformanceStatsTableBuilder("Makespan Comparison", makespanStats).build();
    new ScenarioPerformanceStatsTableBuilder("Avg. Response Time Comparison", avgResponseTimeStats).build();
    new ScenarioPerformanceStatsTableBuilder("Mean Utilization Comparison", meanUtilizationStats).build();
    new ScenarioPerformanceStatsTableBuilder("Avg. Iteration Comparison", avgIterationCountStats).build();
    
    new ScenarioPerformanceStatsTableBuilder("DOI", doiStats, "src/main/java/com/skripsi/app/output/"+sceneName, new TexTable()).build();
    new ScenarioPerformanceStatsTableBuilder("Makespan", makespanStats, "src/main/java/com/skripsi/app/output/"+sceneName, new TexTable()).build();
    new ScenarioPerformanceStatsTableBuilder("Avg. Response Time", avgResponseTimeStats, "src/main/java/com/skripsi/app/output/"+sceneName, new TexTable()).build();
    new ScenarioPerformanceStatsTableBuilder("Mean Utilization", meanUtilizationStats, "src/main/java/com/skripsi/app/output/"+sceneName, new TexTable()).build();
    new ScenarioPerformanceStatsTableBuilder("Avg. Iteration", avgIterationCountStats, "src/main/java/com/skripsi/app/output/"+sceneName, new TexTable()).build();
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

  public boolean isIncludeIteration() {
    return includeIteration;
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
