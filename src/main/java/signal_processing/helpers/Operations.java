package signal_processing.helpers;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import signal_processing.ISignal;
import signal_processing.Signal;
import signal_processing.signals.GeneratedSignal;
import com.google.common.collect.Ordering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Operations {

    public static ISignal getMultiplySignals(ISignal signal1, ISignal signal2) {
        GeneratedSignal calculatedSignal = new GeneratedSignal();
        List<Double> tempX = new ArrayList<>();
        List<Double> tempY = new ArrayList<>();
        List<Double> tempList = new ArrayList<>();

        for (int i = 0; i < signal1.getValuesX().size(); i++) {
            int count = 0;
            for (int j = 0; j < signal2.getValuesY().size(); j++) {
                if (signal1.getValuesX().get(i).equals(signal2.getValuesX().get(j))) {
                    tempX.add(signal1.getValuesX().get(i));
                    tempY.add(signal1.getValuesY().get(i) * signal2.getValuesY().get(j));
                    count++;
                    tempList.add((double) j);
                }
            }
            if (count == 0) {
                tempX.add(signal1.getValuesX().get(i));
                tempY.add(0d);
            }
        }

        for (int k = 0; k < signal2.getValuesX().size(); k++) {
            int count = 0;
            for (int l = 0; l < tempList.size(); l++) {
                if (k == tempList.get(l)) {
                    count++;
                }
            }
            if (count == 0) {
                tempX.add(signal2.getValuesX().get(k));
                tempY.add(0d);
            }
        }

        calculatedSignal.setValuesX(tempX);
        calculatedSignal.setValuesY(tempY);
        return calculatedSignal;
    }

    public static ISignal getAddSignals(ISignal signal1, ISignal signal2) {
        GeneratedSignal calculatedSignal = new GeneratedSignal();
        List<Double> tempX = new ArrayList<>();
        List<Double> tempY = new ArrayList<>();
        List<Double> tempList = new ArrayList<>();

        for (int i = 0; i < signal1.getValuesX().size(); i++) {
            int count = 0;
            for (int j = 0; j < signal2.getValuesX().size(); j++) {
                if (signal1.getValuesX().get(i).equals(signal2.getValuesX().get(j))) {
                    tempX.add(signal1.getValuesX().get(i));
                    tempY.add(signal1.getValuesY().get(i) + signal2.getValuesY().get(j));
                    count++;
                    tempList.add((double) j);
                }
            }
            if (count == 0) {
                tempX.add(signal1.getValuesX().get(i));
                tempY.add(signal1.getValuesY().get(i));
            }
        }

        for (int k = 0; k < signal2.getValuesX().size(); k++) {
            int count = 0;
            for (int l = 0; l < tempList.size(); l++) {
                if (k == tempList.get(l)) {
                    count++;
                }
            }
            if (count == 0) {
                tempX.add(signal2.getValuesX().get(k));
                tempY.add(signal2.getValuesY().get(k));
            }
        }

        calculatedSignal.setValuesX(tempX);
        calculatedSignal.setValuesY(tempY);
        return calculatedSignal;
    }

    public static ISignal getDivideSignals(ISignal signal1, ISignal signal2) {
        GeneratedSignal calculatedSignal = new GeneratedSignal();
        List<Double> tempX = new ArrayList<>();
        List<Double> tempY = new ArrayList<>();
        List<Double> tempList = new ArrayList<>();

        for (int i = 0; i < signal1.getValuesX().size(); i++) {
            int count = 0;
            for (int j = 0; j < signal2.getValuesX().size(); j++) {
                if (signal1.getValuesX().get(i).equals(signal2.getValuesX().get(j))) {
                    if (signal2.getValuesY().get(j) != 0) {
                        tempX.add(signal1.getValuesX().get(i));
                        tempY.add(signal1.getValuesY().get(i) / signal2.getValuesY().get(j));
                    }
                }
                count++;
                tempList.add((double) j);
            }
            if (count == 0) {
                tempX.add(signal1.getValuesX().get(i));
                tempY.add(0d);
            }
        }

        for (int k = 0; k <signal2.getValuesX().size(); k++) {
            int count = 0;
            for (int l = 0; l < tempList.size(); l++) {
                if (k == tempList.get(l)) {
                    count++;
                }
            }
            if (count == 0) {
                tempX.add(signal2.getValuesX().get(k));
                tempY.add(0d);
            }
        }

        calculatedSignal.setValuesX(tempX);
        calculatedSignal.setValuesY(tempY);
        return calculatedSignal;
    }

    public static ISignal getSubstractSignals(ISignal signal1, ISignal signal2) {
        GeneratedSignal calculatedSignal = new GeneratedSignal();
        List<Double> tempX = new ArrayList<>();
        List<Double> tempY = new ArrayList<>();
        List<Double> tempList = new ArrayList<>();

        for (int i = 0; i < signal1.getValuesX().size(); i++) {
            int count = 0;
            for (int j = 0; j < signal2.getValuesX().size(); j++) {
                if (signal1.getValuesX().get(i).equals(signal2.getValuesX().get(j))) {
                    tempX.add(signal1.getValuesX().get(i));
                    tempY.add(signal1.getValuesY().get(i) - signal2.getValuesY().get(j));
                    count++;
                    tempList.add((double) j);
                }
            }
            if (count == 0) {
                tempX.add(signal1.getValuesX().get(i));
                tempY.add(signal1.getValuesY().get(i));
            }
        }

        for (int k = 0; k < signal2.getValuesX().size(); k++) {
            int count = 0;
            for (int l = 0; l <tempList.size(); l++) {
                if (k == tempList.get(l)) {
                    count++;
                }
            }
            if (count == 0) {
                tempX.add(signal2.getValuesX().get(k));
                tempY.add( - signal2.getValuesY().get(k));
            }
            count = 0;
        }

        calculatedSignal.setValuesX(tempX);
        calculatedSignal.setValuesY(tempY);
        return calculatedSignal;
    }

    public static ISignal sampling(ISignal signal, double samples) {
        GeneratedSignal sampledSignal = new GeneratedSignal();

        int i = 0;
        int stepNominator = 0;
        Double start = signal.getStartTime();

        List<Double> tmpX = new ArrayList<>();
        List<Double> tmpY = new ArrayList<>();

        while(start < signal.getLastSample() && signal.getValuesX().size() > i) {
            double startTmp = (stepNominator / samples) + signal.getStartTime();
            start = startTmp;
            if (signal.getValuesX().get(i) >= start) {
                tmpX.add(start);
                tmpY.add(signal.getValuesY().get(i));
                stepNominator++;
            }
            i++;
        }

        sampledSignal.setValuesX(tmpX);
        sampledSignal.setValuesY(tmpY);
        return sampledSignal;
    }

//    public static ISignal quantizationBits(ISignal signal, int bits) {
//        ISignal quantizedSignal = signal.copy();
//
//        List<Double> levels = new ArrayList<>();
//        double max = Collections.max(signal.getValuesY());
//        double min = Collections.min(signal.getValuesY());
//        double q = (Math.abs(max-min)) / (Math.pow(2, bits) - 1);
//
//        for (int i = 0; i < Math.pow(2, bits); i++) {
//            levels.add(min + i * q);
//        }
//
//        List<Double> tmpX = new ArrayList<>();
//        List<Double> tmpY = new ArrayList<>();
//
//        for (int i = 0; i < signal.getValuesX().size(); i++) {
//            quantizedSignal.getValuesX().add(signal.getValuesX().get(i));
//
//            final int j = i;
//            Collections.sort(levels, Ordering.natural().onResultOf(p -> Math.abs(signal.getValuesY().get(j) - p)));
//            quantizedSignal.getValuesY().add(levels.get(0));
//        }
//
//        return  quantizedSignal;
//    }

    public static ISignal quantization(ISignal signal, int numberOfLevels) {
        GeneratedSignal quantizedSignal = new GeneratedSignal();
        double max = Collections.max(signal.getValuesY());
        double min = Collections.min(signal.getValuesY());
        double step = (Math.abs(max-min)) / (numberOfLevels - 1);
        List<Double> levels = new ArrayList<>();

        for (int i = 0; i < numberOfLevels; i++) {
            levels.add(min + i * step);
        }

        List<Double> tmpX = new ArrayList<>();
        List<Double> tmpY = new ArrayList<>();

        for (int i = 0; i < signal.getValuesX().size(); i++) {
            tmpX.add(signal.getValuesX().get(i));

            final int j = i;
            Collections.sort(levels, Ordering.natural().onResultOf(p -> Math.abs(signal.getValuesY().get(j) - p)));
            tmpY.add(levels.get(0));
        }

        quantizedSignal.setValuesX(tmpX);
        quantizedSignal.setValuesY(tmpY);
        return quantizedSignal;
    }

    public static ISignal reconstruction(ISignal signal, double reconstructionsFrequency) {
        double step = 1 / signal.getFrequency();
        ISignal sampledSignal = sampling(signal, reconstructionsFrequency);
//        ISignal sampledSignal = signal;
        GeneratedSignal reconstructedSignal = new GeneratedSignal();
        double sincSum;
        double sampledSignalSize = sampledSignal.getValuesX().size();
        double sampledSignalZeroX = sampledSignal.getValuesX().get(0);

        List<Double> tmpX = new ArrayList<>();
        List<Double> tmpY = new ArrayList<>();

        for (Double i = sampledSignalZeroX; i <= sampledSignal.getValuesX().get((int)sampledSignalSize - 1); i += step) {
            sincSum = 0d;

            for (int j = 0; j < sampledSignalSize; j++) {
                sincSum += sampledSignal.getValuesY().get(j) * sinc(i / (1 / reconstructionsFrequency) - 1);
            }
            tmpX.add(i);
            tmpY.add(sincSum);
        }

        reconstructedSignal.setValuesX(tmpX);
        reconstructedSignal.setValuesY(tmpY);
        return reconstructedSignal;
    }

    public static double sinc(double t) {
        if (Math.abs(t - 0d) < 0.0001) {
            return 1d;
        } else {
            return Math.sin(t * Math.PI) / (t * Math.PI);
        }
    }

    public static ISignal zeroExploration(ISignal signal, double reconstructionFrequency) {
        ISignal sampledSignal = sampling(signal, reconstructionFrequency);
//        ISignal sampledSignal = signal;
        GeneratedSignal reconstructedSignal = new GeneratedSignal();
        double step = 1 / signal.getFrequency();
        int nextIndex = 1;
        double lastValue = sampledSignal.getValuesY().get(0);
        double sampledSignalSize = sampledSignal.getValuesX().size();
        double sampledSignalZeroX = sampledSignal.getValuesX().get(0);

        List<Double> tmpX = new ArrayList<>();
        List<Double> tmpY = new ArrayList<>();

        for (Double i = sampledSignalZeroX; i <= sampledSignal.getValuesX().get((int)sampledSignalSize - 1); i += step) {
            if (nextIndex < sampledSignalSize && sampledSignal.getValuesX().get(nextIndex) <= i) {
                lastValue = sampledSignal.getValuesY().get(nextIndex);
                nextIndex++;
            }
            tmpX.add(i);
            tmpY.add(lastValue);
        }

        reconstructedSignal.setValuesX(tmpX);
        reconstructedSignal.setValuesY(tmpY);
        return reconstructedSignal;
    }

    public static JFreeChart getChart(ISignal signal, ISignal sampled) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        final XYSeries series1 = new XYSeries("source");
        List<Double> x1 = signal.getValuesX();
        List<Double> y1 = signal.getValuesY();
        for (int i = 0; i < x1.size(); i++) {
            series1.add(x1.get(i), y1.get(i));
        }
        dataset.addSeries(series1);

        final XYSeries series2 = new XYSeries("sampling");
        List<Double> x2 = sampled.getValuesX();
        List<Double> y2 = sampled.getValuesY();
        for (int i = 0; i < x2.size(); i++) {
            series2.add(x2.get(i), y2.get(i));
        }

        dataset.addSeries(series2);

        JFreeChart chart = ChartFactory.createXYLineChart(
                signal.getSignalName(),
                "x",
                "y",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false);
        return chart;
    }

    public static JFreeChart getChart(ISignal signal) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        final XYSeries series1 = new XYSeries("source");
        List<Double> x1 = signal.getValuesX();
        List<Double> y1 = signal.getValuesY();
        for (int i = 0; i < x1.size(); i++) {
            series1.add(x1.get(i), y1.get(i));
        }
        dataset.addSeries(series1);

        JFreeChart chart = ChartFactory.createXYLineChart(
                signal.getSignalName(),
                "x",
                "y",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false);
        return chart;
    }


}
