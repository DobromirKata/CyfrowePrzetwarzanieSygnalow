package application.view;

import signal_processing.Signal;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class SignalPanel extends JPanel {
    private JLabel signalName;
    public JPanel signalPanel;
    private JComboBox signalType;
    private JSpinner firstSample;
    private JSpinner lastSample;
    private JSpinner startTime;
    private JSpinner endTime;
    private JSpinner frequency;
    private JSpinner amplitude;
    private JSpinner basicPeriod;
    private JSpinner fillingFactor;
    private JSlider probability;
    private JLabel probabilityValue;
    private JSpinner jumpPoint;
    private JSpinner sampleJump;
    private JLabel infoAverage;
    private JLabel infoAbsoluteAverage;
    private JButton renderButton;
    private JLabel infoAveragePower;
    private JLabel infoVariance;
    private JLabel infoRootMeanSquare;
    private SpinnerNumberModel lastSampleModel;

    private DecimalFormat df;

    public SignalPanel() {
        initializeView();
    }

    private void initializeView() {
        firstSample.addChangeListener(e -> onFirstSampleChange());
        probability.addChangeListener(e -> onProbabilityChange());

//        Set models
        setInputModels();
//        Set decimal format
        setDecimalFormat();
    }

    private void setInputModels() {
//        Signal type
        signalType.setModel(new DefaultComboBoxModel(Signal.getSignals()));
//        First sample & last sample
        firstSample.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        lastSampleModel = new SpinnerNumberModel(200, 2, Integer.MAX_VALUE, 1);
        lastSample.setModel(lastSampleModel);
//        Start & duration time
        startTime.setModel(new SpinnerNumberModel(0.0, 0.0, 999999.0, 0.1));
        endTime.setModel(new SpinnerNumberModel(200.0, 0.1, 999999.0, 0.1));
//        Frequency & amplitude
        frequency.setModel(new SpinnerNumberModel(1.0, 0.1, 999999.0, 0.1));
        amplitude.setModel(new SpinnerNumberModel(1.0, 0.1, 999999.0, 0.1));
//        Period & duty cycle
        basicPeriod.setModel(new SpinnerNumberModel(0.0, -999999.0, 999999.0, 0.1));
        fillingFactor.setModel(new SpinnerNumberModel(0.0, 0, 1, 0.1));
//        Jump point & sample jump
        jumpPoint.setModel(new SpinnerNumberModel(0.0, -999999.0, 999999.0, 0.1));
        sampleJump.setModel(new SpinnerNumberModel(0.0, -999999.0, 999999.0, 0.1));
    }

    private void setDecimalFormat() {
        df = new DecimalFormat("0.00");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
    }

//    Actions
    private void onFirstSampleChange() {
        int lastSampleValue = (int) lastSample.getValue();
        int firstSampleValue = (int) firstSample.getValue();
        lastSampleModel.setMinimum(firstSampleValue + 1);
        if (lastSampleValue < firstSampleValue + 1) {
            lastSample.setValue(firstSampleValue + 1);
        }
    }

    private void onProbabilityChange() {
        double p = probability.getValue() / 100.0;
        probabilityValue.setText(df.format(p));
    }



    public JComboBox getSignalType() {
        return signalType;
    }

    public JSpinner getFirstSample() {
        return firstSample;
    }

    public JSpinner getLastSample() {
        return lastSample;
    }

    public JSpinner getStartTime() {
        return startTime;
    }

    public JSpinner getEndTime() {
        return endTime;
    }

    public JSpinner getFrequency() {
        return frequency;
    }

    public JSpinner getAmplitude() {
        return amplitude;
    }

    public JSpinner getBasicPeriod() {
        return basicPeriod;
    }

    public JSpinner getFillingFactor() {
        return fillingFactor;
    }

    public JSlider getProbability() {
        return probability;
    }

    public JLabel getProbabilityValue() {
        return probabilityValue;
    }

    public JSpinner getJumpPoint() {
        return jumpPoint;
    }

    public JSpinner getSampleJump() {
        return sampleJump;
    }

    public JButton getRenderButton() {
        return renderButton;
    }

    public JLabel getInfoAverage() {
        return infoAverage;
    }

    public JLabel getInfoAbsoluteAverage() {
        return infoAbsoluteAverage;
    }

    public JLabel getInfoAveragePower() {
        return infoAveragePower;
    }

    public JLabel getInfoVariance() {
        return infoVariance;
    }

    public JLabel getInfoRootMeanSquare() {
        return infoRootMeanSquare;
    }

    public void addRenderSignalButtonListener(ActionListener listener) {
        renderButton.addActionListener(listener);
    }
}
