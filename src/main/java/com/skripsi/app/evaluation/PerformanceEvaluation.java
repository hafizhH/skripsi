package com.skripsi.app.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.stat.inference.TTest;

import com.skripsi.app.simulation.Scenario;
import com.skripsi.app.tables.HypothesisTestTableBuilder;

public class PerformanceEvaluation {
  private List<Scenario> scenario;

  private List<List<Double>> doi;
	private List<List<Double>> makespan;
	private List<List<Double>> avgResponseTime;
	private List<List<Double>> meanUtilization;
  private List<List<Double>> avgIterationCount;

  private double[] t1;
  private double[] t2;
  private boolean[] tests1;
  private boolean[] tests2;

  public PerformanceEvaluation() {
    scenario = new ArrayList<Scenario>();
    doi = new ArrayList<List<Double>>();
    makespan = new ArrayList<List<Double>>();
    avgResponseTime = new ArrayList<List<Double>>();
    meanUtilization = new ArrayList<List<Double>>();
    avgIterationCount = new ArrayList<List<Double>>();
  }

  public void addScene(Scenario scene) {
    scenario.add(scene);
  }

  public void evaluate() {
    // for (int k = 0; k < 3; k++) {
    //   List<Double> doi2 = new ArrayList<Double>();
    //   List<Double> makespan2 = new ArrayList<Double>();
    //   List<Double> avgResponseTime2 = new ArrayList<Double>();
    //   List<Double> meanUtilization2 = new ArrayList<Double>();
    //   List<Double> avgIterationCount2 = new ArrayList<Double>();
    //   for (int i = 0; i < scenario.size(); i++) {
    //     List<Performance> performances = scenario.get(i).getModelTrialPerformances()[k].getPerformanceList();
    //     for (int j = 0; j < performances.size(); j++) {
    //       doi2.add(performances.get(j).getDoi());
    //       makespan2.add(performances.get(j).getMakespan());
    //       avgResponseTime2.add(performances.get(j).getAvgResponseTime());
    //       meanUtilization2.add(performances.get(j).getMeanUtilization());
    //       avgIterationCount2.add(performances.get(j).getAvgIterationCount());
    //     }
    //   }
    //   doi.add(doi2);
    //   makespan.add(makespan2);
    //   avgResponseTime.add(avgResponseTime2);
    //   meanUtilization.add(meanUtilization2);
    //   avgIterationCount.add(avgIterationCount2);
    // }
    // TrialPerformances woa = new TrialPerformances("WOA");
    TrialPerformances[] woa = new TrialPerformances[scenario.size()];
    TrialPerformances[] mwoa = new TrialPerformances[scenario.size()];
    TrialPerformances[] sa = new TrialPerformances[scenario.size()];
    for (int i = 0; i < scenario.size(); i++) {
      woa[i] = scenario.get(i).getModelTrialPerformances()[0];
      mwoa[i] = scenario.get(i).getModelTrialPerformances()[1];
      sa[i] = scenario.get(i).getModelTrialPerformances()[2];
    }
    TrialPerformances woaMergedPerformances = TrialPerformances.merge("WOA Merged Performances", woa);
    TrialPerformances mwoaMergedPerformances = TrialPerformances.merge("MWOA Merged Performances", mwoa);
    TrialPerformances saMergedPerformances = TrialPerformances.merge("SA Merged Performances", sa);

    // this.doi.add(Arrays.asList((Double[]) woaMergedPerformances.getPerformanceList().stream().map(perf -> perf.getDoi()).toArray()));
    
    // hypothesesTest();
    // hypothesesTest2();
    comparePerformance(mwoaMergedPerformances, woaMergedPerformances);
    comparePerformance(mwoaMergedPerformances, saMergedPerformances);
  }

  private void comparePerformance(TrialPerformances model1, TrialPerformances model2) {
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
    HypothesisTest meanUtilizationTest = new HypothesisTest("Mean Utilization Test", meanUtilization1, meanUtilization2, 0.05, true);
    
    double[] avgIterationCount1 = model1.getPerformanceList().stream().mapToDouble(perf -> perf.getAvgIterationCount()).toArray();
    double[] avgIterationCount2 = model2.getPerformanceList().stream().mapToDouble(perf -> perf.getAvgIterationCount()).toArray();
    HypothesisTest avgIterationCountTest = new HypothesisTest("Avg. Iteration Test", avgIterationCount1, avgIterationCount2, 0.05, true);

    new HypothesisTestTableBuilder(model1.getName() + " vs " + model2.getName(), Arrays.asList(new HypothesisTest[] {doiTest, makespanTest, avgResponseTimeTest, meanUtilizationTest, avgIterationCountTest})).build();
    new HypothesisTestTableBuilder(model1.getName() + " vs " + model2.getName(), Arrays.asList(new HypothesisTest[] {doiTest, makespanTest, avgResponseTimeTest, meanUtilizationTest, avgIterationCountTest}), "src/main/java/com/skripsi/app/output/").build();
  }

  // private void hypothesesTest() {
  //   t1 = new double[5];
  //   t1[0] = new TTest().pairedTTest(doi.get(0).stream().mapToDouble(i -> i).toArray(), doi.get(1).stream().mapToDouble(i -> i).toArray()) / 2.0;
  //   t1[1] = new TTest().pairedTTest(makespan.get(0).stream().mapToDouble(i -> i).toArray(), makespan.get(1).stream().mapToDouble(i -> i).toArray()) / 2.0;
  //   t1[2] = new TTest().pairedTTest(avgResponseTime.get(0).stream().mapToDouble(i -> i).toArray(), avgResponseTime.get(1).stream().mapToDouble(i -> i).toArray()) / 2.0;
  //   t1[3] = new TTest().pairedTTest(meanUtilization.get(0).stream().mapToDouble(i -> i).toArray(), meanUtilization.get(1).stream().mapToDouble(i -> i).toArray()) / 2.0;
  //   t1[4] = new TTest().pairedTTest(avgIterationCount.get(0).stream().mapToDouble(i -> i).toArray(), avgIterationCount.get(1).stream().mapToDouble(i -> i).toArray()) / 2.0;

  //   t2 = new double[4];
  //   t2[0] = new TTest().pairedTTest(doi.get(0).stream().mapToDouble(i -> i).toArray(), doi.get(2).stream().mapToDouble(i -> i).toArray()) / 2.0;
  //   t2[1] = new TTest().pairedTTest(makespan.get(0).stream().mapToDouble(i -> i).toArray(), makespan.get(2).stream().mapToDouble(i -> i).toArray()) / 2.0;
  //   t2[2] = new TTest().pairedTTest(avgResponseTime.get(0).stream().mapToDouble(i -> i).toArray(), avgResponseTime.get(2).stream().mapToDouble(i -> i).toArray()) / 2.0;
  //   t2[3] = new TTest().pairedTTest(meanUtilization.get(0).stream().mapToDouble(i -> i).toArray(), meanUtilization.get(2).stream().mapToDouble(i -> i).toArray()) / 2.0;
  //   // t2[4] = new TTest().pairedTTest(avgIterationCount.get(0).stream().mapToDouble(i -> i).toArray(), avgIterationCount.get(2).stream().mapToDouble(i -> i).toArray()) / 2.0;
  // }

  // private void hypothesesTest2() {
  //   tests1 = new boolean[5];
  //   tests1[0] = new TTest().pairedTTest(doi.get(0).stream().mapToDouble(i -> i).toArray(), doi.get(1).stream().mapToDouble(i -> i).toArray(), 0.05);
  //   tests1[1] = new TTest().pairedTTest(makespan.get(0).stream().mapToDouble(i -> i).toArray(), makespan.get(1).stream().mapToDouble(i -> i).toArray(), 0.05);
  //   tests1[2] = new TTest().pairedTTest(avgResponseTime.get(0).stream().mapToDouble(i -> i).toArray(), avgResponseTime.get(1).stream().mapToDouble(i -> i).toArray(), 0.05);
  //   tests1[3] = new TTest().pairedTTest(meanUtilization.get(0).stream().mapToDouble(i -> i).toArray(), meanUtilization.get(1).stream().mapToDouble(i -> i).toArray(), 0.05);
  //   tests1[4] = new TTest().pairedTTest(avgIterationCount.get(0).stream().mapToDouble(i -> i).toArray(), avgIterationCount.get(1).stream().mapToDouble(i -> i).toArray(), 0.05);

  //   tests2 = new boolean[4];
  //   tests2[0] = new TTest().pairedTTest(doi.get(0).stream().mapToDouble(i -> i).toArray(), doi.get(2).stream().mapToDouble(i -> i).toArray(), 0.05);
  //   tests2[1] = new TTest().pairedTTest(makespan.get(0).stream().mapToDouble(i -> i).toArray(), makespan.get(2).stream().mapToDouble(i -> i).toArray(), 0.05);
  //   tests2[2] = new TTest().pairedTTest(avgResponseTime.get(0).stream().mapToDouble(i -> i).toArray(), avgResponseTime.get(2).stream().mapToDouble(i -> i).toArray(), 0.05);
  //   tests2[3] = new TTest().pairedTTest(meanUtilization.get(0).stream().mapToDouble(i -> i).toArray(), meanUtilization.get(2).stream().mapToDouble(i -> i).toArray(), 0.05);
  //   // tests2[4] = new TTest().pairedTTest(avgIterationCount.get(0).stream().mapToDouble(i -> i).toArray(), avgIterationCount.get(2).stream().mapToDouble(i -> i).toArray(), 0.1);
  // }

  // public void printTestValue() {
  //   System.out.println("\nPaired-T p-value 1");
  //   System.out.println("DOI : " + t1[0]);
  //   System.out.println("Makespan : " + t1[1]);
  //   System.out.println("Avg. Response Time : " + t1[2]);
  //   System.out.println("Mean Utilization : " + t1[3]);
  //   System.out.println("Avg. Iteration Count : " + t1[4]);

  //   System.out.println("\nPaired-T p-value 2");
  //   System.out.println("DOI : " + t2[0]);
  //   System.out.println("Makespan : " + t2[1]);
  //   System.out.println("Avg. Response Time : " + t2[2]);
  //   System.out.println("Mean Utilization : " + t2[3]);
  //   // System.out.println("Avg. Iteration Count : " + t2[4]);

  //   System.out.println("\nT-Test 1");
  //   System.out.println("DOI Test : " + tests1[0]);
  //   System.out.println("Makespan Test : " + tests1[1]);
  //   System.out.println("Avg. Response Time Test : " + tests1[2]);
  //   System.out.println("Mean Utilization Test : " + tests1[3]);
  //   System.out.println("Avg. Iteration Count Test : " + tests1[4]);

  //   System.out.println("\nT-Test 2");
  //   System.out.println("DOI Test : " + tests2[0]);
  //   System.out.println("Makespan Test : " + tests2[1]);
  //   System.out.println("Avg. Response Time Test : " + tests2[2]);
  //   System.out.println("Mean Utilization Test : " + tests2[3]);
  //   // System.out.println("Avg. Iteration Count Test : " + tests2[4]);
  // }
}
