package me.nathan3882.excelreporter.reporting;

import java.util.LinkedList;

public class BugFixesReport implements IReport {

    private final LinkedList<String> columnValues;
    private final LinkedList<String> rowsData;
    private LinkedList<IReportRow> reportRows;

    public BugFixesReport(IColumn iColumn, LinkedList<String> rowsData) {
        columnValues = iColumn.getValues();
        this.rowsData = rowsData;

        for (String rowDatum : rowsData) {

        }
    }

    public BugFixesReport generateReport(LinkedList<String> rowsData) {


        return null;
    }

    @Override
    public LinkedList<String> getColumnValues() {
        return columnValues;
    }

    @Override
    public LinkedList<String> getRowsData() {
        return this.rowsData;
    }

    @Override
    public LinkedList<IReportRow> getReportRows() {
        return this.reportRows;
    }
}
