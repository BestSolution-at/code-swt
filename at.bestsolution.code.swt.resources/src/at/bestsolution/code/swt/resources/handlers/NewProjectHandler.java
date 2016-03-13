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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewProjectHandler {
	@Execute
	void createProject(Shell shell) {
		ProjectTitleAreaDialog dialog = new ProjectTitleAreaDialog(shell);
		dialog.open();
	}

	static class ProjectTitleAreaDialog extends TitleAreaDialog {

		private Text projectName;

		public ProjectTitleAreaDialog(Shell parentShell) {
			super(parentShell);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite c = (Composite) super.createDialogArea(parent);
			getShell().setText("Create Project");
			setTitle("Create Project");
			setMessage("Create a new project");

			Composite container = new Composite(c, SWT.NONE);
			container.setLayout(new GridLayout(2, false));
			container.setLayoutData(new GridData(GridData.FILL_BOTH));

			{
				Label l = new Label(container, SWT.NONE);
				l.setText("Name");

				projectName = new Text(container, SWT.BORDER);
				projectName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			}

			return c;
		}

		@Override
		protected void okPressed() {
			if( ! projectName.getText().trim().isEmpty() ) {
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName.getText().trim());
				if( project.exists() ) {
					setErrorMessage("There's a already a project named '"+projectName.getText().trim()+"'");
				} else {
					try {
						project.create(null);
						project.open(null);
						super.okPressed();
					} catch (CoreException e) {
						LoggerCreator.createLogger(getClass()).error("Could not create project '"+projectName.getText()+"'", e);
					}
				}
			}
		}

	}
}
