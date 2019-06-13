package signal_processing.signals;

import signal_processing.Signal;

// Szygnał sinusoidalny
public class SinusoidalSignal extends Signal {

    public SinusoidalSignal(int firstSample, int lastSample, double amplitude,
                            double startTime, double endTime, double basicPeriod,
                            double frequency) {
        super(firstSample, lastSample);
        setAmplitude(amplitude);
        setStartTime(startTime);
        setEndTime(endTime);
        setBasicPeriod(basicPeriod);
        setFrequency(frequency);
        updateValues();
    }

    public SinusoidalSignal() {
        super(0, 200);
        setAmplitude(1);
        setStartTime(0);
        setEndTime(200);
        setBasicPeriod(200);
        setFrequency(1);
    }

    public double getValue(double x, double k) {
        return getAmplitude() * Math.sin((2 * Math.PI / getBasicPeriod()) *
                (x - getStartTime()));
    }

    public void updateValues() {
        x.clear();
        y.clear();
        int samples = (int) (getFrequency() * getEndTime());
        for (int i = getFirstSample();  i <= samples ; i++) {
            double t = (i / getFrequency()) + getStartTime();
            x.add(t);
            y.add(getValue(t, 0));
        }
    }

    public String getSignalName() {
        return "Sygnał sinusoidalny";
    }

    public String[] getAvailableParameters() {
        return new String[] {
                "firstSample",
                "lastSample",
                "amplitude",
                "startTime",
                "endTime",
                "basicPeriod",
                "frequency"
        };
    }
}
