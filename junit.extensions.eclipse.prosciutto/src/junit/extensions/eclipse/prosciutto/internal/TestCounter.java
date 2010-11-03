package junit.extensions.eclipse.prosciutto.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jdt.junit.model.ITestElement.Result;

public class TestCounter {
	private List<String> elements = new ArrayList<String>();
	private int totalTests;
	private int okTests;
	private int failureTests;
	private int ignoreTests;
	private int errorTests;

	public TestCounter(ITestElementContainer session) {
		count(session);
	}
	
	private void count(ITestElementContainer container) {
		ITestElement[] children = container.getChildren();
		if(children == null) return;
		for(ITestElement element : children){
			if (element instanceof ITestElementContainer) {
				ITestElementContainer cont = (ITestElementContainer) element;
				count(cont);
				continue;
			}
			totalTests++;
			Result result = element.getTestResult(false);
			if(result == null) continue;
			if(result.equals(Result.IGNORED)) ignoreTests++;
			if(result.equals(Result.OK)) okTests++;
			if(result.equals(Result.FAILURE)) failureTests++;
			if(result.equals(Result.ERROR)) errorTests++;
			elements.add(element.toString());
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
	
}
