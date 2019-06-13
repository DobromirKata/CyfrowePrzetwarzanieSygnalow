package signal_processing.helpers;

import signal_processing.ISignal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statistics {
    private ISignal signal;
    private ISignal reconstructedSignal;

    public Statistics(ISignal signal){
        this.signal = signal;
    }

    public Statistics(ISignal signal, ISignal reconstructedSignal) {
        this.signal = signal;
        this.reconstructedSignal = reconstructedSignal;
    }

    public double getAverage(){
        double sum = 0d;
        for (Double value : signal.getValuesY()) {
            sum += value;
        }
        return sum * (1 / (signal.getEndTime() + 1));
    }

    public double getAbsoluteMean() {
        double sum = 0d;
        for (Double value : signal.getValuesY()) {
            sum += Math.abs(value);
        }
        return sum * (1 / signal.getEndTime() + 1);
    }

    public double getAveragePower() {
        double sum = 0d;
        for (Double value : signal.getValuesY()) {
            sum += Math.pow(value, 2);
        }
        return sum * (1 / (signal.getEndTime() + 1));
    }

    public double getEffectiveValue() {
        double sum = 0d;
        for (Double value : signal.getValuesY()) {
            sum += Math.pow(value - getAverage(), 2);
        }
        return sum * (1 / (signal.getEndTime() + 1));
    }

    public double getVariance() {
        double sum = 0d;
        for (Double value : signal.getValuesY()) {
            sum += Math.pow(value, 2);
        }
        return Math.sqrt(sum * (1 / signal.getEndTime() + 1));
    }

    // Mean square error
    public double MSE() {
        double meanSquareError = 0d;
        double reconstructedSignalSize = reconstructedSignal.getValuesX().size();

        for (int i = 0; i < reconstructedSignalSize; i++) {
            meanSquareError += Math.pow(signal.getValuesY().get(i) - reconstructedSignal.getValuesY().get(i), 2);
        }
        return meanSquareError / signal.getValuesX().size();
    }

    // Signal noise ratio
    public double SNR() {
        double signalNoiseRatio = 0d;
        double sum = 0d;
        double meanSquareError = MSE();
        double reconstructedSignalSize = reconstructedSignal.getValuesX().size();

        for (int i = 0; i < reconstructedSignalSize; i++) {
            sum += Math.pow(signal.getValuesY().get(i), 2);
        }
        signalNoiseRatio = 10 * Math.log10(sum / meanSquareError);

        return signalNoiseRatio;
    }

    // Peak signal to noise ratio
    public double PSNR() {
        double peakSignalToNoiseRatio = 0d;
        List<Double> valuesList = new ArrayList<>();
        double up = 0d;
        double meanSquareError = MSE();
        double reconstructedSignalSize = reconstructedSignal.getValuesX().size();

        for (int i = 0; i < reconstructedSignalSize; i++) {
            valuesList.add(signal.getValuesY().get(i));
        }
        up = Collections.max(valuesList);
        peakSignalToNoiseRatio = 10 * Math.log10(up / meanSquareError);

        return peakSignalToNoiseRatio;
    }

    // Maximum difference
    public double MD() {
        double maximumDifference = 0d;
        List<Double> valuesList = new ArrayList<>();
        double reconstructedSignalSize = reconstructedSignal.getValuesX().size();

        for (int i = 0; i < reconstructedSignalSize; i++){
            valuesList.add(Math.abs(signal.getValuesY().get(i) - reconstructedSignal.getValuesY().get(i)));
        }
        maximumDifference = Collections.max(valuesList);

        return maximumDifference;
    }

    // Effective number of bits
    public double ENOB() {
        double effectiveNumberOfBits = 0d;
        double signalNoiseRatio = SNR();

        effectiveNumberOfBits = (signalNoiseRatio - 1.76) / 6.02;

        return effectiveNumberOfBits;
    }
}
