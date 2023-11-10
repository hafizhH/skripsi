package com.skripsi.app.utils;

import java.util.List;
import java.util.Objects;

import org.cloudsimplus.builders.tables.CsvTable;
import org.cloudsimplus.builders.tables.MarkdownTable;
import org.cloudsimplus.builders.tables.Table;
import org.cloudsimplus.builders.tables.TableBuilderAbstract;

public class StatsTableBuilder extends TableBuilderAbstract<Stats> {
  // public static final String DEF_FORMAT = "%d";
  // private static final String SECONDS = "Seconds";
  // private static final String CPU_CORES = "CPU cores";
  // private static final String ID = "ID";
  // private static final String MI = "MI";
  // private String timeFormat = "%.1f";
  // private String idFormat = "%d";
  // private String lengthFormat = "%d";
  // private String peFormat = "%d";
  private String strFormat = "%-30.30s";
  private String strFormat2 = "%-10.10s";
  private String realFormat = "%4.4f";

  public StatsTableBuilder(String title, List<? extends Stats> list) {
    super(list);
    this.setTitle(title);
  }

  public StatsTableBuilder(String title, List<? extends Stats> list, Table table) {
    super(list, table);
    this.setTitle(title);
  }

  protected void createTableColumns() {
    if (this.getTable().getClass() == CsvTable.class) {
      this.addColumn(this.getTable().newColumn("Stats"), (stats) -> {
        return stats.getName();
      });
      this.addColumn(this.getTable().newColumn("Min"), (stats) -> {
        return stats.calcQuartile()[0];
      });
      this.addColumn(this.getTable().newColumn("Max"), (stats) -> {
        return stats.calcQuartile()[1];
      });
      this.addColumn(this.getTable().newColumn("Mean"), (stats) -> {
        return stats.calcMean();
      });
      this.addColumn(this.getTable().newColumn("Stdev"), (stats) -> {
        return stats.calcStdevP();
      });
    } else {
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat, "Stats"),
          String.format(this.strFormat, "Name"), this.strFormat), (stats) -> {
            return stats.getName();
          });
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Min"), "Secs", this.realFormat), (stats) -> {
            return stats.calcQuartile()[0];
          });
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Max"), "Secs", this.realFormat), (stats) -> {
            return stats.calcQuartile()[1];
          });
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Mean"), "Secs", this.realFormat), (stats) -> {
            return stats.calcMean();
          });
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Stdev"), "Secs", this.realFormat), (stats) -> {
            return stats.calcStdevP();
          });
    }
    // this.addColumn(this.getTable().newColumn("DC", "ID", this.idFormat),
    // (cloudlet) -> {
    // return cloudlet.getVm().getHost().getDatacenter().getId();
    // });
    // this.addColumn(this.getTable().newColumn("Host", "ID", this.idFormat),
    // (cloudlet) -> {
    // return cloudlet.getVm().getHost().getId();
    // });
    // this.addColumn(this.getTable().newColumn("Host PEs ", "CPU cores",
    // this.peFormat), (cloudlet) -> {
    // return cloudlet.getVm().getHost().getWorkingPesNumber();
    // });
    // this.addColumn(this.getTable().newColumn("VM", "ID", this.idFormat),
    // (cloudlet) -> {
    // return cloudlet.getVm().getId();
    // });
    // this.addColumn(this.getTable().newColumn(" VM PEs", "CPU cores",
    // this.peFormat), (cloudlet) -> {
    // return cloudlet.getVm().getPesNumber();
    // });
    // this.addColumn(this.getTable().newColumn("CloudletLen", "MI",
    // this.lengthFormat), Cloudlet::getLength);
    // this.addColumn(this.getTable().newColumn("FinishedLen", "MI",
    // this.lengthFormat), Cloudlet::getFinishedLengthSoFar);
    // this.addColumn(this.getTable().newColumn("CloudletPEs", "CPU cores",
    // this.peFormat), Cloudlet::getPesNumber);
    // this.addColumn(this.getTable().newColumn("StartTime", "Seconds",
    // this.timeFormat), Cloudlet::getExecStartTime);
    // this.addColumn(this.getTable().newColumn("FinishTime", "Seconds",
    // this.timeFormat), Cloudlet::getFinishTime);
    // this.addColumn(this.getTable().newColumn("ExecTime", "Seconds",
    // this.timeFormat), Cloudlet::getActualCpuTime);
  }

  public StatsTableBuilder setRealFormat(String realFormat) {
    this.realFormat = (String) Objects.requireNonNullElse(realFormat, "");
    return this;
  }

  public final String getRealFormat() {
    return this.realFormat;
  }

  public StatsTableBuilder setStrFormat(String strFormat) {
    this.strFormat = (String) Objects.requireNonNullElse(strFormat, "");
    return this;
  }

  public final String getStrFormat() {
    return this.strFormat;
  }

  // public StatsTableBuilder setTimeFormat(String timeFormat) {
  // this.timeFormat = (String)Objects.requireNonNullElse(timeFormat, "");
  // return this;
  // }

  // public StatsTableBuilder setLengthFormat(String lengthFormat) {
  // this.lengthFormat = (String)Objects.requireNonNullElse(lengthFormat, "");
  // return this;
  // }

  // public StatsTableBuilder setIdFormat(String idFormat) {
  // this.idFormat = (String)Objects.requireNonNullElse(idFormat, "");
  // return this;
  // }

  // public StatsTableBuilder setPeFormat(String peFormat) {
  // this.peFormat = (String)Objects.requireNonNullElse(peFormat, "");
  // return this;
  // }

  // public final String getTimeFormat() {
  // return this.timeFormat;
  // }

  // public final String getIdFormat() {
  // return this.idFormat;
  // }

  // public final String getLengthFormat() {
  // return this.lengthFormat;
  // }

  // public final String getPeFormat() {
  // return this.peFormat;
  // }
}
