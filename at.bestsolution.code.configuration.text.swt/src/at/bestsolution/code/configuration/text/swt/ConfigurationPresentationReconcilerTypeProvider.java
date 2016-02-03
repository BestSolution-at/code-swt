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
package at.bestsolution.code.configuration.text.swt;

import java.util.Map;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.configuration.text.ConfigurationModelDependentTypeProvider;
import org.eclipse.fx.code.editor.configuration.text.ConfigurationModelProvider;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import at.bestsolution.code.swt.services.PresentationReconcilerTypeProvider;

@SuppressWarnings("restriction")
@Component
public class ConfigurationPresentationReconcilerTypeProvider extends ConfigurationModelDependentTypeProvider<PresentationReconciler> implements PresentationReconcilerTypeProvider {
	@Reference(cardinality=ReferenceCardinality.MULTIPLE,policy=ReferencePolicy.DYNAMIC,policyOption=ReferencePolicyOption.GREEDY)
	@Override
	public void registerModelProvider(ConfigurationModelProvider provider, Map<String, Object> properties) {
		super.registerModelProvider(provider, properties);
	}

	@Override
	public void unregisterModelProvider(ConfigurationModelProvider provider) {
		super.unregisterModelProvider(provider);
	}

	@Override
	public Class<? extends PresentationReconciler> getType(Input<?> s) {
		return ConfigurationPresentationReconciler.class;
	}
}
