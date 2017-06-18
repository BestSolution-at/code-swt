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
package at.bestsolution.code.swt;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.services.ContextInformation;
import org.eclipse.fx.code.editor.services.DelegatingEditingContext;
import org.eclipse.fx.code.editor.services.EditingContext;
import org.eclipse.fx.core.Subscription;
import org.eclipse.fx.core.di.ContextBoundValue;
import org.eclipse.fx.core.di.ContextValue;
import org.eclipse.fx.core.text.TextEditAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.LineNumberChangeRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

@SuppressWarnings("restriction")
public class TextEditor {

	private IDocument document;

	private SourceViewerConfiguration configuration;

	private IDocumentPartitioner partitioner;


	private Input<?> input;

	private ContextBoundValue<Input<?>> activeInput;

	private SourceViewer viewer;

	private EditingContext editingContext;

	private static final SharedColors SHARED_COLORS = new SharedColors();

	private static Font font;

	public TextEditor() {
		if( font == null ) {
			loadFont("Hack-Bold");
			loadFont("Hack-BoldItalic");
			loadFont("Hack-Italic");
			loadFont("Hack-Regular");
			font = new Font(Display.getCurrent(), "Hack", Display.getCurrent().getSystemFont().getFontData()[0].getHeight(), SWT.NORMAL);
		}
	}

	private static void loadFont(String name) {
		try {
			File f = new File(FileLocator.toFileURL(TextEditor.class.getClassLoader().getResource("at/bestsolution/code/swt/hack/"+name+".ttf")).toURI());
			Display.getCurrent().loadFont(f.getAbsolutePath());
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Inject
	public void setDocument(IDocument document) {
		if( viewer != null ) {
			throw new IllegalArgumentException("The document has to be set before the editor is initialized");
		}
		this.document = document;
	}

	@Inject
	public void setSourceViewerConfiguration(SourceViewerConfiguration configuration) {
		if( viewer != null ) {
			throw new IllegalArgumentException("The configuration has to be set before the editor is initialized");
		}
		this.configuration = configuration;
	}

	@Inject
	public void setPartitioner(IDocumentPartitioner partitioner) {
		if( viewer != null ) {
			throw new IllegalArgumentException("The partitioner has to be set before the editor is initialized");
		}

		this.partitioner = partitioner;
	}

	@Inject
	public void setInput(Input<?> input) {
		if( viewer != null ) {
			throw new IllegalArgumentException("The input has to be set before the editor is initialized");
		}

		this.input = input;
	}

	@Inject
	@Optional
	public void setActiveInputTracker(@ContextValue("activeInput") ContextBoundValue<Input<?>> activeInput) {
		this.activeInput = activeInput;
	}

	@Inject
	public void setEditingContext(EditingContext editingContext) {
		if( viewer != null ) {
			throw new IllegalArgumentException("The EditingContext has to be set before the editor is initialized");
		}
		this.editingContext = editingContext;
	}

	@PostConstruct
	public void initUI(Composite pane) {
		viewer = createSourceViewer(pane);
		viewer.getTextWidget().setFont(font);
		if( document instanceof IDocumentExtension3 ) {
			((IDocumentExtension3)document).setDocumentPartitioner(configuration.getConfiguredDocumentPartitioning(viewer),partitioner);
		} else {
			document.setDocumentPartitioner(partitioner);
		}
		document.setDocumentPartitioner(partitioner);
		partitioner.connect(document);

		viewer.configure(configuration);
		viewer.setDocument(document/*, configuration.getAnnotationModel()*/);
		if( activeInput != null ) {
			activeInput.publish(input);
		}

		if (editingContext instanceof DelegatingEditingContext) {
			((DelegatingEditingContext) editingContext).setDelegate(new EditingContext() {

				@Override
				public void triggerAction(TextEditAction action) {
					// TODO Auto-generated method stub

				}

				@Override
				public void showContextInformation(ContextInformation contextInformation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setSelection(IRegion selection) {
					viewer.getTextWidget().setSelection(selection.getOffset(), selection.getOffset()+selection.getLength());
				}

				@Override
				public void setCaretOffset(int offset, boolean keepSelection) {
					viewer.getTextWidget().setCaretOffset(offset);
				}

				@Override
				public void setCaretOffset(int offset) {
					viewer.getTextWidget().setCaretOffset(offset);
				}

				@Override
				public Subscription registerOnSelectionChanged(Consumer<IRegion> arg0) {
					// TODO Auto-generated method stub
					return new Subscription() {

						@Override
						public void dispose() {
							// TODO Auto-generated method stub

						}
					};
				}

				@Override
				public Subscription registerOnCaretOffsetChanged(Consumer<Integer> arg0) {
					// TODO Auto-generated method stub
					return new Subscription() {

						@Override
						public void dispose() {
							// TODO Auto-generated method stub

						}
					};
				}

				@Override
				public IRegion getSelection() {
					Point selection = viewer.getTextWidget().getSelection();
					return new Region(selection.x, selection.y - selection.y);
				}

				@Override
				public int getCaretOffset() {
					return viewer.getTextWidget().getCaretOffset();
				}

				@Override
				public void revealCaret() {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

	protected SourceViewer createSourceViewer(Composite parent) {
		SourceViewer sourceViewer = new SourceViewer(parent,new CompositeRuler(),SWT.H_SCROLL|SWT.V_SCROLL);
		{
			LineNumberChangeRulerColumn column = new LineNumberChangeRulerColumn(SHARED_COLORS);
			column.setFont(font);
			sourceViewer.addVerticalRulerColumn(column);
		}

		IAnnotationModel annotationModel = sourceViewer.getAnnotationModel();
		if( annotationModel != null ) {
			AnnotationRulerColumn column = new AnnotationRulerColumn(annotationModel, 12);
			column.setFont(font);
			sourceViewer.addVerticalRulerColumn(column);
		}


		return sourceViewer;
	}

	@Persist
	public void save() {
		input.persist();
		documentSaved();
	}

	protected void documentSaved() {

	}

	@Focus
	void focused() {
		if( activeInput != null ) {
			activeInput.publish(input);
		}

		viewer.getTextWidget().setFocus();
//		//TODO We should remember the caret offset
//		if( viewer.getTextWidget().getCaretOffset() == -1 && viewer.getTextWidget().getContent().getCharCount() > 0 ) {
//			viewer.getTextWidget().setCaretOffset(0);
//		}
	}

	@PreDestroy
	void destroy() {
		if( activeInput != null && activeInput.getValue() == input ) {
			activeInput.publish(null);
		}
		this.input = null;
	}

	static class SharedColors implements ISharedTextColors {
		private Map fDisplayTable;

		@Override
		public Color getColor(RGB rgb) {
			if (rgb == null)
				return null;

			if (fDisplayTable == null)
				fDisplayTable= new HashMap(2);

			final Display display= Display.getCurrent();

			Map colorTable= (Map) fDisplayTable.get(display);
			if (colorTable == null) {
				colorTable= new HashMap(10);
				fDisplayTable.put(display, colorTable);
				display.disposeExec(new Runnable() {
					public void run() {
						dispose(display);
					}
				});
			}

			Color color= (Color) colorTable.get(rgb);
			if (color == null) {
				color= new Color(display, rgb);
				colorTable.put(rgb, color);
			}

			return color;
		}

		@Override
		public void dispose() {
			if (fDisplayTable == null)
				return;

			Iterator iter= fDisplayTable.values().iterator();
			while (iter.hasNext())
				dispose((Map)iter.next());
			fDisplayTable= null;
		}

		private void dispose(Display display) {
			if (fDisplayTable != null)
				dispose((Map)fDisplayTable.remove(display));
		}

		private void dispose(Map colorTable) {
			if (colorTable == null)
				return;

			Iterator iter= colorTable.values().iterator();
			while (iter.hasNext())
				((Color) iter.next()).dispose();

			colorTable.clear();
		}
	}
}
