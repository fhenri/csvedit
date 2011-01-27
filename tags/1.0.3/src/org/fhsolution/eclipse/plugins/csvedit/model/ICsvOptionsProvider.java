package org.fhsolution.eclipse.plugins.csvedit.model;

/**
 *
 * @author fhenri
 *
 */
public interface ICsvOptionsProvider {
    
    public boolean getUseFirstLineAsHeader();
    
    public String getCustomDelimiter();

}
