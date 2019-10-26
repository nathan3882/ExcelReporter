package me.nathan3882.reporting.individualreports;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * This enum serves a purpose of providing a key that matches the parameter name
 * inside Jaspersoft(registered) Studio and a value in which is the user facing
 * text that will be substituted into the parameter upon filling the .jasper file.
 */
public enum BugFixesParameter {

    REPORT_TITLE_PARAM("reportTitle", "Nathans 3 Month Review Report"),
    RUN_DATE_STRING_PARAM("runDateString", null),
    JIRA_REF_PARAM("jiraRefParam", "Jira Ref"),
    SUMMARY_PARAM("summaryParam", "Summary"),
    CLIENT_OR_ONLINE_PARAM("clientOrOnlineParam", "Client/Online"),
    STATUS_PARAM("statusParam", "Status"),
    COMPLETION_DATE_PARAM("completionDateParam", "Completion Date"),
    USER_TYPE_PARAM("userTypeParam", "User Type");


    private final String key;
    private final String value;

    BugFixesParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * This function is for getting the value
     *
     * @return null if no value else value associated with the {@link BugFixesParameter}
     */
    @Nullable
    public String getValue() {
        return value;
    }

    /**
     * This function is for getting the key
     *
     * @return the key associated with the {@link BugFixesParameter}
     */
    @NotNull
    public String getKey() {
        return key;
    }
}