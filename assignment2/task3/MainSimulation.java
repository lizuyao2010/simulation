import java.util.*;
import java.io.*;

//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	// The signal list is started and actSignal is declaree. actSignal is the latest signal that has been fetched from the 
    	// signal list in the main loop below.

    	Signal actSignal;
    	new SignalList();

    	// Here process instances are created (two queues and one generator) and their parameters are given values. 

        Random slump = new Random();
        Component C1 = new Component();
        C1.sendTo = null;
        Component C2 = new Component();
        C2.sendTo = null;
        Component C5 = new Component();
        C5.sendTo = null;
        Component C3 = new Component();
        C1.sendTo = null;
        Component C4 = new Component();
        C2.sendTo = null;


    	
    	//To start the simulation the first signals are put in the signal list

    	SignalList.SendSignal(READY, C1, time);
        SignalList.SendSignal(READY, C2, time);
        SignalList.SendSignal(READY, C5, time);
        SignalList.SendSignal(READY, C3, time);
        SignalList.SendSignal(READY, C4, time);


    	// This is the main loop

    	while (time < 100000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
            if (actSignal.signalType==READY && brokenNumber==5){
                SignalList.SendSignal(READY, C1, time);
                SignalList.SendSignal(READY, C2, time);
                SignalList.SendSignal(READY, C5, time);
                brokenNumber=0;
            }
            else{

                if (actSignal.signalType==DEAD && actSignal.destination==C1) {
                    SignalList.SendSignal(DEAD, C2, time);
                    SignalList.SendSignal(DEAD, C5, time);
                }
                if (actSignal.signalType==DEAD && actSignal.destination==C3) {
                    SignalList.SendSignal(DEAD, C4, time);
                }
    		    actSignal.destination.TreatSignal(actSignal);
            }
    	}


    }
}