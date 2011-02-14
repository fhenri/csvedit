package org.fhsolution.eclipse.plugins.csvedit.customeditor.model;

import java.io.Reader;
import java.io.Writer;

import org.fhsolution.eclipse.plugins.csvedit.model.AbstractCSVFile;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

/**
 *
 * {@link DefaultCSVFile} implements the {@link AbstractCSVFile} abstract
 * methods based on the values stored in the preferences system
 * @author jpizar
 *
 */
public class DefaultCSVFile extends AbstractCSVFile {

    /** Preferences provider */
    private final PreferencesCSVOptionsProvider optionsProvider;

    /**
     * Constructor
     * @param provider the {@link PreferencesCSVOptionsProvider}
     */
    public DefaultCSVFile(PreferencesCSVOptionsProvider provider) {
        super();
        this.optionsProvider = provider;
    }

    @Override
    public boolean isFirstLineHeader() {
        return optionsProvider.getUseFirstLineAsHeader();
    }

    @Override
    public boolean getSensitiveSearch() {
        return optionsProvider.getSensitiveSearch();
    }

    @Override
    protected CsvReader initializeReader (Reader reader) {

        CsvReader csvReader = new CsvReader(reader);

        String customDelimiter = optionsProvider.getCustomDelimiter();
        char customDelimiterAsChar = customDelimiter.charAt(0);
        csvReader.setDelimiter(customDelimiterAsChar);

        String commentChar = optionsProvider.getCommenChar();
        if (commentChar != null && commentChar != "") {
            char commentCharAsChar = commentChar.charAt(0);
            csvReader.setComment(commentCharAsChar);
            csvReader.setUseComments(true);

        }
        return csvReader;
    }

    @Override
    protected CsvWriter initializeWriter (Writer writer) {
        String customDelimiter = optionsProvider.getCustomDelimiter();
        CsvWriter csvWriter = new CsvWriter(writer, customDelimiter.charAt(0));
        return csvWriter;
    }
}