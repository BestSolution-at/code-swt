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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.services.CompletionProposal;
import org.eclipse.fx.code.editor.services.ProposalComputer;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import at.bestsolution.code.swt.services.CompletionProposalPresenter;

@SuppressWarnings("restriction")
public class DefaultSourceViewerConfiguration extends SourceViewerConfiguration {
	private final Input<?> input;
	private final PresentationReconciler reconciler;
	private final ProposalComputer proposalComputer;
	private final IAnnotationModel annotationModel;
	private final CompletionProposalPresenter proposalPresenter;
//	private final List<AnnotationPresenter> annotationPresenters;
//	private final TextHoverMap hoverMap;

	@Inject
	public DefaultSourceViewerConfiguration(
			Input<?> input,
			PresentationReconciler reconciler,
			@Optional ProposalComputer proposalComputer,
			@Optional IAnnotationModel annotationModel,
			@Optional CompletionProposalPresenter proposalPresenter/*,
			@Optional AnnotationPresenter presenter,
			@Optional TextHoverMap hoverMap*/
			) {
		this.input = input;
//		this.hoverMap = hoverMap;
		this.reconciler = reconciler;
		this.proposalComputer = proposalComputer;
		this.annotationModel = annotationModel;
		this.proposalPresenter = proposalPresenter == null ? DefaultProposal::new : proposalPresenter;
//		if( presenter != null ) {
//			this.annotationPresenters = Collections.singletonList(presenter);
//		} else {
//			this.annotationPresenters = Collections.emptyList();
//		}
	}

	public final IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		return reconciler;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		if( proposalComputer != null ) {
			ContentAssistant a = new ContentAssistant() {
				@Override
				protected void install() {
					super.install();
					sourceViewer.getTextWidget().addVerifyKeyListener(new VerifyKeyListener() {

						@Override
						public void verifyKey(VerifyEvent event) {
							if( (event.stateMask & SWT.MODIFIER_MASK & SWT.CTRL) == SWT.CTRL ) {
								showPossibleCompletions();
							}
						}
					});
				}
			};
			a.enableAutoActivation(true);
			a.setAutoActivationDelay(0);
			a.setContentAssistProcessor(new IContentAssistProcessor() {

				@Override
				public String getErrorMessage() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public IContextInformationValidator getContextInformationValidator() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public char[] getContextInformationAutoActivationCharacters() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public char[] getCompletionProposalAutoActivationCharacters() {
					return new char[] {'.'};
				}

				@Override
				public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
					return computeProposals(viewer.getDocument(), offset).toArray(new ICompletionProposal[0]);
				}
			}, IDocument.DEFAULT_CONTENT_TYPE);
			return a;
		}
		return super.getContentAssistant(sourceViewer);
	}

	List<ICompletionProposal> computeProposals(IDocument document, Integer offset) {
		try {
			return proposalComputer.compute().get()
					.stream()
					.map(proposalPresenter::createProposal)
					.collect(Collectors.toList());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

//	@Override
//	public IAnnotationModel getAnnotationModel() {
//		return annotationModel;
//	}

//	@Override
//	public List<AnnotationPresenter> getAnnotationPresenters() {
//		return annotationPresenters;
//	}
//
//	@Override
//	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
//		if( hoverMap != null ) {
//			return hoverMap.getHoverMap().get(contentType);
//		}
//		return super.getTextHover(sourceViewer, contentType);
//	}

	static class DefaultProposal implements ICompletionProposal {
		private final CompletionProposal proposal;

		public DefaultProposal(CompletionProposal proposal) {
			this.proposal = proposal;
		}

		@Override
		public void apply(IDocument document) {
			this.proposal.apply(document);
		}

		@Override
		public Point getSelection(IDocument document) {
			org.eclipse.fx.code.editor.services.CompletionProposal.TextSelection selection = proposal.getSelection(document);
			return new Point(selection.offset, selection.length);
		}

		@Override
		public String getAdditionalProposalInfo() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getDisplayString() {
			return proposal.getLabel().toString();
		}

		@Override
		public Image getImage() {
			return null;
		}

		@Override
		public IContextInformation getContextInformation() {
			return null;
		}
	}
}
