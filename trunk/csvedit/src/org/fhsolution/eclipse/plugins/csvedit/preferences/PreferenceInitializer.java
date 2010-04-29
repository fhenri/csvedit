package org.fhsolution.eclipse.plugins.csvedit.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.fhsolution.eclipse.plugins.csvedit.Activator;

/**
 * Class used to initialize default preference values.
 *
 * @author fhenri
 *
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /**
     * 
     *
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_USEFIRSTLINEASHEADER, true);
        store.setDefault(PreferenceConstants.P_CUSTOMDELIMITER, ",");
    }

}
