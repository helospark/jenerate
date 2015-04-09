// $Id$
package org.jenerate;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.preference.PluginPreference;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;

/**
 * Jenerate plugin preferences initializer. Takes the default values for all plugin preferences and register them.
 * 
 * @author jiayun
 * @author maudrain
 */
public class JeneratePreferenceInitializer extends AbstractPreferenceInitializer {

    private static final String JENERATE_PLUGIN_ID = "org.jenerate";

    @Override
    public void initializeDefaultPreferences() {
        initializeDefaultValues(DefaultScope.INSTANCE.getNode(JENERATE_PLUGIN_ID));
    }

    /**
     * Package private for testing purpose
     */
    void initializeDefaultValues(IEclipsePreferences iEclipsePreferences) {
        for (PluginPreference<?> jeneratePreference : JeneratePreferences.getAllPreferences()) {
            putPreference(iEclipsePreferences, jeneratePreference);
        }
    }

    /**
     * XXX looks like PrefPage method and PrefMngr method. Package private for testing purpose
     */
    void putPreference(IEclipsePreferences iEclipsePreferences, PluginPreference<?> pluginPreference) {
        Class<?> type = pluginPreference.getType();
        String key = pluginPreference.getKey();
        Object defaultValue = pluginPreference.getDefaultValue();
        if (Boolean.class.isAssignableFrom(type)) {
            iEclipsePreferences.putBoolean(key, ((Boolean) defaultValue).booleanValue());
        } else if (String.class.isAssignableFrom(type)) {
            iEclipsePreferences.put(key, ((String) defaultValue));
        } else if (MethodContentStrategyIdentifier.class.isAssignableFrom(type)) {
            iEclipsePreferences.put(key, ((MethodContentStrategyIdentifier) defaultValue).name());
        } else {
            throw new UnsupportedOperationException("The preference type '" + type + "' for plugin preference '"
                    + pluginPreference + "' is not currently handled. ");
        }
    }
}
