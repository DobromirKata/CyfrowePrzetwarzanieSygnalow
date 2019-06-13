package application.view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ReconstructionPanel {
    private JPanel mainPanel;
    private JComboBox reconstructionSignal;
    private JSpinner reconstructionFrequency;
    private JButton setAsSignal1Button;
    private JButton setAsSignal2Button;
    private JButton exportButton;
    private JButton previewButton;
    private JPanel signalPanel;
    private JLabel noSignal;
    private JRadioButton extrapolationRadioButton;
    private JRadioButton interpolationRadioButton;
    private JRadioButton sincRadioButton;
    private JTable reconstructionStats;
    private ChartPanel chartPanel;
    private DefaultTableModel tableModel;

    public ReconstructionPanel() {
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        comboBoxModel.addElement("Signal 1");
        comboBoxModel.addElement("Signal 2");
        reconstructionSignal.setModel(comboBoxModel);
        chartPanel = new ChartPanel(null);
        reconstructionFrequency.setModel(new SpinnerNumberModel(0.1, 0.001, 1.0, 0.01));
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Value");
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void addReconstructionFrequencyListener(ChangeListener listener) {
        reconstructionFrequency.addChangeListener(listener);
    }

    public void addReconstructionSignalListener(ActionListener listener) {
        reconstructionSignal.addActionListener(listener);
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

    public void addRadioButtonListener(ActionListener listener) {
        extrapolationRadioButton.addActionListener(listener);
        interpolationRadioButton.addActionListener(listener);
        sincRadioButton.addActionListener(listener);
    }

    public void updateButtons(int selectedSignal) {
        setAsSignal1Button.setEnabled(selectedSignal != 0);
        setAsSignal2Button.setEnabled(selectedSignal != 1);
    }

    public void displaySignal(JFreeChart chart) {

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(1));
        renderer.setSeriesPaint(0, Color.gray);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesStroke(1, new BasicStroke(2));
        renderer.setSeriesPaint(1, new Color(160,82,55));

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

    public void updateStats(Object[][] info) {
        tableModel.getDataVector().removeAllElements();
        for (Object[] row : info) {
            tableModel.addRow(row);
        }
        reconstructionStats.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }
}
