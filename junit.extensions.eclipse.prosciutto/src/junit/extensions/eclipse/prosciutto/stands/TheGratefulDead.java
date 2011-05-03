package junit.extensions.eclipse.prosciutto.stands;

import junit.extensions.eclipse.prosciutto.internal.preferences.Preference;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class TheGratefulDead extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		boolean state = HandlerUtil.toggleCommandState(event.getCommand());
		Preference.ENABLED.setValue(!state); //TODO 逆でいいの？
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//		MessageDialog.openInformation(
//				window.getShell(),
//				"The Grateful Dead",
//				""+Preference.ENABLED + ":" + Preference.ENABLED.getValue());
		return null;
	}
}
