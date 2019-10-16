package me.nathan3882.excelreporter.reporting;

import com.sun.istack.internal.Nullable;
import me.nathan3882.excelreporter.parsing.Parser;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * This enum allows the program to determine what information is being read at what time
 * to further classify the data allowing the display of information on a jasper report
 */
public enum BugFixesColumn implements IColumn {

    DUMMY(-1),
    JIRA_REF(0),
    SUMMARY(1),
    CLIENT_OR_ONLINE(2),
    STATUS(3),
    COMPLETION_DATE(4),
    USER_TYPE(5),
    NATHAN_INVESTIGATION(6),
    NOTES_OR_REPLICATION_STEPS(7);

    private final int index;

    BugFixesColumn(int index) {
        this.index = index;
    }

    /**
     * Get a BugFixesColumn by index.
     *
     * @param index the index to look for
     * @return the associated BugFixesColumn else null.
     */
    @Nullable
    public static BugFixesColumn getByIndex(int index) {
        for (BugFixesColumn value : values()) {
            if (value.getIndex() == index) {
                return value;
            }
        }
        Enumeration da;
        return null;
    }

    @Override
    public LinkedList<String> getValues() {
        return new LinkedList<String>(
                Arrays.stream(values())
                        .map(value -> value.name() + Parser.getDelimiter() + value.getIndex())
                        .collect(Collectors.toCollection(LinkedList::new)));
    }

    public int getIndex() {
        return index;
    }
}