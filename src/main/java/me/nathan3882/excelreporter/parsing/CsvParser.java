package me.nathan3882.excelreporter.parsing;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.javaws.exceptions.InvalidArgumentException;
import me.nathan3882.excelreporter.parsing.responses.CsvParseResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class CsvParser extends Parser<CsvParseResponse> {

    private static final String DELIMITER = ",-=";

    public CsvParser() {

    }

    public CsvParser(File fileToParse) {
        super(fileToParse);
    }

    @Override
    public ParseFormat getFormat() {
        return ParseFormat.CSV;
    }

    @Nullable
    public CsvParseResponse parse() throws IOException, NullPointerException {
        return parse(getFileToParse());
    }

    /**
     * This funciton allows a csv file to be parsed into a nice string representation
     *
     * @param fileToParse the excel file to parse
     * @return The string representation of this parsed excel document
     * @throws IOException          if the workbook could not be created from the file.
     * @throws NullPointerException could return if fed a null value
     */
    @Nullable
    @Override
    public CsvParseResponse parse(@NotNull File fileToParse) throws IOException, NullPointerException {
        Objects.requireNonNull(fileToParse);

        Workbook wb = WorkbookFactory.create(fileToParse);
        Sheet sheet = wb.getSheetAt(0);
        LinkedList<String> rowsData = new LinkedList<>();
        for (Row row : sheet) {
            String textSoFar = ""; //will contain stuff like this "column one one one,-=columntwo" etc
            for (Cell cell : row) {
                String text = getDateFormatter().formatCellValue(cell) + getDelimiter();
                textSoFar += text;
                getLogger().info("Adding row text \"" + text + "\"");
            }
            rowsData.add(textSoFar);
            getLogger().info("Adding \"" + textSoFar + "\" to row data");


            //this row has been completely parsed - now add to a linked list of rows.
        }
        wb.close();
        getLogger().info("Went through all rows (all " + rowsData.size() + " of them)!");

        getLogger().info("Instantiating CsvParseResponse with associated data");

        String[] objects = rowsData.toArray(new String[0]);

        CsvParseResponse response;
        try {
            response = new CsvParseResponse(String.class, objects);
        } catch (InvalidArgumentException e) {
            getLogger().error("Couldn't get a response from data ", e);
            return null;
        }
        if (response.wasSuccessfull()) {
            getLogger().info("Done!");
            return response;
        } else {
            return null;
        }

    }

    private static String getDelimiter() {
        return DELIMITER;
    }
}
