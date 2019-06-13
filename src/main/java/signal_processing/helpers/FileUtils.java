package signal_processing.helpers;

import signal_processing.ISignal;
import signal_processing.signals.GeneratedSignal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static final int CONTROL_NUMBER_START = 98172419;
    public static final int CONTROL_NUMBER_END = 129837128;

    public static void saveSignal(ISignal signal, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        DataOutputStream dos = new DataOutputStream(fos);

        dos.writeInt(CONTROL_NUMBER_START);
        dos.writeDouble(signal.getStartTime());
        dos.writeDouble(signal.getFrequency());
        dos.writeInt(0); // value type

        List<Double> x = signal.getValuesX();
        List<Double> y = signal.getValuesY();

        dos.writeInt(x.size()); // values count

        for (Double d : x) {
            dos.writeDouble(d);
        }
        for (Double d : y) {
            dos.writeDouble(d);
        }

        dos.writeInt(CONTROL_NUMBER_END);
        dos.close();
    }

    public static ISignal loadSignal(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        DataInputStream dis = new DataInputStream(fis);

        int control_start = dis.readInt();
        if (control_start != CONTROL_NUMBER_START) {
            throw new IOException();
        }

        double startTime = dis.readDouble();
        double frequency = dis.readDouble();
        int type = dis.readInt();

        int count = dis.readInt();

        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            x.add(dis.readDouble());
        }
        for (int i = 0; i < count; i++) {
            y.add(dis.readDouble());
        }

        int control_end = dis.readInt();
        if (control_end != CONTROL_NUMBER_END) {
            throw new IOException();
        }

        GeneratedSignal signal = new GeneratedSignal();
        signal.setValuesX(x);
        signal.setValuesY(y);

        dis.close();
        return signal;
    }
}
