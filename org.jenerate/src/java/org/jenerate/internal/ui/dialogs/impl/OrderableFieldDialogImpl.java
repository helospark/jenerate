// $Id$
package org.jenerate.internal.ui.dialogs.impl;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.identifier.CommandIdentifier;
import org.jenerate.internal.domain.identifier.StrategyIdentifier;
import org.jenerate.internal.manage.DialogStrategyManager;
import org.jenerate.internal.manage.PreferencesManager;

/**
 * An abstract {@link Dialog} allowing configuration of the different parameters for the method generation. It allows to
 * reorganize the fields present in the {@link AbstractFieldDialog} moving them up or down. This class contains some
 * code from org.eclipse.ui.externaltools.internal.ui.BuilderPropertyPage
 * 
 * @author jiayun
 */
public class OrderableFieldDialogImpl<U extends MethodGenerationData> extends FieldDialogImpl<U> {

    private Button upButton;
    private Button downButton;

    public OrderableFieldDialogImpl(CommandIdentifier commandIdentifier, Shell parentShell, String dialogTitle,
            IField[] fields, LinkedHashSet<StrategyIdentifier> possibleStrategies, boolean disableAppendSuper,
            PreferencesManager preferencesManager, IDialogSettings dialogSettings,
            LinkedHashMap<String, IJavaElement> insertPositions, DialogStrategyManager dialogStrategyManager) {
        super(commandIdentifier, parentShell, dialogTitle, fields, possibleStrategies, disableAppendSuper,
                preferencesManager, dialogSettings, insertPositions, dialogStrategyManager);
    }

    @Override
    public void create() {
        super.create();

        Table fieldTable = fieldViewer.getTable();
        fieldTable.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleTableSelectionChanged();
            }
        });
    }

    private void handleTableSelectionChanged() {
        Table fieldTable = fieldViewer.getTable();
        TableItem[] items = fieldTable.getSelection();
        boolean validSelection = items != null && items.length > 0;
        boolean enableUp = validSelection;
        boolean enableDown = validSelection;
        if (validSelection) {
            int indices[] = fieldTable.getSelectionIndices();
            int max = fieldTable.getItemCount();
            enableUp = indices[0] != 0;
            enableDown = indices[indices.length - 1] < max - 1;
        }
        upButton.setEnabled(enableUp);
        downButton.setEnabled(enableDown);
    }

    @Override
    protected void addButtons(final Composite buttonComposite) {
        super.addButtons(buttonComposite);

        GridData data;
        upButton = new Button(buttonComposite, SWT.PUSH);
        upButton.setText("&Up");
        upButton.setEnabled(false);
        upButton.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                moveSelectionUp();
                handleTableSelectionChanged();
            }
        });
        data = new GridData(GridData.FILL_HORIZONTAL);
        upButton.setLayoutData(data);

        downButton = new Button(buttonComposite, SWT.PUSH);
        downButton.setText("Do&wn");
        downButton.setEnabled(false);
        downButton.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                moveSelectionDown();
                handleTableSelectionChanged();
            }
        });
        data = new GridData(GridData.FILL_HORIZONTAL);
        downButton.setLayoutData(data);
    }

    /**
     * Move the current selection in the field list up.
     */
    private void moveSelectionUp() {
        Table builderTable = fieldViewer.getTable();
        int indices[] = builderTable.getSelectionIndices();
        int newSelection[] = new int[indices.length];
        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            if (index > 0) {
                move(builderTable.getItem(index), index - 1);
                newSelection[i] = index - 1;
            }
        }
        builderTable.setSelection(newSelection);
    }

    /**
     * Move the current selection in the field list down.
     */
    private void moveSelectionDown() {
        Table builderTable = fieldViewer.getTable();
        int indices[] = builderTable.getSelectionIndices();
        if (indices.length < 1) {
            return;
        }
        int newSelection[] = new int[indices.length];
        int max = builderTable.getItemCount() - 1;
        for (int i = indices.length - 1; i >= 0; i--) {
            int index = indices[i];
            if (index < max) {
                move(builderTable.getItem(index), index + 1);
                newSelection[i] = index + 1;
            }
        }
        builderTable.setSelection(newSelection);
    }

    /**
     * Moves an entry in the field table to the given index.
     */
    private void move(TableItem item, int index) {
        Object data = item.getData();
        boolean checked = fieldViewer.getChecked(data);
        item.dispose();
        fieldViewer.insert(data, index);
        fieldViewer.setChecked(data, checked);
    }
}