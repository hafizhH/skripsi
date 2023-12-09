package com.skripsi.app.tables;

import java.util.Iterator;
import java.util.List;

import org.cloudsimplus.builders.tables.CsvTable;
import org.cloudsimplus.builders.tables.TableColumn;

public class TexTable extends CsvTable {
  private String rowOpeningStr;
  private String rowClosingStr;

  public TexTable() {
    this("");
  }

  public TexTable(String title) {
    super(title);
    this.setColumnSeparator(" & ");
    this.rowOpeningStr = "";
    this.rowClosingStr = " \\\\ \\hline%n";
  }

  public void setRowOpeningClosingStr(String opening, String closing) {
    this.rowOpeningStr = opening;
    this.rowClosingStr = closing;
  }

  @Override
  protected String rowOpening() {
    return this.rowOpeningStr;
  }

  @Override
  protected String rowClosing() {
    return this.rowClosingStr;
  }

  @Override
  protected void printColumnHeaders() {
    return;
  }

  public void print() {
    this.printTableOpening();
    this.printTitle();
    this.printColumnHeaders();
    Iterator<List<Object>> rowIt = this.getRows().iterator();
    while(rowIt.hasNext()) {
      List<Object> row = rowIt.next();
      printRow(row, (rowIt.hasNext()) ? true : false);
    }
    this.printTableClosing();
    this.getPrintStream().println();
 }

 private void printRow(List<Object> row, boolean includeRowClosing) {
    this.getPrintStream().printf(this.rowOpening());
    List<TableColumn> cols = this.getColumns().stream().limit((long)Math.min(this.getColumns().size(), row.size())).toList();
    int idxCol = 0;
    Iterator<TableColumn> var4 = cols.iterator();

    while(var4.hasNext()) {
      TableColumn col = (TableColumn)var4.next();
      this.getPrintStream().print(col.generateData(row.get(idxCol++)));
    }
    if (includeRowClosing) {
      this.getPrintStream().printf(this.rowClosing());
    }
  }
}
