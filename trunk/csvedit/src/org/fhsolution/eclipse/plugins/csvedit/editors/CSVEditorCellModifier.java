package org.fhsolution.eclipse.plugins.csvedit.editors;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import org.fhsolution.eclipse.plugins.csvedit.model.CSVRow;

/**
 *
 * @author fhenri
 *
 */
public class CSVEditorCellModifier implements ICellModifier {

    /**
     * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
     */
    public boolean canModify (Object element, String property) {
        return true;
    }

    /**
     * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
     */
    public Object getValue (Object element, String property) {
        int elementIndex = Integer.parseInt(property);
        CSVRow row = (CSVRow) element;

        if(elementIndex < row.getNumberOfElements()) {
            return row.getElementAt(elementIndex);
        }
        else {
            return "";
        }
    }

    /**
     * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
     */
    public void modify (Object element, String property, Object value) {
        int elementIndex = Integer.parseInt(property);

        if (element instanceof TableItem) {
            CSVRow row = (CSVRow) ((TableItem) element).getData();

            if(elementIndex < row.getNumberOfElements()) {
                row.setRowEntry(elementIndex, value.toString());
            }
            else {
                for (int i=row.getNumberOfElements();i<elementIndex + 1;i++) {
                    row.addElement("");
                }
                row.setRowEntry(elementIndex, value.toString());
            }
        }
    }
}
