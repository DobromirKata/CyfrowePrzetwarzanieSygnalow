package signal_processing.signals;

import signal_processing.Signal;

public class SymmetricalRectangularSignal extends Signal {
    public SymmetricalRectangularSignal(int firstSample, int lastSample, double amplitude,
                                        double startTime, double endTime, double basicPeroid,
                                        double fillingFactor, double frequency) {
        super(firstSample, lastSample);
        setAmplitude(amplitude);
        setStartTime(startTime);
        setEndTime(endTime);
        setBasicPeriod(basicPeroid);
        setFillingFactor(fillingFactor);
        setFrequency(frequency);
        updateValues();
    }

    public SymmetricalRectangularSignal() {
        super(0, 200);
        setAmplitude(1);
        setStartTime(0);
        setEndTime(200);
        setBasicPeriod(200);
        setFillingFactor(0.5);
        setFrequency(1);
    }

    public double getValue(double x, double k) {
        if (((x) >= getBasicPeriod()  + getStartTime()) &&
            ((x) < getFillingFactor() * getBasicPeriod() +  k * getBasicPeriod() + getStartTime()) ) {
            return getAmplitude();
        } else if ((x) >= (getFillingFactor() * getBasicPeriod() + k * getBasicPeriod() + getStartTime()) ||
                ((x) < (k * getBasicPeriod() + getStartTime()))) {
            return (-getAmplitude());
        }
        return 0d;
    }

    public void updateValues() {
        x.clear();
        y.clear();
        int k = 0;
        int samples = (int) (getFrequency() * getEndTime());
        for (int i  = getFirstSample(); i <= samples; i++){
            double t = (i / getFrequency()) + getStartTime();
            if (t >= getBasicPeriod() * (k + 1) + getStartTime()) {
                k++;
            }
            x.add(t);
            y.add(getValue(t, k));
        }
    }

    public String getSignalName() {
        return "Sygnał prostokątny symetryczny";
    }

    public String[] getAvailableParameters() {
        return new String[] {
                "firstSample",
                "lastSample",
                "amplitude",
                "startTime",
                "endTime",
                "basicPeriod",
                "fillingFactor",
                "frequency"
        };
    }
}
