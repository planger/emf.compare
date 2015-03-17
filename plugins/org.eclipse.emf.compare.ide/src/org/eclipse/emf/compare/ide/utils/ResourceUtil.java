/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.utils;

import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.ide.EMFCompareIDEPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * This class will be used to provide various utilities aimed at IResource manipulation.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ResourceUtil {
	/** Content types of the files to consider as potential models. */
	private static final String[] MODEL_CONTENT_TYPES = new String[] {
			"org.eclipse.emf.compare.content.type", "org.eclipse.emf.ecore", //$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.emf.ecore.xmi", }; //$NON-NLS-1$

	/**
	 * This does not need to be instantiated.
	 */
	private ResourceUtil() {
		// hides default constructor
	}

	/**
	 * This will try and load the given file as an EMF model, and return the corresponding {@link Resource} if
	 * at all possible.
	 * 
	 * @param storage
	 *            The file we need to try and load as a model.
	 * @param resourceSet
	 *            The resource set in which to load this Resource.
	 * @param options
	 *            The options to pass to {@link Resource#load(java.util.Map)}.
	 * @return The loaded EMF Resource if {@code file} was a model, {@code null} otherwise.
	 */
	public static Resource loadResource(IStorage storage, ResourceSet resourceSet, Map<?, ?> options) {
		final URI uri = createURIFor(storage);

		InputStream stream = null;
		Resource resource = null;
		try {
			resource = resourceSet.createResource(uri);
			stream = storage.getContents();
			resource.load(stream, options);
		} catch (IOException e) {
			// return null
		} catch (CoreException e) {
			// return null
		} catch (WrappedException e) {
			// return null
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// Should have been caught by the outer try
				}
			}
		}

		return resource;
	}

	/**
	 * Checks whether the two given storages point to binary identical data.
	 * 
	 * @param left
	 *            First of the two storages which content we are testing.
	 * @param right
	 *            Second of the two storages which content we are testing.
	 * @return <code>true</code> if {@code left} and {@code right} are binary identical.
	 */
	public static boolean binaryIdentical(IStorage left, IStorage right) {
		Reader leftReader = null;
		Reader rightReader = null;
		try {
			leftReader = new BufferedReader(new InputStreamReader(left.getContents()));
			rightReader = new BufferedReader(new InputStreamReader(right.getContents()));

			final int bufferSize = 16384;
			final char[] leftBuff = new char[bufferSize];
			final char[] rightBuff = new char[bufferSize];
			int readLeft = leftReader.read(leftBuff);
			int readRight = rightReader.read(rightBuff);
			while (readLeft > 0 && readRight > 0 && equalArrays(readLeft, readRight, leftBuff, rightBuff)) {
				readLeft = leftReader.read(leftBuff);
				readRight = rightReader.read(rightBuff);
			}
			// One last check in case we've reached the end of one side but not of the other
			return equalArrays(readLeft, readRight, leftBuff, rightBuff);
		} catch (CoreException e) {
			logError(e);
		} catch (IOException e) {
			logError(e);
		} finally {
			if (leftReader != null) {
				Closeables.closeQuietly(leftReader);
			}
			if (rightReader != null) {
				Closeables.closeQuietly(rightReader);
			}
		}
		return false;
	}

	/**
	 * Checks whether the three given storages point to binary identical data. This could be done by calling
	 * {@link #binaryIdentical(IStorage, IStorage)} twice, though this implementation allows us to shortcut
	 * whenever one byte differs... and will read one less file from its input stream.
	 * 
	 * @param left
	 *            First of the three storages which content we are testing.
	 * @param right
	 *            Second of the three storages which content we are testing.
	 * @param origin
	 *            Third of the three storages which content we are testing.
	 * @return <code>true</code> if {@code left}, {@code right} and {@code origin} are binary identical.
	 */
	public static boolean binaryIdentical(IStorage left, IStorage right, IStorage origin) {
		Reader leftReader = null;
		Reader rightReader = null;
		Reader originReader = null;
		try {
			leftReader = new BufferedReader(new InputStreamReader(left.getContents()));
			rightReader = new BufferedReader(new InputStreamReader(right.getContents()));
			originReader = new BufferedReader(new InputStreamReader(origin.getContents()));

			final int bufferSize = 16384;
			final char[] leftBuff = new char[bufferSize];
			final char[] rightBuff = new char[bufferSize];
			final char[] originBuff = new char[bufferSize];
			int readLeft = leftReader.read(leftBuff);
			int readRight = rightReader.read(rightBuff);
			int readOrigin = originReader.read(originBuff);
			while (readLeft > 0 && readRight > 0 && readOrigin > 0
					&& equalArrays(readLeft, readRight, readOrigin, leftBuff, rightBuff, originBuff)) {
				readLeft = leftReader.read(leftBuff);
				readRight = rightReader.read(rightBuff);
				readOrigin = originReader.read(originBuff);
			}
			// One last check in case we've reached the end of one side but not of the other
			return equalArrays(readLeft, readRight, readOrigin, leftBuff, rightBuff, originBuff);
		} catch (CoreException e) {
			logError(e);
		} catch (IOException e) {
			logError(e);
		} finally {
			if (leftReader != null) {
				Closeables.closeQuietly(leftReader);
			}
			if (rightReader != null) {
				Closeables.closeQuietly(rightReader);
			}
			if (originReader != null) {
				Closeables.closeQuietly(originReader);
			}
		}
		return false;
	}

	/**
	 * Create the URI with which we'll load the given IFile as an EMF resource.
	 * 
	 * @param file
	 *            The file for which we need an EMF URI.
	 * @return The created URI.
	 * @since 3.1
	 */
	public static URI createURIFor(IFile file) {
		// whether it exists or not (no longer), use platform:/resource
		return URI.createPlatformResourceURI(file.getFullPath().toString(), true);
	}

	/**
	 * Create the URI with which we'll load the given IStorage as an EMF resource.
	 * 
	 * @param storage
	 *            The storage for which we need an EMF URI.
	 * @return The created URI.
	 */
	public static URI createURIFor(IStorage storage) {
		if (storage instanceof IFile) {
			return createURIFor((IFile)storage);
		}

		String path = getFixedPath(storage).toString();

		// Given the two paths
		// "g:/ws/project/test.ecore"
		// "/project/test.ecore"
		// We have no way to determine which is absolute and which should be platform:/resource
		URI uri;
		if (path.startsWith("platform:/plugin/")) { //$NON-NLS-1$
			uri = URI.createURI(path);
		} else if (path.startsWith("file:/")) { //$NON-NLS-1$
			uri = URI.createURI(path);
		} else {
			uri = URI.createFileURI(path);
		}

		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IPath iPath = new Path(path);
		if (root != null && iPath.segmentCount() >= 2 && root.getFile(iPath).exists()) {
			uri = URI.createPlatformResourceURI(path, true);
		}

		return uri;
	}

	/**
	 * Returns a path for this storage after fixing from an {@link IStoragePathProvider} if one exists.
	 * 
	 * @param storage
	 *            The storage for which we need a fixed full path.
	 * @return The full path to this storage, fixed if need be.
	 * @since 3.2
	 */
	public static IPath getFixedPath(IStorage storage) {
		final Object adapter = Platform.getAdapterManager().loadAdapter(storage,
				IStoragePathProvider.class.getName());
		if (adapter instanceof IStoragePathProvider) {
			return ((IStoragePathProvider)adapter).computeFixedPath(storage);
		}
		return storage.getFullPath();
	}

	/**
	 * This can be called to save all resources contained by the resource set. This will not try and save
	 * resources that do not support output.
	 * 
	 * @param resourceSet
	 *            The resource set to save.
	 * @param options
	 *            The options we are to pass on to {@link Resource#save(Map)}.
	 */
	public static void saveAllResources(ResourceSet resourceSet, Map<?, ?> options) {
		EList<Resource> resources = resourceSet.getResources();
		for (Resource resource : resources) {
			saveResource(resource, options);
		}
	}

	/**
	 * This can be called to save the given resource. This will not try and save a resource that do not
	 * support output.
	 * 
	 * @param resource
	 *            The resource to save.
	 * @param options
	 *            The options we are to pass on to {@link Resource#save(Map)}.
	 * @since 3.1
	 */
	public static void saveResource(Resource resource, Map<?, ?> options) {
		if (supportsOutput(resource)) {
			try {
				resource.save(options);
			} catch (IOException e) {
				logError(e);
			}
		}
	}

	/**
	 * This will return <code>true</code> if the given <em>contentTypeId</em> represents a content-type
	 * contained in the given array.
	 * 
	 * @param contentTypeId
	 *            Fully qualified identifier of the content type we seek.
	 * @param contentTypes
	 *            The array of content-types to compare against.
	 * @return <code>true</code> if the given array contains a content-type with this id.
	 * @since 3.1
	 */
	public static boolean hasContentType(String contentTypeId, List<IContentType> contentTypes) {
		IContentTypeManager ctManager = Platform.getContentTypeManager();
		IContentType expected = ctManager.getContentType(contentTypeId);
		if (expected == null) {
			return false;
		}

		boolean hasContentType = false;
		for (int i = 0; i < contentTypes.size() && !hasContentType; i++) {
			if (contentTypes.get(i).isKindOf(expected)) {
				hasContentType = true;
			}
		}
		return hasContentType;
	}

	/**
	 * Checks whether the given file has one of the content types described in {@link #MODEL_CONTENT_TYPES}.
	 * 
	 * @param file
	 *            The file which contents are to be checked.
	 * @return <code>true</code> if this file has one of the "model" content types.
	 * @since 3.1
	 */
	public static boolean hasModelType(IFile file) {
		boolean isModel = false;
		// Try a first pass without the file contents, since some content type parsers can be very sluggish
		// (EMF uses a sax parser to describe its content)
		final IContentTypeManager ctManager = Platform.getContentTypeManager();
		final List<IContentType> fileNameTypes = Lists.newArrayList(ctManager.findContentTypesFor(file
				.getName()));
		for (int i = 0; i < MODEL_CONTENT_TYPES.length && !isModel; i++) {
			isModel = hasContentType(MODEL_CONTENT_TYPES[i], fileNameTypes);
		}
		if (isModel) {
			return true;
		}

		// Fall back to the slower test
		final List<IContentType> contentTypes = Lists.newArrayList(getContentTypes(file));
		contentTypes.removeAll(fileNameTypes);
		for (int i = 0; i < MODEL_CONTENT_TYPES.length && !isModel; i++) {
			isModel = hasContentType(MODEL_CONTENT_TYPES[i], contentTypes);
		}
		return isModel;
	}

	/**
	 * Returns the whole list of content types of the given IFile, or an empty array if none.
	 * 
	 * @param file
	 *            The file we need the content types of.
	 * @return All content types associated with the given file, an empty array if none.
	 * @since 3.1
	 */
	public static IContentType[] getContentTypes(IFile file) {
		final IContentTypeManager ctManager = Platform.getContentTypeManager();

		InputStream resourceContent = null;
		IContentType[] contentTypes = new IContentType[0];
		try {
			resourceContent = file.getContents();
			contentTypes = ctManager.findContentTypesFor(resourceContent, file.getName());
		} catch (CoreException e) {
			ctManager.findContentTypesFor(file.getName());
		} catch (IOException e) {
			ctManager.findContentTypesFor(file.getName());
		} finally {
			Closeables.closeQuietly(resourceContent);
		}
		return contentTypes;
	}

	/**
	 * Disable saving for resources that cannot support it.
	 * 
	 * @param resource
	 *            The resource we are to check.
	 * @return <code>true</code> if we can save this <code>resource</code>, <code>false</code> otherwise.
	 */
	private static boolean supportsOutput(Resource resource) {
		final URI uri = resource.getURI();
		if (uri.isPlatformResource() || uri.isRelative() || uri.isFile()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the two arrays contain identical data in the {@code [0:length]} range. Note that we
	 * won't even check the arrays' contents if {@code length1} is not equal to {@code length2}.
	 * 
	 * @param length1
	 *            Length of the data range to check within {@code array1}.
	 * @param length2
	 *            Length of the data range to check within {@code array2}.
	 * @param array1
	 *            First of the two arrays which content we need to check.
	 * @param array2
	 *            Second of the two arrays which content we need to check.
	 * @return <code>true</code> if the two given arrays contain identical data in the {@code [0:length]}
	 *         range.
	 */
	private static boolean equalArrays(int length1, int length2, char[] array1, char[] array2) {
		if (length1 == length2) {
			boolean result = true;
			if (array1 == array2) {
				result = true;
			} else if (array1 == null || array2 == null) {
				result = false;
			} else {
				for (int i = 0; i < length1 && result; i++) {
					result = array1[i] == array2[i];
				}
			}
			return result;
		}
		return false;
	}

	/**
	 * Checks whether the three arrays contain identical data in the {@code [0:length]} range. Note that we
	 * will only check the arrays' contents if {@code length1} is equal to {@code length2} and {@code length3}
	 * .
	 * 
	 * @param length1
	 *            Length of the data range to check within {@code array1}.
	 * @param length2
	 *            Length of the data range to check within {@code array2}.
	 * @param length3
	 *            Length of the data range to check within {@code array3}.
	 * @param array1
	 *            First of the three arrays which content we need to check.
	 * @param array2
	 *            Second of the three arrays which content we need to check.
	 * @param array3
	 *            Third of the three arrays which content we need to check.
	 * @return <code>true</code> if the three given arrays contain identical data in the {@code [0:length]}
	 *         range.
	 */
	private static boolean equalArrays(int length1, int length2, int length3, char[] array1, char[] array2,
			char[] array3) {
		if (length1 == length2 && length1 == length3) {
			boolean result = true;
			if (array1 == array2 && array1 == array3) {
				result = true;
			} else if (array1 == null || array2 == null || array3 == null) {
				result = false;
			} else {
				for (int i = 0; i < length1 && result; i++) {
					result = array1[i] == array2[i] && array1[i] == array3[i];
				}
			}
			return result;
		}
		return false;
	}

	/**
	 * Logs the given exception as an error.
	 * 
	 * @param e
	 *            The exception we need to log.
	 */
	private static void logError(Exception e) {
		final IStatus status = new Status(IStatus.ERROR, EMFCompareIDEPlugin.PLUGIN_ID, e.getMessage(), e);
		EMFCompareIDEPlugin.getDefault().getLog().log(status);
	}
}
