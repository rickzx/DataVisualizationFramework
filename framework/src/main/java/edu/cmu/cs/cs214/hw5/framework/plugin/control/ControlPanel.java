package edu.cmu.cs.cs214.hw5.framework.plugin.control;

import edu.cmu.cs.cs214.hw5.framework.core.DataVisualizationFramework;
import edu.cmu.cs.cs214.hw5.framework.plugin.Plugin;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code ControlPanel} is an abstract class the represents the control panel
 * of a plugin. A control panel may consist of multiple control points. It provides
 * users controls to the behavior of the plugin.
 *
 * @author Team6
 */
public abstract class ControlPanel {
    private List<ControlPoint> controlPoints;
    private String controlPanelName;
    private Plugin plugin;

    /**
     * Initializes the {@code ControlPanel} object from a control panel name and the plugin
     * it belongs to.
     *
     * @param controlPanelName      The name of the control panel
     * @param plugin                The plugin it belongs to
     */
    public ControlPanel(String controlPanelName, Plugin plugin) {
        this.controlPanelName = controlPanelName;
        this.plugin = plugin;
        controlPoints = new ArrayList<>();
    }

    /**
     * Adds a control point to the control panel
     *
     * @param controlPoint  {@code ControlPoint} to be added
     */
    public void addControlPoint(ControlPoint controlPoint) {
        controlPoints.add(controlPoint);
    }

    /**
     * Gets a list of control points of the control panel
     *
     * @return      A list of control points of the control panel
     */
    public List<ControlPoint> getControlPoints() {
        return controlPoints;
    }

    /**
     * Gets a map of parameters representing the current status of the control panel.
     * The key of the parameter is obtained by calling {@code getControlName} on each ControlPoints.
     * The value of the parameter is obtained by calling {@code getCurrentValue} on each ControlPoints.
     *
     * @return      A map of parameters representing the current status of the control panel.
     */
    public Map<String, String> getParameters() {
        Map<String, String> parameters = new HashMap<>();
        for(ControlPoint controlPoint: getControlPoints()) {
            parameters.put(controlPoint.getControlName(), controlPoint.getCurrentValue().toString());
        }
        return parameters;
    }

    /**
     * Gets the plugin the control panel belongs to
     *
     * @return      the plugin the control panel belongs to
     */
    public Plugin getParentPlugin() {return plugin;}

    /**
     * Gets the framework the control panel lives in
     *
     * @return      the framework the control panel lives in
     */
    public DataVisualizationFramework getFramework() {
        return plugin.getFramework();
    }

    /**
     * Gets the name of the control panel
     *
     * @return      the name of the control panel
     */
    public String getControlPanelName() {
        return controlPanelName;
    }

    /**
     * Visualizes the GUI of the control panel
     *
     * @return      {@code JPanel} that represents the GUI of the control panel
     */
    public abstract JPanel onDisplay();

    @Override
    public String toString() {
        return controlPanelName;
    }
}
