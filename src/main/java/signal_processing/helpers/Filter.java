package signal_processing.helpers;

import signal_processing.ISignal;
import signal_processing.Signal;
import signal_processing.signals.GeneratedSignal;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    public static Signal spliceOfSignals(List<Double> answer, List <Double> input){
        GeneratedSignal splicedSignal = new GeneratedSignal();
        int M=answer.size();
        int N=input.size();

        List<Double> tmpX = new ArrayList<>();
        List<Double> tmpY = new ArrayList<>();

        for (int i = 0; i < M+N-1; i++) {
            double sum = 0.0;

            for (int k = 0; k < M; k++) {
                try {
                    sum+=answer.get(k)*input.get(i-k);
                } catch (IndexOutOfBoundsException e) { }
            }

            tmpX.add((double) i);
            tmpY.add(sum);
        }

        splicedSignal.setValuesX(tmpX);
        splicedSignal.setValuesY(tmpY);
        return splicedSignal;
    }

    public static Signal spliceOfSignals (List<Double> answer, List <Double> input,double lastTime){
        GeneratedSignal splicedSignal = new GeneratedSignal();
        int M=answer.size();
        int N=input.size();

        List<Double> tmpX = new ArrayList<>();
        List<Double> tmpY = new ArrayList<>();

        double T=lastTime/(M+N-1);
        for(int n=0;n<M+N-1;++n) {
            double sum=0.0;
            for (int k = 0; k < M; ++k) {
                if(k<M && (n-k) >= 0 && (n-k)<N ){
                    sum+=answer.get(k)*input.get(n-k);
                }
            }

            tmpX.add((double) n * T);
            tmpY.add(sum);
        }

        splicedSignal.setValuesX(tmpX);
        splicedSignal.setValuesY(tmpY);
        return splicedSignal;
    }

    public static double rectangleWindow(){
        return 1.0;
    }

    public static double hannignWindow(int n, double M){

        return 0.5-0.5*Math.cos(2*Math.PI*(double)n/M);
    }

    public static List<Double> impulseAnswerBottomHan(int M, Double K){

        List <Double> points= new ArrayList<>();
        for (int n=0;n<M;n++){
            if(n == (M - 1.0) / 2.0){
                points.add(2/K* hannignWindow(n,M));
            } else {
                points.add(Math.sin(2.0 * Math.PI * ((double) n - (M - 1.0) / 2.0) / K) / (Math.PI * ((double) n - (M - 1.0) / 2.0))* hannignWindow(n,M));
            }

        }
        return points;
    }

    public static List<Double> impulseAnswerBottomRec (int M,Double K){

        List <Double> points= new ArrayList<>();
        for (int n=0;n<M;++n){
            if(n == (M - 1.0) / 2.0){
                points.add(2/K);
            } else {
                points.add(Math.sin(2.0 * Math.PI * ((double) n - (M - 1.0) / 2.0) / K) / (Math.PI * ((double) n - (M - 1.0) / 2.0))*rectangleWindow());
            }
        }
        return points;
    }

    public static List <Double> impulseAnswerTop(List<Double> bottom ){
        List <Double> points= new ArrayList<>();

        for(int n=0; n<bottom.size();n++){
            points.add(bottom.get(n)*Math.pow(-1,n));
        }

        return points;
    }

    public static Signal filterSignal(ISignal signal, int type, int M, double cutoffFrequency){

        List <Double> tmp = new ArrayList<>();

        Double K=signal.getFrequency() / cutoffFrequency;
        switch(type){
            case 0:
//                Bottom rectangular
                tmp=impulseAnswerBottomRec(M,K);
                break;

            case 1:
//                Top rectangular
                K=signal.getFrequency()/(signal.getFrequency()/2-cutoffFrequency);
                tmp=impulseAnswerTop(impulseAnswerBottomRec(M,K));
                break;

            case 2:
//                Bottom Hanning
                tmp=impulseAnswerBottomHan(M,K);
                break;

            case 3:
//                Top Hanning
                K=signal.getFrequency()/(signal.getFrequency()/2-cutoffFrequency);
                tmp=impulseAnswerTop(impulseAnswerBottomHan(M,K));
                break;

        }


        return spliceOfSignals(tmp,signal.getValuesY(),signal.getEndTime());
    }
}
