package application.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;

public class CorrelationPanel {
    private JPanel mainPanel;
    private JButton startButton;
    private JButton stopButton;
    private JSlider speedSlider;
    private JLabel sliderValue;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public CorrelationPanel() {
//        speedSlider.addChangeListener(e -> updateSliderValue(e));
    }

    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public void addStopButtonListener(ActionListener listener) {
        //stopButton.addActionListener(listener);
    }

    public void addSpeedSliderListener(ChangeListener listener) {
//        speedSlider.addChangeListener(listener);
    }

    private void updateSliderValue(ChangeEvent event) {
        JSlider source = (JSlider) event.getSource();
        sliderValue.setText(Integer.toString(source.getValue()));
    }

    public void updateButtons(boolean isWorking) {
        startButton.setEnabled(!isWorking);
        stopButton.setEnabled(isWorking);
    }
}
