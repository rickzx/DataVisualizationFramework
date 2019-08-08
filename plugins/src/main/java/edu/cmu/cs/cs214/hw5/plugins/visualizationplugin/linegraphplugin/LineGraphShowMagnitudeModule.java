package edu.cmu.cs.cs214.hw5.plugins.visualizationplugin.linegraphplugin;

import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPoint;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * The {@code LineGraphShowMagnitudeModule} class controls whether or not
 * to show the magnitude of the sentiment using a bubble chart
 *
 */
public class LineGraphShowMagnitudeModule implements ControlPoint<Boolean> {
    private JCheckBox checkBox;

    public LineGraphShowMagnitudeModule() {
        checkBox = new JCheckBox("Show Magnitude with Bubble Chart");
    }

    @Override
    public String getControlName() {
        return "showMagnitude";
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }

    @Override
    public Boolean getCurrentValue() {
        return checkBox.isSelected();
    }

    @Override
    public JPanel onDisplay() {
        JPanel panel = new JPanel();
        checkBox.setSelected(getDefaultValue());

        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        checkBoxPanel.add(checkBox);
        panel.add(checkBoxPanel);
        return panel;
    }
}
