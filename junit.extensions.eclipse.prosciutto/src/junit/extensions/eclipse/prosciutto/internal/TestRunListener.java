package junit.extensions.eclipse.prosciutto.internal;

import junit.extensions.eclipse.prosciutto.ProsciuttoActivator;
import junit.extensions.eclipse.prosciutto.internal.preferences.Preference;

import java.io.File;
import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.egit.core.AdaptableFileTreeIterator;
import org.eclipse.egit.core.op.AddToIndexOperation;
import org.eclipse.egit.core.op.CommitOperation;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.IndexDiff;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;

public class TestRunListener extends org.eclipse.jdt.junit.TestRunListener {

	private final class GitJob extends Job {
		private final ITestRunSession session;
		
		private GitJob(String name, ITestRunSession session) {
			super(name);
			this.session = session;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			Boolean enabled = Preference.ENABLED.getValue().equals("true");
			if (!enabled) return Status.OK_STATUS;
			
			try {
				String author = Preference.AUTHOR.getValue();
				String committer = Preference.COMMITTER.getValue();
				
				String commitMessage = formatTestResult(session);
				
				IResource project = session.getLaunchedProject().getResource();
				File gitDir = new File(project.getLocation().toFile().toString() + "/.git");
				Repository repository = new FileRepository(gitDir);
				
				AdaptableFileTreeIterator fileTreeIterator =
					new AdaptableFileTreeIterator(repository.getWorkTree(),
							ResourcesPlugin.getWorkspace().getRoot());
				IndexDiff indexDiff = new IndexDiff(repository, Constants.HEAD, fileTreeIterator);
				boolean hasDiff = indexDiff.diff();
				if (indexDiff.diff()) {
					ArrayList<IResource> resources = new ArrayList<IResource>();
					resources.addAll(includeList(project.getProject(), indexDiff.getAdded()));
					resources.addAll(includeList(project.getProject(), indexDiff.getChanged()));
					resources.addAll(includeList(project.getProject(), indexDiff.getRemoved()));
					resources.addAll(includeList(project.getProject(), indexDiff.getMissing()));
					resources.addAll(includeList(project.getProject(), indexDiff.getModified()));
					resources.addAll(includeList(project.getProject(), indexDiff.getUntracked()));

					new AddToIndexOperation(resources).execute(null);
				}
				
				if (hasDiff) {
					CommitOperation commitOperation = new CommitOperation(null, null, null,
							author, committer,
							commitMessage);
					commitOperation.setCommitAll(true);
					commitOperation.setRepos(new Repository[]{repository});
					commitOperation.execute(null);
				}
			} catch (Exception e) {
				return new Status(Status.ERROR, ProsciuttoActivator.PLUGIN_ID,
						"check \n" +
						" * running under git project" +
						" * Preferences > ProsciuttoGit > Autor and Committer settings.",e);
			}
			return Status.OK_STATUS;
		}

		String formatTestResult(ITestRunSession session) {
			TestCounter counter = new TestCounter(session);
			
			StringBuffer elementTrace = new StringBuffer();
			for (String element : counter.getElements())
				elementTrace.append(element + "\n");
			String message = session.getTestRunName() + ":"
					+ session.getTestResult(true) + ", " + "total:"
					+ counter.getTotalTests() + ", " + "pass:"
					+ counter.getOKTests() + ", " + "fail:"
					+ counter.getFailureTests() + ", " + "error:"
					+ counter.getErrorTests() + ", " + "ignore:"
					+ counter.getIgnoreTests() + "\n"
					+ "\n"
					+ elementTrace.toString();
			return message;
		}

		List<IResource> includeList(IProject project, Set<String> added) {
			ArrayList<IResource> files = new ArrayList<IResource>();
			
			String repoRelativePath = RepositoryMapping.getMapping(project)
					.getRepoRelativePath(project);
			if (repoRelativePath.isEmpty()) repoRelativePath += "/";
			
			for (String filename : added) {
				if (!filename.startsWith(repoRelativePath)) continue;
				IFile member = project.getFile(filename
						.substring(repoRelativePath.length()));
				if (!files.contains(member)) files.add(member);
			}
			return files;
		}
	}

	public TestRunListener() {
		JUnitCore.addTestRunListener(this);
	}

	@Override
	public void sessionFinished(final ITestRunSession session) {
		new GitJob("ProscuittoGit", session).schedule();
	}
}
