package com.skripsi.app.utils;

import java.util.List;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

public class Performance {
	private List<Cloudlet> finishedCloudlet;
	private List<Vm> finishedVm;
	
	private double[] execTime;
	private double doi;
	private double makespan;
	private double avgResponseTime;
	private double meanUtilization;

	private boolean isCalculated;

  public Performance(List<Cloudlet> finishedCloudlet, List<Vm> finishedVm) {
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

	private void calculate() {
		this.execTime = calcExecTime();
		this.doi = calcDOI();
		this.makespan = calcMakespan();
		this.avgResponseTime = calcAvgResponseTime();
		this.meanUtilization = calcMeanUtilization();
	}

	public static void compare(Performance a, Performance b) {
		if (!a.isCalculated)
			a.calculate();
		if (!b.isCalculated)
			b.calculate();
		System.out.println("\nPerformance Comparison");
		System.out.println("Total Finished Cloudlets : " + a.finishedCloudlet.size() + " vs " + b.finishedCloudlet.size());
		System.out.println("Makespan : " + a.makespan + " vs " + b.makespan + " (diff : " + (a.makespan - b.makespan) + ")");
		System.out.println("Avg. Response Time : " + a.avgResponseTime + " vs " + b.avgResponseTime + " (diff : " + (a.avgResponseTime - b.avgResponseTime) + ")");
		System.out.println("VM Mean Utilization Rate : " + a.meanUtilization + " vs " + b.meanUtilization + " (diff : " + (a.meanUtilization - b.meanUtilization) + ")");
		System.out.println("VM Load DOI : " + a.doi + " vs " + b.doi + " (diff : " + (a.doi - b.doi) + ")");
	}
}