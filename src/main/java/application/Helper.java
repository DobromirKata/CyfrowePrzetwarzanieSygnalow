package application;

import application.view.SignalWindow;
import signal_processing.ISignal;

import javax.swing.*;
import java.util.List;

import java.util.ArrayList;

public class Helper {
    private static List<JFrame> windows = new ArrayList<>();

    public static void openWindow(ISignal signal) {
        SignalWindow signalWindow = new SignalWindow(signal);
        JFrame frame = new JFrame(signal.getSignalName());

        frame.add(signalWindow.getMainPanel());
        windows.add(frame);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
