package edu.cmu.cs.cs214.hw5.plugins.dataplugin.twitterplugin;

import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPoint;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The {@code TwitterInputModule} class controls the Twitter account name input
 * field of the {@code TwitterControlPanel}
 *
 * @author Team6
 */
public class TwitterInputModule implements ControlPoint<String> {
    private String defaultValue;
    private final JTextField inputField = new JTextField(20);

    TwitterInputModule(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getControlName() {
        return "User";
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String getCurrentValue() {
        return inputField.getText().trim();
    }

    @Override
    public JPanel onDisplay() {
        JPanel inputModule = new JPanel();
        JLabel symbol = new JLabel("@");
        inputField.setText(getDefaultValue());
        inputModule.setLayout(new BoxLayout(inputModule, BoxLayout.LINE_AXIS));
        inputModule.add(symbol);
        inputModule.add(inputField);
        return inputModule;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
