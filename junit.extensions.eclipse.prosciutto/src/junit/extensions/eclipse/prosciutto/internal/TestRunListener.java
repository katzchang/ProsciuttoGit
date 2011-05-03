package junit.extensions.eclipse.prosciutto.internal;

import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.model.ITestRunSession;

public class TestRunListener extends org.eclipse.jdt.junit.TestRunListener {
	public TestRunListener() {
		JUnitCore.addTestRunListener(this);
	}

	@Override
	public void sessionFinished(final ITestRunSession session) {
		new GitJob("ProscuittoGit", session).schedule();
	}
}
