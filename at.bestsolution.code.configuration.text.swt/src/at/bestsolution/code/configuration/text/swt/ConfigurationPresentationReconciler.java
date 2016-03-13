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
package at.bestsolution.code.configuration.text.swt;

import javax.inject.Inject;

import org.eclipse.fx.code.editor.configuration.LanguageDef;
import org.eclipse.fx.code.editor.configuration.Partition;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;

import at.bestsolution.code.configuration.text.swt.internal.ConfigurationRuleScanner;
import at.bestsolution.code.swt.services.TextAttributeFactory;

@SuppressWarnings("restriction")
public class ConfigurationPresentationReconciler extends PresentationReconciler {

	@Inject
	public ConfigurationPresentationReconciler(LanguageDef model, TextAttributeFactory textAttributeFactory) {
		for (Partition sc : model.getPartitionList()) {
			ConfigurationRuleScanner s = new ConfigurationRuleScanner(model, sc, textAttributeFactory);
			DefaultDamagerRepairer dr = new DefaultDamagerRepairer(s);
			setDamager(dr, sc.getName());
			setRepairer(dr, sc.getName());
		}
	}
}
