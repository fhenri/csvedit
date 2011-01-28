package org.fhsolution.eclipse.plugins.csvedit.sorter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.fhsolution.eclipse.plugins.csvedit.model.CSVRow;

/**
 *
 * @author fhenri
 *
 */
public class CSVTableSorter extends ViewerSorter {

    private int propertyIndex;
    private static final int DESCENDING = 1;

    private int direction = DESCENDING;

    /**
     * Public Constructor
     */
    public CSVTableSorter() {
        this.propertyIndex = -1;
        direction = DESCENDING;
    }

    /**
     * Set the column on which the user wants to sort table.
     *
     * @param column columnId selected by the user.
     */
    public void setColumn(int column) {
        if (column == this.propertyIndex) {
            // Same column as last sort; toggle the direction
            direction = 1 - direction;
        } else {
            // New column; do an ascending sort
            this.propertyIndex = column;
            direction = DESCENDING;
        }
    }

    /**
     * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        // this is necessary at opening of csv file so column are not sorted.
        if (propertyIndex == -1) return 0;

        String row1 = ((CSVRow) e1).getElementAt(propertyIndex);
        String row2 = ((CSVRow) e2).getElementAt(propertyIndex);

        int rc = row1.compareTo(row2);

        // If descending order, flip the direction
        if (direction == DESCENDING) {
            rc = -rc;
        }
        return rc;
    }
}
