package com.skripsi.app.tables;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

import java.util.Iterator;

import org.cloudsimplus.builders.tables.AbstractTable;
import org.cloudsimplus.builders.tables.CsvTable;
import org.cloudsimplus.builders.tables.TableBuilderAbstract;
import org.cloudsimplus.builders.tables.TableColumn;
import org.cloudsimplus.cloudlets.CloudletExecution;

import com.skripsi.app.evaluation.SimulationPerformance;

public class SimulationPerformancesTableBuilder extends TableBuilderAbstract<SimulationPerformance> {
  private List<? extends SimulationPerformance> list;
  private boolean iterationIncluded;

  public SimulationPerformancesTableBuilder(String title, List<? extends SimulationPerformance> list, String pathString, boolean iterationIncluded) {
    this(title, list, pathString, new CsvTable(), iterationIncluded);
  }

  public SimulationPerformancesTableBuilder(String title, List<? extends SimulationPerformance> list, String pathString, AbstractTable table, boolean iterationIncluded) {
    super(list);
    this.list = list;
    this.iterationIncluded = iterationIncluded;
    try {
      String ext = (table.getClass() == TexTable.class) ? ".tex" : ".csv";
      Path path = Paths.get(pathString, title + "-performance" + ext);
      table.setPrintStream(new PrintStream(new File(path.toUri())));
      super.setTable(table);
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    this.setTitle(title);
  }

  protected void createTableColumns() {
    if (this.getTable().getClass() == TexTable.class || this.getTable().getClass() == CsvTable.class) {
      DecimalFormat df = new DecimalFormat("#0.#####");
      this.addColumn(this.getTable().newColumn("Trial ID"), (performance) -> {
        return performance.getTrialIndex() + 1;
      });
      this.addColumn(this.getTable().newColumn("DOI"), (performance) -> {
        return df.format(performance.getDoi());
      });
      this.addColumn(this.getTable().newColumn("Makespan"), (performance) -> {
        return df.format(performance.getMakespan());
      });
      this.addColumn(this.getTable().newColumn("Avg. Response Time"), (performance) -> {
        return df.format(performance.getAvgResponseTime());
      });
      this.addColumn(this.getTable().newColumn("Mean Utilization"), (performance) -> {
        return df.format(performance.getMeanUtilization());
      });
      if (iterationIncluded) {
        this.addColumn(this.getTable().newColumn("Avg. Iteration Count"), (performance) -> {
          return df.format(performance.getAvgIterationCount());
        });
      }
      // for (int i = 0; i < list.get(0).getFinishedVm().size(); i++) {
      //   this.addVmColumn(this.getTable().newColumn("Assigned Cloudlets VM-"+i), i);
      // }
      // for (int i = 0; i < list.get(0).getFinishedVm().size(); i++) {
      //   this.addVmProcTimeColumn(this.getTable().newColumn("Processing Time VM-"+i), i);
      // }
    }
  }

  public TableBuilderAbstract<SimulationPerformance> addVmColumn(TableColumn col, int index) {
    return this.addColumn(col, (performance) -> {
      String res = "";
      Iterator<CloudletExecution> it = performance.getFinishedVm().get(index).getCloudletScheduler().getCloudletFinishedList().iterator();
      while (it.hasNext()) {
        res += (it.next().getId() + 1) + ",";
      }
      if (res.length() > 0) {
        res = res.substring(0, res.length()-1);
      }
      return res + "";
    });
  }

  public TableBuilderAbstract<SimulationPerformance> addVmProcTimeColumn(TableColumn col, int index) {
    return this.addColumn(col, (performance) -> {
      return performance.getExecTime()[index];
    });
  }
}
