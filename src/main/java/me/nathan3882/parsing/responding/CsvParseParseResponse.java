package me.nathan3882.parsing.responding;

import com.sun.istack.internal.Nullable;
import com.sun.javaws.exceptions.InvalidArgumentException;
import lombok.Setter;
import me.nathan3882.parsing.Parser.ParseFormat;

import static me.nathan3882.parsing.Parser.ParseFormat.CSV;

@Setter
public class CsvParseParseResponse extends ParseResponse<String> {

    private Boolean wasSuccessfull = Boolean.FALSE;

    public CsvParseParseResponse(Class<String> clazz, String... data) throws InvalidArgumentException {
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


    /**
     * This will fetch the data that was recieved by the Parser instance that called it
     * @return an array of objects of type Sgtrin
     */
    @Override
    public String[] getResponseData() {
        return this.responseData;
    }

    @Override
    public ParseFormat getFormat() {
        return CSV;
    }
}
