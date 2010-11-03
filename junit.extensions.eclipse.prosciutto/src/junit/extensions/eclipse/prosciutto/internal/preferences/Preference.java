package junit.extensions.eclipse.prosciutto.internal.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import junit.extensions.eclipse.prosciutto.ProsciuttoActivator;

/**
 * Constants for plug-in preferences
 */
public enum Preference {

	AUTHOR,
	COMMITTER
	;
	
	public String getValue() {
		IPreferenceStore store = ProsciuttoActivator.getDefault().getPreferenceStore();
		return store.getString(name());
	}
	
	public void setValue(String value){
		IPreferenceStore store = ProsciuttoActivator.getDefault().getPreferenceStore();
		store.setValue(name(), value);
	}
	
}
