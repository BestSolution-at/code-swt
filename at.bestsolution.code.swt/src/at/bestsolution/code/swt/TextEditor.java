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
package at.bestsolution.code.swt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.core.di.ContextBoundValue;
import org.eclipse.fx.core.di.ContextValue;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
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

	private static final SharedColors SHARED_COLORS = new SharedColors();

	private static Font font;

	public TextEditor() {
		if( font == null ) {
			try {
				FileLocator.toFileURL(getClass().getClassLoader().getResource("at/bestsolution/code/swt/hack/Hack-Regular.ttf"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			font = new Font(Display.getCurrent(), "Hack", Display.getCurrent().getSystemFont().getFontData()[0].getHeight(), SWT.NORMAL);
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
