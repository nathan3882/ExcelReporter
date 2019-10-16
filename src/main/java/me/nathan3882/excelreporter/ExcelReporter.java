package me.nathan3882.excelreporter;

import com.sun.javaws.exceptions.InvalidArgumentException;
import me.nathan3882.excelreporter.parsing.CsvParser;
import me.nathan3882.excelreporter.parsing.responses.CsvParseResponse;

import java.io.File;
import java.io.IOException;

public class ExcelReporter {

    public static void main(String[] args) {

        CsvParser parser = new CsvParser(new File(String.valueOf(ExcelReporter.class.getClassLoader().getResource("fixes.xlsx"))));

        try {
            CsvParseResponse response = parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
