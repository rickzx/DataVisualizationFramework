package edu.cmu.cs.cs214.hw5.plugins.visualizationplugin.piechartplugin;

import edu.cmu.cs.cs214.hw5.framework.core.SentimentType;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPoint;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code PieChartFilterTypeModule} class controls what type of sentiment
 * to show in the pie chart.
 *
 * @author Team6
 */
public class PieChartFilterTypeModule implements ControlPoint<Boolean> {
    private int SENTIMENT_TYPE_NUM = 5;
    private JCheckBox[] checkBoxes = new JCheckBox[SENTIMENT_TYPE_NUM];

    public PieChartFilterTypeModule() {
       int i = 0;
       for (SentimentType type : SentimentType.values()) {
           checkBoxes[i] = new JCheckBox(type.toString());
           i++;
       }
    }

    @Override
    public String getControlName() {
        return "filterSentimentType";
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }

    @Override
    public Boolean getCurrentValue() {
        return Arrays.stream(checkBoxes).anyMatch(AbstractButton::isSelected);
    }

    public List<SentimentType> getSelectedSentimentTypes() {
        JCheckBox checkbox;
        List<SentimentType> selectedTypes = new ArrayList<>();
        for (int i = 0; i < SENTIMENT_TYPE_NUM; i++) {
            checkbox = checkBoxes[i];
            if (checkbox.isSelected()) {
                for (SentimentType type : SentimentType.values()) {
                    if (type.toString().equals(checkbox.getText())) {
                        selectedTypes.add(type);
                    }
                }
            }
        }
        return  selectedTypes;
    }

    @Override
    public JPanel onDisplay() {
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.setSelected(getDefaultValue());
            checkBoxPanel.add(checkBox);
        }

        panel.add(checkBoxPanel);
        return panel;
    }
}
