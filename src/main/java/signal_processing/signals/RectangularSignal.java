package signal_processing.signals;

import signal_processing.Signal;

// Sygnał prostokątny
public class RectangularSignal extends Signal {
    public RectangularSignal(int firstSample, int lastSample, double amplitude,
                             double startTime, double endTime, double basicPeriod,
                             double fillingFactor, double frequency) {
        super(firstSample, lastSample);
        setAmplitude(amplitude);
        setStartTime(startTime);
        setEndTime(endTime);
        setBasicPeriod(basicPeriod);
        setFillingFactor(fillingFactor);
        setFrequency(frequency);
        updateValues();
    }

    public RectangularSignal() {
        super(0, 200);
        setAmplitude(1);
        setStartTime(0);
        setEndTime(200);
        setBasicPeriod(200);
        setFillingFactor(0.5);
        setFrequency(1);
    }

    public double getValue(double x, double k) {
            if (((x) >= (k * getBasicPeriod() + getStartTime()))
                    &&
                    ((x) < (getFillingFactor() * getBasicPeriod() + k *
                            getBasicPeriod() + getStartTime())) ){
               return getAmplitude();
            } else if (((x) >= (getFillingFactor() * getBasicPeriod() + k *
                    getBasicPeriod() + getStartTime()))
                    ||
                    (x) <(k * getBasicPeriod() + getStartTime())){
                return 0d;
            }
            return 0;
    }

    public void updateValues() {
        x.clear();
        y.clear();
        int samples = (int) (getFrequency() * getEndTime());
        int k = 0;

        for (int i = getFirstSample(); i <= samples; i++) {
            double t = (i / getFrequency()) + getStartTime();
            if (i >= getBasicPeriod() * (k+1) + getStartTime()) {
                k++;
            }
            x.add(t);
            y.add(getValue(t, k));
        }
    }

    public String getSignalName() {
        return "Sygnał prostokątny";
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
