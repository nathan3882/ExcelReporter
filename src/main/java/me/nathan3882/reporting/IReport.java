package me.nathan3882.reporting;

import me.nathan3882.parsing.ExportType;
import me.nathan3882.reporting.columns.Pair.ColumnNameValuePair;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.util.LinkedList;

public interface IReport {

    void export(ExportType exportType, File toThisFile) throws JRException;

    LinkedList<? extends IReportRow> createReportRowsFromDataRows(LinkedList<String> rowsData);

    /**
     * This allows a user to return a LinkedList in which contains what the
     * IColumn implementation of getValues() contains.
     *
     * @return the list of column values - like so: "CONSTANT_NAME,-=3" = index of 3
     */
    LinkedList<ColumnNameValuePair> getColumnValues();

    LinkedList<String> getDataRows();

    LinkedList<? extends IReportRow> getReportRows();
}
