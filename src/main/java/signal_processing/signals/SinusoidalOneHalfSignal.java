package signal_processing.signals;

import signal_processing.Signal;

// Syngał sinusoidalny wyprostowany jednopołówkowo
public class SinusoidalOneHalfSignal extends Signal {

    public SinusoidalOneHalfSignal(int firstSample, int lastSample, double amplitude,
                                   double startTime, double endTime, double basicPeroid,
                                   double frequency) {
        super(firstSample, lastSample);
        setAmplitude(amplitude);
        setStartTime(startTime);
        setEndTime(endTime);
        setBasicPeriod(basicPeroid);
        setFrequency(frequency);
        updateValues();
    }

    public SinusoidalOneHalfSignal() {
        super(0, 800);
        setAmplitude(1);
        setStartTime(0);
        setEndTime(800);
        setBasicPeriod(200);
        setFrequency(1);
    }

    public double getValue(double x, double k) {
        return 0.5 * getAmplitude() * (Math.sin((2 * Math.PI / getBasicPeriod()) *
                (x - getStartTime())) + Math.abs(Math.sin((2 * Math.PI / getBasicPeriod()) *
                (x - getStartTime()))));
    }

    public void updateValues() {
        x.clear();
        y.clear();
        int samples = (int) (getFrequency() * getEndTime());
        for (int i = getFirstSample(); i <= samples; i++) {
            double t = (i / getFrequency()) + getStartTime();
            x.add(t);
            y.add(getValue(t, 0));
        }
    }

    public String getSignalName() {
        return "Sygnał sinusoidalny jednopołówkowy";
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
