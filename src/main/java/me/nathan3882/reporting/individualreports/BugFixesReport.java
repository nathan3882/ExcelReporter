package me.nathan3882.reporting.individualreports;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.nathan3882.parsing.ExportType;
import me.nathan3882.parsing.Parser;
import me.nathan3882.reporting.BaseReport;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static me.nathan3882.excelreporter.ExcelReporter.PATH_TO_REPORT;
import static me.nathan3882.reporting.individualreports.BugFixesColumn.*;

public class BugFixesReport extends BaseReport {

    private static final Logger LOGGER = LoggerFactory.getLogger(BugFixesColumn.class);
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy @ HH:mm:ss aa");

    private final LinkedList<BugFixesColumn> columnValues;

    public BugFixesReport(BugFixesColumn[] iColumn, String[] dataRows) {
        columnValues = new LinkedList<>(Arrays.asList(iColumn));
        this.exportableData = createExportableDataFromRowsData(new LinkedList<>(Arrays.asList(dataRows)));
    }

    @Override
    public void export(ExportType exportType, File compiledReportFile) throws JRException, IOException {

        switch (exportType) {
            case PDF:
                final String pathToCompiledReport = compiledReportFile.getPath();

                Map<String, Object> parametersWithValuesMap = new HashMap<>();

                String formattedRunDate = SIMPLE_DATE_FORMAT.format(new Date());
                for (BugFixesReportParameter value : BugFixesReportParameter.values()) {
                    if (value == BugFixesReportParameter.RUN_DATE_STRING) {
                        parametersWithValuesMap.put(value.getKey(), formattedRunDate);
                    }
                    parametersWithValuesMap.put(value.getKey(), value.getValue());
                }

                Collection<Map<String, Object>> exportableData = getExportableData();
                JRDataSource source = new JRBeanCollectionDataSource(exportableData);
                JasperPrint result = JasperFillManager.fillReport(pathToCompiledReport, parametersWithValuesMap, source);


                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(result));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream(PATH_TO_REPORT + "hello.pdf")));
                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
                configuration.setMetadataAuthor("Petter");  //why not set some config as we like
                exporter.setConfiguration(configuration);
                exporter.exportReport();
            break;

            default:
                break;
        }

    }

    @Override
    public Collection<Map<String, Object>> createExportableDataFromRowsData(LinkedList<String> dataRows) {
        Date startDate = new Date();
        getLogger().info("INSTANTIATING REPORT ROWS @ " + new SimpleDateFormat().format(startDate));
        String delimiter = Parser.getDelimiter();

        //the dataRowsAsMap map on line below has keys matching the enum BugFixesReportRow's keys
        Collection<Map<String, Object>> unsortedRows = new LinkedList<>();
        for (int i = 1; i < dataRows.size(); i++) { //This will skip the first row, which is just the excel headers
            String aRow = dataRows.get(i);
            String[] cells = aRow.split(delimiter);
            if (cells.length == 0) { //no point in keeping on going if no cells
                continue;
            }
            Map<String, String> bugFixesDataRowsAsMap = new HashMap<>();
            int size = getColumnValues().size();
            for (int j = 0; j <= 5; j++) { //set to from JIRA REF to USER_TYPE
                BugFixesColumn associatedColumn = BugFixesColumn.getByIndex(j);

                int requiredIndex = associatedColumn.getIndex(); //get the index that should match -> index == j should be true

                if (requiredIndex == j) { //should match, check to be extra sure (should never be false)
                    String cell = null;
                    try {
                        cell = cells[j];

                    }catch(IndexOutOfBoundsException e) {
                        System.out.println("Error");
                    }
                    bugFixesDataRowsAsMap.put(associatedColumn.getKey(), cell);
                }
            }
            String jiraRef = bugFixesDataRowsAsMap.get(JIRA_REF.getKey());
            String summary = bugFixesDataRowsAsMap.get(SUMMARY.getKey());
            String clientOrOnline = bugFixesDataRowsAsMap.get(CLIENT_OR_ONLINE.getKey());
            String status = bugFixesDataRowsAsMap.get(STATUS.getKey());
            String completionDate = bugFixesDataRowsAsMap.get(COMPLETION_DATE.getKey());
            String userType = bugFixesDataRowsAsMap.get(USER_TYPE.getKey());

            Map<String, Object> row = new HashMap<>();
            row.put(JIRA_REF.getKey(), jiraRef);
            row.put(SUMMARY.getKey(), summary);
            row.put(CLIENT_OR_ONLINE.getKey(), clientOrOnline);
            row.put(STATUS.getKey(), status);
            row.put(COMPLETION_DATE.getKey(), completionDate);
            row.put(USER_TYPE.getKey(), userType);
            unsortedRows.add(row);
        }

        long differenceMillis = System.currentTimeMillis() - startDate.getTime();
        getLogger().info("Created a total of " + unsortedRows.size() + " report rows" +
                " in " + differenceMillis + "ms");

        return unsortedRows;
    }

    @Override
    public LinkedList<BugFixesColumn> getColumnValues() {
        return columnValues;
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        return SIMPLE_DATE_FORMAT;
    }

    private static Logger getLogger() {
        return LOGGER;
    }

    public enum BugFixesReportParameter {

        REPORT_TITLE("reportTitle", "Nathans 3 Month Review Report"),
        RUN_DATE_STRING("runDateString", null);

        private final String key;
        private final String value;

        BugFixesReportParameter(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * This function is for getting the value
         *
         * @return null if no value else value associated with the {@link BugFixesReportParameter}
         */
        @Nullable
        public String getValue() {
            return value;
        }

        /**
         * This function is for getting the key
         *
         * @return the key associated with the {@link BugFixesReportParameter}
         */
        @NotNull
        public String getKey() {
            return key;
        }
    }

}
