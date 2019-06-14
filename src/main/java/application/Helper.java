package application;

import application.view.CorrelationWindow;
import application.view.ReconstructionWindow;
import application.view.SignalWindow;
import application.view.SimpleWindow;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import signal_processing.ISignal;
import signal_processing.helpers.Operations;
import signal_processing.helpers.Statistics;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

import java.util.ArrayList;

public class Helper {
    private static List<JFrame> windows = new ArrayList<>();

    public static void openWindow(ISignal signal, int bins) {
        JFreeChart signalChart = Helper.defaultChart(signal);
        HistogramDataset histogramDataset = getHistogramDataset(signal, bins);
        JFreeChart histogram = Helper.histogram(histogramDataset);
        Statistics stats = new Statistics(signal);
        SignalWindow signalWindow = new SignalWindow(signalChart, histogram, stats);
        JFrame frame = new JFrame(signal.getSignalName());

        frame.add(signalWindow.getMainPanel());
        windows.add(frame);

        frame.setSize(800, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void openSimpleWindow(String title, JFreeChart chart) {
        SimpleWindow simpleWindow = new SimpleWindow(chart);

        JFrame frame = new JFrame(title);
        frame.add(simpleWindow.getMainPanel());
        windows.add(frame);

        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void openReconstructionWindow(String title, JFreeChart chart, Statistics stats) {

        ReconstructionWindow reconstructionWindow = new ReconstructionWindow(chart, stats);

        JFrame frame = new JFrame(title);
        frame.add(reconstructionWindow.getMainPanel());
        windows.add(frame);

        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void openCorrelationWindow(String title, JFreeChart chart1, JFreeChart chart2, JFreeChart chart3) {
        CorrelationWindow correlationWindow = new CorrelationWindow(chart1, chart2, chart3);

        JFrame frame = new JFrame(title);
        frame.add(correlationWindow.getMainPanel());
        windows.add(frame);

        frame.setSize(800, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static JFreeChart defaultChart(ISignal signal) {
        final XYSeries series = new XYSeries("data");
        List<Double> x = signal.getValuesX();
        List<Double> y = signal.getValuesY();
        for (int i = 0; i < x.size(); i++) {
            series.add(x.get(i), y.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        return ChartFactory.createXYLineChart(
                signal.getSignalName(),
                "x",
                "y",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false);
    }

    public static JFreeChart histogram(final HistogramDataset dataset) {
        return ChartFactory.createHistogram(
                "Histogram",
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
    }

    public static HistogramDataset getHistogramDataset(ISignal signal, int bins) {
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        double[] values = new double[signal.getValuesY().size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = signal.getValuesY().get(i);
        }
        dataset.addSeries("H1", values, bins, Collections.min(signal.getValuesY()), Collections.max(signal.getValuesY()));

        return dataset;
    }

    public static String operationAsString(int operation) {
        String[] operations = new String[] { "+", "-", "*", "/" };
        return operations[operation];
    }


}
