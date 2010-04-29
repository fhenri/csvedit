package org.fhsolution.eclipse.plugins.csvedit.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.fhsolution.eclipse.plugins.csvedit.model.CSVRow;

/**
 *
 * @author fhenri
 *
 */
public class CSVTableFilter extends ViewerFilter {

    private String searchString;

    /**
     * @param s string to search
     */
    public void setSearchText (String s) {
        // Search must be a substring of the existing value
        this.searchString = ".*" + s + ".*";
    }

    /**
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean select (Viewer viewer, Object parentElement, Object element) {

        if (searchString == null || searchString.length() == 0) {
            return true;
        }

        // loop on all column of the current row to find matches
        CSVRow row = (CSVRow) element;
        for (String s:row.getEntries()) {
            if (s.matches(searchString)) {
                return true;
            }
        }

        return false;
    }
}