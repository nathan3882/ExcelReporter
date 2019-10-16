package me.nathan3882.excelreporter.parsing.responses;

import com.sun.istack.internal.Nullable;
import com.sun.javaws.exceptions.InvalidArgumentException;
import lombok.AccessLevel;
import lombok.Setter;
import me.nathan3882.excelreporter.parsing.Parser.ParseFormat;

import static me.nathan3882.excelreporter.parsing.Parser.ParseFormat.CSV;

@Setter
public class CsvParseResponse extends Responder<String> {

    private Boolean wasSuccessfull = null;

    public CsvParseResponse(Class<String> clazz, String... data) throws InvalidArgumentException {
        super(clazz, data);
    }

    /**
     * this function tells us if the parse response from the initial
     * parse was successful or not
     * @return null if not finished parsing yet - true or false if it's finished
     */
    @Nullable
    @Override
    public Boolean wasSuccessfull() {
        return wasSuccessfull;
    }

    @Override
    String[] getResponseData() {
        return this.responseData;
    }

    @Override
    public ParseFormat getFormat() {
        return CSV;
    }
}
