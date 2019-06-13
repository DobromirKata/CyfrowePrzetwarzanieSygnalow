package signal_processing.signals;

import signal_processing.ISignal;
import signal_processing.Signal;
import java.util.Random;

// Szum impulsowy
public class ImpulseNoise extends Signal {
    private Random rng = new Random();
    private double probability;

    public ImpulseNoise(int firstSample, int lastSample, double probability) {
        super(firstSample, lastSample);
        this.probability = probability;
        updateValues();

    }

    public ImpulseNoise() {
        super(0, 200);
        probability = 0.05;
    }

    public double getValue(double x, double k) {
        if (rng.nextDouble() <= probability) {
            return 1d;
        } else {
            return 0d;
        }
    }

    public void updateValues() {
        x.clear();
        y.clear();
        for (int i = getFirstSample(); i <= getLastSample(); i++ ){
            x.add((double) i);
            y.add(getValue(i, 0));
        }
    }

    public String getSignalName() {
        return "Szum impulsowy";
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String[] getAvailableParameters() {
        return new String[] {
                "firstSample",
                "lastSample",
                "probability"
        };
    }

    @Override
    public ISignal copy() {
        ImpulseNoise signal = (ImpulseNoise) super.copy();
        signal.setProbability(probability);

        return signal;
    }
}
