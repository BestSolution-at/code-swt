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
package at.bestsolution.code.swt.services.internal;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.osgi.service.component.annotations.Component;

import at.bestsolution.code.swt.services.PresentationReconcilerTypeProvider;

@SuppressWarnings("restriction")
@Component(property="service.ranking:Integer=-1")
public class NoHighlightPresentationReconcilerTypeProvider implements PresentationReconcilerTypeProvider {

	@Override
	public Class<? extends PresentationReconciler> getType(Input<?> s) {
		return NoHighlightPresentationReconciler.class;
	}

	@Override
	public boolean test(Input<?> t) {
		return true;
	}

	static class NoHighlightPresentationReconciler extends PresentationReconciler {
		public NoHighlightPresentationReconciler() {
			org.eclipse.jface.text.rules.DefaultDamagerRepairer r = new org.eclipse.jface.text.rules.DefaultDamagerRepairer(new NoHighlightScanner());
			setDamager(r, "__dftl_partition_content_type");
			setRepairer(r, "__dftl_partition_content_type");
		}
	}

	static class NoHighlightScanner extends RuleBasedScanner {
		public NoHighlightScanner() {
			setDefaultReturnToken(new Token("sourcetext"));
		}
	}
}
