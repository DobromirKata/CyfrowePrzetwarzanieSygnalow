package signal_processing;

import signal_processing.signals.GeneratedSignal;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Signal implements ISignal {
    protected List<Double> x = new ArrayList<>();
    protected List<Double> y = new ArrayList<>();

    private int firstSample;
    private int lastSample;
    private double amplitude;
    private double startTime;
    private double endTime;
    private double frequency;
    private double basicPeriod;
    private double fillingFactor;
    private boolean isRendered;

    public Signal(int firstSample, int lastSample) {
        this.firstSample = firstSample;
        this.lastSample = lastSample;
    }

    public int getFirstSample() {
        return firstSample;
    }

    public void setFirstSample(int firstSample) {
        this.firstSample = firstSample;
    }

    public int getLastSample() {
        return lastSample;
    }

    public void setLastSample(int lastSample) {
        this.lastSample = lastSample;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getBasicPeriod() {
        return basicPeriod;
    }

    public void setBasicPeriod(double basicPeriod) {
        this.basicPeriod = basicPeriod;
    }

    public double getFillingFactor() {
        return fillingFactor;
    }

    public void setFillingFactor(double fillingFactor) {
        this.fillingFactor = fillingFactor;
    }

    public static String[] getSignals() {
        return new String[] {
                "Szum jednostajny",
                "Szum gaussowski",
                "Szum impulsowy",
                "Sygnał sinusoidalny",
                "Sygnał sinusoidalny wyprostowany jednopołówkowo",
                "Sygnał sinusoidalny wyprostowany dwupołówkowo",
                "Sygnał prostokątny",
                "Sygnał prostokątny symetryczny",
                "Sygnał trójkątny",
                "Skok jednostkowy",
                "Impuls jednostkowy",
                "Sygnał wygenerowany"
        };
    }

    public static String[] getAllParameters() {
        return new String[] {
                "firstSample",
                "lastSample",
                "amplitude",
                "startTime",
                "endTime",
                "frequency",
                "basicPeriod",
                "fillingFactor",
                "probability",
                "sampleJump",
                "jumpPoint"
        };
    }

    public List<Double> getValuesX() {
        return x;
    }

    public List<Double> getValuesY() {
        return y;
    }

    public boolean isRendered() {
        return isRendered;
    }

    public void setRendered(boolean rendered) {
        isRendered = rendered;
    }

    public ISignal copy() {
        ISignal signal;
        try {
            signal = getClass().getConstructor().newInstance();
        } catch (Exception e) {
            signal = new GeneratedSignal();
        }

        signal.setFirstSample(firstSample);
        signal.setLastSample(lastSample);
        signal.setAmplitude(amplitude);
        signal.setStartTime(startTime);
        signal.setEndTime(endTime);
        signal.setFrequency(frequency);
        signal.setBasicPeriod(basicPeriod);
        signal.setFillingFactor(fillingFactor);
        signal.updateValues();

        return signal;
    }
}
