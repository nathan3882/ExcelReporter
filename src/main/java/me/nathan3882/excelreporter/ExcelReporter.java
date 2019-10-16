package me.nathan3882.excelreporter;

import me.nathan3882.excelreporter.parsing.CsvParser;
import me.nathan3882.excelreporter.parsing.responding.ParseResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ExcelReporter {

    public static void main(String[] args) throws URISyntaxException {

        URI uri = ExcelReporter.class.getClassLoader().getResource("fixes.xlsx").toURI();
        File fixes = new File(uri);

        CsvParser csvParser = new CsvParser(fixes);

        ParseResponse csvParseParseResponse;
        try {
            csvParseParseResponse = csvParser.parse();
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            return;
        }

        String[] responseData = (String[]) csvParseParseResponse.getResponseData(); //Csv uses String.class



    }
}
