package junit.extensions.eclipse.prosciutto.internal.preferences;

import junit.extensions.eclipse.prosciutto.ProsciuttoActivator;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import static junit.extensions.eclipse.prosciutto.internal.preferences.Preference.*;

public class ProsciuttoGitPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public ProsciuttoGitPreferencePage() {
		super(FLAT);
		setPreferenceStore(ProsciuttoActivator.getDefault().getPreferenceStore());
		noDefaultAndApplyButton();
	}
	
	public void createFieldEditors() {
		Composite comp = getFieldEditorParent();
		comp.setLayout(new GridLayout());
		Group group = new Group(comp, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout layout = new GridLayout();
		group.setLayout(layout);
		group.setText("Author / Committer");
		
		addField(new StringFieldEditor(AUTHOR.name(), "&Author:", 50, group));
		addField(new StringFieldEditor(COMMITTER.name(), "&Committer:", 50, group));
	}
	
	public void init(IWorkbench workbench) {
		// TODO should add validation? 
		setValid(true);
	}
}