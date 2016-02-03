/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package at.bestsolution.code.swt.resources.handlers;

import javax.inject.Named;

import org.eclipse.core.resources.IContainer;
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

public class NewFolderHandler {
	@Execute
	public void createFolder(Shell shell, @Named(WorkspaceViewer.SELECTED_RESOURCE_KEY) IResource selectedResource) {
		IContainer container = null;
		if( selectedResource instanceof IContainer ) {
			container = (IContainer) selectedResource;
		} else {
			container = selectedResource.getParent();
		}
		if( container != null ) {
			NewFolderDialog d = new NewFolderDialog(shell, container);
			d.open();
		} else {
			MessageDialog.openInformation(shell, "No parent selected", "You need to select a parent folder");
		}
	}

	static class NewFolderDialog extends TitleAreaDialog {
		private final IContainer container;
		private Text folderName;

		public NewFolderDialog(Shell parentShell, IContainer container) {
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
				l.setText("Folder Name");

				folderName = new Text(container, SWT.BORDER);
				folderName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			}

			return c;
		}

		@Override
		protected void okPressed() {
			if( ! folderName.getText().trim().isEmpty() ) {
				IFolder folder = container.getFolder(new Path(folderName.getText().trim()));
				if( folder.exists() ) {
					setErrorMessage("The folder named '"+folderName.getText().trim()+"' exists already");
				} else {
					try {
						folder.create(true, true, null);
						super.okPressed();
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
