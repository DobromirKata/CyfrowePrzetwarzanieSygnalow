package signal_processing;

import java.util.List;

public interface ISignal {
    int getFirstSample();
    void setFirstSample(int firstSample);
    int getLastSample();
    void setLastSample(int lastSample);
    double getAmplitude();
    void setAmplitude(double amplitude);
    double getStartTime();
    void setStartTime(double startTime);
    double getEndTime();
    void setEndTime(double endTime);
    double getFrequency();
    void setFrequency(double frequency);
    double getBasicPeriod();
    void setBasicPeriod(double basicPeriod);
    double getFillingFactor();
    void setFillingFactor(double fillingFactor);
    double getValue(double x, double k);
    void updateValues();
    String getSignalName();
    String[] getAvailableParameters();
    List<Double> getValuesX();
    List<Double> getValuesY();
    boolean isRendered();
    void setRendered(boolean value);
    ISignal copy();
}
