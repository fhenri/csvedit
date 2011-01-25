package org.fhsolution.eclipse.plugins.csvedit.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.fhsolution.eclipse.plugins.csvedit.Activator;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 *
 * @author fhenri
 *
 */
public class CSVPreferencePage extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage {

    /**
     * Public constructor
     */
    public CSVPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Some options that will be use for all the csv files");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     *
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    public void createFieldEditors() {

        addField(new BooleanFieldEditor(
                PreferenceConstants.USE_FIRST_LINE_AS_HEADER,
                "&Use the first line of the CSV file as the column headers",
                getFieldEditorParent()));

        StringFieldEditor customDelimiterField = new StringFieldEditor(
                PreferenceConstants.CUSTOM_DELIMITER,
                "Choose the delimiter to use:", 2, getFieldEditorParent());
        customDelimiterField.setTextLimit(1);
        customDelimiterField.setEmptyStringAllowed(false);
        addField(customDelimiterField);

        addField(new BooleanFieldEditor(
                PreferenceConstants.CASE_SENSITIVE_SEARCH,
                "&make search case sensitive",
                getFieldEditorParent()));

    }

    /**
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
     */
    public boolean performOk () {
        // TODO here we should reload all opened csv file with the new pref.
        return super.performOk();
    }

    /**
     *
     *
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

}