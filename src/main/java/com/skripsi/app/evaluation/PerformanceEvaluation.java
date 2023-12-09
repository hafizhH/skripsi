package com.skripsi.app.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.skripsi.app.simulation.Scenario;
import com.skripsi.app.tables.HypothesisTestTableBuilder;
import com.skripsi.app.tables.TexTable;

public class PerformanceEvaluation {
  private List<Scenario> scenario;

  public PerformanceEvaluation() {
    scenario = new ArrayList<Scenario>();
  }

  public void addScene(Scenario scene) {
    scenario.add(scene);
  }

  public void evaluate() {
    ScenarioPerformance[] woa = new ScenarioPerformance[scenario.size()];
    ScenarioPerformance[] mwoa = new ScenarioPerformance[scenario.size()];
    ScenarioPerformance[] sa = new ScenarioPerformance[scenario.size()];
    for (int i = 0; i < scenario.size(); i++) {
      woa[i] = scenario.get(i).getModelTrialPerformances()[0];
      mwoa[i] = scenario.get(i).getModelTrialPerformances()[1];
      sa[i] = scenario.get(i).getModelTrialPerformances()[2];
    }
    ScenarioPerformance woaMergedPerformances = ScenarioPerformance.merge("WOA Performances", woa);
    ScenarioPerformance mwoaMergedPerformances = ScenarioPerformance.merge("MWOA Performances", mwoa);
    ScenarioPerformance saMergedPerformances = ScenarioPerformance.merge("SA Performances", sa);

    comparePerformance(mwoaMergedPerformances, woaMergedPerformances);
    comparePerformance(mwoaMergedPerformances, saMergedPerformances);
  }

  private void comparePerformance(ScenarioPerformance model1, ScenarioPerformance model2) {
    double[] doi1 = model1.getPerformanceList().stream().mapToDouble(perf -> perf.getDoi()).toArray();
    double[] doi2 = model2.getPerformanceList().stream().mapToDouble(perf -> perf.getDoi()).toArray();
    HypothesisTest doiTest = new HypothesisTest("DOI Test", doi1, doi2, 0.05, true);
    
    double[] makespan1 = model1.getPerformanceList().stream().mapToDouble(perf -> perf.getMakespan()).toArray();
    double[] makespan2 = model2.getPerformanceList().stream().mapToDouble(perf -> perf.getMakespan()).toArray();
    HypothesisTest makespanTest = new HypothesisTest("Makespan Test", makespan1, makespan2, 0.05, true);
    
    double[] avgResponseTime1 = model1.getPerformanceList().stream().mapToDouble(perf -> perf.getAvgResponseTime()).toArray();
    double[] avgResponseTime2 = model2.getPerformanceList().stream().mapToDouble(perf -> perf.getAvgResponseTime()).toArray();
    HypothesisTest avgResponseTimeTest = new HypothesisTest("Avg. Response Time Test", avgResponseTime1, avgResponseTime2, 0.05, true);
    
    double[] meanUtilization1 = model1.getPerformanceList().stream().mapToDouble(perf -> perf.getMeanUtilization()).toArray();
    double[] meanUtilization2 = model2.getPerformanceList().stream().mapToDouble(perf -> perf.getMeanUtilization()).toArray();
    HypothesisTest meanUtilizationTest = new HypothesisTest("Mean Utilization Test", meanUtilization1, meanUtilization2, 0.05, false);
    
    HypothesisTest avgIterationCountTest = null;
    if (model1.getPerformanceList().get(0).isIncludeIteration() && model2.getPerformanceList().get(0).isIncludeIteration()) {
      double[] avgIterationCount1 = model1.getPerformanceList().stream().mapToDouble(perf -> perf.getAvgIterationCount()).toArray();
      double[] avgIterationCount2 = model2.getPerformanceList().stream().mapToDouble(perf -> perf.getAvgIterationCount()).toArray();
      avgIterationCountTest = new HypothesisTest("Avg. Iteration Test", avgIterationCount1, avgIterationCount2, 0.05, true);
    }

    HypothesisTest[] htList = (avgIterationCountTest != null) ? new HypothesisTest[] {doiTest, makespanTest, avgResponseTimeTest, meanUtilizationTest, avgIterationCountTest} : new HypothesisTest[] {doiTest, makespanTest, avgResponseTimeTest, meanUtilizationTest};

    new HypothesisTestTableBuilder(model1.getName() + " vs " + model2.getName(), Arrays.asList(htList)).build();
    new HypothesisTestTableBuilder(model1.getName() + " vs " + model2.getName(), Arrays.asList(htList), "src/main/java/com/skripsi/app/output/", new TexTable()).build();
  }
}