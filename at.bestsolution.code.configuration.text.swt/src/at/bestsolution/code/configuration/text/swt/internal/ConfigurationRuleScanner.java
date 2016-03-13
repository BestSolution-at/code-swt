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
package at.bestsolution.code.configuration.text.swt.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.eclipse.fx.code.editor.configuration.LanguageDef;
import org.eclipse.fx.code.editor.configuration.Partition;
import org.eclipse.fx.code.editor.configuration.TokenScanner;
import org.eclipse.fx.code.editor.configuration.TokenScanner_CharacterRule;
import org.eclipse.fx.code.editor.configuration.TokenScanner_Keyword;
import org.eclipse.fx.code.editor.configuration.TokenScanner_MultiLineRule;
import org.eclipse.fx.code.editor.configuration.TokenScanner_PatternRule;
import org.eclipse.fx.code.editor.configuration.TokenScanner_SingleLineRule;
import org.eclipse.fx.code.editor.configuration.text.Util;
import org.eclipse.fx.text.rules.CharacterRule;
import org.eclipse.fx.text.rules.CombinedWordRule;
import org.eclipse.fx.text.rules.JavaLikeWordDetector;
import org.eclipse.fx.text.rules.RegexRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

import at.bestsolution.code.swt.services.TextAttributeFactory;

@SuppressWarnings("restriction")
public class ConfigurationRuleScanner extends RuleBasedScanner {
	public ConfigurationRuleScanner(LanguageDef languageDef, Partition parition, TextAttributeFactory factory) {
		Token defaultToken = null;
		IRule[] rules = new IRule[getRuleCount(parition)];
		int i = 0;
		Map<Token,TokenScanner_Keyword> keyWordList = new HashMap<>();
		for( org.eclipse.fx.code.editor.configuration.Token st : parition.getTokenList() ) {
			Token token = new Token(factory.getAttribute(languageDef.getFileSuffix(), st.getName()));
			if( st.isDefaultToken() ) {
				defaultToken = token;
				setDefaultReturnToken(token);
			}

			for (TokenScanner ru : st.getTokenScannerList()) {
				if( ru instanceof TokenScanner_SingleLineRule ) {
					TokenScanner_SingleLineRule sru = (TokenScanner_SingleLineRule) ru;
					String endSeq = sru.getEndSeq();
					rules[i++] = Util.wrap(sru.getCheck(),new SingleLineRule(
							sru.getStartSeq(),
							endSeq,
							token,
							sru.getEscapedBy() != null ? sru.getEscapedBy().charAt(0) : 0,
							endSeq == null || endSeq.isEmpty()));
				} else if( ru instanceof TokenScanner_MultiLineRule ) {
					TokenScanner_MultiLineRule sml = (TokenScanner_MultiLineRule) ru;
					String endSeq = sml.getEndSeq();
					rules[i++] = Util.wrap(sml.getCheck(),new MultiLineRule(
							sml.getStartSeq(),
							endSeq,
							token,
							sml.getEscapedBy() != null ? sml.getEscapedBy().charAt(0) : 0,
							endSeq == null || endSeq.isEmpty()));
				} else if( ru instanceof TokenScanner_CharacterRule ) {
					TokenScanner_CharacterRule scr = (TokenScanner_CharacterRule) ru;
					char[] c = new char[scr.getCharacterList().size()];
					for( int j = 0; j < c.length; j++ ) {
						c[j] = scr.getCharacterList().get(j).charAt(0);
					}
					rules[i++] = Util.wrap(scr.getCheck(),new CharacterRule(token, c));
				} else if( ru instanceof TokenScanner_Keyword ) {
					keyWordList.put(token,(TokenScanner_Keyword) ru);
				} else if( ru instanceof TokenScanner_PatternRule ) {
					TokenScanner_PatternRule rr = (TokenScanner_PatternRule) ru;
					rules[i++] = Util.wrap(rr.getCheck(),new RegexRule(token, Pattern.compile(rr.getStartPattern()), Math.max(1,rr.getStartLength()),Pattern.compile(rr.getContainmentPattern())));
				}
			}
		}

		if( parition.getWhitespace() != null ) {
			if( parition.getWhitespace().isJavawhiteSpace() ) {
				rules[i++] = new WhitespaceRule(new IWhitespaceDetector() {

					@Override
					public boolean isWhitespace(char c) {
						return Character.isWhitespace(c);
					}
				});
			} else {
				rules[i++] = new WhitespaceRule(new FixedCharacterWSDetector(parition.getWhitespace().getCharacterList()));
			}
		}


		if( ! keyWordList.isEmpty() ) {
			JavaLikeWordDetector wordDetector= new JavaLikeWordDetector();
			CombinedWordRule combinedWordRule= new CombinedWordRule(wordDetector, defaultToken);
			for( Entry<Token, TokenScanner_Keyword> kg : keyWordList.entrySet() ) {
				CombinedWordRule.WordMatcher wordRule= new CombinedWordRule.WordMatcher();
				for( String k : kg.getValue().getKeywordList() ) {
					wordRule.addWord(k, kg.getKey());
				}
				combinedWordRule.addWordMatcher(wordRule);
			}
			rules[rules.length-1] = combinedWordRule;
		}

		setRules(rules);
	}

	private static int getRuleCount(Partition partition) {
		int rv = 0;
		boolean hasKeyGroup = false;
		for (org.eclipse.fx.code.editor.configuration.Token token : partition.getTokenList()) {
			rv += token.getTokenScannerList().stream().filter( s -> ! (s instanceof TokenScanner_Keyword)).count();
			if( token.getTokenScannerList().stream().filter( s -> s instanceof TokenScanner_Keyword).count() > 0 ) {
				hasKeyGroup = true;
			}
		}

		if( hasKeyGroup ) {
			rv += 1;
		}

		if(partition.getWhitespace() != null) {
			rv += 1;
		}

		return rv;
	}

	static class JSWSDectector implements IWhitespaceDetector {

		@Override
		public boolean isWhitespace(char c) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	static class FixedCharacterWSDetector implements IWhitespaceDetector {
		private final char[] chars;

		public FixedCharacterWSDetector(List<String> list) {
			chars = new char[list.size()];
			int i = 0;
			for( String s : list ) {
				chars[i++] = s.charAt(0);
			}
		}

		@Override
		public boolean isWhitespace(char c) {
			for( char ch : chars ) {
				if( ch == c ) {
					return true;
				}
			}
			return false;
		}
	}
}
