package com.skripsi.app.evaluation;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletExecution;
import org.cloudsimplus.vms.Vm;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Performance {
  private String name;
	private List<Cloudlet> finishedCloudlet;
	private List<Vm> finishedVm;
  private List<List<Double>> bestFitnessHistory;
	
	private double[] execTime;
	private double doi;
	private double makespan;
	private double avgResponseTime;
	private double meanUtilization;
  private double avgIterationCount;

	private boolean isCalculated;

  public Performance(String name, List<Cloudlet> finishedCloudlet, List<Vm> finishedVm) {
    this(name, finishedCloudlet, finishedVm, null);
	}

  public Performance(String name, List<Cloudlet> finishedCloudlet, List<Vm> finishedVm, List<List<Double>> bestFitnessHistory) {
    this.bestFitnessHistory = bestFitnessHistory;
    this.name = name;
		this.finishedCloudlet = finishedCloudlet;
		this.finishedVm = finishedVm;
		this.isCalculated = false;
		calculate();
	}

	// private double[] calcExecTime() {
	// 	double[] execTime = new double[finishedVm.size()];
	// 	for (int i = 0; i < finishedCloudlet.size(); i++) {
	// 		Cloudlet cl = finishedCloudlet.get(i);
	// 		execTime[finishedVm.indexOf(cl.getVm())] += cl.getFinishTime() - cl.getStartTime();
	// 	}
	// 	return execTime;
	// }

  // private double[] calcExecTime() {
	// 	double[] execTime = new double[finishedVm.size()];
	// 	for (int i = 0; i < finishedVm.size(); i++) {
  //     List<CloudletExecution> vmCloudlets = finishedVm.get(i).getCloudletScheduler().getCloudletFinishedList();
  //     double start = Double.MAX_VALUE;
  //     double end = 0.0;
  //     for (int j = 0; j < vmCloudlets.size(); j++) {
  //       start = Math.min(start, vmCloudlets.get(j).getCloudlet().getStartTime());
  //       end = Math.max(end, vmCloudlets.get(j).getCloudlet().getFinishTime());
  //     }
	// 		execTime[i] += end - start;
	// 	}
	// 	return execTime;
	// }

	private double calcDOI() {
		double execTimeSum = 0.0;
		for (int i = 0; i < finishedVm.size(); i++) {
			execTimeSum += execTime[i];
		}
		double meanExecTime = execTimeSum / finishedVm.size();
		double diffMeanExecTimeSum = 0.0;
		for (int i = 0; i < finishedVm.size(); i++) {
			diffMeanExecTimeSum += ((execTime[i] - meanExecTime) * (execTime[i] - meanExecTime));
		}
		return Math.sqrt(diffMeanExecTimeSum / finishedVm.size());
	}

	private double calcMakespan() {
		double start = Double.MAX_VALUE;
		double end = 0.0;
		for (int i = 0; i < finishedCloudlet.size(); i++) {
			start = Math.min(start, finishedCloudlet.get(i).getStartTime());
			end = Math.max(end, finishedCloudlet.get(i).getFinishTime());
		}
		return end - start;
	}

	private double calcAvgResponseTime() {
		double responseTimeSum = 0.0;
		for (int i = 0; i < finishedCloudlet.size(); i++) {
			responseTimeSum += (finishedCloudlet.get(i).getFinishTime() - finishedCloudlet.get(i).getDcArrivalTime());
		}
		return (responseTimeSum / finishedCloudlet.size());
	}

	private double calcMeanUtilization() {
		double vmUtilSum = 0.0;
		for (int i = 0; i < finishedVm.size(); i++) {
			vmUtilSum += (execTime[i] / makespan);
		}
		return vmUtilSum / finishedVm.size();
	}

  private double calcAvgIterationCount() {
    double iterationCountSum = 0.0;
    for (int i = 0; i < bestFitnessHistory.size(); i++) {
      iterationCountSum += bestFitnessHistory.get(i).size();
		}
		return (iterationCountSum / bestFitnessHistory.size());
  }

  public void calculate() {
		// this.execTime = calcExecTime();
    this.execTime = finishedVm.stream().mapToDouble(vm -> vm.getLastBusyTime() - vm.getStartTime()).toArray();
		this.doi = calcDOI();
		this.makespan = calcMakespan();
		this.avgResponseTime = calcAvgResponseTime();
		this.meanUtilization = calcMeanUtilization();
    if (this.bestFitnessHistory != null) {
      this.avgIterationCount = calcAvgIterationCount();
    }
	}

	// public void printEvaluation() {
	// 	System.out.println("Total Finished Cloudlets : " + this.finishedCloudlet.size());
	// 	System.out.println("Makespan : " + this.makespan);
	// 	System.out.println("Avg. Response Time : " + this.avgResponseTime);
	// 	System.out.println("VM Mean Utilization Rate : " + this.meanUtilization);
	// 	System.out.println("VM Load DOI : " + this.doi);
	// }

  public void plotFitnessGraph(int index) {
    DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
    for (int i = 0; i < bestFitnessHistory.get(index).size(); i++) {
      line_chart_dataset.addValue(bestFitnessHistory.get(index).get(i), "Fitness Value", Integer.toString(i));
    }

    JFreeChart lineChartObject = ChartFactory.createLineChart("Fitness Function Convergence Graph", "Iteration", "Fitness Value",
      line_chart_dataset,PlotOrientation.VERTICAL, true,true,false);

    int width = 640;    /* Width of the image */
    int height = 480;   /* Height of the image */
    Path path = Paths.get("src/main/java/com/skripsi/app/output", String.format("%s-convergence-cl%s.jpeg", this.name, index));
    File lineChart = new File(path.toUri());
    try {
      ChartUtils.saveChartAsJPEG(lineChart, lineChartObject, width, height);
    } catch (Exception e) {
      System.out.print(e);
    }
  }

  public String getName() {
    return name;
  }

  public List<Cloudlet> getFinishedCloudlet() {
    return finishedCloudlet;
  }

  public List<Vm> getFinishedVm() {
    return finishedVm;
  }

  public double[] getExecTime() {
    return execTime;
  }

  public double getDoi() {
    return doi;
  }

  public double getMakespan() {
    return makespan;
  }

  public double getAvgResponseTime() {
    return avgResponseTime;
  }

  public double getMeanUtilization() {
    return meanUtilization;
  }

  public boolean isCalculated() {
    return isCalculated;
  }

  public List<List<Double>> getBestFitnessHistory() {
    return bestFitnessHistory;
  }

  public double getAvgIterationCount() {
    return avgIterationCount;
  }

  public void setName(String name) {
    this.name = name;
  }
}