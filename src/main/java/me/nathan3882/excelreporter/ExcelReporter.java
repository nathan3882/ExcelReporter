package me.nathan3882.excelreporter;

import me.nathan3882.parsing.CsvParser;
import me.nathan3882.parsing.ExportType;
import me.nathan3882.parsing.responding.CsvParseParseResponse;
import me.nathan3882.reporting.individualreports.BugFixesField;
import me.nathan3882.reporting.individualreports.BugFixesReport;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ExcelReporter {

    public static final String PATH_TO_REPORT = "C:\\Users\\natha\\JaspersoftWorkspace\\MyReports\\";

    public static void main(String[] args) throws URISyntaxException, IOException, JRException {

        URI uri = ExcelReporter.class.getClassLoader().getResource("fixes.xlsx").toURI();
        File fixes = new File(uri);

        CsvParser csvParser = new CsvParser(fixes);

        CsvParseParseResponse csvParseParseResponse;
        try {
            csvParseParseResponse = csvParser.parse();
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            return;
        }

        String[] responseData = csvParseParseResponse.getResponseData(); //Csv uses String.class

        BugFixesReport bugFixesReport = new BugFixesReport(BugFixesField.values(), responseData);

        bugFixesReport.export(ExportType.PDF, new File(PATH_TO_REPORT + "BugFixesReportFile.jasper"));

    }
}
