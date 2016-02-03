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
package at.bestsolution.code.swt.e4;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.fx.code.editor.Constants;
import org.eclipse.fx.code.editor.services.EditorOpener;
import org.eclipse.fx.core.URI;
import org.osgi.service.component.annotations.Component;

@SuppressWarnings({ "restriction" })
@Component(service=IContextFunction.class,property={"service.context.key=org.eclipse.fx.code.editor.services.EditorOpener"})
public class EditorOpenerCF extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context) {
		return ContextInjectionFactory.make(EditorOpenerImpl.class, context);
	}

	static class EditorOpenerImpl implements EditorOpener {

		private final MWindow window;

		private final MPerspective perspective;

		private final EModelService modelService;

		@Inject
		public EditorOpenerImpl(MWindow window, @Optional MPerspective perspective, EModelService modelService) {
			this.window = window;
			this.perspective = perspective;
			this.modelService = modelService;
		}

		@Override
		public boolean test(String uri) {
			return true;
		}

		@Override
		public boolean openEditor(String uri) {
			List<MPart> list = modelService.findElements(perspective == null ? window : perspective, MPart.class, EModelService.ANYWHERE, (p) -> {
				return uri.equals(p.getPersistedState().get(Constants.DOCUMENT_URL));
			});

			MPart part = null;
			if( list.isEmpty() ) {
				List<MElementContainer> containerList = this.modelService.findElements(perspective == null ? window : perspective, null, MElementContainer.class, Collections.singletonList(Constants.EDITOR_CONTAINER_TAG));
				if( ! containerList.isEmpty() ) {
					MElementContainer<MUIElement> container = containerList.get(0);

					if( container != null ) {
						part = modelService.createModelElement(MPart.class);
						part.setCloseable(true);
						part.setLabel(URI.create(uri).lastSegment());
						part.setContributionURI("bundleclass://at.bestsolution.code.swt/at.bestsolution.code.swt.TextEditor");
						part.setContributorURI("platform:/plugin/at.bestsolution.code.swt.e4");
						part.getPersistedState().put(Constants.DOCUMENT_URL, uri);
						part.getTags().add(EPartService.REMOVE_ON_HIDE_TAG);
						container.getChildren().add(part);
					}
				}

			} else {
				part = list.get(0);
			}

			if( part != null ) {
				IEclipseContext context = modelService.getContainingContext(part);
				if( context != null ) {
					EPartService partService = context.get(EPartService.class);
					partService.activate(part);
				}
				return true;
			}

			return false;
		}

	}
}
