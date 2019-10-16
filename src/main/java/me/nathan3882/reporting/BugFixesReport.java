package me.nathan3882.reporting;

import me.nathan3882.parsing.ExportType;
import me.nathan3882.parsing.Parser;
import me.nathan3882.reporting.columns.BugFixesColumn;
import me.nathan3882.reporting.columns.IColumn;
import me.nathan3882.reporting.columns.Pair.ColumnNameValuePair;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BugFixesReport implements IReport {

    private static final Logger LOGGER = LoggerFactory.getLogger(BugFixesColumn.class);

    private final LinkedList<ColumnNameValuePair> columnValues;

    private final LinkedList<String> dataRows;
    private LinkedList<BugFixesReportRow> reportRows;


    public BugFixesReport(IColumn iColumn, String[] dataRows) {
        columnValues = iColumn.getValues();
        this.dataRows = new LinkedList<>();
        this.dataRows.addAll(Arrays.asList(dataRows));
        createReportRowsFromDataRows(this.dataRows);
    }

    public BugFixesReport(IColumn iColumn, LinkedList<String> dataRows) {
        columnValues = iColumn.getValues();
        this.dataRows = dataRows;
        createReportRowsFromDataRows(dataRows);
    }

    @Override
    public void export(ExportType exportType, File toThisFile) throws JRException {

        switch (exportType) {
            case PDF:
                final String pathToReport = toThisFile.getPath();


                JasperReport report = JasperCompileManager.compileReport(pathToReport);
                JRDataSource source = new JREmptyDataSource();

                Map<String, Object> parametersWithValuesMap = new HashMap<>();

                for (BugFixesReportRow reportRow : getReportRows()) {
                    parametersWithValuesMap.put(reportRow)
                }
                JasperPrint result = JasperFillManager.fillReport(pathToReport, )
                break;

            default:
                break;
        }

    }

    @Override
    public LinkedList<ColumnNameValuePair> getColumnValues() {
        return columnValues;
    }

    @Override
    public LinkedList<String> getDataRows() {
        return this.dataRows;
    }

    @Override
    public LinkedList<BugFixesReportRow> getReportRows() {
        return this.reportRows;
    }

    @Override
    public LinkedList<BugFixesReportRow> createReportRowsFromDataRows(LinkedList<String> rowsData) {
        Date startDate = new Date();
        getLogger().info("INSTANTIATING REPORT ROWS @ " + new SimpleDateFormat().format(startDate));
        String delimiter = Parser.getDelimiter();

        LinkedList<BugFixesReportRow> reportRows = new LinkedList<>();
        for (int i = 0; i < rowsData.size(); i++) {
            String aRow = rowsData.get(i);
            String[] cells = aRow.split(delimiter);
            for (int j = 0; j < cells.length; j++) {
                ColumnNameValuePair pair = getColumnValues().get(j);

                BugFixesColumn parameterName = pair.getKey(); //get the title of the row as an enum

                int requiredIndex = pair.getValue(); //get the index that should match -> index == j should be true

                if (requiredIndex == j) {
                    reportRows.add(new BugFixesReportRow());
                }

            }
        }
        long differenceMillis = System.currentTimeMillis() - startDate.getTime();
        getLogger().info("Created a total of " + reportRows.size() + " report rows" +
                " in " + differenceMillis + "ms");

        return reportRows;
    }

    private static Logger getLogger() {
        return LOGGER;
    }
}
