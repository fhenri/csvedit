package org.fhsolution.eclipse.plugins.csvedit.customeditor;

import org.fhsolution.eclipse.plugins.csvedit.customeditor.model.DefaultCSVFile;
import org.fhsolution.eclipse.plugins.csvedit.customeditor.model.PreferencesCSVOptionsProvider;
import org.fhsolution.eclipse.plugins.csvedit.editors.MultiPageCSVEditor;
import org.fhsolution.eclipse.plugins.csvedit.model.AbstractCSVFile;

public class DefaultCSVMultipageEditor extends MultiPageCSVEditor {

    /**
     * Create the CSV file
     */
    protected AbstractCSVFile createCSVFile()
    {
    	PreferencesCSVOptionsProvider pref = new PreferencesCSVOptionsProvider();
    	return new DefaultCSVFile(pref);
    }
	
}
