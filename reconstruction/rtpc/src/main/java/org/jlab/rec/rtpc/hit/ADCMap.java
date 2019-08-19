/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.rec.rtpc.hit;

import java.util.HashMap;

/**
 *
 * @author davidpayette
 */
public class ADCMap {
    
    private HashMap<Integer,double[]> _ADCMap;
    private HashMap<Integer,double[]> _intADCMap;

    private int TrigWindSize = 10000;
    private int SignalStepSize = 10;
    
    public ADCMap(){
        _ADCMap = new HashMap<>();
        _intADCMap = new HashMap<>();

    }
    
    public void simulateSignal(int Pad, double time, double Edep){
        if(!_ADCMap.containsKey(Pad)){
            _ADCMap.put(Pad, new double[TrigWindSize]);
        }
        for(int tbin=0;tbin<TrigWindSize;tbin+=SignalStepSize){   
            _ADCMap.get(Pad)[tbin] += EtoS(time,tbin,Edep);
        }
    }
    
    public void integrateSignal(int Pad){
        double integral = 0;
        int BinSize = 40;
        int NBinKept = 3;
        for(int tbin = 0; tbin < TrigWindSize; tbin += SignalStepSize){  
            if(tbin>0) integral+=0.5*(_ADCMap.get(Pad)[tbin-SignalStepSize]+_ADCMap.get(Pad)[tbin])*SignalStepSize;	         		         	
            if(tbin%BinSize==0 && tbin>0){ // integration over BinSize
                if(tbin%(BinSize*NBinKept)==0){ // one BinSize over NBinKept is read out
                    if(!_intADCMap.containsKey(Pad)){
                        _intADCMap.put(Pad, new double[TrigWindSize]);
                    }
                    _intADCMap.get(Pad)[tbin] = integral;
                }	             
                integral=0;
            }
        }
    }
    
    public double getSignal(int Pad, int Time){
        return _intADCMap.get(Pad)[Time];
    }
    
    public HashMap<Integer,double[]> getADCMap(){
        return _intADCMap;
    }
    
    private double EtoS(double tsignal, double t, double e_tot){

        double sig;
        //t = noise_elec(t);    // change t to simulate the electronics noise, also modifies the amplitude
        double p0 = 0.0;     
        double p2 = 178.158;    
        double p3 = 165.637;     
        double p4 = 165.165;

        if(t<tsignal) sig = p0+e_tot*p2*Math.exp(-(t-tsignal)*(t-tsignal)/(2*p3*p3))/(0.5*(p3+p4)*Math.sqrt(2*Math.PI));
        else       sig = p0+e_tot*p2*Math.exp(-(t-tsignal)*(t-tsignal)/(2*p4*p4))/(0.5*(p3+p4)*Math.sqrt(2*Math.PI)); 

        return sig;
    }
    
    /*private double noise_elec(double time){
        Random noise = new Random();
        double sigTelec = 5; // 5 ns uncertainty on the signal
        return noise.nextGaussian()*sigTelec + tim;
    }*/
    
}
