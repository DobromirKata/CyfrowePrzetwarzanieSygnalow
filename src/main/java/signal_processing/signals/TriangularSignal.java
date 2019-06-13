package signal_processing.signals;

import signal_processing.Signal;

// Sygnał trójkątny
public class TriangularSignal extends Signal {
    public TriangularSignal(int firstSample, int lastSample, double amplitude,
                            double startTime, double endTime, double frequency,
                            double basicPeriod, double fillingFactor) {
        super(firstSample, lastSample);
        setAmplitude(amplitude);
        setStartTime(startTime);
        setEndTime(endTime);
        setFrequency(frequency);
        setBasicPeriod(basicPeriod);
        setFillingFactor(fillingFactor);
        updateValues();
    }

    public TriangularSignal() {
        super(0, 200);
        setAmplitude(1);
        setStartTime(0);
        setEndTime(200);
        setFrequency(1);
        setBasicPeriod(200);
        setFillingFactor(0.5);
    }

    public double getValue(double x, double k) {
        if((x >= (k * getBasicPeriod() + getStartTime())) &&(x < (getFillingFactor() * getBasicPeriod() + k * getBasicPeriod() + getStartTime()))){
            return getAmplitude() / (getFillingFactor() * getBasicPeriod()) * (x - k * getBasicPeriod() - getStartTime());
        } else if((x >= (getFillingFactor() * getBasicPeriod() + k * getBasicPeriod() + getStartTime())) || (x < (getBasicPeriod() + k * getBasicPeriod() + getStartTime()))){
            return -getAmplitude() / (getBasicPeriod() * (1 - getFillingFactor())) * (x - k * getBasicPeriod() - getStartTime()) + getAmplitude() / (1 - getFillingFactor());
        }
        return 0;
    }

    public void updateValues() {
        x.clear();
        y.clear();
        int k = 0;
        int samples = (int) (getEndTime() * getFrequency());
        for (int i = getFirstSample(); i <= samples; i++) {
            double t = (i / getFrequency()) + getStartTime();
            if (t >= getBasicPeriod() * (k + 1) + getStartTime()) {
                k++;
            }
            x.add(t);
            y.add(getValue(t, k));
        }
    }

    public String getSignalName() {
        return "Sygnał trójkątny";
    }

    public String[] getAvailableParameters() {
        return new String[] {
                "firstSample",
                "lastSample",
                "amplitude",
                "startTime",
                "endTime",
                "frequency",
                "basicPeriod",
                "fillingFactor"
        } ;
    }
}
