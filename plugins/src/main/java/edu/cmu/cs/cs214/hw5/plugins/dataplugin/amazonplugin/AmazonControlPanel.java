package edu.cmu.cs.cs214.hw5.plugins.dataplugin.amazonplugin;

import edu.cmu.cs.cs214.hw5.framework.plugin.Plugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ApplyButton;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPanel;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPoint;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * The {@code AmazonControlPanel} class represents the control panel of the
 * {@code AmazonPlugin}
 *
 * @author Team6
 */
public class AmazonControlPanel extends ControlPanel {

    private static final int STRUT = 10;
    private static final String MESSAGE = "Performing sentiment analysis on reviews. This might take several seconds.";

    AmazonControlPanel(String controlPanelName, Plugin plugin) {
        super(controlPanelName, plugin);
    }

    @Override
    public JPanel onDisplay() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        JLabel header = new JLabel("Amazon Product Review");
        header.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        controlPanel.add(header);
        controlPanel.add(Box.createVerticalStrut(STRUT));
        for(ControlPoint controlPoint: getControlPoints()) {
            JPanel subPanel = controlPoint.onDisplay();
            controlPanel.add(subPanel);
            controlPanel.add(Box.createVerticalStrut(STRUT));
        }

        ApplyButton applyButton = new ApplyButton(() -> getFramework().loadAccount(getParameters()), MESSAGE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        buttonPanel.add(applyButton);
        controlPanel.add(buttonPanel);

        return controlPanel;
    }
}
