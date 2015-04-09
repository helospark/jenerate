package org.jenerate.internal.manage.impl;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jenerate.JeneratePlugin;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.preference.PluginPreference;
import org.jenerate.internal.manage.PreferencesManager;

/**
 * Default implementation of the {@link PreferencesManager}
 * 
 * @author maudrain
 */
public final class PreferencesManagerImpl implements PreferencesManager {

    /**
     * XXX looks like PrefPage method and PrefInit method.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCurrentPreferenceValue(PluginPreference<T> preference) {
        Class<T> type = preference.getType();
        String key = preference.getKey();
        IPreferenceStore preferenceStore = JeneratePlugin.getDefault().getPreferenceStore();
        if (Boolean.class.isAssignableFrom(type)) {
            return (T) Boolean.valueOf(preferenceStore.getBoolean(key));
        } else if (String.class.isAssignableFrom(type)) {
            return (T) preferenceStore.getString(key);
        } else if (MethodContentStrategyIdentifier.class.isAssignableFrom(type)) {
            return (T) MethodContentStrategyIdentifier.valueOf(preferenceStore.getString(key));
        } else {
            throw new UnsupportedOperationException("The preference type '" + type + "' for plugin preference '"
                    + preference + "' is not currently handled. ");
        }
    }
}
