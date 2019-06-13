package signal_processing.helpers;

import signal_processing.ISignal;
import signal_processing.signals.GeneratedSignal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Correlation {

    //PARAMETRY
    private int    bufferLength;                        // Wielkość buffora
    private double samplingFrequency=0.0;               // Częstotliwość próbkowania sygnałów
    private double raportPeriod;                        // Okres raportowania
    private double signalSpeed;                         // Prędkość rozchodzenia sygnału

    //SYGNAŁY
    private ISignal soundingSignal;                      // Sygnał sondujący
    private ISignal reflectedSignal;                     // Sygnał odbity
    private GeneratedSignal correlatedSignal;                     // Sygnał po przeprowadzonej korelacji
    private ISignal firstSubSignal;                      // Pierwszy podsygnał
    private ISignal secondSubSignal;                     // Drugi podsygnał
    private int correlationType;                            // Typ sensora
    private ISignal target;                              // Oddalający sięobiekt
    private double distance;                            // Obliczona odległość
    private Position position;


    public Correlation(int bufferLength, double samplingFrequency, double raportPeriod, double signalSpeed, int sensorType, ISignal first, ISignal second, Position position) {
        this.bufferLength = bufferLength;
        this.samplingFrequency = samplingFrequency;
        this.raportPeriod = raportPeriod;
        this.signalSpeed = signalSpeed;
        this.correlationType = sensorType;
        this.firstSubSignal=first;
        this.secondSubSignal=second;
        this.convertToSoundingSignal(first, second);
        this.position = position;
    }

    public double getRaportPeriod() {
        return raportPeriod;
    }

    public ISignal getSoundingSignal() {
        return soundingSignal;
    }

    public ISignal getReflectedSignal() {
        return reflectedSignal;
    }

    public GeneratedSignal getCorrelatedSignal() {
        return correlatedSignal;
    }

    public int getCorrelationType() {
        return correlationType;
    }

    public double getDistance() {
        return distance;
    }

    public  void spliceCorrelationOfSignals(List<Double> first, List<Double> second){
        List<Double> firstSignalY=first;
        Collections.reverse(firstSignalY);
        correlatedSignal= (GeneratedSignal) Filter.spliceOfSignals(firstSignalY,second);
    }

    public void directCorrelationOfSignals (List<Double> first,List<Double> second){

        correlatedSignal=new GeneratedSignal();
        List<Double> tmpX = new ArrayList<>();
        List<Double> tmpY = new ArrayList<>();

        for (int i = -(second.size()-1), j = 0; i < first.size(); i++, j++) {
            double sum = 0.0;

            for (int k = 0; k < first.size(); k++) {
                try {
                    sum += first.get(k) * second.get(k-i);
                } catch (IndexOutOfBoundsException e) { }
            }
            tmpX.add((double) j);
            tmpY.add(sum);
        }

        correlatedSignal.setValuesX(tmpX);
        correlatedSignal.setValuesY(tmpY);
    }

    public  void choose(List <Double> first, List<Double> second){
        if (correlationType == 0) {
            spliceCorrelationOfSignals(first, second);
        } else {
            directCorrelationOfSignals(first, second);
        }
    }

    public void convertToSoundingSignal(ISignal first, ISignal second){
        soundingSignal=Operations.getAddSignals(first, second);
    }

    public void  generateReflectedSignal(){
        double delay = position.getPosition() / signalSpeed;
        firstSubSignal.setFirstSample((int) (delay*samplingFrequency));
        secondSubSignal.setFirstSample((int) (delay*samplingFrequency));
        firstSubSignal.updateValues();
        secondSubSignal.updateValues();
        reflectedSignal = Operations.getAddSignals(firstSubSignal, secondSubSignal);
    }

    public void distanceSensor(){
        generateReflectedSignal();
        choose(soundingSignal.getValuesY().subList(0, bufferLength), reflectedSignal.getValuesY().subList(0, bufferLength));
        double maxValueY = -Double.MAX_VALUE;
        for (int i = correlatedSignal.getValuesX().size() / 2; i < correlatedSignal.getValuesX().size(); i++) {
            if (correlatedSignal.getValuesY().get(i) > maxValueY) {
                maxValueY = correlatedSignal.getValuesY().get(i);
            }
        }
        int maxValue = correlatedSignal.getValuesY().indexOf(maxValueY);
        int centerValue = correlatedSignal.getValuesY().size() / 2;
        double shift = Math.abs(maxValue-centerValue) / samplingFrequency;
        distance = shift * signalSpeed;
    }
}
