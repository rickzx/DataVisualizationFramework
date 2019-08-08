package edu.cmu.cs.cs214.hw5.framework.gui;

import edu.cmu.cs.cs214.hw5.framework.core.Account;
import edu.cmu.cs.cs214.hw5.framework.core.DataVisualizationFrameworkImpl;
import edu.cmu.cs.cs214.hw5.framework.core.FrameworkChangeListener;
import edu.cmu.cs.cs214.hw5.framework.plugin.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.VisualizationPlugin;


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code FrameworkPanel} class represents the top level JPanel of
 * the GUI of the framework. The GUI is divided in to three sections.
 * The left section is used for data plugin control panel, and for displaying
 * loaded accounts. The middle section is used for visualization control panel.
 * The right section is used for data visualization.
 *
 * @author Team6
 */
public class FrameworkPanel extends JPanel implements FrameworkChangeListener {
    private JFrame parentFrame;
    private DataVisualizationFrameworkImpl framework;

    private static final int FOOTER_HEIGHT = 30;
    private static final int PANEL_BORDER = 30;
    private static final int BOTTOM_BORDER = 80;
    private static final int STRUT = 10;
    private static final double GRIDBAG_WEIGHT_Y = 0.5;
    private static final int SCROLLBAR_UNIT_INCREMENT = 30;
    private static final int DIVIDER_LOCATION = 400;
    private static final int SPLIT_WIDTH = 300;

    private static final int TABLE_HEIGHT = 100;

    private static final int BIG_FONT_SIZE = 15;
    private static final int SMALL_FONT_SIZE = 12;

    private static final String GITHUB_URL = "https://github.com/CMU-17-214/Team6";

    private static final String FILE_MENU_TITLE = "File";
    private static final String MENU_SETTINGS = "Plugin Settings";
    private static final String MENU_EXIT = "Exit";

    private static final String HELP_MENU_TITLE = "Help";
    private static final String MENU_ABOUT = "About";

    private GridBagConstraints c = new GridBagConstraints();

    private JLabel footer;
    private JPanel dataPanel;
    private JPanel dataInputPanel;
    private JPanel visualizationPanel;
    private SettingsPanel settingsPanel;
    private DefaultTableModel tableModel;
    private Map<VisualizationPlugin, JPanel> panelMap;
    private Map<VisualizationPlugin, JPanel> controlPanelMap;


    /**
     * Initializes the {@code FrameworkPanel} object from parent JFrame and the framework
     * it belongs to.
     *
     * @param parentFrame   The parent JFrame of the framework panel
     * @param framework     The framework this panel belongs to
     */
    public FrameworkPanel(JFrame parentFrame, DataVisualizationFrameworkImpl framework) {
        this.parentFrame = parentFrame;
        this.framework = framework;

        panelMap = new HashMap<>();
        controlPanelMap = new HashMap<>();

        settingsPanel = new SettingsPanel(framework);
        setLayout(new BorderLayout());

        // Data Panel
        dataPanel = new JPanel(new BorderLayout());
        dataInputPanel = new JPanel(new CardLayout());
        dataPanel.setBorder(new EmptyBorder(PANEL_BORDER, PANEL_BORDER, PANEL_BORDER, PANEL_BORDER));
        dataPanel.add(dataInputPanel, BorderLayout.NORTH);
        dataPanel.add(getAccountPanel(), BorderLayout.SOUTH);

        // Visualization Panel
        visualizationPanel = new JPanel();
        visualizationPanel.setLayout(new GridBagLayout());
        visualizationPanel.setBorder(new EmptyBorder(PANEL_BORDER, PANEL_BORDER, PANEL_BORDER, PANEL_BORDER));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(visualizationPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLLBAR_UNIT_INCREMENT);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLLBAR_UNIT_INCREMENT);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dataPanel, scrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(DIVIDER_LOCATION);
        dataPanel.setMinimumSize(new Dimension(SPLIT_WIDTH, 0));
        scrollPane.setMinimumSize(new Dimension(SPLIT_WIDTH, 0));
        add(splitPane, BorderLayout.CENTER);


        // Status bar (footer)
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);

        statusPanel.setPreferredSize(new Dimension(getWidth(), FOOTER_HEIGHT));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        footer = new JLabel("Idle");
        footer.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(footer);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(FILE_MENU_TITLE);

        JMenuItem settingsMenuItem = new JMenuItem(MENU_SETTINGS);
        settingsMenuItem.setMnemonic(KeyEvent.VK_S);
        settingsMenuItem.addActionListener(e -> {
            JFrame frame = getNewTopFrame("Plugin Settings", settingsPanel);
            frame.setVisible(true);
            frame.requestFocusInWindow();
        });

        fileMenu.add(settingsMenuItem);

        fileMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem(MENU_EXIT);
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener(event -> System.exit(0));
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu(HELP_MENU_TITLE);

        JMenuItem helpMenuItem = new JMenuItem(HELP_MENU_TITLE);
        helpMenuItem.setMnemonic(KeyEvent.VK_F1);
        helpMenuItem.addActionListener(e -> openWebpage(GITHUB_URL));

        helpMenu.add(helpMenuItem);

        helpMenu.addSeparator();

        JMenuItem aboutMenuItem = new JMenuItem(MENU_ABOUT);
        aboutMenuItem.addActionListener(e -> {
            JFrame frame = getNewTopFrame("About", getAboutPanel());
            frame.setVisible(true);
            frame.requestFocusInWindow();
        });

        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        parentFrame.setJMenuBar(menuBar);
    }

    /**
     * Helper method to open a web page
     *
     * @param urlString     The url of the web page
     */
    private static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JFrame getNewTopFrame(String title, JPanel panel) {
        parentFrame.setEnabled(false);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.add(panel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentFrame.setEnabled(true);
            }
        });
        frame.pack();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setResizable(false);

        return frame;
    }

    private JPanel getAboutPanel() {
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.setBorder(new EmptyBorder(PANEL_BORDER, PANEL_BORDER, PANEL_BORDER, PANEL_BORDER));

        JLabel programLabel = new JLabel("Sentiment Analysis Visualization Framework");
        programLabel.setFont(new Font("Helvetica", Font.BOLD, BIG_FONT_SIZE));
        aboutPanel.add(programLabel);
        aboutPanel.add(Box.createVerticalStrut(STRUT));

        JLabel infoLabel = new JLabel("Carnegie Mellon University 17-214 F18 Course Project");
        infoLabel.setFont(new Font("Helvetica", Font.PLAIN, SMALL_FONT_SIZE));
        aboutPanel.add(infoLabel);

        aboutPanel.add(Box.createVerticalStrut(STRUT));
        aboutPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        aboutPanel.add(Box.createVerticalStrut(STRUT));

        JLabel authorLabel = new JLabel("Created by Xun Zhou, Siyue Jin");
        authorLabel.setFont(new Font("Helvetica", Font.PLAIN, SMALL_FONT_SIZE));
        aboutPanel.add(authorLabel);

        return aboutPanel;
    }

    private JPanel getAccountPanel() {
        JPanel accountPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JLabel title = new JLabel("Accounts Loaded");
        titlePanel.add(title);

        String[] columnName = {"Account"};
        tableModel = new DefaultTableModel(0, columnName.length);
        tableModel.setColumnIdentifiers(columnName);
        JTable accountTable = new JTable(tableModel);
        accountTable.setColumnSelectionAllowed(false);
        accountTable.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(accountTable);

        scrollPane.setPreferredSize(new Dimension(dataPanel.getPreferredSize().width, TABLE_HEIGHT));
        tablePanel.add(scrollPane);

        JButton removeButton = new JButton("-");
        removeButton.addActionListener(e -> {
            int currentRow = accountTable.getSelectedRow();
            if (currentRow >= 0 && currentRow < tableModel.getRowCount()) {
                framework.removeAccount((String) tableModel.getValueAt(currentRow, 0));
                tableModel.removeRow(currentRow);
                for(VisualizationPlugin plugin: framework.getCurrentVisualizationPlugins()) {
                    onDisplay(plugin, plugin.onDisplay(framework.getAccounts()));
                }
            }
        });

        buttonPanel.add(removeButton);
        tablePanel.add(buttonPanel);

        accountPanel.add(titlePanel, BorderLayout.NORTH);
        accountPanel.add(tablePanel, BorderLayout.SOUTH);

        accountPanel.setBorder(new EmptyBorder(0, 0, BOTTOM_BORDER, 0));

        return accountPanel;
    }

    @Override
    public void onDataPluginRegistered(DataPlugin plugin) {
        dataInputPanel.add(plugin.getControlPanel().onDisplay(), plugin.getPluginName());
        CardLayout cardLayout = (CardLayout) dataInputPanel.getLayout();
        cardLayout.show(dataInputPanel, plugin.getPluginName());
        footer.setText("Data plugins registered");
        settingsPanel.onDataPluginRegistered(plugin);
    }

    @Override
    public void onVisualizationPluginRegistered(VisualizationPlugin plugin) {
        Border border = BorderFactory.createLoweredBevelBorder();
        footer.setText("Visualization plugins registered");
        settingsPanel.onVisualizationPluginRegistered(plugin);

        JPanel pluginControlPanel = new JPanel();
        pluginControlPanel.add(plugin.getControlPanel().onDisplay());
        pluginControlPanel.setBorder(border);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = controlPanelMap.size();
        c.insets = new Insets(0, PANEL_BORDER, 0, PANEL_BORDER);
        c.weighty = GRIDBAG_WEIGHT_Y;
        c.anchor = GridBagConstraints.NORTH;
        visualizationPanel.add(pluginControlPanel, c);
        controlPanelMap.put(plugin, pluginControlPanel);

        JPanel pluginVisualizationPanel = new JPanel();
        pluginVisualizationPanel.add(plugin.onDisplay(new ArrayList<>()));
        c.gridx = 3;
        visualizationPanel.add(pluginVisualizationPanel, c);

        c.gridx = 2;
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.VERTICAL;
        visualizationPanel.add(new JSeparator(SwingConstants.VERTICAL), c);

        c.gridx = 0;
        visualizationPanel.add(new JSeparator(SwingConstants.VERTICAL), c);

        panelMap.put(plugin, pluginVisualizationPanel);
    }

    @Override
    public void onCurrentPluginsChanged(DataPlugin dataPlugin, List<VisualizationPlugin> visualizationPlugins) {
        dataInputPanel.add(dataPlugin.getControlPanel().onDisplay(), dataPlugin.getPluginName());
        CardLayout cardLayout = (CardLayout) dataInputPanel.getLayout();
        cardLayout.show(dataInputPanel, dataPlugin.getPluginName());

        visualizationPanel.removeAll();

        for (int i = 0; i < visualizationPlugins.size(); i++) {
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = i;
            c.insets = new Insets(0, PANEL_BORDER, 0, PANEL_BORDER);
            c.weighty = GRIDBAG_WEIGHT_Y;
            c.anchor = GridBagConstraints.NORTH;
            visualizationPanel.add(controlPanelMap.get(visualizationPlugins.get(i)), c);

            c.gridx = 3;
            visualizationPanel.add(panelMap.get(visualizationPlugins.get(i)), c);

            c.gridx = 2;
            c.insets = new Insets(0, 0, 0, 0);
            c.fill = GridBagConstraints.VERTICAL;
            visualizationPanel.add(new JSeparator(SwingConstants.VERTICAL), c);

            c.gridx = 0;
            visualizationPanel.add(new JSeparator(SwingConstants.VERTICAL), c);
        }

        visualizationPanel.revalidate();
        visualizationPanel.repaint();
    }

    @Override
    public void onAccountLoaded(String accountName, List<Account> accounts) {
        footer.setText(String.format("Account: %s loaded successfully", accountName));
        tableModel.addRow(new String[] {accountName});
        for(VisualizationPlugin plugin: framework.getCurrentVisualizationPlugins()) {
            onDisplay(plugin, plugin.onDisplay(accounts));
        }
    }


    @Override
    public void onDisplay(VisualizationPlugin plugin, JPanel visual) {
        JPanel toChange = panelMap.get(plugin);
        toChange.removeAll();
        toChange.add(visual);
        visualizationPanel.revalidate();
        visualizationPanel.repaint();
    }

    @Override
    public void onFooterTextChanged(String text) {
        footer.setText(text);
    }
}
