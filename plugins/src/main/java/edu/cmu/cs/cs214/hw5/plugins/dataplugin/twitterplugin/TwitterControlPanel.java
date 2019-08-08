package edu.cmu.cs.cs214.hw5.plugins.dataplugin.twitterplugin;

import edu.cmu.cs.cs214.hw5.framework.plugin.Plugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPanel;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPoint;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ApplyButton;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * The {@code TwitterControlPanel} class represents the control panel of the
 * {@code TwitterPlugin}
 *
 * @author Team6
 */
public class TwitterControlPanel extends ControlPanel {
    public TwitterControlPanel(String controlPanelName, Plugin plugin) {
        super(controlPanelName, plugin);
    }
    private static final int STRUT = 10;
    private static final String MESSAGE = "Performing sentiment analysis on tweets. This might take several seconds.";

    @Override
    public JPanel onDisplay() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        JLabel header = new JLabel("Twitter");
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
