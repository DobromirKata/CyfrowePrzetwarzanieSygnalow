package application.view;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import signal_processing.ISignal;
import signal_processing.helpers.Statistics;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ReconstructionWindow {
    private JPanel signalPanel;
    private JTable statsTable;
    private JPanel mainPanel;

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

        tableModel.addRow(new Object[] { "Błąd średniokwadratowy", df.format(stats.MSE()) });
        tableModel.addRow(new Object[] { "Stosunek sygnał - szum", df.format(stats.SNR()) });
        tableModel.addRow(new Object[] { "Szczytowy stosunek sygnał - szum", df.format(stats.PSNR()) });
        tableModel.addRow(new Object[] { "Maksymalna różnica", df.format(stats.MD()) });
        tableModel.addRow(new Object[] { "Efektywna liczba bitów", df.format(stats.ENOB()) });

        statsTable.setModel(tableModel);
    }

    private void setDecimalFormat() {
        df = new DecimalFormat("0.00000");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
