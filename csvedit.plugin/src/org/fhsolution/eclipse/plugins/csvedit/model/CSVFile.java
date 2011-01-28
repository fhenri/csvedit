package org.fhsolution.eclipse.plugins.csvedit.model;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author fhenri
 *
 */
public class CSVFile {

    private int nbOfColumns = 1;
    private boolean displayFirstLine = true;
    private ArrayList<CSVRow> rows = new ArrayList<CSVRow>();
    private ArrayList<String> header = new ArrayList<String>();
    private ArrayList<ICsvFileModelListener> listeners = new ArrayList<ICsvFileModelListener>();

    private PreferencesCSVOptionsProvider optionsProvider;

    public CSVFile(PreferencesCSVOptionsProvider provider) {
        setCsvOptionsProvider(provider);
    }

    public void setInput(String text) {
        readLines(text);
    }

    public void displayFirstLine(boolean display) {
        displayFirstLine = display;
    }

    public void setCsvOptionsProvider(PreferencesCSVOptionsProvider provider) {
        optionsProvider = provider;
    }

    public PreferencesCSVOptionsProvider getCsvOptionsProvider() {
        return optionsProvider;
    }

    private void readLines(String fileText) {
        rows.clear();

        try {
            String customDelimiter = this.optionsProvider.getCustomDelimiter();
            char customDelimiterAsChar = customDelimiter.charAt(0);

            CsvPreference customCSVPreference = CsvPreference.STANDARD_PREFERENCE;
            customCSVPreference.setDelimiterChar(customDelimiterAsChar);
            StringReader sr = new StringReader(fileText);
            CsvListReader clr = new CsvListReader(sr,customCSVPreference);
            List<String> line = clr.read();

            // should I read first line ?
            if (!displayFirstLine) {
                clr.read();
            }

            while (line != null) {

                CSVRow csvRow = new CSVRow(line, this);
                rows.add(csvRow);
                if (nbOfColumns < line.size()) {
                    nbOfColumns = line.size();
                }
                line = clr.read();
            }

            populateHeaders();
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
        CSVRow newRow = CSVRow.createEmptyLine(nbOfColumns, this);
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

    private void populateHeaders() {
        header.clear();

        if (optionsProvider.getUseFirstLineAsHeader() && rows.size() > 0) {
            Object[] columnTitles = rows.get(0).getEntries().toArray();
            for (Object o : columnTitles) {
                header.add(o.toString());
            }
            rows.remove(0);

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

    protected void rowChanged(CSVRow row, int rowIndex) {
        for (ICsvFileModelListener l : listeners) {
            ((ICsvFileModelListener) l).entryChanged(row, rowIndex);
        }
    }

    public void removeRow(int rowIndex) {
        rows.remove(rowIndex);
    }

    public void removeModelListener(ICsvFileModelListener csvFileListener) {
        listeners.remove(csvFileListener);
    }

    public void addModelListener(ICsvFileModelListener csvFileListener) {
        if (!listeners.contains(csvFileListener))
            listeners.add(csvFileListener);

    }

    public String getTextRepresentation() {

        StringWriter sw = new StringWriter();
        try {
            CsvListWriter clw = new CsvListWriter(sw, CsvPreference.STANDARD_PREFERENCE);

            if (optionsProvider.getUseFirstLineAsHeader() && header.size() > 0) {
                clw.write(header);
            }
            for (CSVRow row : rows) {
                clw.write(row.getEntries());
            }
            clw.close();
            sw.close();
        } catch (Exception e) {
        }

        return sw.toString();

    }

}
