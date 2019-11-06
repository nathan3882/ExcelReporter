package me.nathan3882.reporting.individualreports;

import me.nathan3882.excelreporter.ExcelReporter;
import me.nathan3882.parsing.ExportType;
import me.nathan3882.parsing.Parser;
import me.nathan3882.reporting.AbstractBaseReport;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
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

import static me.nathan3882.reporting.individualreports.BugFixesField.*;

public class BugFixesReport extends AbstractBaseReport {

    private static final Logger LOGGER = LoggerFactory.getLogger(BugFixesField.class);
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy @ HH:mm:ss aa");

    private final LinkedList<BugFixesField> columnValues;

    public BugFixesReport(BugFixesField[] iColumn, String[] dataRows) {
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
                for (BugFixesParameter value : BugFixesParameter.values()) {
                    if (value == BugFixesParameter.RUN_DATE_STRING_PARAM) {
                        parametersWithValuesMap.put(value.getKey(), formattedRunDate);
                    } else {
                        parametersWithValuesMap.put(value.getKey(),
                                value.getValue());
                    }
                }

                Collection<Map<String, Object>> exportableData = getExportableData();

                JRDataSource source = new JRBeanCollectionDataSource(exportableData);
                JasperPrint result = JasperFillManager.fillReport(pathToCompiledReport,
                        parametersWithValuesMap, source);


                JRPdfExporter exporter = new JRPdfExporter();

                exporter.setExporterInput(new SimpleExporterInput(result));
                String path = ExcelReporter.PATH_TO_EXPORTED_PDF;
                File file = new File(path);
                if (file.exists()) {
                    if (file.delete()) {
                        LOGGER.info("Deleted current PDF to now replace with new PDF.");
                    }
                } else {
                    LOGGER.info("No need to delete current PDF as doesn't exist.");

                }
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
                        new FileOutputStream(path)));

                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
                configuration.setMetadataAuthor("Nathan");  //why not set some config as we like
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
        rowsInADocumentLoop:
        for (int i = 1; i < dataRows.size(); i++) { //This will skip the first row, which is just the excel headers
            String aRow = dataRows.get(i);
            String[] cells = aRow.split(delimiter);
            if (cells.length == 0) { //no point in keeping on going if no cells
                continue;
            }
            Map<String, String> bugFixesDataRowsAsMap = new HashMap<>();
            int size = getColumnValues().size();

            cellsInARowLoop:
            for (int j = 0; j <= 5; j++) { //set to from JIRA REF to USER_TYPE
                BugFixesField associatedColumn = BugFixesField.getByIndex(j);
                if (associatedColumn == null) continue;
                int requiredIndex = associatedColumn.getIndex(); //get the index that should match -> index == j should be true

                if (requiredIndex == j) { //should match, check to be extra sure (should never be false)
                    String cell;
                    try {
                        cell = cells[j];
                        if (j == JIRA_REF.getIndex()) {
                            //check if jira ref valid
                            if (!isJiraRefValid(cell)) {
                                continue rowsInADocumentLoop; //continue to the next row as this row starts with an invalid JIRA REF
                            }
                        }
                    } catch (IndexOutOfBoundsException e) {
                        LOGGER.error("Couldn't recieve cell @ index " + j + ".", e);
                        break cellsInARowLoop;
                    }
                    bugFixesDataRowsAsMap.put(associatedColumn.getKey(), cell);
                }
            }

            String jiraRefKey = JIRA_REF.getKey();
            String summaryKey = SUMMARY.getKey();
            String clientOrOnlineKey = CLIENT_OR_ONLINE.getKey();
            String statusKey = STATUS.getKey();
            String completionDateKey = COMPLETION_DATE.getKey();
            String userTypeKey = USER_TYPE.getKey();

            String jiraRef = bugFixesDataRowsAsMap.get(jiraRefKey);
            String summary = bugFixesDataRowsAsMap.get(summaryKey);
            String clientOrOnline = bugFixesDataRowsAsMap.get(clientOrOnlineKey);
            String status = bugFixesDataRowsAsMap.get(statusKey);
            String completionDate = bugFixesDataRowsAsMap.get(completionDateKey);
            String userType = bugFixesDataRowsAsMap.get(userTypeKey);

            Map<String, Object> row = new HashMap<>();
            row.put(jiraRefKey, jiraRef);
            row.put(summaryKey, summary);
            row.put(clientOrOnlineKey, clientOrOnline);
            row.put(statusKey, status);
            row.put(completionDateKey, completionDate);
            row.put(userTypeKey, userType);

            unsortedRows.add(row); //Is a valid jira ref, add the rows and stuff

        }

        long differenceMillis = System.currentTimeMillis() - startDate.getTime();
        getLogger().info("Created a total of " + unsortedRows.size() + " report rows" +
                " in " + differenceMillis + "ms");

        return unsortedRows;
    }

    @Override
    public LinkedList<BugFixesField> getColumnValues() {
        return columnValues;
    }

    private boolean isJiraRefValid(String jiraRef) {
        return jiraRef.startsWith("SCO") || jiraRef.startsWith("ONL") || jiraRef.startsWith("MOB");
    }

    private static Logger getLogger() {
        return LOGGER;
    }

}
