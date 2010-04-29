package org.fhsolution.eclipse.plugins.csvedit.model;

import org.fhsolution.eclipse.plugins.csvedit.Activator;
import org.fhsolution.eclipse.plugins.csvedit.preferences.PreferenceConstants;

/**
 *
 * @author fhenri
 *
 */
public class PreferencesCsvOptionsProvider implements ICsvOptionsProvider {

    private boolean useFirstLineAsHeader;
    private String customDelimiter;

    public PreferencesCsvOptionsProvider()
    {
        useFirstLineAsHeader =
            Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_USEFIRSTLINEASHEADER);
        customDelimiter =
            Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_CUSTOMDELIMITER);
    }

    public String getCustomDelimiter() {
        return customDelimiter;
    }

    public boolean getUseFirstLineAsHeader() {
        return useFirstLineAsHeader;
    }

}
