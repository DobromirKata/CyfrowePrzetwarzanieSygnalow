package application.view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;

public class SimpleWindow {
    private JPanel mainPanel;

    private ChartPanel chartPanel = new ChartPanel(null);

    public SimpleWindow(JFreeChart chart) {
        mainPanel.add(chartPanel);
        chartPanel.setChart(chart);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
