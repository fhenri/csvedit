package org.fhsolution.eclipse.plugins.csvedit.model;

public interface IRowChangesListener
{
    /**
     * Element at the given index position has changes in
     * @param row the {@link CSVRow} which changed
     * @param index the index position
     */
    void rowChanged(CSVRow row, int index);
}

