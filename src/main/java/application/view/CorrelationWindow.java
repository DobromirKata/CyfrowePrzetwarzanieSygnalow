package application.view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;

public class CorrelationWindow {
    private JPanel mainPanel;
    private JPanel signalA;
    private JPanel signalB;
    private JPanel signalC;

    public CorrelationWindow(JFreeChart signalAChart, JFreeChart signalBChart, JFreeChart signalCChart) {
        ChartPanel chartPanelA = new ChartPanel(signalAChart);
        ChartPanel chartPanelB = new ChartPanel(signalBChart);
        ChartPanel chartPanelC = new ChartPanel(signalCChart);

        signalA.add(chartPanelA);
        signalB.add(chartPanelB);
        signalC.add(chartPanelC);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
