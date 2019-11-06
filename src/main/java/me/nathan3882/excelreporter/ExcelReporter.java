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

public class ExcelReporter {

    public static final String PATH_TO_REPORT = "C:\\Users\\natha\\JaspersoftWorkspace\\MyReports\\";
    public static final String PATH_TO_EXCEL_FILE = "C:\\Users\\natha\\OneDrive\\Desktop\\Bug Fixes.xlsx";
    public static final String PATH_TO_EXPORTED_PDF = "C:\\Users\\natha\\OneDrive\\Desktop\\Xlsx as Pdf.pdf";

    public static void main(String[] args) throws IOException, JRException {

        File fixes = new File(PATH_TO_EXCEL_FILE);

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
