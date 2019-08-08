package edu.cmu.cs.cs214.hw5.plugins.visualizationplugin.piechartplugin;


import edu.cmu.cs.cs214.hw5.framework.core.Account;
import edu.cmu.cs.cs214.hw5.framework.core.AnalyzedPost;
import edu.cmu.cs.cs214.hw5.framework.core.Config;
import edu.cmu.cs.cs214.hw5.framework.core.DataVisualizationFramework;
import edu.cmu.cs.cs214.hw5.framework.core.SentimentType;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import edu.cmu.cs.cs214.hw5.framework.plugin.VisualizationPlugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPanel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The {@code PieChartPlugin} class is a visualization plugin that shows
 * the proportion of different segment types using a pie chart.
 *
 * @author Team6
 */
public class PieChartPlugin implements VisualizationPlugin {
    private DataVisualizationFramework framework;
    private ControlPanel controlPanel;
    private PieChartFilterTypeModule typeFilter;

    private static final int WIDTH = 650;
    private static final int HEIGHT = 450;

    private static final String INITIAL_TITLE = "Sentiment of Posts by Type";
    private static final int MAX_TITLE_LENGTH = 30;

    @Override
    public String getPluginName() { return "Pie Graph Visualization"; }

    @Override
    public void onRegister(DataVisualizationFramework framework) {
        this.framework = framework;
        this.controlPanel = new PieChartControlPanel("Pie Graph Control Panel", this);
        typeFilter = new PieChartFilterTypeModule();
        this.controlPanel.addControlPoint(typeFilter);
    }

    @Override
    public JPanel onDisplay(List<Account> accounts) {
        JPanel visual = new JPanel();
        visual.setLayout(new BoxLayout(visual, BoxLayout.Y_AXIS));

        Container chartPanel;
        if (!typeFilter.getCurrentValue()) {
            chartPanel = getBlankChart();
        } else {
            List<SentimentType> selectedTypes = typeFilter.getSelectedSentimentTypes();
            chartPanel = getChartPanel(accounts, selectedTypes);
        }

        visual.add(chartPanel);
        visual.revalidate();
        return visual;
    }

    @Override
    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    @Override
    public DataVisualizationFramework getFramework() {
        return framework;
    }

    @Override
    public String toString() { return getPluginName(); }

    private Container getChartPanel(List<Account> accounts, List<SentimentType> selectedTypes) {
        int numCharts = accounts.size();

        if (numCharts == 0) {
            return getBlankChart();
        }

        List<PieChart> charts = new ArrayList<>();

        for (Account account : accounts) {
            String chartTitle = account.getAccountName();
            chartTitle = (chartTitle == null || chartTitle.length()
                    <= MAX_TITLE_LENGTH)? chartTitle : chartTitle.substring(0, MAX_TITLE_LENGTH);

            PieChart chart = new PieChartBuilder().width(WIDTH).height(HEIGHT)
                    .title(chartTitle).build();
            chart.getStyler().setChartBackgroundColor(Config.BACKGROUND_COLOR);

            Map<SentimentType, Integer> dataMap = parseData(account, selectedTypes);
            List<SentimentType> types = new ArrayList<>(dataMap.keySet());

            Color[] sliceColors = new Color[]{
                    new Color(47, 170, 202),
                    new Color(108, 136, 154),
                    new Color(126, 126, 139),
                    new Color(152, 111, 117),
                    new Color(178, 93, 93)};
            chart.getStyler().setSeriesColors(sliceColors);
            chart.getStyler().setLegendFont(chart.getStyler().getLegendFont().deriveFont(7.0f));

            for (SentimentType type : SentimentType.values()) {
                if (types.contains(type)) {
                    chart.addSeries(legendFormatHelper(type), dataMap.get(type));
                } else {
                    chart.addSeries(legendFormatHelper(type), 0);
                }
            }
            charts.add(chart);
        }

        JPanel chartMatrix = new SwingWrapper<>(charts).getChartMatrix();
        chartMatrix.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        return chartMatrix;
    }

    private Container getBlankChart() {
        PieChart chart = new PieChartBuilder().width(WIDTH).height(HEIGHT).title(INITIAL_TITLE).build();
        chart.getStyler().setChartBackgroundColor(Config.BACKGROUND_COLOR);
        return new XChartPanel<>(chart);
    }

    private Map<SentimentType, Integer> parseData(Account account, List<SentimentType> selectedTypes) {
        Map<SentimentType, List<Integer>> tempMap = new TreeMap<>();

        for(AnalyzedPost post: account.getPosts()) {
            SentimentType type = post.getSentiment().getSentimentType();
            if (selectedTypes.contains(type)) {
                tempMap.computeIfAbsent(type, k -> new ArrayList<>()).add(1);
            }
        }

        Map<SentimentType, Integer> dataMap = new TreeMap<>();
        tempMap.forEach((type, count) -> dataMap.put(type, count.stream().mapToInt(a -> a).sum()));
        return dataMap;
    }



    private String legendFormatHelper(SentimentType type) {
        StringBuilder newString = new StringBuilder();
        String[] tokens = type.toString().split("\\(");
        newString.append(tokens[0]);
        newString.append("\n(");
        newString.append(tokens[1]);
        return newString.toString();
    }
}
