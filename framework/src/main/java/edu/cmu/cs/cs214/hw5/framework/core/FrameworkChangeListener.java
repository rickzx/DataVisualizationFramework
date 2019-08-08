package edu.cmu.cs.cs214.hw5.framework.core;

import edu.cmu.cs.cs214.hw5.framework.plugin.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.VisualizationPlugin;

import javax.swing.JPanel;
import java.util.List;

/**
 * The {@code FrameworkChangeListener} interface outlines the core
 * method of a framework listener used by the GUI. A framework listener
 * observes changes in the framework and passes those changes to
 * the GUI.
 *
 * @author Team6
 */
public interface FrameworkChangeListener {
    /**
     * Performs GUI changes when a data plugin is registered
     *
     * @param plugin    The data plugin registered
     */
    void onDataPluginRegistered(DataPlugin plugin);

    /**
     * Performs GUI changes when a visualization plugin is registered
     *
     * @param plugin    The visualization plugin registered
     */
    void onVisualizationPluginRegistered(VisualizationPlugin plugin);

    /**
     * Performs GUI changes when the current combination of plugins is changed
     *
     * @param dataPlugin            Current data plugin
     * @param visualizationPlugins  Current list of visualization plugins
     */
    void onCurrentPluginsChanged(DataPlugin dataPlugin, List<VisualizationPlugin> visualizationPlugins);

    /**
     * Performs GUI changes when an account is loaded
     *
     * @param accountName   The name of the account registered
     * @param accounts      Current list of accounts
     */
    void onAccountLoaded(String accountName, List<Account> accounts);

    /**
     * Performs GUI changes when a visualization plugin is ready to display visual
     *
     * @param plugin    The visualization plugin to display visual
     * @param visual    The visual to be displayed as a JPanel object
     */
    void onDisplay(VisualizationPlugin plugin, JPanel visual);

    /**
     * Performs GUI changes when the footer text at the status bar is changed
     *
     * @param text  The footer text to display
     */
    void onFooterTextChanged(String text);
}
