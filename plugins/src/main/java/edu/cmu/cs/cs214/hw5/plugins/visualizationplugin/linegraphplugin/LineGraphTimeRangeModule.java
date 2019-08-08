package edu.cmu.cs.cs214.hw5.plugins.visualizationplugin.linegraphplugin;

import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPoint;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The {@code LineGraphTimeRangeModule} controls the range of timestamp
 * of the posts to display.
 *
 * @author Team6
 */
public class LineGraphTimeRangeModule implements ControlPoint<TimeRange> {
    private final JTextField startField = new JTextField(10);
    private final JTextField endField = new JTextField(10);

    @Override
    public String getControlName() {
        return "TimeRange";
    }

    @Override
    public TimeRange getDefaultValue() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date start = simpleDateFormat.parse("01/01/1970");
            Date end = new Date();
            return new TimeRange(start, end);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public TimeRange getCurrentValue() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date start = simpleDateFormat.parse(startField.getText());
            Date end = simpleDateFormat.parse(endField.getText());
            return new TimeRange(start, end);
        } catch (ParseException e) {
            return getDefaultValue();
        }
    }

    @Override
    public JPanel onDisplay() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel startPanel = new JPanel();
        JLabel startLabel = new JLabel("Start date (MM/dd/yyyy): ");
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.LINE_AXIS));
        startPanel.add(startLabel);
        startPanel.add(startField);

        JPanel endPanel = new JPanel();
        JLabel endLabel = new JLabel("End date (MM/dd/yyyy): ");
        endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.LINE_AXIS));
        endPanel.add(endLabel);
        endPanel.add(endField);

        panel.add(startPanel);
        panel.add(endPanel);

        return panel;
    }
}
