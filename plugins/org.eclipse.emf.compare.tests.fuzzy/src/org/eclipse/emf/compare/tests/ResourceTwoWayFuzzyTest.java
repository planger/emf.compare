/*******************************************************************************
 * Copyright (c) EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.After;
import org.junit.AfterClass;

/**
 * A variation of the {@link TwoWayFuzzTest} that saves and loads models from resources.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ResourceTwoWayFuzzyTest extends TwoWayFuzzyTest {

	private static final Map<Object, Object> SAVE_OPTIONS = Collections.emptyMap();

	private static final Map<Object, Object> LOAD_OPTIONS = Collections.emptyMap();

	private static final String BASE_PATH = "fuzzy/tmp/"; //$NON-NLS-1$

	private static final String LEFT_XMI = "left.xmi"; //$NON-NLS-1$

	private static final String RIGHT_XMI = "right.xmi"; //$NON-NLS-1$

	private static final String LEFT_WORKING_XMI = "left_working.xmi"; //$NON-NLS-1$

	private static final String RIGHT_WORKING_XMI = "right_working.xmi"; //$NON-NLS-1$

	private final Resource.Factory resourceFactory = new XMIResourceFactoryImpl() {
		@Override
		public Resource createResource(URI uri) {
			return new XMIResourceImpl(uri) {
				@Override
				protected boolean useUUIDs() {
					return true;
				}
			};
		}
	};

	private Resource leftWorkingResource;

	private Resource rightWorkingResource;

	private Resource leftResource;

	private Resource rightResource;

	@Override
	public void prepareTwoVersions() {
		try {
			prepareResources();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private void prepareResources() throws IOException {
		createLeftResource();
		createRightResource();
		unloadLeftAndRightResources();
		copyAndLoadWorkingResources();
		reloadLeftAndRightResources();
	}

	private void createLeftResource() throws IOException {
		removeAllDuplicateCrossReferences(generatedRootObject);
		leftResource = createNewResourceFile(LEFT_XMI);
		leftResource.getContents().add(generatedRootObject);
		leftResource.save(SAVE_OPTIONS);
	}

	private void createRightResource() throws IOException {
		rightResource = createNewResourceFile(RIGHT_XMI);
		rightResource.getContents().add(generatedRootObject);
		mutateUtil.mutate();
		removeAllDuplicateCrossReferences(generatedRootObject);
		removeAllInvalidReferences(generatedRootObject);
		rightResource.save(SAVE_OPTIONS);
	}

	private Resource createNewResourceFile(String fileName) throws IOException {
		File file = new File(getFilePath(fileName));
		if (file.exists()) {
			file.delete();
		}
		return loadResource(file);
	}

	private Resource loadResource(File file) throws IOException {
		final URI uri = URI.createFileURI(file.getAbsolutePath());
		Resource resource = resourceFactory.createResource(uri);
		if (!file.exists()) {
			resource.save(SAVE_OPTIONS);
		}
		resource.load(LOAD_OPTIONS);
		return resource;
	}

	private void unloadLeftAndRightResources() {
		rightResource.unload();
		leftResource.unload();
	}

	private void copyAndLoadWorkingResources() throws IOException {
		copyFile(LEFT_XMI, LEFT_WORKING_XMI);
		copyFile(RIGHT_XMI, RIGHT_WORKING_XMI);
		leftWorkingResource = loadResource(new File(getFilePath(LEFT_WORKING_XMI)));
		rightWorkingResource = loadResource(new File(getFilePath(RIGHT_WORKING_XMI)));
	}

	private void copyFile(String sourcePath, String targetPath) throws IOException {
		final String fullSourcePath = getFilePath(sourcePath);
		final String fullTargetPath = getFilePath(targetPath);
		final File sourceFile = new File(fullSourcePath);
		final File targetFile = new File(fullTargetPath);
		copyFile(sourceFile, targetFile);
	}

	private void copyFile(File source, File dest) throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} finally {
			input.close();
			output.close();
		}
	}

	private String getFilePath(String fileName) {
		return BASE_PATH + fileName;
	}

	private void reloadLeftAndRightResources() throws IOException {
		rightResource.load(LOAD_OPTIONS);
		leftResource.load(LOAD_OPTIONS);
	}

	@Override
	protected Notifier getLeftOriginalNotifier() {
		return leftResource;
	}

	@Override
	protected Notifier getRightOriginalNotifier() {
		return rightResource;
	}

	@Override
	protected Notifier getLeftWorkingNotifier() {
		return leftWorkingResource;
	}

	@Override
	protected Notifier getRightWorkingNotifier() {
		return rightWorkingResource;
	}

	@Override
	protected void reportFailure(TwoWayMergeData data, EList<Diff> differences) {
		EObject left = ((Resource)data.left).getContents().get(0);
		EObject right = ((Resource)data.right).getContents().get(0);
		TwoWayMergeData objectData = new TwoWayMergeData(left, right, data.direction);
		super.reportFailure(objectData, differences);
	}

	@After
	public void removeTemporaryFiles() {
		unload(leftResource);
		unload(rightResource);
		unload(leftWorkingResource);
		unload(rightWorkingResource);
		deleteFile(LEFT_XMI);
		deleteFile(RIGHT_XMI);
		deleteFile(LEFT_WORKING_XMI);
		deleteFile(RIGHT_WORKING_XMI);
	}

	private void unload(Resource resource) {
		if (resource != null && resource.isLoaded()) {
			resource.unload();
		}
	}

	private void deleteFile(String path) {
		final File file = new File(getFilePath(path));
		if (file.exists()) {
			file.delete();
		}
	}

	@AfterClass
	public static void removeTemporaryFolder() {
		File baseFolder = new File(BASE_PATH);
		baseFolder.delete();
	}
}
