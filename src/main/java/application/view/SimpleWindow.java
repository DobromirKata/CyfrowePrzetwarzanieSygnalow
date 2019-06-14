package application.view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;

public class SimpleWindow {
    private JPanel mainPanel;
    private JPanel chartContainer;

    private ChartPanel chartPanel = new ChartPanel(null);

    public SimpleWindow(JFreeChart chart) {
        chartContainer.add(chartPanel);
        chartPanel.setChart(chart);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
