package edu.cmu.cs.cs214.hw5.framework.core;

import edu.cmu.cs.cs214.hw5.framework.plugin.VisualizationPlugin;

import java.util.List;
import java.util.Map;

/**
 * The {@code DataVisualizationFramework} interface outlines the core methods
 * to be implemented for the data visualization framework. The framework
 * reads posts from the data plugin, perform sentiment analysis on the posts,
 * and passes an account to the visualization plugin for display.
 *
 * @author Team6
 */
public interface DataVisualizationFramework {

    /**
     * Loads an account given a map of parameters. The framework passes the parameters
     * to the data plugin, and calls the {@code connect} and {@code queryPost} methods of the plugin.
     * The framework then creates an account from the posts returned by the plugin, and
     * adds it to the account list.
     *
     * @param param     Map of parameters that is used for loading accounts. A simple param map
     *                  could be a mapping from the String "User" to username.
     * @return          {@code true} if the account is loaded successfully, otherwise {@code false}
     */
    boolean loadAccount(Map<String, String> param);

    /**
     * Removes an account given an account name. The framework searches the loaded account
     * and removes the account that matches the account name if presented.
     *
     * @param accountName   The name of the account to be removed
     */
    void removeAccount(String accountName);

    /**
     * Refreshes the visuals of all the visualization plugins from the current framework
     * status. The framework passes the current account information to every visualization plugins
     * and refreshes their display.
     *
     * A call to this method might be costly. Should call {@code changeVisual} instead if one
     * only wants to change the display of a single visualization plugin.
     */
    void refreshAllVisual();

    /**
     * Changes the visual of the given visualization plugin from the current framework
     * status. The framework passes the current account information to the given plugin
     * and refreshes its display.
     *
     * @param plugin    {@code VisualizationPlugin} whose visual is to be changed
     */
    void changeVisual(VisualizationPlugin plugin);

    /**
     * Refreshes the visuals of all the visualization plugins when an account is reloaded,
     * i.e. an account with an existing account name is loaded again.
     * The framework passes the specified account information to every visualization
     * plugins and refreshes their display.
     *
     * A call to this method might be costly.
     *
     * @param accountName   The name of the account to be reloaded
     * @param posts   The updated posts of the account to be reloaded
     */
    void refreshAllVisualOnAccountReloaded(String accountName, List<AnalyzedPost> posts);
}
