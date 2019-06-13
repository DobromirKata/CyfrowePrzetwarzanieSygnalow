package application.view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;

public class SignalWindow {
    private JPanel mainPanel;
    private JPanel signalPanel;
    private JPanel histogramPanel;
    private ChartPanel signalChartPanel = new ChartPanel(null);
    private ChartPanel histogramChartPanel = new ChartPanel(null);

    public SignalWindow() {
        signalPanel.add(signalChartPanel);
        histogramPanel.add(histogramChartPanel);
    }

    public SignalWindow(JFreeChart signal, JFreeChart histogram) {
        this();
        signalChartPanel.setChart(signal);
        histogramChartPanel.setChart(histogram);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
