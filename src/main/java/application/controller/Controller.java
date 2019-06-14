package application.controller;

import application.Helper;
import application.model.Model;
import application.view.*;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.util.ShapeUtils;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import signal_processing.ISignal;
import signal_processing.Signal;
import signal_processing.helpers.*;
import signal_processing.signals.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Controller {
    private View view;
    private Model model;
    private SignalPanel signalPanel;
    private OperationsPanel operationsPanel;
    private DecimalFormat df;
    private JFileChooser fileChooser;
    private SamplingPanel samplingPanel;
    private QuantizationPanel quantizationPanel;
    private ReconstructionPanel reconstructionPanel;
    private FilterPanel filterPanel;
    private CorrelationPanel correlationPanel;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        signalPanel = view.getSignalPanel1();
        signalPanel.addRenderSignalButtonListener(e -> onRender());
        operationsPanel = view.getOperationsPanel();
//        Set defaults
        setDefaults();
//        Actions
        assignActions();
//        Decimal format
        setDecimalFormat();
//        Set signal controls
        updateSignalControls();

        initializeSamplingPanel();
        initializeQuantizationPanel();
        initializeReconstructionPanel();
        initializeFilterPanel();
        initializeCorrelationPanel();

        view.addDisplayButtonListener(e -> onSignalDisplay());
        view.addExportButtonListener(e -> onSignalExport());
        view.addImportButtonListener(e -> onSignalImport());
        view.addSelectedSignalsListener(e -> onSignalsSelect(e));

        operationsPanel.addCalcButtonListener(e -> onSignalsCalc());
        samplingPanel.addSamplingButtonListener(e -> onSampleSignal());
        quantizationPanel.addQuantizeSignalButtonListener(e -> onQuantizeSignal());
        reconstructionPanel.addReconstructionButtonListener(e -> onReconstructSignal());
    }

    private void initializeSamplingPanel() {
        samplingPanel = new SamplingPanel();
        JTabbedPane tabbedPane = view.getTabbedPane();
        tabbedPane.addTab("Próbkowanie", samplingPanel.getMainPanel());
        samplingPanel.addSamplingFrequencyListener(e -> onSamplingFrequencyChange(e));
    }

    private void initializeQuantizationPanel() {
        quantizationPanel = new QuantizationPanel();
        JTabbedPane tabbedPane = view.getTabbedPane();
        tabbedPane.addTab("Kwantyzacja", quantizationPanel.getMainPanel());
        quantizationPanel.addQuantizationLevelsListener(e -> onQuantizationLevelsChange(e));
    }

    private void initializeReconstructionPanel() {
        reconstructionPanel = new ReconstructionPanel();
        JTabbedPane tabbedPane = view.getTabbedPane();
        tabbedPane.addTab("Rekonstrukcja", reconstructionPanel.getMainPanel());
        reconstructionPanel.addReconstructionFrequencyListener(e -> onReconstructionFrequencyChange(e));
        reconstructionPanel.addRadioButtonListener(e -> onReconstructionTypeChange(e));
    }

    private void initializeFilterPanel() {
        filterPanel = new FilterPanel();
        JTabbedPane tabbedPane = view.getTabbedPane();
        tabbedPane.addTab("Filtrowanie", filterPanel.getMainPanel());
        filterPanel.addFilterTypeListener(e -> onFilterTypeChange(e));
        filterPanel.addWindowTypeListener(e -> onWindowTypeChange(e));
        filterPanel.addCutoffFrequencyListener(e -> onCutoffFrequencyChange(e));
        filterPanel.addStartFilterButtonListener(e -> onFilterSignal());
    }

    private void initializeCorrelationPanel() {
        correlationPanel = new CorrelationPanel();
        JTabbedPane tabbedPane = view.getTabbedPane();
        tabbedPane.addTab("Korelacja", correlationPanel.getMainPanel());
        correlationPanel.addSpeedSliderListener(e -> onSpeedSliderChange(e));
        correlationPanel.addStartButtonListener(e -> onCorrelationStart());
//        correlationPanel.addStopButtonListener(e -> onCorrelationStop());
    }

    private void onSamplingFrequencyChange(ChangeEvent event) {
        JSpinner source = (JSpinner) event.getSource();
        model.setSamplingFrequency((double)source.getValue());
    }

    private void onQuantizationLevelsChange(ChangeEvent event) {
        JSpinner source = (JSpinner) event.getSource();
        model.setQuantizationLevels((int)source.getValue());
    }

    private void onReconstructionFrequencyChange(ChangeEvent event) {
        JSpinner source = (JSpinner) event.getSource();
        model.setReconstructionFrequency((double)source.getValue());
    }

    private void onReconstructionTypeChange(ActionEvent event) {
        JRadioButton source = (JRadioButton) event.getSource();
        String name = source.getActionCommand();
        switch (name) {
            default:
                model.setReconstructionType(0);
                break;
            case "Funkcja sinc":
                model.setReconstructionType(1);
                break;
        }
    }

    private void reconstructSignal() throws Exception {
        int index = model.getReconstructionSignal();
        ISignal signal = model.getSignal();
        if (signal == null || signal.getValuesX().size() == 0 || signal.getValuesY().size() == 0) {
            throw new Exception("Signal not found.");
        }
        ISignal reconstructed;
        double reconstructionFrequency = model.getReconstructionFrequency();
        switch (model.getReconstructionType()) {
//            case 1:
//              ...
//                break;
            case 2:
                reconstructed = Operations.reconstruction(signal, reconstructionFrequency);
                break;
            default:
                reconstructed = Operations.zeroExploration(signal, reconstructionFrequency);
                break;
        }
        model.setReconstructedSignal(reconstructed);
        model.setOriginalReconstructionSignal(signal.copy());
        reconstructionPanel.hideNoSignal();
        updateReconstructionStats();
    }

    private void onFilterTypeChange(ActionEvent event) {
        JComboBox source = (JComboBox) event.getSource();
        model.setFilterType(source.getSelectedIndex());
    }

    private void onWindowTypeChange(ActionEvent event) {
        JComboBox source = (JComboBox) event.getSource();
        model.setWindowType(source.getSelectedIndex());
    }

    private void onCutoffFrequencyChange(ChangeEvent event) {
        JSpinner source = (JSpinner) event.getSource();
        model.setCutoffFrequency((double) source.getValue());
    }

    private void filterSignal() throws Exception {
        int index = model.getFilterSignal();
        ISignal signal = model.getSignal();
        if (signal == null || signal.getValuesX().size() == 0 || signal.getValuesY().size() == 0) {
            throw new Exception("Signal not found.");
        }
        ISignal filtered;
        double cutoffFrequency = model.getCutoffFrequency();

        int filterType = model.getFilterType();
        int windowType = model.getWindowType();
        int m = 15;

        if (filterType == 0 && windowType == 0) {
            filtered = Filter.filterSignal(signal, 0, m, cutoffFrequency);
        } else if (filterType == 1 && windowType == 0) {
            filtered = Filter.filterSignal(signal, 1, m, cutoffFrequency);
        } else if (filterType == 0 && windowType == 1) {
            filtered = Filter.filterSignal(signal, 2, m, cutoffFrequency);
        } else {
            filtered = Filter.filterSignal(signal, 3, m, cutoffFrequency);
        }

        model.setFilteredSignal(filtered);
        model.setOriginalFilteredSignal(signal.copy());
        filterPanel.hideNoSignal();
    }

    private void onPreviewButtonInFilter() {
        try {
            filterSignal();
            ISignal signal = model.getSignal();
            JFreeChart chart = Operations.getChart(signal, model.getFilteredSignal());
            filterPanel.displaySignal(chart);
            filterPanel.hideNoSignal();
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
    }


    private void updateReconstructionStats() {
        Statistics statistics = new Statistics(model.getOriginalReconstructionSignal(), model.getReconstructedSignal());
        Object[][] stats = {
                { "Mean square error", df.format(statistics.MSE()) },
                { "Signal noise ratio", df.format(statistics.SNR()) },
                { "Peak signal to noise ratio", df.format(statistics.PSNR()) },
                { "Maximum difference", df.format(statistics.MD()) },
                { "Effective number of bits", df.format(statistics.ENOB()) }
        };
        reconstructionPanel.updateStats(stats);
    }

    private void onSpeedSliderChange(ChangeEvent event) {
        JSlider source = (JSlider) event.getSource();
        model.setSpeed(source.getValue());
    }

    private void onCorrelationStart() {
        try {
            Correlation correlation = model.getCorrelation();

            ISignal signal1 = model.getSignal();
            ISignal signal2 = model.getSignal();
            if (signal1 == null || signal2 == null) {
                throw new Exception("Both signals must be generated.");
            }

            Position position = model.getPosition();
            if (position == null) {
                position = new Position(0.05);
                model.setPosition(position);
            }

            correlation = new Correlation(100, 0.5, 0.5, 0.5, 0, signal1, signal2, position);
            model.setCorrelation(correlation);
            correlation.distanceSensor();

            GeneratedSignal correlated = correlation.getCorrelatedSignal();
            correlated.setName("Signals correlation");

            view.renderSentSignal(signal1);
            view.renderReceivedSignal(signal2);
            view.renderCorrelatedSignal(correlated);


//            Thread thread = new Thread(new CorrelationThread());
//            thread.start();

//            model.setCorrelationWorking(true);
//            updateCorrelationButtons();

        } catch (Exception e) {
            e.printStackTrace();
            view.displayError(e.getMessage());
        }
    }

//    private void onCorrelationStop() {
//        model.setCorrelationWorking(false);
//        updateCorrelationButtons();
//    }

//    private void updateCorrelationButtons() {
//        boolean isWorking = model.isCorrelationWorking();
//        correlationPanel.updateButtons(isWorking);
//    }

    public class CorrelationThread implements Runnable {
        @Override
        public void run() {
            Position position = model.getPosition();
            Correlation correlation = model.getCorrelation();
            double time = 0.0;

            while (model.isCorrelationWorking()) {
                try {
                    Thread.sleep(1000);

                    System.out.println(correlation.getDistance());
                    position.setPosition(time++);

                    correlation.distanceSensor();
                    ISignal correlated = correlation.getCorrelatedSignal();
                    view.renderCorrelatedSignal(correlated);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setDecimalFormat() {
        df = new DecimalFormat("0.00000");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
    }

    private void assignActions() {

            signalPanel.getSignalType().addActionListener(e -> onSignalChange());
            signalPanel.getFirstSample().addChangeListener(e -> updateFirstSample());
            signalPanel.getLastSample().addChangeListener(e -> updateLastSample());
            signalPanel.getAmplitude().addChangeListener(e -> updateAmplitude());
            signalPanel.getStartTime().addChangeListener(e -> updateStartTime());
            signalPanel.getEndTime().addChangeListener(e -> updateEndTime());
            signalPanel.getFrequency().addChangeListener(e -> updateFrequency());
            signalPanel.getBasicPeriod().addChangeListener(e -> updateBasicPeriod());
            signalPanel.getFillingFactor().addChangeListener(e -> updateFillingFactor());
            signalPanel.getProbability().addChangeListener(e -> updateProbability());
            signalPanel.getJumpPoint().addChangeListener(e -> updateJumpPoint());
            signalPanel.getSampleJump().addChangeListener(e -> updateSampleJump());
            signalPanel.getRenderButton().addActionListener(e -> onSignalRender());
            view.getHistogramBins().addChangeListener(e -> onHistogramChange());

        view.getFile_item_1().addActionListener(e -> onImport());
    }

    private void onSignalChange() {
        int selectedSignal = signalPanel.getSignalType().getSelectedIndex();

        if (selectedSignal != 11) {
            setSignal(selectedSignal);
            updateSignalControls();
        }
    }

    private void updateSignalControls() {
        SignalPanel panel = signalPanel;
        ISignal signal = model.getSignal();
        for (String parameter : Signal.getAllParameters()) {
            try {
                Method method = panel.getClass().getMethod("get" + StringUtils.capitalize(parameter));
                JComponent component = (JComponent) method.invoke(panel, null);
                boolean exists = Arrays.stream(signal.getAvailableParameters()).anyMatch(parameter::equals);
//                component.setEnabled(exists);
                component.getParent().getParent().setVisible(exists);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        signalPanel.getFirstSample().setValue(signal.getFirstSample());
        signalPanel.getLastSample().setValue(signal.getLastSample());
        signalPanel.getAmplitude().setValue(signal.getAmplitude());
        signalPanel.getStartTime().setValue(signal.getStartTime());
        signalPanel.getEndTime().setValue(signal.getEndTime());
        signalPanel.getFrequency().setValue(signal.getFrequency());
        signalPanel.getBasicPeriod().setValue(signal.getBasicPeriod());
        signalPanel.getFillingFactor().setValue(signal.getFillingFactor());

        if (signal.getClass().getName().equals("signal_processing.signals.ImpulseNoise")) {
            ImpulseNoise tmp = (ImpulseNoise) signal;
            signalPanel.getProbability().setValue((int) (tmp.getProbability() * 100));
        }
        if (signal.getClass().getName().equals("signal_processing.signals.IndividualJumpSignal")) {
            IndividualJumpSignal tmp = (IndividualJumpSignal) signal;
            signalPanel.getJumpPoint().setValue(tmp.getJumpPoint());
        }
        if (signal.getClass().getName().equals("signal_processing.signals.IndividualImpulseSignal")) {
            IndividualImpulseSignal tmp = (IndividualImpulseSignal) signal;
            signalPanel.getSampleJump().setValue(tmp.getSampleJump());
        }
    }

    private void onSignalRender() {
        ISignal signal = model.getSignal();
        signal.updateValues();

        Statistics stats = model.getStats();
        renderSignal();
        renderHistogram();

        signal.setRendered(true);

        if (model.isBothSignalsRendered()) {
            int[] indices = view.getSelectedSignalIndices();
            view.enableOperationsButtons(indices.length == 2);
        }
//
//        signalPanel.getInfoAverage().setText(df.format(stats.getAverage()));
//        signalPanel.getInfoAbsoluteAverage().setText(df.format(stats.getAbsoluteMean()));
//        signalPanel.getInfoAveragePower().setText(df.format(stats.getAveragePower()));
//        signalPanel.getInfoVariance().setText(df.format(stats.getVariance()));
//        signalPanel.getInfoRootMeanSquare().setText(df.format(stats.getEffectiveValue()));
    }

    private void onHistogramChange() {
        renderHistogram();
    }

    private void onPreview() {
        generateSignal();
        ISignal generatedSignal = model.getGeneratedSignal();

        final XYSeries series = new XYSeries("data");

        List<Double> x = generatedSignal.getValuesX();
        List<Double> y = generatedSignal.getValuesY();
        for (int i = 0; i < x.size(); i++) {
            series.add(x.get(i), y.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        view.renderGeneratedSignal(dataset);

        Statistics stats = model.getGeneratedStats();

        operationsPanel.getInfoAverage().setText(df.format(stats.getAverage()));
        operationsPanel.getInfoAbsoluteAverage().setText(df.format(stats.getAbsoluteMean()));
        operationsPanel.getInfoAveragePower().setText(df.format(stats.getAveragePower()));
        operationsPanel.getInfoVariance().setText(df.format(stats.getVariance()));
        operationsPanel.getInfoRootMeanSquare().setText(df.format(stats.getEffectiveValue()));
    }

    public void onExport() {
        fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(view.getMainPanel());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String selectedFile = fileChooser.getSelectedFile().getPath();
            try {
                generateSignal();
                FileUtils.saveSignal(model.getGeneratedSignal(), selectedFile);
                JOptionPane.showMessageDialog(view.getFrame(), "Saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                String message = "Could not save file: " + selectedFile;
                JOptionPane.showMessageDialog(view.getFrame(), message, "Saving error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void onImport() {
        fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(view.getMainPanel());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String selectedFile = fileChooser.getSelectedFile().getPath();
            try {
                ISignal signal = FileUtils.loadSignal(selectedFile);
                model.setSignal(signal);
                renderSignal();
                renderHistogram();
                signalPanel.getSignalType().setSelectedIndex(11);
                updateSignalControls();

                signal.setRendered(true);

                int[] indices = view.getSelectedSignalIndices();
                view.enableOperationsButtons(indices.length == 2);


            } catch (IOException ex) {
                String message = "Could not import file: " + selectedFile;
                JOptionPane.showMessageDialog(view.getFrame(), message, "Loading error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setSignal(int type) {
        model.setSignal(type);
    }

    private void generateSignal() {
        // TODO: Correct signal generation after finish
//        model.generateSignal(view.getOperation(), view.getOrder());
    }

    private void renderSignal() {
        ISignal signal = model.getSignal();
//        TODO: Correct render
//        JPanel panel = (index == 0 ? view.getSignalChart1() : view.getSignalChart2());
        final XYSeries series = new XYSeries("data");
        List<Double> x = signal.getValuesX();
        List<Double> y = signal.getValuesY();
        for (int i = 0; i < x.size(); i++) {
            series.add(x.get(i), y.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
//        view.renderSignal(index, signal, dataset);
    }

    private void renderHistogram() {
        ISignal signal = model.getSignal();
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        double[] values = new double[signal.getValuesY().size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = signal.getValuesY().get(i);
        }
        int bins = view.getHistogramBins().getValue();
        dataset.addSeries("H1", values, bins, Collections.min(signal.getValuesY()), Collections.max(signal.getValuesY()));

//        TODO: Correct render histogram call in view
//        view.renderHistogram(dataset);
    }

    private void setDefaults() {
            setSignal(signalPanel.getSignalType().getSelectedIndex());
            updateFirstSample();
            updateLastSample();
            updateAmplitude();
            updateStartTime();
            updateEndTime();
            updateFrequency();
            updateBasicPeriod();
            updateFillingFactor();
            updateProbability();
            updateJumpPoint();
            updateSampleJump();
    }

    private void updateFirstSample() {
        model.getSignal().setFirstSample((int) signalPanel.getFirstSample().getValue());
    }
    private void updateLastSample() {
        model.getSignal().setLastSample((int) signalPanel.getLastSample().getValue());
    }
    private void updateAmplitude() {
        model.getSignal().setAmplitude((double) signalPanel.getAmplitude().getValue());
    }
    private void updateStartTime() {
        model.getSignal().setStartTime((double) signalPanel.getStartTime().getValue());
    }
    private void updateEndTime() {
        model.getSignal().setEndTime((double) signalPanel.getEndTime().getValue());
    }
    private void updateFrequency() {
        model.getSignal().setFrequency((double) signalPanel.getFrequency().getValue());
    }
    private void updateBasicPeriod() {
        model.getSignal().setBasicPeriod((double) signalPanel.getBasicPeriod().getValue());
    }
    private void updateFillingFactor() {
        model.getSignal().setFillingFactor((double) signalPanel.getFillingFactor().getValue());
    }
    private void updateProbability() {
        if (model.getSignal().getClass().getName().equals("signal_processing.signals.ImpulseNoise")) {
            ImpulseNoise signal = (ImpulseNoise) model.getSignal();
            signal.setProbability((signalPanel.getProbability().getValue() / 100d));
        }
    }
    private void updateJumpPoint() {
        if (model.getSignal().getClass().getName().equals("signal_processing.signals.IndividualJumpSignal")) {
            IndividualJumpSignal signal = (IndividualJumpSignal) model.getSignal();
            signal.setJumpPoint((double) signalPanel.getJumpPoint().getValue());
        }
    }
    private void updateSampleJump() {
        if (model.getSignal().getClass().getName().equals("signal_processing.signals.IndividualImpulseSignal")) {
            IndividualImpulseSignal signal = (IndividualImpulseSignal) model.getSignal();
            signal.setSampleJump((double) signalPanel.getSampleJump().getValue());
        }
    }

    private void onRender() {
        ISignal signal = model.getSignal();
        view.addSignal(signal.getSignalName());
        int bins = view.getHistogramBins().getValue();
        Helper.openWindow(signal, bins);
        model.addSignalToList(signal);
        view.setSelection(model.getSignalsCount() - 1);
    }

    private ISignal getSelectedSignal() {
        int selectedIndex = view.getSelectedSignalIndex();
        return model.getSignalFromList(selectedIndex);
    }

    private void onSignalDisplay() {
        ISignal signal = getSelectedSignal();
        int bins = view.getHistogramBins().getValue();
        Helper.openWindow(signal, bins);
    }

    private void onSignalExport() {
        ISignal signal = getSelectedSignal();
        fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(view.getMainPanel());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String selectedFile = fileChooser.getSelectedFile().getPath();
            try {
                FileUtils.saveSignal(signal, selectedFile);
                JOptionPane.showMessageDialog(view.getFrame(), "Zapisano.", "Komunikat", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                view.displayError("Nie można zapisać pliku " + selectedFile);
            }
        }
    }

    private void onSignalImport() {
        fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(view.getMainPanel());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String selectedFile = fileChooser.getSelectedFile().getPath();
            try {
                GeneratedSignal signal = (GeneratedSignal) FileUtils.loadSignal(selectedFile);
                signal.setName("Wczytany sygnał");
                model.addSignalToList(signal);
                view.addSignal("Wczytany sygnał");
                signalPanel.getSignalType().setSelectedIndex(11);

            } catch (IOException ex) {
                String message = "Nie można wczytać pliku: " + selectedFile;
                JOptionPane.showMessageDialog(view.getFrame(), message, "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onSignalsSelect(ListSelectionEvent event) {
        JList list = (JList) event.getSource();
        int[] indices = list.getSelectedIndices();
        operationsPanel.setButtonEnabled(indices.length == 2);
        samplingPanel.setButtonEnabled(indices.length == 1);
        quantizationPanel.setButtonEnabled(indices.length == 1);
        reconstructionPanel.setEnabled(indices.length == 1);
        filterPanel.setEnabled(indices.length == 1);
    }

    private void onSignalsCalc() {
        int operation = operationsPanel.getOperation();
        int[] indices = view.getSelectedSignalIndices();
        ISignal signal1 = model.getSignalFromList(indices[0]);
        ISignal signal2 = model.getSignalFromList(indices[1]);

        int index1 = indices[0] + 1;
        int index2 = indices[1] + 1;

        if (operationsPanel.getOrder() == 1) {
            ISignal tmp = signal1;
            signal1 = signal2;
            signal2 = tmp;
            int tmpindex = index1;
            index1 = index2;
            index2 = tmpindex;
        }

        model.generateSignal(operation, signal1, signal2);

        GeneratedSignal result = (GeneratedSignal) model.getGeneratedSignal();
        model.addSignalToList(result);
        view.addSignal(MessageFormat.format("{0} [{1}] {2} {3} [{4}]", signal1.getSignalName(), index1, Helper.operationAsString(operation), signal2.getSignalName(), index2));

        int bins = view.getHistogramBins().getValue();
        Helper.openWindow(result, bins);
    }

    private void onSampleSignal() {
        ISignal signal = getSelectedSignal();
        double freq = model.getSamplingFrequency();
        GeneratedSignal generatedSignal = (GeneratedSignal) Operations.sampling(signal, freq);
        JFreeChart chart = Operations.getChart(signal, generatedSignal);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(1));
        renderer.setSeriesPaint(0, Color.gray);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShape(1, new Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesStroke(1, new BasicStroke(2));
        renderer.setSeriesPaint(1, new Color(0,109,13));

        plot.setRenderer(renderer);

        int index = view.getSelectedSignalIndex() + 1;
        String message = MessageFormat.format("Próbkowanie ({2}) - {0} [{1}]", signal.getSignalName(), index, freq);
        Helper.openSimpleWindow(message, chart);

        view.addSignal(message);
        model.addSignalToList(generatedSignal);
    }

    private void onQuantizeSignal() {
        ISignal signal = getSelectedSignal();
        int levels = model.getQuantizationLevels();
        GeneratedSignal generatedSignal = (GeneratedSignal) Operations.quantization(signal, levels);

        JFreeChart chart = Operations.getChart(signal, generatedSignal);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(1));
        renderer.setSeriesPaint(0, Color.gray);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesStroke(1, new BasicStroke(3));
        renderer.setSeriesPaint(1, new Color(207,91,75));

        plot.setRenderer(renderer);



        int index = view.getSelectedSignalIndex() + 1;
        String message = MessageFormat.format("Kwantyzacja ({2}) - {0} [{1}]", signal.getSignalName(), index, levels);
        Helper.openSimpleWindow(message, chart);

        view.addSignal(message);
        model.addSignalToList(generatedSignal);
    }

    private void onReconstructSignal() {
        ISignal signal = getSelectedSignal();
        double freq = model.getReconstructionFrequency();
        int type = model.getReconstructionType();

        GeneratedSignal generatedSignal;

        if (type == 0) {
            generatedSignal = (GeneratedSignal) Operations.zeroExploration(signal, freq);
        } else {
            generatedSignal = (GeneratedSignal) Operations.reconstruction(signal, freq);
        }

        JFreeChart chart = Operations.getChart(signal, generatedSignal);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(1));
        renderer.setSeriesPaint(0, Color.gray);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesStroke(1, new BasicStroke(2));
        renderer.setSeriesPaint(1, new Color(0,152,255));

        plot.setRenderer(renderer);

        int index = view.getSelectedSignalIndex() + 1;
        String message = MessageFormat.format("Rekonstrukcja ({2}) - {0} [{1}]", signal.getSignalName(), index, freq);
        Statistics stats = new Statistics(signal, generatedSignal);
        Helper.openReconstructionWindow(message, chart, stats);

        view.addSignal(message);
        model.addSignalToList(generatedSignal);
    }

    private void onFilterSignal() {
        int filterType = model.getFilterType();
        int windowType = model.getWindowType();
        double cutoffFrequency = model.getCutoffFrequency();
        int m = 15;

        ISignal signal = getSelectedSignal();
        GeneratedSignal filtered;
        if (filterType == 0 && windowType == 0) {
            filtered = (GeneratedSignal) Filter.filterSignal(signal, 0, m, cutoffFrequency);
        } else if (filterType == 1 && windowType == 0) {
            filtered = (GeneratedSignal) Filter.filterSignal(signal, 1, m, cutoffFrequency);
        } else if (filterType == 0 && windowType == 1) {
            filtered = (GeneratedSignal) Filter.filterSignal(signal, 2, m, cutoffFrequency);
        } else {
            filtered = (GeneratedSignal) Filter.filterSignal(signal, 3, m, cutoffFrequency);
        }

        JFreeChart chart = Operations.getChart(signal, filtered);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(1));
        renderer.setSeriesPaint(0, Color.gray);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesStroke(1, new BasicStroke(3));
        renderer.setSeriesPaint(1, new Color(87,86,211));

        plot.setRenderer(renderer);


        String ftname = (filterType == 0 ? "dol." : "gór.");
        String wname = (windowType == 0 ? "prost." : "Hann.");

        int index = view.getSelectedSignalIndex() + 1;
        String message = MessageFormat.format("Filtrowanie ({2}/{3}) - {0} [{1}]", signal.getSignalName(), index, ftname, wname);
        Helper.openSimpleWindow(message, chart);

        view.addSignal(message);
        model.addSignalToList(filtered);
    }
}
