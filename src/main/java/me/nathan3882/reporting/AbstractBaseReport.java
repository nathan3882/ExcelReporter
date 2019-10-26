package me.nathan3882.reporting;

import lombok.Getter;
import me.nathan3882.parsing.ExportType;
import me.nathan3882.reporting.individualreports.BugFixesField;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

@Getter
public abstract class AbstractBaseReport {

    protected Collection<Map<String, Object>> exportableData = new ArrayList<>();

    protected abstract void export(ExportType exportType, File toThisFile) throws JRException, IOException;

    /**
     * This allows an implementor
     * @param rowsData
     * @return
     */
    abstract protected Collection<Map<String, Object>> createExportableDataFromRowsData(LinkedList<String> rowsData);

    /**
     * This allows an implementor to return a LinkedList in which contains what the
     * IColumn implementation of getValues() contains.
     *
     * @return the list of column values - like so: "CONSTANT_NAME,-=3" = index of 3
     */
    abstract protected LinkedList<BugFixesField> getColumnValues();

}
