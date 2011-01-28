package org.fhsolution.eclipse.plugins.csvedit.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Pattern searchPattern;

    /**
     * Build a pattern. we use pattern so we can make non case sensitive search
     *
     * @param s string to search
     */
    public void setSearchText (String s, boolean isCaseSensitive) {
        // Search must be a substring of the existing value
        this.searchString = ".*" + s + ".*";
        if (isCaseSensitive) {
            System.out.println("case sensitive search");
            searchPattern =
                Pattern.compile(searchString);
        } else {
            System.out.println("non sensitive search");
            searchPattern =
                Pattern.compile(searchString, Pattern.CASE_INSENSITIVE);
        }
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
            Matcher m = searchPattern.matcher(s);
            if (m.matches()) {
                return true;
            }
        }

        return false;
    }
}