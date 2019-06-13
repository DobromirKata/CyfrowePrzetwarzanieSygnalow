package application.view;

import org.jfree.chart.ChartPanel;
import javax.swing.*;

public class OperationsPanel extends JPanel {
    private JComboBox operationMode;
    private JButton setAsSignal1Button;
    private JButton exportButton;
    private JPanel operationsPanel;
    private JPanel signalPanel;
    private JLabel signalName;
    private JLabel infoAverage;
    private JLabel infoAbsoluteAverage;
    private JLabel infoAveragePower;
    private JLabel infoVariance;
    private JLabel infoRootMeanSquare;
    private JButton operationsSignalA;
    private JButton operationsSignalB;
    private JButton reverseButton;
    private JButton previewButton;
    private JButton setAsSignal2Button;
    private JLabel noSignal;
    private String[] signals = new String[] { "Signal 1", "Signal 2" };
    private ChartPanel chartPanel;

    public JPanel getOperationsPanel() {
        return operationsPanel;
    }

    public OperationsPanel() {
        initializeView();
        assignActions();
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

    public JButton getSetAsSignal1Button() {
        return setAsSignal1Button;
    }

    public JButton getExportButton() {
        return exportButton;
    }

    public JButton getPreviewButton() {
        return previewButton;
    }

    public JButton getSetAsSignal2Button() {
        return setAsSignal2Button;
    }

    public JPanel getSignalPanel() {
        return signalPanel;
    }

    public JLabel getNoSignal() {
        return noSignal;
    }

    private void initializeView() {
        chartPanel = new ChartPanel(null);
        String[] operations = new String[] { "+", "-", "*", "/" };
        operationMode.setModel(new DefaultComboBoxModel(operations));
    }

    private void assignActions() {
        reverseButton.addActionListener(e -> reverseSignals());
        operationsSignalA.addActionListener(e -> reverseSignals());
        operationsSignalB.addActionListener(e -> reverseSignals());
    }

    public void enableButtons() {
        setAsSignal1Button.setEnabled(true);
        setAsSignal2Button.setEnabled(true);
        previewButton.setEnabled(true);
        exportButton.setEnabled(true);
    }

    private void reverseSignals() {
        if (operationsSignalA.getText().equals(signals[0])) {
            operationsSignalA.setText(signals[1]);
        } else {
            operationsSignalA.setText(signals[0]);
        }
        if (operationsSignalB.getText().equals(signals[0])) {
            operationsSignalB.setText(signals[1]);
        } else {
            operationsSignalB.setText(signals[0]);
        }
    }

    public int getOperation() {
        return operationMode.getSelectedIndex();
    }

    public int getOrder() {
        return (operationsSignalA.getText().equals(signals[0]) ? 0 : 1);
    }


}
