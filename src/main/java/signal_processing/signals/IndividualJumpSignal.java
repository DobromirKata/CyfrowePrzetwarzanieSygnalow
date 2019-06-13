package signal_processing.signals;

import signal_processing.ISignal;
import signal_processing.Signal;

// Skok jednostkowy
public class IndividualJumpSignal extends Signal {
    private double jumpPoint;

    public IndividualJumpSignal(int firstSample, int lastSample, double amplitude,
                                double startTime, double endTime, double frequency,
                                double jumpPoint) {
        super(firstSample, lastSample);
        setAmplitude(amplitude);
        setStartTime(startTime);
        setEndTime(endTime);
        setFrequency(frequency);
        this.jumpPoint = jumpPoint;
        updateValues();
    }

    public IndividualJumpSignal() {
        super(0, 200);
        setAmplitude(1);
        setStartTime(0);
        setEndTime(200);
        setFrequency(1);
        jumpPoint = 100;
    }

    public double getValue(double x, double k) {
        if (x > jumpPoint) {
            return getAmplitude();
        } else if (x == jumpPoint) {
            return getAmplitude() / 2;
        } else {
            return 0d;
        }
    }

    public void updateValues() {
        x.clear();
        y.clear();
        int samples = (int) (getFrequency() * getEndTime());
        for (int i = getFirstSample(); i < samples; i++) {
            double t = (i / getFrequency()) + getStartTime();
            x.add(t);
            y.add(getValue(t, 0));
        }
    }

    public String getSignalName() {
        return "Skok jednostkowy";
    }

    public double getJumpPoint() {
        return jumpPoint;
    }

    public void setJumpPoint(double jumpPoint) {
        this.jumpPoint = jumpPoint;
    }

    public String[] getAvailableParameters() {
        return new String[] {
                "firstSample",
                "lastSample",
                "amplitude",
                "startTime",
                "endTime",
                "frequency",
                "jumpPoint"
        };
    }

    @Override
    public ISignal copy() {
        IndividualJumpSignal signal = (IndividualJumpSignal) super.copy();
        signal.setJumpPoint(jumpPoint);

        return signal;
    }
}
