package me.nathan3882.reporting;

import me.nathan3882.reporting.Pair.ColumnNameValuePair;

import java.util.LinkedList;

/**
 * This interface allowss multiple enumerations (columns) to be defined.
 * This means I will be able to adapt the application to accept a wide range
 * of formats in the future.
 */
public interface IColumn {
    LinkedList<ColumnNameValuePair> getValues();
}
