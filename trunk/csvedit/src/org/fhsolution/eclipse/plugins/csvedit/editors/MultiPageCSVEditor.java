package org.fhsolution.eclipse.plugins.csvedit.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.fhsolution.eclipse.plugins.csvedit.filter.CSVTableFilter;
import org.fhsolution.eclipse.plugins.csvedit.model.CSVFile;
import org.fhsolution.eclipse.plugins.csvedit.model.CSVRow;
import org.fhsolution.eclipse.plugins.csvedit.model.ICsvFileModelListener;
import org.fhsolution.eclipse.plugins.csvedit.model.PreferencesCSVOptionsProvider;
import org.fhsolution.eclipse.plugins.csvedit.providers.CSVContentProvider;
import org.fhsolution.eclipse.plugins.csvedit.providers.CSVLabelProvider;
import org.fhsolution.eclipse.plugins.csvedit.sorter.CSVTableSorter;

/**
 *
 * @author fhenri
 *
 */
public class MultiPageCSVEditor extends MultiPageEditorPart
implements IResourceChangeListener {

    private boolean isPageModified;

    /** index of the source page */
    private static final int indexSRC = 1;
    /** index of the table page */
    private static final int indexTBL = 0;

    /** The text editor used in page 0. */
    private TextEditor editor;

    /** The table viewer used in page 1. */
    private TableViewer tableViewer;

    /** use a preference page specific for each csv file. */

    private CSVTableSorter tableSorter;
    private CSVLabelProvider labelProvider;

    private Menu tableHeaderMenu;

    private CSVFile model;

    /**
     *
     */
    private final ICsvFileModelListener csvFileListener = new ICsvFileModelListener() {
        public void entryChanged(CSVRow row, int rowIndex) {
            tableViewer.update(row, new String[] { Integer.toString(rowIndex) });
            tableModified();
        }
    };

    PreferencesCSVOptionsProvider pref = new PreferencesCSVOptionsProvider();
    /**
     * Creates a multi-page editor example.
     */
    public MultiPageCSVEditor () {
        super();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
        model = new CSVFile(pref);
    }

    /**
     * Creates the pages of the multi-page editor.
     *
     * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
     */
    protected void createPages () {
        try {
            createTablePage();
            createSourcePage();
            updateTitle();
            populateTablePage();
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    /**
     * Creates page 0 of the multi-page editor, which contains a text editor.
     */
    private void createSourcePage () {
        try {
            editor = new TextEditor();
            addPage(editor, getEditorInput());
            setPageText(indexSRC, "CSV Source");
        } catch (PartInitException e) {
            ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
        }
    }

    /**
     *
     */
    private void createTablePage () {
        Composite parent = getContainer();

        // XXX move all the creation into its own component
        Canvas canvas = new Canvas(parent, SWT.None);

        GridLayout layout = new GridLayout(5, false);
        canvas.setLayout(layout);

        // create the header part with the search function and Add/Delete rows
        Label searchLabel = new Label(canvas, SWT.NONE);
        searchLabel.setText("Search: ");
        final Text searchText = new Text(canvas, SWT.BORDER | SWT.SEARCH);
        searchText.setLayoutData(
                new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

        // Create and configure the buttons
        Button insert = new Button(canvas, SWT.PUSH | SWT.CENTER);
        insert.setText("Insert Row");
        insert.setToolTipText("Insert a new row before the current one");
        GridData buttonInsertGridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
        buttonInsertGridData.widthHint = 80;
        insert.setLayoutData(buttonInsertGridData);
        insert.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                CSVRow row = (CSVRow) ((IStructuredSelection)
                        tableViewer.getSelection()).getFirstElement();
                if (row != null) {
                    model.addLineAfterElement(row);
                    tableViewer.refresh();
                    tableModified();
                }
            }
        });
        insert.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                //if(((e.stateMask & SWT.CTRL) != 0) & (e.keyCode == 'd')) {
                //if (e.stateMask == SWT.CTRL && e.keyCode == 'd') {
                if (e.character == SWT.DEL) {
                    System.out.println("DEL PRESSED");
                    CSVRow row = (CSVRow) ((IStructuredSelection)
                                tableViewer.getSelection()).getFirstElement();
                    if (row != null) {
                        model.addLineAfterElement(row);
                        tableViewer.refresh();
                        tableModified();
                    }
                }
            }
        });

        Button add = new Button(canvas, SWT.PUSH | SWT.CENTER);
        add.setText("Add Row");
        add.setToolTipText("Add a new row at the end of the file");
        GridData buttonAddGridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
        buttonAddGridData.widthHint = 80;
        add.setLayoutData(buttonAddGridData);
        add.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                model.addLine();
                tableViewer.refresh();
                tableModified();
            }
        });

        Button delete = new Button(canvas, SWT.PUSH | SWT.CENTER);
        delete.setText("Delete Row");
        delete.setToolTipText("Delete the current row");
        GridData buttonDelGridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
        buttonDelGridData.widthHint = 80;
        delete.setLayoutData(buttonDelGridData);
        delete.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                CSVRow row = (CSVRow) ((IStructuredSelection)
                        tableViewer.getSelection()).getFirstElement();
                if (row != null) {
                    model.removeLine(row);
                    tableViewer.refresh();
                    tableModified();
                }
            }
        });
        /*
        insert.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.stateMask == SWT.CTRL && e.keyCode == 'd') {
                    System.out.println("DEL pressed");
                    CSVRow row = (CSVRow) ((IStructuredSelection)
                            tableViewer.getSelection()).getFirstElement();
                    if (row != null) {
                        model.removeLine(row);
                        tableViewer.refresh();
                        tableModified();
                    }
                }
            }
        });
        */

        // case sensitive search ?
        Label sensitiveLabel = new Label(canvas, SWT.NONE);
        sensitiveLabel.setText("case sensitive");
        final Button sensitiveBtn = new Button(canvas, SWT.CHECK);
        sensitiveBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        // manage 1st line - should only be visible if global option is set
        if (pref.getUseFirstLineAsHeader()) {
            Label encodingLineLabel = new Label(canvas, SWT.NONE);
            encodingLineLabel.setText("Display 1st line");
            final Button encodingLineBtn = new Button(canvas, SWT.CHECK);
            encodingLineBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
            encodingLineBtn.setSelection(true);
            encodingLineBtn.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    model.displayFirstLine(encodingLineBtn.getSelection());
                    updateTableFromTextEditor();
                }
            });
        }

        tableViewer = new TableViewer(canvas,
                SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION |SWT.BORDER);
        tableViewer.setUseHashlookup(true);
        final Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // set the sorter for the table
        tableSorter = new CSVTableSorter();
        tableViewer.setSorter(tableSorter);

        // set a table filter
        final CSVTableFilter tableFilter = new CSVTableFilter();
        tableViewer.addFilter(tableFilter);

        // add the filtering and coloring when searching specific elements.
        searchText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                tableFilter.setSearchText(searchText.getText(), sensitiveBtn.getSelection());
                labelProvider.setSearchText(searchText.getText());
                tableViewer.refresh();
            }
        });
        sensitiveBtn.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                tableFilter.setSearchText(searchText.getText(), sensitiveBtn.getSelection());
                labelProvider.setSearchText(searchText.getText());
                tableViewer.refresh();
            }
        });

        /*
        // create a TableCursor to navigate around the table
        final TableCursor cursor = new TableCursor(table, SWT.NONE);
        // create an editor to edit the cell when the user hits "ENTER"
        // while over a cell in the table
        final ControlEditor editor = new ControlEditor(cursor);
        editor.grabHorizontal = true;
        editor.grabVertical = true;

        cursor.addSelectionListener(new SelectionAdapter() {
            // This is called as the user navigates around the table
            public void widgetSelected(SelectionEvent e) {
                // Select the row in the table where the TableCursor is
                table.setSelection(new TableItem[] {cursor.getRow()});
            }

            // when the user hits "ENTER" in the TableCursor,
            // pop up a text editor so that user can change the text of the cell
            public void widgetDefaultSelected(SelectionEvent e) {
                // Begin an editing session
                final Text text = new Text(cursor, SWT.NONE);

                // Copy the text from the cell to the Text
                int column = cursor.getColumn();
                text.setText(cursor.getRow().getText(column));

                // Add a handler to detect key presses
                text.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        // tab will save & move to the next column
                        if (e.character == SWT.TAB) {
                            TableItem row = cursor.getRow();
                            int column = cursor.getColumn();
                            row.setText(column, text.getText());
                            text.dispose();
                            cursor.setSelection(row, column+1);
                            tableModified();
                        }
                        // close the text editor and copy the data over
                        // when the user hits "ENTER"
                        if (e.character == SWT.CR) {
                            TableItem row = cursor.getRow();
                            row.setText(cursor.getColumn(), text.getText());
                            System.out.println("call table modified");
                            tableModified();
                            text.dispose();
                        }
                        // close the text editor when the user hits "ESC"
                        if (e.character == SWT.ESC) {
                            text.dispose();
                        }
                    }
                });
                // close the text editor when the user tabs away
                text.addFocusListener(new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        text.dispose();
                    }
                });
                editor.setEditor(text);
                text.setFocus();
            }
        });

        /*
        // Hide the TableCursor when the user hits the "CTRL" or "SHIFT" key.
        // This allows the user to select multiple items in the table.
        cursor.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

                // delete line
                if (e.character == SWT.DEL) {
                    TableItem row = cursor.getRow();
                    tableModified();
                    row.dispose();
                    //table.showItem(row);
                    //cursor.setSelection(row, 0);
                }

                // insert line
                if (e.character == (char) SWT.F8) {
                    TableItem row = cursor.getRow();
                    row.dispose();
                }

                // add line

                cursor.setVisible(true);
                cursor.setFocus();

                if (e.keyCode == SWT.CTRL
                    || e.keyCode == SWT.SHIFT
                    || (e.stateMask & SWT.CONTROL) != 0
                    || (e.stateMask & SWT.SHIFT) != 0) {
                    cursor.setVisible(false);
                    return;
                }
            }
        });

        // When the user double clicks in the TableCursor, pop up a text editor so that
        // they can change the text of the cell.
        cursor.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                final Text text = new Text(cursor, SWT.NONE);
                TableItem row = cursor.getRow();
                int column = cursor.getColumn();
                text.setText(row.getText(column));
                text.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        // close the text editor and copy the data over
                        // when the user hits "ENTER"
                        if (e.character == SWT.CR) {
                            TableItem row = cursor.getRow();
                            System.out.println("on row " + row);
                            int column = cursor.getColumn();
                            row.setText(column, text.getText());
                            tableModified();
                            text.dispose();
                        }
                        // close the text editor when the user hits "ESC"
                        if (e.character == SWT.ESC) {
                            text.dispose();
                        }
                    }
                });
                // close the text editor when the user clicks away
                text.addFocusListener(new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        text.dispose();
                    }
                });
                editor.setEditor(text);
                text.setFocus();
            }
        });

        // Show the TableCursor when the user releases the "SHIFT" or "CTRL" key.
        // This signals the end of the multiple selection task.
        table.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {

                if (e.keyCode == SWT.CONTROL && (e.stateMask & SWT.SHIFT) != 0)
                    return;
                if (e.keyCode == SWT.SHIFT && (e.stateMask & SWT.CONTROL) != 0)
                    return;
                if (e.keyCode != SWT.CONTROL
                    && (e.stateMask & SWT.CONTROL) != 0)
                    return;
                if (e.keyCode != SWT.SHIFT && (e.stateMask & SWT.SHIFT) != 0)
                    return;

                TableItem[] selection = table.getSelection();
                TableItem row =
                    (selection.length == 0) ? table.getItem(table.getTopIndex()) : selection[0];
                table.showItem(row);
                cursor.setSelection(row, 0);
                cursor.setVisible(true);
                cursor.setFocus();
            }
        });
        */

        // Layout the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 5;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        tableViewer.getControl().setLayoutData(gridData);

        addPage(canvas);
        setPageText(indexTBL, "CSV Table");
    }

    /*
    private void filterTextForSearch() {
    }
    */

    /**
     * Set Name of the file to the tab
     */
    private void updateTitle () {
        IEditorInput input = getEditorInput();
        setPartName(input.getName());
        setTitleToolTip(input.getToolTipText());
    }

    /**
     * @throws Exception
     */
    private void populateTablePage () throws Exception {

        tableViewer.setContentProvider(new CSVContentProvider());
        labelProvider = new CSVLabelProvider();
        tableViewer.setLabelProvider(labelProvider);

        // make the selection available
        getSite().setSelectionProvider(tableViewer);

        tableViewer.getTable().getDisplay().asyncExec(
                new Runnable() {
                    public void run() { updateTableFromTextEditor(); }
                }
        );
    }

    /**
     *
     */
    public void tableModified () {
        boolean wasDirty = isDirty();
        isPageModified = true;
        if (!wasDirty) {
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    /**
     *
     */
    private void updateTableFromTextEditor () {

        for (TableColumn column : tableViewer.getTable().getColumns()) {
            column.dispose();
        }

        // PropertyFile propertyFile = (PropertyFile) treeViewer.getInput();
        model.removeModelListener(csvFileListener);

        model.setInput(editor.getDocumentProvider().getDocument(
                editor.getEditorInput()).get());

        tableHeaderMenu = new Menu(tableViewer.getTable());
        // create columns
        for (int i = 0; i < model.getHeader().size(); i++) {
            final TableColumn column = new TableColumn(tableViewer.getTable(), SWT.LEFT);
            final int index = i;
            column.setText(model.getHeader().get(i));
            column.setWidth(100);
            column.setResizable(true);
            column.setMoveable(true);

            // create menu item
            final MenuItem itemName = new MenuItem(tableHeaderMenu, SWT.CHECK);
            itemName.setText(column.getText());
            itemName.setSelection(column.getResizable());
            itemName.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                    if (itemName.getSelection()) {
                        column.setWidth(150);
                        column.setResizable(true);
                    } else {
                        column.setWidth(0);
                        column.setResizable(false);
                    }
                }
            });

            // Setting the right sorter
            column.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    tableSorter.setColumn(index);
                    int dir = tableViewer.getTable().getSortDirection();
                    if (tableViewer.getTable().getSortColumn() == column) {
                        dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
                    } else {

                        dir = SWT.DOWN;
                    }
                    tableViewer.getTable().setSortDirection(dir);
                    tableViewer.getTable().setSortColumn(column);
                    tableViewer.refresh();
                }
            });
        }

        tableViewer.setInput(model);
        model.addModelListener(csvFileListener);

        tableViewer.getTable().addListener(SWT.MenuDetect, new Listener() {
            public void handleEvent(Event event) {
                tableViewer.getTable().setMenu(tableHeaderMenu);
            }
        });

        String[] columnProperties = new String[model.getColumnCount()];
        CellEditor[] cellEditors = new CellEditor[model.getColumnCount()];

        for (int i = 0; i < model.getColumnCount(); i++) {
            columnProperties[i] = Integer.toString(i);
            cellEditors[i] = new TextCellEditor(tableViewer.getTable());
        }

        tableViewer.setColumnProperties(columnProperties);
        tableViewer.setCellEditors(cellEditors);
        tableViewer.setCellModifier(new CSVEditorCellModifier());
    }

    /**
     * The <code>MultiPageEditorPart</code> implementation of this
     * <code>IWorkbenchPart</code> method disposes all nested editors.
     * This method is automatically called when the editor is closed
     * and marks the end of the editor's life cycle.
     * It cleans up any platform resources, such as images, clipboard,
     * and so on, which were created by this class.
     *
     * @see org.eclipse.ui.part.MultiPageEditorPart#dispose()
     */
    public void dispose () {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        super.dispose();
    }

    /**
     * Saves the multi-page editor's document.
     * If the save is successful, the part should fire a property
     * changed event (PROP_DIRTY property), reflecting the new dirty state.
     * If the save is canceled via user action, or for any other reason,
     * the part should invoke setCanceled on the IProgressMonitor to
     * inform the caller
     *
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave (IProgressMonitor monitor) {
        if (getActivePage() == indexTBL && isPageModified) {
            updateTextEditorFromTable();
        } else {
            updateTableFromTextEditor();
        }

        isPageModified = false;
        editor.doSave(monitor);
    }

    /**
     * Returns whether the "Save As" operation is supported by this part.
     *
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed () {
        return true;
    }

    /**
     * Saves the multi-page editor's document as another file. Also updates the
     * text for page 0's tab, and updates this multi-page editor's input to
     * correspond to the nested editor's.
     *
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    public void doSaveAs () {
        if (getActivePage() == indexTBL && isPageModified) {
            updateTextEditorFromTable();
        } else {
            updateTableFromTextEditor();
        }

        isPageModified = false;

        editor.doSaveAs();
        setInput(editor.getEditorInput());
        updateTitle();
    }

    /**
     * Sets the cursor and selection state for this editor as specified by
     * the given marker
     *
     * @param marker
     */
    public void gotoMarker (IMarker marker) {
        setActivePage(indexTBL);
        IDE.gotoMarker(getEditor(indexTBL), marker);
    }

    /**
     * Initializes this editor with the given editor site and input.
     * This method is automatically called shortly after editor construction;
     * it marks the start of the editor's lifecycle.

     * The <code>MultiPageEditorExample</code> implementation of this method
     * checks that the input is an instance of <code>IFileEditorInput</code>.
     *
     * @see org.eclipse.ui.part.MultiPageEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    public void init (IEditorSite site, IEditorInput editorInput) throws PartInitException {
        if (!(editorInput instanceof IFileEditorInput))
            throw new PartInitException("Invalid Input: Must be IFileEditorInput");
        super.init(site, editorInput);
    }

    /**
     * @see org.eclipse.ui.part.MultiPageEditorPart#handlePropertyChange(int)
     */
    protected void handlePropertyChange (int propertyId) {
        if (propertyId == IEditorPart.PROP_DIRTY)
            isPageModified = isDirty();
        super.handlePropertyChange(propertyId);
    }

    /**
     * @see org.eclipse.ui.part.MultiPageEditorPart#isDirty()
     */
    public boolean isDirty () {
        return isPageModified || super.isDirty();
    }

    /**
     * Calculates the contents of page 2 when the it is activated.
     *
     * @see org.eclipse.ui.part.MultiPageEditorPart#pageChange(int)
     */
    protected void pageChange (int newPageIndex) {
        switch (newPageIndex) {
           case indexSRC :
              if (isDirty())
                 updateTableFromTextEditor();
              break;
           case indexTBL :
              if (isPageModified)
                 updateTextEditorFromTable();
              break;
        }
        isPageModified = false;
        super.pageChange(newPageIndex);
     }

    /**
     *
     */
    private void updateTextEditorFromTable () {
        editor
           .getDocumentProvider()
           .getDocument(editor.getEditorInput())
           .set(((CSVFile) tableViewer.getInput()).getTextRepresentation());
     }

    /**
     * When the focus shifts to the editor, this method is called;
     * it must then redirect focus to the appropriate editor based
     * on which page is currently selected.
     *
     * @see org.eclipse.ui.part.MultiPageEditorPart#setFocus()
     */
    public void setFocus () {
        switch (getActivePage()) {
        case indexSRC:
            editor.setFocus();
            break;
        case indexTBL:
            tableViewer.getTable().setFocus();
            break;
        }
    }

    /**
     * Closes all project files on project close.
     *
     * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
     */
    public void resourceChanged (final IResourceChangeEvent event) {
        if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
                    for (int i = 0; i < pages.length; i++) {
                        if (((FileEditorInput) editor.getEditorInput()).getFile().getProject().equals(event.getResource())) {
                            IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
                            pages[i].closeEditor(editorPart, true);
                        }
                    }
                }
            });
        }
    }
}
