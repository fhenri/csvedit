package org.fhsolution.eclipse.plugins.csvedit.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a row made of String elements
 *
 * @author fhenri
 *
 */
public class CSVRow {

    private ArrayList<String> entries = new ArrayList<String>();
    private CSVFile parent;

    public CSVRow(List<String> line, CSVFile model) {
        parent = model;
        entries.addAll(line);
    }

    public ArrayList<String> getEntries() {
        return entries;
    }

    public void setRowEntry(int elementIndex, String elementString) {
        if (entries.get(elementIndex).compareTo(elementString) != 0)  {
            entries.set(elementIndex, elementString);
            parent.rowChanged(this, elementIndex);
        }
    }

    /**
     * return the element at a given index.
     * This method makes sure that if the current line does not have as many
     * elements as the header, it will not break and return an empty string
     *
     * @param index
     * @return
     */
    public String getElementAt(int index) {
        if (index >= entries.size()) {
            return "";
        }
        return entries.get(index);
    }

    /**
     * Return the number of elements in this row
     * @return
     */
    public int getNumberOfElements () {
        return entries.size();
    }

    public String toString() {
        String result = "";
        for (String s:entries) {
            String delimiter = parent.getCsvOptionsProvider().getCustomDelimiter();
            result = result.concat(s).concat(delimiter);
        }
        return result;
    }

    /**
     * @param element
     */
    public void addElement(String element) {
        entries.add(element);
    }

    public static CSVRow createEmptyLine (int nbOfColumns, CSVFile model) {
        List<String> line = new LinkedList<String>();
        for (int i=0; i<nbOfColumns; i++) {
            line.add("");
        }
        return new CSVRow(line, model);
    }

    /**
     * A CSVRow is equal to another one if all element are equals.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (Object anObject) {
        if (!(anObject instanceof CSVRow)) {
            return false;
        }

        CSVRow thisRow = (CSVRow) anObject;
        for (int i=0; i<getNumberOfElements(); i++) {
            if (!(getElementAt(i).equals(thisRow.getElementAt(i)))) {
                return false;
            }
        }
        return true;
    }
}
