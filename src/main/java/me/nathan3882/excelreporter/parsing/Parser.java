package me.nathan3882.excelreporter.parsing;

import com.sun.istack.internal.Nullable;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * This Parser allows sub classes to provide their own implementations for parsing different files.
 *
 * @param <T> Type type that the parser will return; for example,
 *            is it a JasperPrint a String or an OutputStream
 */
public abstract class Parser<T> implements IParsable {


    protected static final Logger PARSER_LOGGER;
    private static final DataFormatter DATE_FORMATTER;

    static {
        PARSER_LOGGER = LoggerFactory.getLogger(Parser.class);
        DATE_FORMATTER = new DataFormatter();
    }

    @Nullable
    protected File fileToParse;

    protected Parser(File fileToParse) {
        this.fileToParse = fileToParse;
    }

    protected Parser() {

    }

    abstract T parse(File fileToParse) throws Throwable;

    protected File getFileToParse() {
        return this.fileToParse;
    }

    protected DataFormatter getDateFormatter() {
        return DATE_FORMATTER;
    }

    protected Logger getLogger() {
        return PARSER_LOGGER;
    }

    public enum ParseFormat {
        CSV
    }

    /**
     * This enum serves as a way to determine outcomes of parsing operations
     */
    protected enum ParseStatus {

        PROVIDED_FILE_INVALID(1),
        COULDNT_PARSE_REASON_UNKNOWN(2),
        SUCCESSFULLY_PARSED(3),
        NO_DATA_RETURNED(4);

        private final int value;

        ParseStatus(int value) {
            this.value = value;
        }

        /**
         * This function returns the primitive int representation of a {@link ParseStatus}
         *
         * @return int associated with the ParseStatus
         */
        public int intValue() {
            return value;
        }
    }


}
