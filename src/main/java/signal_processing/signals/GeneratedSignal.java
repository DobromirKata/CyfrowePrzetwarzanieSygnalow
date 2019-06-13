package signal_processing.signals;

import signal_processing.Signal;

import java.util.List;

public class GeneratedSignal extends Signal {

    private String name = "Wygenerowany sygnał";

    public GeneratedSignal() {
        super(0, 0);
    }

    public double getValue(double x, double k) {
        return 0;
    }

    public void updateValues() {

    }

    public String getSignalName() {
        return name;
    }

    public String[] getAvailableParameters() {
        return new String[0];
    }

    public void setValuesX(List<Double> x) {
        this.x = x;
    }

    public void setValuesY(List<Double> y) {
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }
}
