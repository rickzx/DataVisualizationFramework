package edu.cmu.cs.cs214.hw5.framework.plugin;

import edu.cmu.cs.cs214.hw5.framework.core.DataVisualizationFramework;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPanel;

/**
 * The {@code Plugin} interface generalizes the basic plugin behavior.
 *
 * @author Team6
 */
public interface Plugin {
    /**
     * Gets the name of the plugin.
     *
     * @return      The name of the plugin
     */
    String getPluginName();

    /**
     * Reacts on the registration of the plugin. The method gets called when
     * the plugin is registered to the framework.
     *
     * @param dataVisualizationFramework    The framework the plugin registered to
     */
    void onRegister(DataVisualizationFramework dataVisualizationFramework);

    /**
     * Gets the control panel of the plugin
     *
     * @return      {@code ControlPanel} of the plugin
     */
    ControlPanel getControlPanel();

    /**
     * Gets the framework the plugin belongs to
     *
     * @return      The framework the plugin belongs to
     */
    DataVisualizationFramework getFramework();
}
