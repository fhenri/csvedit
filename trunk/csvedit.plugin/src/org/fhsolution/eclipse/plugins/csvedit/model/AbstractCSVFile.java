/* Copyright 2011 csvedit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fhsolution.eclipse.plugins.csvedit.model;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public abstract class AbstractCSVFile implements IRowChangesListener {

    private int nbOfColumns;
    private boolean displayFirstLine;
    private final ArrayList<CSVRow> rows;
    private final ArrayList<String> header;
    private final ArrayList<ICsvFileModelListener> listeners;

    /**
     * Default constructor
     */
    public AbstractCSVFile() {
        nbOfColumns = 1;
        displayFirstLine = true;
        rows = new ArrayList<CSVRow>();
        header = new ArrayList<String>();
        listeners = new ArrayList<ICsvFileModelListener>();
    }

    /**
     * Check if first line in the file will be considered as the file header
     * @return true if the first line in the file represents the header
     */
    public abstract boolean isFirstLineHeader();

    /**
     * Check search in the text must be case sensitive
     * @return true if the search must be case sensitive.
     */
    public abstract boolean getSensitiveSearch();

    /**
     * Get custom delimiter to use as a separator
     * @return the delimiter
     */
    public abstract char getCustomDelimiter();

    /**
     * Get the character that defines comment lines
     * @return the comment line starting character. If no comments are allowed in this
     * file, then Character.UNASSIGNED constant must be returned;
     *
     */
    public abstract char getCommentChar();

    public void setInput(String text) {
        readLines(text);
    }

    public void displayFirstLine(boolean display) {
        displayFirstLine = display;
    }

    protected CsvReader initializeReader (Reader reader)
    {
        CsvReader csvReader = new CsvReader(reader);

        char customDelimiter = getCustomDelimiter();
        csvReader.setDelimiter(customDelimiter);

        char commentChar = getCommentChar();
        if (commentChar != Character.UNASSIGNED) {
            csvReader.setComment(commentChar);
            csvReader.setUseComments(true);
        }
        return csvReader;
    }

    protected void readLines(String fileText) {
        rows.clear();

        try {
            CsvReader csvReader = initializeReader(new StringReader(fileText));
            // case when the first line is the encoding
            if (!displayFirstLine) {
                csvReader.skipLine();
            }

            if (isFirstLineHeader()) {
                csvReader.readHeaders();
                String[] rowValues = csvReader.getHeaders();
                populateHeaders(rowValues);
            } else {
                populateHeaders(null);
            }

            while (csvReader.readRecord()) {
                String[] rowValues = csvReader.getValues();
                CSVRow csvRow = new CSVRow(rowValues, this);
                rows.add(csvRow);
                if (rowValues.length > nbOfColumns) {
                    nbOfColumns = rowValues.length;
                }
            }

            csvReader.close();
        } catch (Exception e) {
            System.out.println("exception in readLines " + e);
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void addLine () {
        CSVRow row = CSVRow.createEmptyLine(nbOfColumns, this);
        addLine(row);
    }

    /**
     * @param row
     */
    public void addLineAfterElement (CSVRow row) {
        CSVRow newRow = CSVRow.createEmptyLine(nbOfColumns,  this);
        int indexRow = findRow(row);
        if (indexRow != -1) {
            rows.add(indexRow, newRow);
        }
        else {
            addLine(row);
        }
    }

    /**
     * @param row
     */
    public void addLine (CSVRow row) {
        rows.add(row);
    }

    /**
     * @param row
     * @return
     */
    public int findRow (CSVRow findRow) {
        for (int i = 0; i < getArrayRows().length; i++) {
            CSVRow row = getRowAt(i);
            if (row.equals(findRow)) return i;
        }
        return -1;
    }

    /**
     *
     */
    public void removeLine (CSVRow row) {
        if (!rows.remove(row)) {
            // TODO return error message
        }
    }

    public Object[] getArrayRows() {
        return rows.toArray();
    }

    public List<CSVRow> getRows() {
        return rows;
    }

    public CSVRow getRowAt (int index) {
        return rows.get(index);
    }

    private void populateHeaders (String[] entries) {
        header.clear();

        if (entries != null) {
            for (String entry : entries) {
                header.add(entry);
            }

            for (int i = header.size(); i < nbOfColumns; i++) {
                header.add("");
            }
        } else {
            for (int i = 1; i < nbOfColumns + 1; i++) {
                header.add("Column" + i);
            }
        }
    }

    public ArrayList<String> getHeader() {
        return header;
    }

    public String[] getArrayHeader () {
        return (String[]) header.toArray();
    }

    public int getColumnCount() {
        return nbOfColumns;
    }

    public void rowChanged (CSVRow row, int rowIndex) {
        for (ICsvFileModelListener l : listeners) {
            ((ICsvFileModelListener) l).entryChanged(row, rowIndex);
        }
    }

    public void removeRow (int rowIndex) {
        rows.remove(rowIndex);
    }

    /**
     * Remove the column represented by the index
     *
     * @param colIndex
     */
    public void removeColumn (int colIndex) {
        if (isFirstLineHeader()) {
            System.out.println("remove header element");
            header.remove(colIndex);
        }
        for (CSVRow row : rows) {
            row.removeElementAt(colIndex);
        }
    }

    public void removeModelListener(ICsvFileModelListener csvFileListener) {
        listeners.remove(csvFileListener);
    }

    public void addModelListener(ICsvFileModelListener csvFileListener) {
        if (!listeners.contains(csvFileListener))
            listeners.add(csvFileListener);
    }

    /**
     * Initialize the CsvWriter
     * @param writer
     * @return
     */
    protected CsvWriter initializeWriter (Writer writer)
    {
        char delimiter = getCustomDelimiter();
        CsvWriter csvWriter = new CsvWriter(writer, delimiter);
        return csvWriter;
    }

    public String getTextRepresentation() {

        StringWriter sw = new StringWriter();
        try {
            CsvWriter clw = initializeWriter(sw);

            if (isFirstLineHeader() && header.size() > 0) {
                String[] headerArray = new String[header.size()];
                for (int i=0; i<header.size(); i++) {
                    headerArray[i] = header.get(i);
                }
                clw.writeRecord(headerArray);
            }
            for (CSVRow row : rows) {
                clw.writeRecord(row.getEntriesAsArray());
            }
            clw.close();
            sw.close();
        } catch (Exception e) {
            System.out.println("cannot write csv file");
            e.printStackTrace();
        } finally {
        }

        return sw.toString();

    }

}
