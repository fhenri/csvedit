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

import org.fhsolution.eclipse.plugins.csvedit.Activator;
import org.fhsolution.eclipse.plugins.csvedit.customeditor.preferences.PreferenceConstants;
import org.fhsolution.eclipse.plugins.csvedit.model.ICsvOptionsProvider;

/**
 *
 * @author fhenri
 *
 */
public class PreferencesCSVOptionsProvider implements ICsvOptionsProvider {

    private boolean useFirstLineAsHeader;
    private boolean sensitiveSearch;
    private String customDelimiter;
    private String commentChar;

    public PreferencesCSVOptionsProvider()
    {
        useFirstLineAsHeader =
            Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.USE_FIRST_LINE_AS_HEADER);
        customDelimiter =
            Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.CUSTOM_DELIMITER);
        sensitiveSearch =
            Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CASE_SENSITIVE_SEARCH);
        commentChar =
            Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.COMMENT_CHAR);
    }

    public String getCustomDelimiter() {
        return customDelimiter;
    }

    public boolean getUseFirstLineAsHeader() {
        return useFirstLineAsHeader;
    }

    public boolean getSensitiveSearch () {
        return sensitiveSearch;
    }

    public String getCommenChar() {
        return commentChar;
    }
}
