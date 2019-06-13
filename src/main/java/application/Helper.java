package application;

import application.view.SignalWindow;
import signal_processing.ISignal;

import javax.swing.*;
import java.util.List;

import java.util.ArrayList;

public class Helper {
    private static List<JFrame> windows = new ArrayList<>();

    public static void openWindow(ISignal signal) {
        SignalWindow signalWindow = new SignalWindow();
        JFrame frame = new JFrame(signal.getSignalName());
        windows.add(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
