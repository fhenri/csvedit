package org.fhsolution.eclipse.plugins.csvedit.model;

import org.fhsolution.eclipse.plugins.csvedit.Activator;
import org.fhsolution.eclipse.plugins.csvedit.preferences.PreferenceConstants;

/**
 *
 * @author fhenri
 *
 */
public class PreferencesCSVOptionsProvider implements ICsvOptionsProvider {

    private boolean useFirstLineAsHeader;
    private boolean sensitiveSearch;
    private String customDelimiter;

    public PreferencesCSVOptionsProvider()
    {
        useFirstLineAsHeader =
            Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.USE_FIRST_LINE_AS_HEADER);
        customDelimiter =
            Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.CUSTOM_DELIMITER);
        sensitiveSearch =
            Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CASE_SENSITIVE_SEARCH);
    }

    public String getCustomDelimiter() {
        return customDelimiter;
    }

    public boolean getUseFirstLineAsHeader() {
        return useFirstLineAsHeader;
    }

    public boolean getSensitiveSearch () {
        return sensitiveSearch;
    }
}
