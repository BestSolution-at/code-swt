/*******************************************************************************
* Copyright (c) 2016 BestSolution.at and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* 	Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
*******************************************************************************/
package at.bestsolution.code.swt.resources.handlers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Named;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.bestsolution.code.swt.resources.WorkspaceViewer;

public class NewFileHandler {
	@Execute
	public void createFile(Shell shell, @Named(WorkspaceViewer.SELECTED_RESOURCE_KEY) IResource selectedResource) {
		IContainer container = null;
		if( selectedResource instanceof IContainer ) {
			container = (IContainer) selectedResource;
		} else {
			container = selectedResource.getParent();
		}
		if( container != null ) {
			NewFileDialog d = new NewFileDialog(shell, container);
			d.open();
		} else {
			MessageDialog.openInformation(shell, "No parent selected", "You need to select a parent folder");
		}
	}

	static class NewFileDialog extends TitleAreaDialog {
		private final IContainer container;
		private Text folderName;

		public NewFileDialog(Shell parentShell, IContainer container) {
			super(parentShell);
			this.container = container;
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite c = (Composite) super.createDialogArea(parent);

			Composite container = new Composite(c, SWT.NONE);
			container.setLayoutData(new GridData(GridData.FILL_BOTH));
			container.setLayout(new GridLayout(2, false));

			{
				Label l = new Label(container, SWT.NONE);
				l.setText("File Name");

				folderName = new Text(container, SWT.BORDER);
				folderName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			}

			return c;
		}

		@Override
		protected void okPressed() {
			if( ! folderName.getText().trim().isEmpty() ) {
				IFile folder = container.getFile(new Path(folderName.getText().trim()));
				if( folder.exists() ) {
					setErrorMessage("The folder named '"+folderName.getText().trim()+"' exists already");
				} else {
					try ( InputStream in = new ByteArrayInputStream(new byte[0]) ) {
						folder.create(in, true, null);
						super.okPressed();
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
