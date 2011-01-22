package org.fhsolution.eclipse.plugins.csvedit.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
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
public class CSVPreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {

    public CSVPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("CSV Editor Preferences");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        /*
         * addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH,
         * "&Directory preference:", getFieldEditorParent()));
         */
        addField(new BooleanFieldEditor(
                PreferenceConstants.P_USEFIRSTLINEASHEADER,
                "&Use the first line of the CSV file as the column headers",
                getFieldEditorParent()));

        /*
         * addField(new RadioGroupFieldEditor( PreferenceConstants.P_CHOICE,
         * "An example of a multiple-choice preference", 1, new String[][] { {
         * "&Choice 1", "choice1" }, { "C&hoice 2", "choice2" } },
         * getFieldEditorParent()));
         */
        StringFieldEditor customDelimiterField = new StringFieldEditor(
                PreferenceConstants.P_CUSTOMDELIMITER,
                "Choose the delimiter to use:", 2, getFieldEditorParent());
        customDelimiterField.setTextLimit(1);
        customDelimiterField.setEmptyStringAllowed(false);
        addField(customDelimiterField);
    }

    /**
     *
     *
     * @see
     * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

}