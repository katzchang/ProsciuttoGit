package junit.extensions.eclipse.prosciutto.internal.preferences;

import junit.extensions.eclipse.prosciutto.ProsciuttoActivator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import static junit.extensions.eclipse.prosciutto.internal.preferences.Preference.*;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = ProsciuttoActivator.getDefault().getPreferenceStore();
		store.setDefault(AUTHOR.name(), "");
		store.setDefault(COMMITTER.name(), "");
		store.setDefault(ENABLED.name(), false);
	}

}
