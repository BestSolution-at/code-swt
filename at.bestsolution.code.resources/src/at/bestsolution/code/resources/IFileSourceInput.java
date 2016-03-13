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
package at.bestsolution.code.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.fx.code.editor.Constants;
import org.eclipse.fx.code.editor.LocalFile;
import org.eclipse.fx.code.editor.SourceFileChange;
import org.eclipse.fx.code.editor.SourceFileInput;
import org.eclipse.fx.core.event.EventBus;

@SuppressWarnings("restriction")
public class IFileSourceInput implements SourceFileInput, LocalFile {
	private String data;
	private final String url;
	private final EventBus eventBus;
	private final Map<String, Object> transientData = new HashMap<>();
	private final IFile file;
	private final Charset charSet;

	@Inject
	public IFileSourceInput(@Named(Constants.DOCUMENT_URL) String url, @Optional EventBus eventBus) {
		this.url = url;
		this.charSet = StandardCharsets.UTF_8;
		this.eventBus = eventBus;
		this.file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(url));
	}

	@PostConstruct
	void init() {
		if( eventBus != null ) {
			eventBus.publish(Constants.TOPIC_SOURCE_FILE_INPUT_CREATED, this, true);
		}
	}

	public IFile getFile() {
		return file;
	}

	@Override
	public void updateData(int offset, int length, String replacement) {
		StringBuilder b = new StringBuilder(data.length() - length + replacement.length());
		b.append(data.substring(0, offset));
		b.append(replacement);
		b.append(data.substring(offset+length,data.length()));
		data = b.toString();

		if( eventBus != null ) {
			SourceFileChange sourceChange = new SourceFileChange(this, offset, length, replacement);
			eventBus.publish(Constants.TOPIC_SOURCE_FILE_INPUT_MODIFIED, sourceChange, true);
		}
	}

	@PreDestroy
	@Override
	public final void dispose() {
		doDispose();
	}

	protected void doDispose() {
		if( eventBus != null ) {
			eventBus.publish(Constants.TOPIC_SOURCE_FILE_INPUT_DISPOSED, this, true);
		}
	}

	@Override
	public String getData() {
		if( data == null ) {
			try( InputStream in = file.getContents(); ByteArrayOutputStream out = new ByteArrayOutputStream(); ) {
				byte buf[] = new byte[1024];
				int l;
				while( (l = in.read(buf)) > -1 ) {
					out.write(buf,0,l);
				}

				data = new String(out.toByteArray(), charSet);
			} catch (IOException e) {
				throw new RuntimeException("Unable to read file content of '"+file+"'", e);
			} catch (CoreException e) {
				throw new RuntimeException("Unable to write content to file '"+file+"'", e);
			}
		}
		return data;
	}

	@Override
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public void persist() {
		try (InputStream in = new ByteArrayInputStream(data.getBytes(charSet))) {
			file.setContents(in, true, true, null);
			if( eventBus != null ) {
				eventBus.publish(Constants.TOPIC_SOURCE_FILE_INPUT_SAVED, this, true);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to write content to file '"+file+"'", e);
		} catch (CoreException e) {
			throw new RuntimeException("Unable to write content to file '"+file+"'", e);
		}
	}

	@Override
	public Map<String, Object> getTransientData() {
		return transientData;
	}

	@Override
	public String getURI() {
		return this.url;
	}

	@Override
	public java.nio.file.Path getPath() {
		return file.getRawLocation().toFile().toPath();
	}
}
