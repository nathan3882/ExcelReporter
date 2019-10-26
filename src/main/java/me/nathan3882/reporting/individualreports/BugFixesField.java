package me.nathan3882.reporting.individualreports;

import com.sun.istack.internal.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.nathan3882.reporting.IColumn;
import me.nathan3882.reporting.Pair.ColumnNameValuePair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * This enum allo
 */
@RequiredArgsConstructor
@Getter
public enum BugFixesField implements IColumn {

    DUMMY(-1, "dummy"),
    JIRA_REF(0, "jiraRefField"),
    SUMMARY(1, "summaryField"),
    CLIENT_OR_ONLINE(2, "clientOrOnlineField"),
    STATUS(3, "statusField"),
    COMPLETION_DATE(4, "completionDateField"),
    USER_TYPE(5, "userTypeField");

    private final int index;
    private final String key;

    /**
     * Get a BugFixesField by index.
     *
     * @param index the index to look for
     * @return the associated BugFixesField else null.
     */
    @Nullable
    public static BugFixesField getByIndex(int index) {
        for (BugFixesField value : values()) {
            if (value.getIndex() == index) {
                return value;
            }
        }
        return null;
    }

    @Override
    public LinkedList<ColumnNameValuePair> getValues() {
        return Arrays.stream(values()) //now stream the values of this enum
                .map(value -> new ColumnNameValuePair(value, value.getIndex())) //create a new pair for each
                .collect(Collectors.toCollection(LinkedList::new)); //then add the pair to new linked list
    }
}