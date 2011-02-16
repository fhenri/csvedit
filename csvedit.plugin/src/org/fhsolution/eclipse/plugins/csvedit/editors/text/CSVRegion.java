package org.fhsolution.eclipse.plugins.csvedit.editors.text;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;

/**
 * {@link CSVRegion} defines a piece of information inside a CSV document
 * @author J. Andres Pizarro <j.andres.pizarro+csvedit@gmail.com>
 */
public class CSVRegion implements ITypedRegion {

	/** Region offset */
	private final int m_offset;
	
	/** Region length */
	private final int m_length;
	
	/** Column index */
	private final int m_index;
	
	/**
	 * Constructor
	 * @param offset the region offset inside the document
	 * @param length the region length
	 * @param index the region index
	 */
	public CSVRegion(int offset, int length, int index)
	{
		m_offset = offset;
		m_length = length;
		m_index = index;
	}
	
	public int getLength() {
		return m_length;
	}

	public int getOffset() {
		return m_offset;
	}

	public String getType() {
		return IDocument.DEFAULT_CONTENT_TYPE;
	}

	/**
	 * Get the index where this region is positioned inside a CSV line
	 * @return the column index where this region is located
	 */
	public int getColumnIndex()
	{
		return m_index;
	}
}