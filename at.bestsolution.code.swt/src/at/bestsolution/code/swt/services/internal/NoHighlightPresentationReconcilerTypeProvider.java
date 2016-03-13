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
