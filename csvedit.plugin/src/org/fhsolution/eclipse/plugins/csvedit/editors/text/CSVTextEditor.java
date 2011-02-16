package org.fhsolution.eclipse.plugins.csvedit.editors.text;

import org.eclipse.ui.editors.text.TextEditor;

/**
 * {@link CSVTextEditor} extends basic {@link TextEditor} adding syntax highlighting
 * for the separated elements
 * @author J. Andres Pizarro Gascon
 */
public class CSVTextEditor extends TextEditor {
	
	/**
	 * Constructor
	 */
	public CSVTextEditor(char delimiter)
	{
		setSourceViewerConfiguration(new CSVTextSourceViewerConfiguration(delimiter, getPreferenceStore()));
	}
}