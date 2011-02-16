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
package org.fhsolution.eclipse.plugins.csvedit.customeditor.model;

import org.fhsolution.eclipse.plugins.csvedit.model.AbstractCSVFile;

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
    public char getCustomDelimiter() {
        return optionsProvider.getCustomDelimiter().charAt(0);
    }

    @Override
    public char getCommentChar() {
        String commentChar = optionsProvider.getCommenChar();
        char result = Character.UNASSIGNED;
        if (commentChar != null && commentChar != "") {
           result = commentChar.charAt(0);
        }
        return result;
    }
}