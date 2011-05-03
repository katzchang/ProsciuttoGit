package junit.extensions.eclipse.prosciutto.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.extensions.eclipse.prosciutto.ProsciuttoActivator;
import junit.extensions.eclipse.prosciutto.internal.preferences.Preference;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.AdaptableFileTreeIterator;
import org.eclipse.egit.core.op.AddToIndexOperation;
import org.eclipse.egit.core.op.CommitOperation;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.IndexDiff;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;

public class GitJob extends Job {
	private final ITestRunSession session;
	
	public GitJob(String name, ITestRunSession session) {
		super(name);
		this.session = session;
	}

	@Override
	public IStatus run(IProgressMonitor monitor) {
		Boolean enabled = Preference.ENABLED.getValue().equals("true");
		if (!enabled) return Status.OK_STATUS;
		
		try {
			String author = Preference.AUTHOR.getValue();
			String committer = Preference.COMMITTER.getValue();
			
			String commitMessage = new TestCounter(session).toString();
			
			IResource project = session.getLaunchedProject().getResource();
			File gitDir = new File(project.getLocation().toFile().toString() + "/.git");
			System.out.println("gitDir:" + gitDir);
			Repository repository = new FileRepository(gitDir);
			
			File workTree = repository.getWorkTree();
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			AdaptableFileTreeIterator fileTreeIterator =
				new AdaptableFileTreeIterator(workTree, workspaceRoot);
			
			IndexDiff indexDiff = new IndexDiff(repository, Constants.HEAD, fileTreeIterator);
			
			System.out.println("1.indexDiff.diff():" + indexDiff.diff());
			System.out.println("2.indexDiff.diff():" + indexDiff.diff());
			
			boolean hasDiff = indexDiff.diff();
			System.out.println("3.hasDiff:" + indexDiff.diff());
			System.out.println("4.indexDiff.diff():" + indexDiff.diff());
			if (hasDiff) {
				ArrayList<IResource> resources = new ArrayList<IResource>();
				
				resources.addAll(files(project, indexDiff.getAdded()));
				resources.addAll(files(project, indexDiff.getChanged()));
				resources.addAll(files(project, indexDiff.getRemoved()));
				resources.addAll(files(project, indexDiff.getMissing()));
				resources.addAll(files(project, indexDiff.getModified()));
				resources.addAll(files(project, indexDiff.getUntracked()));

				new AddToIndexOperation(resources).execute(null);
				System.out.println("add:" + resources);
			}
			
			System.out.println("5.indexDiff.diff():" + indexDiff.diff());
			if (hasDiff) {
				CommitOperation commitOperation = new CommitOperation(null, null, null,
						author, committer,
						commitMessage);
				commitOperation.setCommitAll(true);
				commitOperation.setRepos(new Repository[]{repository});
				commitOperation.execute(null);
			}
		} catch (IOException e) {
			return errorStatus(e);
		} catch (CoreException e) {
			return errorStatus(e);
		}
		return Status.OK_STATUS;
	}

	private IStatus errorStatus(Exception e) {
		return new Status(Status.ERROR, ProsciuttoActivator.PLUGIN_ID,
				"check \n" +
				"* JUnit test is running under git project \n" +
				"* Preferences > ProsciuttoGit > Autor and Committer settings.", e);
	}

	private List<IResource> files(IResource project, Set<String> diffs) {
		System.out.println("diffs:" + diffs);
		ArrayList<IResource> files = new ArrayList<IResource>();
		
		String repoRelativePath = RepositoryMapping.getMapping(project)
				.getRepoRelativePath(project);
		if (repoRelativePath.isEmpty()) repoRelativePath += "/";
		
		for (String filename : diffs) {
			if (!filename.startsWith(repoRelativePath)) continue;
			IFile member = project.getProject().getFile(filename
					.substring(repoRelativePath.length()));
			if (!files.contains(member)) files.add(member);
		}
		return files;
	}
}
