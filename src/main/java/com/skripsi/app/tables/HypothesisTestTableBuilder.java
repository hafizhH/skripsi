package com.skripsi.app.tables;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.cloudsimplus.builders.tables.CsvTable;
import org.cloudsimplus.builders.tables.TableBuilderAbstract;

import com.skripsi.app.evaluation.HypothesisTest;
import com.skripsi.app.utils.Stats;

public class HypothesisTestTableBuilder extends TableBuilderAbstract<HypothesisTest> {
  private String strFormat = "%-30.30s";
  private String strFormat2 = "%-10.10s";
  private String realFormat = "%4.4f";

  public HypothesisTestTableBuilder(String title, List<? extends HypothesisTest> list) {
    super(list);
    this.setTitle(title);
  }

  public HypothesisTestTableBuilder(String title, List<? extends HypothesisTest> list, String pathString) {
    super(list);
    CsvTable csvTable = new CsvTable();
    try {
      Path path = Paths.get(pathString, title + "-HypothesesTest.csv");
      csvTable.setPrintStream(new PrintStream(new File(path.toUri())));
      super.setTable(csvTable);
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    this.setTitle(title);
  }

  protected void createTableColumns() {
    if (this.getTable().getClass() == CsvTable.class) {
      this.addColumn(this.getTable().newColumn("Tests"), (ht) -> {
        return ht.getName();
      });
      this.addColumn(this.getTable().newColumn("Test Score"), (ht) -> {
        return ht.getName();
      });
      this.addColumn(this.getTable().newColumn("p-value"), (ht) -> {
        return ht.getpValue();
      });
      this.addColumn(this.getTable().newColumn("Result"), (ht) -> {
        return ht.isH0Rejected();
      });
      this.addColumn(this.getTable().newColumn("Result2"), (ht) -> {
        return ht.isH0Rejected2();
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
        this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "p-value"), " ", this.realFormat),
          (ht) -> {
            return ht.getpValue();
          });
        this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Result")),
          (ht) -> {
            return ht.isH0Rejected();
          });
        this.addColumn(this.getTable().newColumn(String.format(this.strFormat2, "Result2")),
          (ht) -> {
            return ht.isH0Rejected2();
          });
    }
  }
}
