package edu.cmu.cs.cs214.hw5.plugins.dataplugin.amazonplugin;

import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPoint;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * The {@code AmazonASINModule} class controls the ASIN/ISBN input field of the
 * {@code AmazonControlPanel}.
 * <p>
 * Amazon Standard Identification Numbers (ASINs) are unique blocks of
 * 10 letters and/or numbers that identify items.
 * The International Standard Book Number (ISBN) is a unique commercial book identifier barcode.
 * Each ISBN code identifies uniquely a book.
 * <p>
 * ASIN/ISBN can be found in the Product Details section on the Amazon product page.
 *
 * @author Team6
 */
public class AmazonASINModule implements ControlPoint<String> {
    private String defaultValue;
    private final JTextField inputField = new JTextField(20);

    public AmazonASINModule(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getControlName() {
        return "ASIN/ISBN";
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel inputModule = new JPanel();
        JLabel label = new JLabel("Product ASIN/ISBN: ");
        inputField.setText(getDefaultValue());
        inputModule.setLayout(new BoxLayout(inputModule, BoxLayout.LINE_AXIS));
        inputModule.add(label);
        inputModule.add(inputField);

        JPanel hintPanel = new JPanel();
        hintPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel hint = new JLabel("ASIN/ISBN can be found in the Product Detail section.");
        hint.setFont(new Font("Arial", Font.ITALIC, 12));
        hintPanel.add(hint);
        panel.add(inputModule);
        panel.add(hintPanel);

        return panel;
    }
}
