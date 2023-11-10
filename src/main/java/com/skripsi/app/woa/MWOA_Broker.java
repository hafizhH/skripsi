package com.skripsi.app.woa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.util.TimeUtil;
import org.cloudsimplus.vms.Vm;

import com.skripsi.app.utils.Vector;

public class MWOA_Broker extends WOA_Broker {
  private MWOA mwoa;
  private List<Vector> solutionHistory;

  public MWOA_Broker(CloudSimPlus sim, String name, int maxIteration, int agentNum, int allocatedAgentNum, int prevSolutionNum) {
    super(sim, name, maxIteration, agentNum);
    this.mwoa = new MWOA(maxIteration, agentNum, allocatedAgentNum, prevSolutionNum);
    this.solutionHistory = new ArrayList<Vector>();
  }

  private void executeWOA(Cloudlet cl, Function<Integer, Double> fitnessFunction, List<Vector> solutionHistory) {
    this.currentCloudlet = cl;
    mwoa.init(new double[]{ (double)this.targetVmList.size()-1 }, fitnessFunction, solutionHistory);
    mwoa.run();
    List<Map.Entry<Vector, Double>> results = mwoa.getResults();
    int selIndex = (int) Math.round(results.get(0).getKey().getElm(0));
    this.selectedVmIndex = selIndex;
    Vector roundedSolution = new Vector(Arrays.asList(new Double[]{(double) Math.round(results.get(0).getKey().getElm(0))}));
    solutionHistory.add(roundedSolution);
    return;
  }

  @Override
  protected Vm defaultVmMapper(Cloudlet cloudlet) {
    if (cloudlet.isBoundToVm()) {
      return cloudlet.getVm();
    } else if (this.getVmExecList().isEmpty()) {
      return Vm.NULL;
    } else {
      this.targetVmList = getVmCreatedList();
      double startRealtime = TimeUtil.currentTimeSecs();
      executeWOA(cloudlet, super::calculateFitness, solutionHistory);
      double execRealtime = TimeUtil.currentTimeSecs() - startRealtime;
      cloudlet.setSubmissionDelay(cloudlet.getSubmissionDelay() + execRealtime);
      return this.targetVmList.get(this.selectedVmIndex);
    }
  }
}
