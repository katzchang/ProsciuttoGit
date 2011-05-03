package junit.extensions.eclipse.prosciutto.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;

public class TestCounter {
	private final ITestRunSession session;
	private List<String> elements = new ArrayList<String>();
	private int totalTests;
	private int okTests;
	private int failureTests;
	private int ignoreTests;
	private int errorTests;

	public TestCounter(ITestRunSession session) {
		this.session = session;
		count(this, session);
	}
	
	private static void count(TestCounter counter, ITestElementContainer session) {
		ITestElement[] children = session.getChildren();
		if(children == null) return;
		
		for(ITestElement element : children){
			if (element instanceof ITestElementContainer) {
				ITestElementContainer container = (ITestElementContainer) element;
				count(counter, container);
				continue;
			}
			
			counter.totalTests++;
			
			Result result = element.getTestResult(false);
			if(result == null) continue;
			if(result.equals(Result.IGNORED)) counter.ignoreTests++;
			if(result.equals(Result.OK)) counter.okTests++;
			if(result.equals(Result.FAILURE)) counter.failureTests++;
			if(result.equals(Result.ERROR)) counter.errorTests++;
			counter.elements.add(element.toString());
		}
	}
	
	public List<String> getElements() {
		return elements;
	}
	
	public int getTotalTests(){
		return totalTests;
	}
	
	public int getOKTests(){
		return okTests;
	}

	public int getFailureTests() {
		return failureTests;
	}

	public int getIgnoreTests() {
		return ignoreTests;
	}

	public int getErrorTests() {
		return errorTests;
	}

	public String toString() {
		String elementTrace = "";
		
		for (String element : this.getElements())
			elementTrace += element + "\n";
		
		String message = session.getTestRunName() + ":" + session.getTestResult(true) + ", "
				+ "total:" + this.getTotalTests() + ", "
				+ "pass:" + this.getOKTests() + ", "
				+ "fail:" + this.getFailureTests() + ", "
				+ "error:" + this.getErrorTests() + ", "
				+ "ignore:" + this.getIgnoreTests();
		
		if (!elementTrace.isEmpty())
			message += "\n\n" + elementTrace;
		
		return message;
	}
	
}
