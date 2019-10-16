package me.nathan3882.excelreporter.reporting;

import me.nathan3882.excelreporter.parsing.ExportType;

import java.util.LinkedList;

public interface IReport {
    default void export(ExportType exportType) {
        switch (exportType) {
            case PDF:

                break;
            default:
                break;
        }
    }

    /**
     * This allows a user to return a LinkedList in which contains what the
     * IColumn implementation of getValues() contains.
     *
     * @return the list of column values - like so: "CONSTANT_NAME,-=3" = index of 3
     */
    LinkedList<String> getColumnValues();

    LinkedList<String> getRowsData();

    LinkedList<IReportRow> getReportRows();
}
