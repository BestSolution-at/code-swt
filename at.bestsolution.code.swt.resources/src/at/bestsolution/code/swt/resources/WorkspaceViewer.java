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
package at.bestsolution.code.swt.resources;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.fx.code.editor.services.EditorOpener;
import org.eclipse.fx.core.di.ContextBoundValue;
import org.eclipse.fx.core.di.ContextValue;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

@SuppressWarnings("restriction")
public class WorkspaceViewer {
	public static final String SELECTED_RESOURCE_KEY = "selectedResource";

	@Inject
	private EditorOpener editorOpener;
	private TreeViewer viewer;

	@Inject
	@ContextValue(value=SELECTED_RESOURCE_KEY)
	private ContextBoundValue<IResource> primarySelection;

	@PostConstruct
	void init(Composite parent) {
		viewer = new TreeViewer(parent);
		viewer.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IResource r = (IResource) element;
				return r.getName();
			}

			@Override
			public Image getImage(Object element) {
				if( element instanceof IProject ) {
				}
				// TODO Auto-generated method stub
				return super.getImage(element);
			}
		});
		viewer.setContentProvider(new ContentProviderImpl());
		viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		viewer.addOpenListener( s -> {
			IStructuredSelection ss = (IStructuredSelection) s.getSelection();
			@SuppressWarnings("unchecked")
			List<IResource> list = ss.toList();
			list.stream()
				.filter( r -> r instanceof IFile)
				.map( r -> ((IFile)r).getFullPath().toOSString() )
				.forEach(editorOpener::openEditor);
		});
		viewer.addSelectionChangedListener( e -> {
			IStructuredSelection s = (IStructuredSelection) e.getSelection();
			primarySelection.publish((IResource)s.getFirstElement());
		});

		ResourcesPlugin.getWorkspace().addResourceChangeListener(this::handleResourceChanged);
	}

	private void handleResourceChanged(IResourceChangeEvent event) {
		viewer.refresh();
//		try {
//			event.getDelta().accept( new IResourceDeltaVisitor() {
//
//				@Override
//				public boolean visit(IResourceDelta delta) throws CoreException {
//					if( delta.getKind() == IResourceDelta.ADDED ) {
//						IResource resource = delta.getResource();
//						if( resource instanceof IProject ) {
//							viewer.refresh();
//						} else {
//							viewer.refresh(resource.getParent());
//						}
//					} else if( delta.getKind() == IResourceDelta.REMOVED ) {
//						IResource resource = delta.getResource();
//						if( resource instanceof IProject ) {
//							viewer.refresh();
//						} else {
//							viewer.refresh(resource.getParent());
//						}
//					}
//					return true;
//				}
//			});
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	static class ContentProviderImpl implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			IWorkspaceRoot t = (IWorkspaceRoot) inputElement;
			return t.getProjects();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if( parentElement instanceof IContainer ) {
				IContainer p = (IContainer) parentElement;
				try {
					return p.members();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			IResource r = (IResource) element;
			return r.getParent();
		}

		@Override
		public boolean hasChildren(Object element) {
			return element instanceof IContainer;
		}

	}
}
