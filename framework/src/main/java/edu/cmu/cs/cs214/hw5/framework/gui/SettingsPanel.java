package edu.cmu.cs.cs214.hw5.framework.gui;


import edu.cmu.cs.cs214.hw5.framework.core.DataVisualizationFrameworkImpl;
import edu.cmu.cs.cs214.hw5.framework.plugin.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.VisualizationPlugin;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@code SettingsPanel} class represents the settings panel inside the
 * framework GUI. It allows users choose current data and visualization plugins.
 *
 */
public class SettingsPanel extends JPanel {
    private static final int PANEL_BORDER = 25;

    private DataVisualizationFrameworkImpl framework;

    private JComboBox<String> dataPluginList;
    private JPanel visualizationPluginPanel;
    private List<JCheckBox> checkBoxes;

    private Map<String, DataPlugin> dataPlugins;
    private Map<String, VisualizationPlugin> visualizationPlugins;

    /**
     * Initializes the {@code SettingsPanel} given the framework it belongs to.
     *
     * @param framework     The framework it belongs to
     */
    SettingsPanel(DataVisualizationFrameworkImpl framework) {
        this.framework = framework;

        dataPlugins = new HashMap<>();
        visualizationPlugins = new HashMap<>();
        checkBoxes = new ArrayList<>();

        setLayout(new BorderLayout());
        JPanel dataPluginPanel = new JPanel(new BorderLayout());
        dataPluginPanel.setBorder(new EmptyBorder(PANEL_BORDER, PANEL_BORDER, PANEL_BORDER, PANEL_BORDER));
        visualizationPluginPanel = new JPanel();
        visualizationPluginPanel.setLayout(new BoxLayout(visualizationPluginPanel, BoxLayout.Y_AXIS));
        visualizationPluginPanel.setBorder(new EmptyBorder(PANEL_BORDER, PANEL_BORDER, PANEL_BORDER, PANEL_BORDER));

        JLabel dataHeader = new JLabel("Data Plugins");
        dataPluginPanel.add(dataHeader, BorderLayout.NORTH);
        JLabel visualizationHeader = new JLabel("Visualization Plugins");
        visualizationPluginPanel.add(visualizationHeader, BorderLayout.NORTH);

        dataPluginList = new JComboBox<>();
        dataPluginPanel.add(dataPluginList, BorderLayout.CENTER);

        add(dataPluginPanel, BorderLayout.WEST);
        add(visualizationPluginPanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            String pluginName = (String) dataPluginList.getSelectedItem();
            DataPlugin currentDataPlugin = dataPlugins.get(pluginName);
            List<VisualizationPlugin> currentVisualizationPlugins =
                    checkBoxes.stream().filter(AbstractButton::isSelected)
                                       .map(x -> visualizationPlugins.get(x.getText()))
                                       .collect(Collectors.toList());
            framework.setCurrentPlugins(currentDataPlugin, currentVisualizationPlugins);
            SwingUtilities.windowForComponent(this).setVisible(false);
            SwingUtilities.windowForComponent(this).dispose();
        });

        cancelButton.addActionListener(e -> {
            SwingUtilities.windowForComponent(this).setVisible(false);
            SwingUtilities.windowForComponent(this).dispose();
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    void onDataPluginRegistered(DataPlugin dataPlugin) {
        dataPluginList.addItem(dataPlugin.getPluginName());
        dataPluginList.setSelectedItem(dataPlugin.getPluginName());
        dataPlugins.put(dataPlugin.getPluginName(), dataPlugin);
    }

    void onVisualizationPluginRegistered(VisualizationPlugin visualizationPlugin) {
        JCheckBox checkBox = new JCheckBox(visualizationPlugin.getPluginName());
        checkBox.setSelected(true);
        checkBoxes.add(checkBox);
        visualizationPluginPanel.add(checkBox);
        visualizationPlugins.put(visualizationPlugin.getPluginName(), visualizationPlugin);
    }
}
