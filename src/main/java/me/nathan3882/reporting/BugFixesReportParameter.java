package me.nathan3882.reporting;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public enum BugFixesReportParameter {

    REPORT_TITLE("reportTitle", "Nathans 3 Month Review Report"),
    RUN_DATE_STRING("runDateString", null);

    private final String key;
    private final String value;

    BugFixesReportParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * This function is for getting the value
     *
     * @return null if no value else value associated with the {@link BugFixesReportParameter}
     */
    @Nullable
    public String getValue() {
        return value;
    }

    /**
     * This function is for getting the key
     *
     * @return the key associated with the {@link BugFixesReportParameter}
     */
    @NotNull
    public String getKey() {
        return key;
    }
}
