package edu.cmu.cs.cs214.hw5.framework.core;


import edu.cmu.cs.cs214.hw5.framework.nlp.GoogleNLP;
import edu.cmu.cs.cs214.hw5.framework.plugin.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.VisualizationPlugin;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The {@code DataVisualizationFrameworkImpl} class implements the
 * {@code DataVisualizationFramework} interface, and interacts with the GUI.
 *
 * @author Team6
 */
public class DataVisualizationFrameworkImpl implements DataVisualizationFramework {
    private GoogleNLP googleNLP;
    private FrameworkChangeListener listener;

    private DataPlugin currentDataPlugin;
    private List<VisualizationPlugin> currentVisualizationPlugins = new ArrayList<>();

    private List<Account> accounts;

    /**
     * Initializes the {@code DataVisualizationFrameworkImpl} from the Google NLP credential file.
     *
     * @param credentialFile    The name of the Google NLP credential file
     * @throws IOException      if error occurs when reading the credential file
     */
    public DataVisualizationFrameworkImpl(String credentialFile) throws IOException, URISyntaxException {
        googleNLP = new GoogleNLP(credentialFile);
        accounts = new ArrayList<>();
    }

    /**
     * Sets a framework change listener for the framework.
     *
     * @param listener  {@code FrameworkChangeListener} to be set for the framework
     */
    public void setListener(FrameworkChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Registers a data plugin
     *
     * @param dataPlugin    {@code DataPlugin} to be registered
     */
    public void registerDataPlugin(DataPlugin dataPlugin) {
        dataPlugin.onRegister(this);
        notifyDataPluginRegistered(dataPlugin);
        currentDataPlugin = dataPlugin;
    }


    /**
     * Registers a visualization plugin
     *
     * @param visualizationPlugin   {@code VisualizationPlugin} to be registered
     */
    public void registerVisualizationPlugin(VisualizationPlugin visualizationPlugin) {
        visualizationPlugin.onRegister(this);
        notifyVisualizationPluginRegistered(visualizationPlugin);
        currentVisualizationPlugins.add(visualizationPlugin);
    }

    /**
     * Sets the current combination of data and visualization plugins
     * The framework has only one current data plugin, but could have multiple current visualization plugins.
     *
     * @param currentDataPlugin             Current data plugin
     * @param currentVisualizationPlugins   Current list of visualization plugins
     */
    public void setCurrentPlugins(DataPlugin currentDataPlugin, List<VisualizationPlugin> currentVisualizationPlugins) {
        this.currentDataPlugin = currentDataPlugin;
        this.currentVisualizationPlugins = currentVisualizationPlugins;
        notifyCurrentPluginsChanged(currentDataPlugin, currentVisualizationPlugins);
    }

    @Override
    public boolean loadAccount(Map<String, String> param) {
        if(currentDataPlugin.connect(param)) {
            List<Post> posts = currentDataPlugin.queryPosts(param);
//            if (posts.size() == 0) {
//                notifyMessage("Warning: No posts are fetched");
//                return false;
//            }
            if (accounts.stream().anyMatch((a) -> currentDataPlugin.getAccountName().equals(a.getAccountName()))) {
                SwingUtilities.invokeLater(() -> refreshAllVisualOnAccountReloaded(currentDataPlugin.getAccountName(), analyzePosts(posts)));
                return true;
            } else {
                accounts.add(new Account(currentDataPlugin.getAccountName(), analyzePosts(posts)));
                SwingUtilities.invokeLater(() -> notifyAccountLoaded(currentDataPlugin.getAccountName(), accounts));
                return true;
            }
        }
        notifyMessage("Error: Cannot load account");
        return false;
    }

    @Override
    public void removeAccount(String accountName) {
        accounts.removeIf(e -> e.getAccountName().equals(accountName));
    }

    @Override
    public void refreshAllVisual() {
        for(VisualizationPlugin visualizationPlugin: currentVisualizationPlugins) {
            notifyDisplayVisual(visualizationPlugin, visualizationPlugin.onDisplay(accounts));
        }
    }

    @Override
    public void refreshAllVisualOnAccountReloaded(String accountName, List<AnalyzedPost> posts) {
        for (Account account : accounts) {
            if (account.getAccountName().equals(accountName)) {
                accounts.remove(account);
                accounts.add(new Account(accountName, posts));
                refreshAllVisual();
                return;
            }
        }
        notifyMessage("Error: No such account!");
    }

    @Override
    public void changeVisual(VisualizationPlugin plugin) {
        notifyDisplayVisual(plugin, plugin.onDisplay(getAccounts()));
    }

    /**
     * Get current data plugin
     *
     * @return current data plugin
     */
    public DataPlugin getCurrentDataPlugin() {
        return currentDataPlugin;
    }

    /**
     * Get current list of visualization plugins
     *
     * @return current list of visualization plugins
     */
    public List<VisualizationPlugin> getCurrentVisualizationPlugins() {
        return currentVisualizationPlugins;
    }

    /**
     * Get current list of loaded accounts
     *
     * @return current list of loaded accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    private List<AnalyzedPost> analyzePosts(List<Post> posts) {
        List<AnalyzedPost> analyzedPosts = new ArrayList<>();
        for(Post post: posts) {
            Sentiment sentiment = googleNLP.analyzeSentiment(post.getContent());
            analyzedPosts.add(new AnalyzedPost(post, sentiment));
        }
        return analyzedPosts;
    }

    // Notify FrameworkChangeListener Methods
    private void notifyDataPluginRegistered(DataPlugin dataPlugin) {
        listener.onDataPluginRegistered(dataPlugin);
    }

    private void notifyVisualizationPluginRegistered(VisualizationPlugin visualizationPlugin) {
        listener.onVisualizationPluginRegistered(visualizationPlugin);
    }

    private void notifyCurrentPluginsChanged(DataPlugin dataPlugin, List<VisualizationPlugin> visualizationPlugins) {
        listener.onCurrentPluginsChanged(dataPlugin, visualizationPlugins);
    }

    private void notifyAccountLoaded(String accountName, List<Account> accounts) {
        listener.onAccountLoaded(accountName, accounts);
    }

    private void notifyDisplayVisual(VisualizationPlugin plugin, JPanel visual) {
        listener.onDisplay(plugin, visual);
    }

    private void notifyMessage(String message) {
        listener.onFooterTextChanged(message);
    }


}