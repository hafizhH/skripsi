package com.skripsi.app.evaluation;

import java.util.List;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

public class SimulationPerformance {
  private String name;
  private int trialIndex;

  private List<Cloudlet> finishedCloudlet;
	private List<Vm> finishedVm;
  private List<List<Double>> bestFitnessHistory;
	
	private double[] execTime;
	private double doi;
	private double makespan;
	private double avgResponseTime;
	private double meanUtilization;
  private double avgIterationCount;

  private boolean includeIteration;
  private boolean isCalculated;

  public SimulationPerformance(String name, int trialIndex, List<Cloudlet> finishedCloudlet, List<Vm> finishedVm) {
    this(name, trialIndex, finishedCloudlet, finishedVm, null);
	}

  public SimulationPerformance(String name, int trialIndex, List<Cloudlet> finishedCloudlet, List<Vm> finishedVm, List<List<Double>> bestFitnessHistory) {
    this.bestFitnessHistory = bestFitnessHistory;
    this.name = name;
    this.trialIndex = trialIndex;
		this.finishedCloudlet = finishedCloudlet;
		this.finishedVm = finishedVm;
		this.isCalculated = false;
    this.includeIteration = (bestFitnessHistory != null) ? true : false;
		calculate();
	}

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
    if (this.includeIteration) {
      this.avgIterationCount = calcAvgIterationCount();
    }
	}

  // public void plotFitnessGraph(int index) {
  //   DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
  //   for (int i = 0; i < bestFitnessHistory.get(index).size(); i++) {
  //     line_chart_dataset.addValue(bestFitnessHistory.get(index).get(i), "Fitness Value", Integer.toString(i));
  //   }

  //   JFreeChart lineChartObject = ChartFactory.createLineChart("Fitness Function Convergence Graph", "Iteration", "Fitness Value",
  //     line_chart_dataset,PlotOrientation.VERTICAL, true,true,false);

  //   int width = 640;    /* Width of the image */
  //   int height = 480;   /* Height of the image */
  //   Path path = Paths.get("src/main/java/com/skripsi/app/output", String.format("%s-convergence-cl%s.jpeg", this.name, index));
  //   File lineChart = new File(path.toUri());
  //   try {
  //     ChartUtils.saveChartAsJPEG(lineChart, lineChartObject, width, height);
  //   } catch (Exception e) {
  //     System.out.print(e);
  //   }
  // }

  public String getName() {
    return name;
  }
  
	public int getTrialIndex() {
    return trialIndex;
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

	public boolean isIncludeIteration() {
    return includeIteration;
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