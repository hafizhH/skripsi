package com.skripsi.app.simulation;

public class PerformanceCompare {

  private Performance perfA;
  private Performance perfB;

  private double makespanDiff;
  private double avgResponseTimeDiff;
  private double meanUtilizationDiff;
  private double doiDiff;

  private boolean isCompared;

  public PerformanceCompare(Performance a, Performance b) {
    this.perfA = a;
    this.perfB = b;
    if (!perfA.isCalculated())
			this.perfA.calculate();
		if (!perfB.isCalculated())
			this.perfB.calculate();
    this.isCompared = false;
    if (!this.isCompared) {
      compare();
    }
  }

  public void compare() {
    this.isCompared = true;
    this.makespanDiff = perfA.getMakespan() - perfB.getMakespan();
    this.avgResponseTimeDiff = perfA.getAvgResponseTime() - perfB.getAvgResponseTime();
    this.meanUtilizationDiff = perfA.getMeanUtilization() - perfB.getMeanUtilization();
    this.doiDiff = perfA.getDoi() - perfB.getDoi();
  }

  public void printComparison() {
    if (!this.isCompared) {
      compare();
    }
		System.out.println("\nPerformance Comparison");
    System.out.println(this.perfA.getName() + " vs " + this.perfB.getName());
		System.out.println("Total Finished Cloudlets : " + this.perfA.getFinishedCloudlet().size() + " vs " + this.perfA.getFinishedCloudlet().size());
		System.out.println("Makespan : " + this.perfA.getMakespan() + " vs " + this.perfB.getMakespan() + " (diff : " + this.makespanDiff + ")");
		System.out.println("Avg. Response Time : " + this.perfA.getAvgResponseTime() + " vs " + this.perfB.getAvgResponseTime() + " (diff : " + this.avgResponseTimeDiff + ")");
		System.out.println("VM Mean Utilization Rate : " + this.perfA.getMeanUtilization() + " vs " + this.perfB.getMeanUtilization() + " (diff : " + this.meanUtilizationDiff + ")");
		System.out.println("VM Load DOI : " + this.perfA.getDoi() + " vs " + this.perfB.getDoi() + " (diff : " + this.doiDiff + ")");
	}
}
