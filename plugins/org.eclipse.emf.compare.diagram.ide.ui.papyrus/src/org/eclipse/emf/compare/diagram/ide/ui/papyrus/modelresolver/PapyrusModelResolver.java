package org.eclipse.emf.compare.diagram.ide.ui.papyrus.modelresolver;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.logical.IModelResolver;
import org.eclipse.emf.compare.ide.ui.logical.IStorageProviderAccessor;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.papyrus.infra.core.resource.ModelMultiException;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.ModelsReader;
import org.eclipse.papyrus.infra.onefile.utils.OneFileUtils;

/**
 * PapyrusModelResolver.
 * 
 * @author Stefan Dirix
 */
public class PapyrusModelResolver implements IModelResolver {

	/**
	 * Constructor.
	 */
	public PapyrusModelResolver() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	public SynchronizationModel resolveLocalModels(IResource left, IResource right, IResource origin,
			IProgressMonitor monitor) throws InterruptedException {

		try {
			ModelSet leftModelSet = getModelSet(left);
			StorageTraversal leftT = getStorageTraversal(leftModelSet);

			ModelSet rightModelSet = getModelSet(right);
			StorageTraversal rightT = getStorageTraversal(rightModelSet);

			StorageTraversal originT = null;
			if (origin != null) {
				ModelSet originModelSet = getModelSet(origin);
				originT = getStorageTraversal(originModelSet);
			}
			return new SynchronizationModel(leftT, rightT, originT);

		} catch (ModelMultiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private ModelSet getModelSet(IResource resource) throws ModelMultiException {
		ModelSet modelSet = new ModelSet();
		ModelsReader reader = new ModelsReader();
		reader.readModel(modelSet);
		URI uri = URI.createPlatformResourceURI(((IFile)resource).getFullPath().toString(), true);
		modelSet.loadModels(uri);
		return modelSet;
	}

	private StorageTraversal getStorageTraversal(ModelSet set) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

		Set<IStorage> storage = new LinkedHashSet<IStorage>();

		for (Resource resource : set.getResources()) {
			String fileString = resource.getURI().path().replaceAll("%20", " ");
			if (fileString.startsWith("/resource")) {
				fileString = fileString.substring("/resource".length());
			}
			IFile member = (IFile)root.findMember(fileString);
			if (member != null) {
				storage.add(member);
			} else {
				System.out.println("Could not find: " + fileString);
			}

		}
		return new StorageTraversal(storage);
	}

	/**
	 * {@inheritDoc}
	 */
	public SynchronizationModel resolveModels(IStorageProviderAccessor storageAccessor, IStorage left,
			IStorage right, IStorage origin, IProgressMonitor monitor) throws InterruptedException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public StorageTraversal resolveLocalModel(IResource resource, IProgressMonitor monitor)
			throws InterruptedException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canResolve(IStorage sourceStorage) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IResource resource = root.findMember(sourceStorage.getFullPath());
		if (resource != null) {
			return OneFileUtils.isDi(resource) || OneFileUtils.containsModelFiles(resource.getParent());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void initialize() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
