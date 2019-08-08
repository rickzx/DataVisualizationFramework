import edu.cmu.cs.cs214.hw5.framework.core.Config;
import edu.cmu.cs.cs214.hw5.framework.core.DataVisualizationFrameworkImpl;

import edu.cmu.cs.cs214.hw5.framework.gui.FrameworkPanel;
import org.pushingpixels.substance.api.skin.SubstanceDustLookAndFeel;
import edu.cmu.cs.cs214.hw5.framework.plugin.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.VisualizationPlugin;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new SubstanceDustLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Sentiment Analysis Visualization");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            try {
                DataVisualizationFrameworkImpl core = new DataVisualizationFrameworkImpl(Config.CREDENTIAL_FILE);
                FrameworkPanel frameworkPanel = new FrameworkPanel(frame, core);
                core.setListener(frameworkPanel);

                frame.add(frameworkPanel);
                frame.pack();
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
                int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
                frame.setLocation(x, y);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
                frame.requestFocusInWindow();

                List<DataPlugin> dataPlugins = loadDataPlugins();
                dataPlugins.forEach(core::registerDataPlugin);

                List<VisualizationPlugin> plugins = loadVisualizationPlugins();
                plugins.forEach(core::registerVisualizationPlugin);

            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Load data plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<DataPlugin> loadDataPlugins() {
        ServiceLoader<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class);
        List<DataPlugin> result = new ArrayList<>();
        for (DataPlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getPluginName());
            result.add(plugin);
        }
        return result;
    }

    /**
     * Load visualization plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<VisualizationPlugin> loadVisualizationPlugins() {
        ServiceLoader<VisualizationPlugin> plugins = ServiceLoader.load(VisualizationPlugin.class);
        List<VisualizationPlugin> result = new ArrayList<>();
        for (VisualizationPlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getPluginName());
            result.add(plugin);
        }
        return result;
    }
}
