package edu.cmu.cs.cs214.hw5.plugins.visualizationplugin.linegraphplugin;

import edu.cmu.cs.cs214.hw5.framework.core.Account;
import edu.cmu.cs.cs214.hw5.framework.core.AnalyzedPost;
import edu.cmu.cs.cs214.hw5.framework.core.Config;
import edu.cmu.cs.cs214.hw5.framework.core.DataVisualizationFramework;
import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.BubbleChartBuilder;
import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import edu.cmu.cs.cs214.hw5.framework.plugin.VisualizationPlugin;
import edu.cmu.cs.cs214.hw5.framework.plugin.control.ControlPanel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * The {@code LineGraphPlugin} class is a visualization plugin that visualizes
 * sentiment score vs time with a line graph. With the help of the control
 * panel, it could also visualize sentiment score and magnitude vs time with
 * a bubble chart.
 *
 * @author Team6
 */
public class LineGraphPlugin implements VisualizationPlugin {
    private DataVisualizationFramework framework;
    private ControlPanel controlPanel;

    private static final int WIDTH = 650;
    private static final int HEIGHT = 450;

    private static final String LINE_CHART_TITLE = "Sentiment Score vs. Time";
    private static final String BUBBLE_CHART_TITLE = "Sentiment Score vs. Sentiment Magnitude vs. Time";

    @Override
    public String getPluginName() {
        return "Line Graph Visualization";
    }

    @Override
    public void onRegister(DataVisualizationFramework framework) {
        this.framework = framework;
        this.controlPanel = new LineGraphControlPanel("Line Graph Control Panel", this);
        this.controlPanel.addControlPoint(new LineGraphShowMagnitudeModule());
        this.controlPanel.addControlPoint(new LineGraphTimeRangeModule());
    }

    @Override
    public JPanel onDisplay(List<Account> accounts) {
        JPanel visual = new JPanel();
        visual.setLayout(new BoxLayout(visual, BoxLayout.Y_AXIS));
        JPanel chartPanel;

        if(Boolean.valueOf(controlPanel.getParameters().get("showMagnitude"))){
            BubbleChart chart = getBubbleChart(accounts, controlPanel.getParameters());
            chartPanel = new XChartPanel<>(chart);
        } else {
            XYChart chart = getXYChart(accounts, controlPanel.getParameters());
            chartPanel = new XChartPanel<>(chart);
        }
        visual.add(chartPanel);
        visual.revalidate();
        visual.repaint();
        return visual;
    }

    @Override
    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    @Override
    public String toString() {
        return getPluginName();
    }

    private XYChart getXYChart(List<Account> accounts, Map<String, String> parameters) {
        XYChart chart = new XYChartBuilder().width(WIDTH)
                .height(HEIGHT).title(LINE_CHART_TITLE).theme(Styler.ChartTheme.Matlab)
                .xAxisTitle("Time").yAxisTitle("Sentiment Score").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setDatePattern("MM-dd-YY");
        chart.getStyler().setChartBackgroundColor(Config.BACKGROUND_COLOR);

        for(Account account: accounts) {
            Map<Date, Double> dataMap = parseData(account, parameters).get(0);
            List<Date> xData = new ArrayList<>(dataMap.keySet());
            List<Double> yData = new ArrayList<>(dataMap.values());

            String accountName = account.getAccountName();

            XYSeries series;

            series = chart.addSeries(accountName, xData, yData);

            series.setMarker(SeriesMarkers.DIAMOND);
        }
        return chart;
    }

    private BubbleChart getBubbleChart(List<Account> accounts, Map<String, String> parameters) {
        BubbleChartBuilder chartBuilder = new BubbleChartBuilder().width(WIDTH)
                .height(HEIGHT).title(BUBBLE_CHART_TITLE).theme(Styler.ChartTheme.Matlab)
                .xAxisTitle("Time").yAxisTitle("Sentiment Score");
        BubbleChart chart = new BubbleChart(chartBuilder);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setDatePattern("MM-dd-YY");
        chart.getStyler().setChartBackgroundColor(Config.BACKGROUND_COLOR);

        for(Account account: accounts) {
            List<Map<Date, Double>> dataMaps = parseData(account, parameters);
            List<Date> xData = new ArrayList<>(dataMaps.get(0).keySet());
            List<Double> yData = new ArrayList<>(dataMaps.get(0).values());
            List<Double> bubbleData = new ArrayList<>(dataMaps.get(1).values());

            String accountName = account.getAccountName();

            try {
                BubbleSeries series;
                series = chart.addSeries(accountName, xData, yData, bubbleData);
                series.setMarker(SeriesMarkers.DIAMOND);
            } catch (Exception e) {
                return chart;
            }
        }
        return chart;
    }

    private List<Map<Date, Double>> parseData(Account account, Map<String, String> parameters) {
        Map<Date, List<Double>> scoreTempMap = new TreeMap<>();
        Map<Date, List<Double>> magnitudeTempMap = new TreeMap<>();
        List<Map<Date, Double>> result = new ArrayList<>();

        String[] ranges = parameters.get("TimeRange").split(",");

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date start = dateFormat.parse(ranges[0]);
            Date end = dateFormat.parse(ranges[1]);

            for(AnalyzedPost post: account.getPosts()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(post.getTimeStamp());
                calendar.set(Calendar.HOUR_OF_DAY, 20);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date date = calendar.getTime();

                if (date.before(end) && date.after(start)) {
                    scoreTempMap.computeIfAbsent(date, k -> new ArrayList<>()).add(post.getSentiment().getScore());
                    magnitudeTempMap.computeIfAbsent(date, k -> new ArrayList<>()).add(post.getSentiment().getMagnitude());
                }
            }

            Map<Date, Double> scoreMap = new TreeMap<>();
            Map<Date, Double> magnitudeMap = new TreeMap<>();

            scoreTempMap.forEach((date, scores) -> {
                scoreMap.put(date, scores.stream().mapToDouble(a -> a).average().getAsDouble());
            });
            magnitudeTempMap.forEach((date, scores) -> {
                magnitudeMap.put(date, scores.stream().mapToDouble(a -> a).average().getAsDouble());
            });

            // Normalize magnitude to 10 - 60
            Optional<Double> maxValue = magnitudeMap.values().stream().max(Comparator.comparingDouble(x -> x));
            Optional<Double> minValue = magnitudeMap.values().stream().min(Comparator.comparingDouble(x -> x));

            maxValue.ifPresent(aDouble -> magnitudeMap.forEach((k, v) -> {
                double temp = (v - minValue.get()) / (aDouble - minValue.get());
                double norm = temp * 50 + 10;
                magnitudeMap.put(k, norm);
            }));

            result.add(scoreMap);
            result.add(magnitudeMap);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            return result;
        }
    }

    @Override
    public DataVisualizationFramework getFramework() {
        return framework;
    }
}
