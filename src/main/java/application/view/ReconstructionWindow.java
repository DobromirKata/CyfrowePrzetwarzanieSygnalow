package application.view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import signal_processing.ISignal;
import signal_processing.helpers.Statistics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ReconstructionWindow {
    private JPanel signalPanel;
    private JTable statsTable;

    private ChartPanel signalChartPanel = new ChartPanel(null);
    private DefaultTableModel tableModel;
    private DecimalFormat df;

    public ReconstructionWindow(JFreeChart signal, Statistics stats) {
        signalPanel.add(signalChartPanel);
        signalChartPanel.setChart(signal);
        tableModel = new DefaultTableModel();

        setDecimalFormat();

        tableModel.addColumn("Name");
        tableModel.addColumn("Value");

        tableModel.addRow(new Object[] { "Średnia wartość", df.format(stats.getAverage()) });
        tableModel.addRow(new Object[] { "Średnia wartość absolutna", df.format(stats.getAbsoluteMean()) });
        tableModel.addRow(new Object[] { "Średnia moc", df.format(stats.getAveragePower()) });
        tableModel.addRow(new Object[] { "Wariancja", df.format(stats.getVariance()) });
        tableModel.addRow(new Object[] { "Średnia kwadratowa", df.format(stats.getEffectiveValue()) });

        statsTable.setModel(tableModel);
    }

    private void setDecimalFormat() {
        df = new DecimalFormat("0.00000");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
    }
}
