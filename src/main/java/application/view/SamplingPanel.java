package application.view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class SamplingPanel {
    private JPanel mainPanel;
    private JButton setAsSignal1Button;
    private JButton setAsSignal2Button;
    private JButton exportButton;
    private JButton previewButton;
    private JPanel signalPanel;
    private JLabel noSignal;
    private JComboBox quantizationSignal;
    private JSpinner samplingFrequency;
    private ChartPanel chartPanel;

    public SamplingPanel() {
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        comboBoxModel.addElement("Signal 1");
        comboBoxModel.addElement("Signal 2");
        quantizationSignal.setModel(comboBoxModel);
        chartPanel = new ChartPanel(null);
        samplingFrequency.setModel(new SpinnerNumberModel(0.1, 0.001, 1.0, 0.01));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void addSamplingFrequencyListener(ChangeListener listener) {
        samplingFrequency.addChangeListener(listener);
    }

    public void addSamplingSignalListener(ActionListener listener) {
        quantizationSignal.addActionListener(listener);
    }

    public void addSetAsSignal1ButtonListener(ActionListener listener) {
        setAsSignal1Button.addActionListener(listener);
    }

    public void addSetAsSignal2ButtonListener(ActionListener listener) {
        setAsSignal2Button.addActionListener(listener);
    }

    public void addExportButtonListener(ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public void addPreviewButtonListener(ActionListener listener) {
        previewButton.addActionListener(listener);
    }

    public void displaySignal(JFreeChart chart) {

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(1));
        renderer.setSeriesPaint(0, Color.gray);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesStroke(1, new BasicStroke(2));
        renderer.setSeriesPaint(1, new Color(28,104,122));

        plot.setRenderer(renderer);

        chartPanel.setChart(chart);
        chartPanel.validate();

        if (signalPanel.getComponentCount() != 2) {
            signalPanel.add(chartPanel);
            signalPanel.validate();
        }
    }

    public void hideNoSignal() {
        noSignal.setVisible(false);
    }

    public void updateButtons(int selectedSignal) {
        setAsSignal1Button.setEnabled(selectedSignal != 0);
        setAsSignal2Button.setEnabled(selectedSignal != 1);
    }
}
