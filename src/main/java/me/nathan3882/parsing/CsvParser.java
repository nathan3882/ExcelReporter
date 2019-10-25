package me.nathan3882.parsing;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.javaws.exceptions.InvalidArgumentException;
import me.nathan3882.parsing.responding.CsvParseParseResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public class CsvParser extends Parser<CsvParseParseResponse> {

    public CsvParser() {

    }

    public CsvParser(File fileToParse) {
        super(fileToParse);
    }

    @Nullable
    public CsvParseParseResponse parse() throws IOException, NullPointerException, InvalidFormatException {
        return parse(getFileToParse());
    }

    @Override
    public ParseFormat getFormat() {
        return ParseFormat.CSV;
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
    public CsvParseParseResponse parse(@NotNull File fileToParse) throws IOException, NullPointerException, InvalidFormatException {
        Objects.requireNonNull(fileToParse);

        Workbook wb = WorkbookFactory.create(fileToParse);
        Sheet sheet = wb.getSheetAt(0);

        LinkedList<String> rowsData = new LinkedList<>();

        Iterator<Row> rowIterator = sheet.iterator();
        int rowCount = 0;
        while (rowIterator.hasNext()) {
            Row aRow = rowIterator.next();

            String textSoFar = ""; //will contain stuff like this "column one one one,-=columntwo" etc

            Iterator<Cell> columnIterator = aRow.iterator();
            while (columnIterator.hasNext()) {
                Cell cell = columnIterator.next();
                String text = getDateFormatter().formatCellValue(cell) + getDelimiter();
                textSoFar += text;
            }
            if (!(textSoFar.equals(DELIMITER))) { //why add an empty row.
                rowsData.add(textSoFar);

                getLogger().info("Adding \"" + textSoFar + "\" to row data");
            }
            rowCount++;
            //this row has been completely parsed - now add to a linked list of rows.
        }
        wb.close();
        getLogger().info("Went through all rows (all " + rowCount + " of them) - found " + rowsData.size() + " rows with actual data!");

        getLogger().info("Instantiating CsvParseParseResponse with associated data");

        CsvParseParseResponse response = null;
        try {
            response = new CsvParseParseResponse(String.class, rowsData.toArray(new String[0]));
            response.setWasSuccessfull(Boolean.TRUE);
        } catch (InvalidArgumentException e) {
            getLogger().error("Couldn't get a response from data ", e);
        }

        if (response != null && response.wasSuccessfull()) {
            getLogger().info("Done!");
            return response;
        } else {
            return null;
        }

    }

}
