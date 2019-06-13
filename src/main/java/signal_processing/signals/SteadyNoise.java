package signal_processing.signals;

import signal_processing.Signal;
import java.util.Random;

// Szum jednostkowy
public class SteadyNoise extends Signal {
    private Random rng = new Random();

    public SteadyNoise(int firstSample, int lastSample, double amplitude,
                       double startTime, double endTime, double frequency) {
        super(firstSample, lastSample);
        setAmplitude(amplitude);
        setStartTime(startTime);
        setEndTime(endTime);
        setFrequency(frequency);
        updateValues();
    }

    public SteadyNoise() {
        super(0, 200);
        setAmplitude(1);
        setStartTime(0);
        setEndTime(200);
        setFrequency(1);
    }

    public double getValue(double x, double k) {
        return rng.nextDouble() * -(2 * getAmplitude()) + getAmplitude();
    }

    public void updateValues() {
        x.clear();
        y.clear();
        int samples = (int) (getEndTime() * getFrequency());
        for (int i = getFirstSample(); i <= samples; i++) {
            x.add((i / getFrequency()) + getStartTime());
            y.add(getValue(0d, 0d));
        }
    }

    public String getSignalName() {
        return "Szum jednostajny";
    }

    public String[] getAvailableParameters() {
        return new String[] {
                "firstSample",
                "lastSample",
                "amplitude",
                "startTime",
                "endTime",
                "frequency"
        };
    }
}
