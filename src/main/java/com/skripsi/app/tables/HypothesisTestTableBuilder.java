package com.skripsi.app.tables;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

import org.cloudsimplus.builders.tables.AbstractTable;
import org.cloudsimplus.builders.tables.CsvTable;
import org.cloudsimplus.builders.tables.TableBuilderAbstract;

import com.skripsi.app.evaluation.HypothesisTest;
import com.skripsi.app.utils.Stats;

public class HypothesisTestTableBuilder extends TableBuilderAbstract<HypothesisTest> {
  private String strFormat = "%-30.30s";
  private String strFormat2 = "%-10.10s";
  private String realFormat = "%4.6f";

  public HypothesisTestTableBuilder(String title, List<? extends HypothesisTest> list) {
    super(list);
    this.setTitle(title);
  }

  public HypothesisTestTableBuilder(String title, List<? extends HypothesisTest> list, String pathString) {
    this(title, list, pathString, new CsvTable());
  }

  public HypothesisTestTableBuilder(String title, List<? extends HypothesisTest> list, String pathString, AbstractTable table) {
    super(list);
    try {
      String ext = (table.getClass() == TexTable.class) ? ".tex" : ".csv";
      Path path = Paths.get(pathString, title + "-HypothesesTest" + ext);
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
      this.addColumn(this.getTable().newColumn("Tests"), (ht) -> {
        return ht.getName();
      });
      this.addColumn(this.getTable().newColumn("Test Score"), (ht) -> {
        return df.format(ht.getTestScore());
      });
      this.addColumn(this.getTable().newColumn("Direction"), (ht) -> {
        return (ht.isLeftTailed()) ? "Kiri" : "Kanan";
      });
      this.addColumn(this.getTable().newColumn("Critical Value"), (ht) -> {
        return df.format(ht.getCriticalValue());
      });
      this.addColumn(this.getTable().newColumn("Significance"), (ht) -> {
        return df.format(ht.getSignificance());
      });
      this.addColumn(this.getTable().newColumn("Result"), (ht) -> {
        return (ht.isH0Rejected()) ? "Tolak H0" : "Tidak Tolak H0";
      });
    } else {
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat, "Tests"),
        String.format(this.strFormat, "Name"), this.strFormat), (ht) -> {
          return ht.getName();
        });
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Test Score"), " ", this.realFormat),
        (ht) -> {
          return ht.getTestScore();
        });
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Direction")),
        (ht) -> {
          return (ht.isLeftTailed()) ? "Kiri" : "Kanan";
        });
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Critical Value"), " ", this.realFormat),
        (ht) -> {
          return ht.getCriticalValue();
        });
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Significance"), " ", this.realFormat),
        (ht) -> {
          return ht.getSignificance();
        });
      this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Result")),
        (ht) -> {
          return (ht.isH0Rejected()) ? "Tolak H0" : "Tidak Tolak H0";
        });
    }
  }
}
