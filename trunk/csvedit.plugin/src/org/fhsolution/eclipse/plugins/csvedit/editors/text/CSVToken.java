package org.fhsolution.eclipse.plugins.csvedit.editors.text;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;

public class CSVToken extends Token {

	/**
	 * Different CSV tokens
	 * @author japg
	 */
	public enum CSVTokenType
	{
		ODD_COLUMN(new TextAttribute(null, null, SWT.NORMAL)),
		EVEN_COLUMN(new TextAttribute(null, null, SWT.BOLD)),
		SEPARATOR(new TextAttribute(null, null, SWT.NORMAL));
		
		/** Text decoration */
		private final TextAttribute m_textDecoration;
		
		/**
		 * Constructor
		 * @param attrs
		 */
		private CSVTokenType(TextAttribute attrs)
		{
			m_textDecoration = attrs;
		}
		
		/**
		 * Get text attributes for this token type
		 * @return
		 */
		TextAttribute getTextAttribute()
		{
			return m_textDecoration;
		}
	}

	/** Column index */
	private final int m_columnIndex;
	
	/**
	 * Constructor
	 * @param type
	 * @param column
	 */
	public CSVToken(CSVTokenType type, int column) {
		super(type.getTextAttribute());
		m_columnIndex = column;
	}
	
	/**
	 * Get column index where this token is located
	 * @return
	 */
	public int getColumnIndex()
	{
		return m_columnIndex;
	}
}