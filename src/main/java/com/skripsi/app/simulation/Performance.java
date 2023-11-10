package com.skripsi.app.simulation;

import java.util.List;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

public class Performance {
  private String name;
	private List<Cloudlet> finishedCloudlet;
	private List<Vm> finishedVm;
	
	private double[] execTime;
	private double doi;
	private double makespan;
	private double avgResponseTime;
	private double meanUtilization;

	private boolean isCalculated;

  public Performance(String name, List<Cloudlet> finishedCloudlet, List<Vm> finishedVm) {
    this.name = name;
		this.finishedCloudlet = finishedCloudlet;
		this.finishedVm = finishedVm;
		this.isCalculated = false;
		calculate();
	}

	private double[] calcExecTime() {
		double[] execTime = new double[finishedVm.size()];
		for (int i = 0; i < finishedCloudlet.size(); i++) {
			Cloudlet cl = finishedCloudlet.get(i);
			execTime[finishedVm.indexOf(cl.getVm())] += cl.getFinishTime() - cl.getExecStartTime();
		}
		return execTime;
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
			start = Math.min(start, finishedCloudlet.get(i).getExecStartTime());
			end = Math.max(end, finishedCloudlet.get(i).getFinishTime());
		}
		return end - start;
	}

	private double calcAvgResponseTime() {
		double responseTimeSum = 0.0;
		for (int i = 0; i < finishedCloudlet.size(); i++) {
			responseTimeSum += (finishedCloudlet.get(i).getFinishTime() - finishedCloudlet.get(i).getArrivedTime());
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

	public void printEvaluation() {
		System.out.println("Total Finished Cloudlets : " + this.finishedCloudlet.size());
		System.out.println("Makespan : " + this.makespan);
		System.out.println("Avg. Response Time : " + this.avgResponseTime);
		System.out.println("VM Mean Utilization Rate : " + this.meanUtilization);
		System.out.println("VM Load DOI : " + this.doi);
	}

	public void calculate() {
		this.execTime = calcExecTime();
		this.doi = calcDOI();
		this.makespan = calcMakespan();
		this.avgResponseTime = calcAvgResponseTime();
		this.meanUtilization = calcMeanUtilization();
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
}