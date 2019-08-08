package edu.cmu.cs.cs214.hw5.plugins.visualizationplugin.piechartplugin;

import edu.cmu.cs.cs214.hw5.framework.plugin.Plugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.VisualizationPlugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPanel;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPoint;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;

/**
 * The {@code PieChartControlPanel} class represents the control panel
 * of {@code PieChartPlugin}.
 *
 * @author Team6
 */
public class PieChartControlPanel extends ControlPanel {
    private static final int STRUT = 10;

   PieChartControlPanel(String controlPanelName, Plugin plugin) {
        super(controlPanelName, plugin);
    }

    @Override
    public JPanel onDisplay() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        JLabel header = new JLabel(getControlPanelName(), SwingConstants.CENTER);
        header.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        controlPanel.add(header);
        controlPanel.add(Box.createVerticalStrut(STRUT));
        for(ControlPoint controlPoint: getControlPoints()) {
            JPanel subPanel = controlPoint.onDisplay();
            controlPanel.add(subPanel);
            controlPanel.add(Box.createVerticalStrut(STRUT));
        }

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> getFramework().changeVisual((VisualizationPlugin) getParentPlugin()));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        buttonPanel.add(applyButton);
        controlPanel.add(buttonPanel);
        return controlPanel;
    }
}
