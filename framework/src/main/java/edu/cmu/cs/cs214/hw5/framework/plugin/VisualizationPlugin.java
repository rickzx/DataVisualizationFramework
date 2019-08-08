package edu.cmu.cs.cs214.hw5.framework.plugin;

import edu.cmu.cs.cs214.hw5.framework.core.Account;

import javax.swing.JPanel;
import java.util.List;

/**
 * The {@code VisualizationPlugin} interface outlines the core methods of
 * a visualization plugin. The {@code VisualizationPlugin} visualizes
 * the list of accounts provided by the framework.
 *
 * @author Team6
 */
public interface VisualizationPlugin extends Plugin {
    /**
     * Visualizes the list of accounts provided by the framework.
     *
     * @param accounts      the list of accounts provided by the framework
     * @return              {@code JPanel} that visualizes the source data
     */
    public JPanel onDisplay(List<Account> accounts);
}
