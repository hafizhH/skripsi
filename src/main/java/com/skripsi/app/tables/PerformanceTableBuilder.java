package com.skripsi.app.tables;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiFunction;

import org.cloudsimplus.builders.tables.AbstractTable;
import org.cloudsimplus.builders.tables.CsvTable;
import org.cloudsimplus.builders.tables.TableBuilderAbstract;
import org.cloudsimplus.builders.tables.TableColumn;

import com.skripsi.app.evaluation.Performance;

public class PerformanceTableBuilder extends TableBuilderAbstract<Performance> {
  private List<? extends Performance> list;

  public PerformanceTableBuilder(String title, List<? extends Performance> list, String pathString) {
    super(list);
    this.list = list;
    CsvTable csvTable = new CsvTable();
    try {
      Path path = Paths.get(pathString, title + "-performance.csv");
      csvTable.setPrintStream(new PrintStream(new File(path.toUri())));
      super.setTable(csvTable);
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    this.setTitle(title);
  }

  protected void createTableColumns() {
    if (this.getTable().getClass() == CsvTable.class) {
      this.addColumn(this.getTable().newColumn("Trial ID"), (performance) -> {
        return performance.getName();
      });
      this.addColumn(this.getTable().newColumn("DOI"), (performance) -> {
        return performance.getDoi();
      });
      this.addColumn(this.getTable().newColumn("Makespan"), (performance) -> {
        return performance.getMakespan();
      });
      this.addColumn(this.getTable().newColumn("Avg. Response Time"), (performance) -> {
        return performance.getAvgResponseTime();
      });
      this.addColumn(this.getTable().newColumn("Mean Utilization"), (performance) -> {
        return performance.getMeanUtilization();
      });
      this.addColumn(this.getTable().newColumn("Avg. Iteration Count"), (performance) -> {
        return performance.getAvgIterationCount();
      });
      for (int i = 0; i < list.get(0).getFinishedVm().size(); i++) {
        this.addVmColumn(this.getTable().newColumn("Num. Cloudlets VM-"+i), i);
      }
      for (int i = 0; i < list.get(0).getFinishedVm().size(); i++) {
        this.addVmProcTimeColumn(this.getTable().newColumn("Processing Time VM-"+i), i);
      }
    }
  }

  public TableBuilderAbstract<Performance> addVmColumn(TableColumn col, int index) {
    return this.addColumn(col, (performance) -> {
      return performance.getFinishedVm().get(index).getCloudletScheduler().getCloudletFinishedList().size();
    });
  }

  public TableBuilderAbstract<Performance> addVmProcTimeColumn(TableColumn col, int index) {
    return this.addColumn(col, (performance) -> {
      return performance.getExecTime()[index];
    });
  }
}
