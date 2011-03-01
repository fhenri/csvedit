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
package org.fhsolution.eclipse.plugins.csvedit.customeditor;

import org.fhsolution.eclipse.plugins.csvedit.customeditor.model.DefaultCSVFile;
import org.fhsolution.eclipse.plugins.csvedit.customeditor.model.PreferencesCSVOptionsProvider;
import org.fhsolution.eclipse.plugins.csvedit.editors.MultiPageCSVEditor;
import org.fhsolution.eclipse.plugins.csvedit.model.AbstractCSVFile;

public class DefaultCSVMultipageEditor extends MultiPageCSVEditor {

    /**
     * Create the CSV file
     */
    protected AbstractCSVFile createCSVFile()
    {
        PreferencesCSVOptionsProvider preferences = new PreferencesCSVOptionsProvider();
        return new DefaultCSVFile(preferences);
    }

}
