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

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.service.component.annotations.Component;

import at.bestsolution.code.swt.services.internal.SWTThreadSynchronize;

@Component(service=IContextFunction.class,property={"service.context.key=org.eclipse.fx.core.ThreadSynchronize"})
public class ThreadSynchronizeCF extends ContextFunction {
	@Override
	public Object compute(IEclipseContext context) {
		return ContextInjectionFactory.make(SWTThreadSynchronize.class, context);
	}
}
