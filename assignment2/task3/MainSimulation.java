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
        /*
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
        */
        for (int i=0; i<5; i++)
        {
            Component C = new Component();
            C.sendTo = null;
            cpList.add(C);
        }


    	
    	//To start the simulation the first signals are put in the signal list
        /*
    	SignalList.SendSignal(READY, C1, time);
        SignalList.SendSignal(READY, C2, time);
        SignalList.SendSignal(READY, C5, time);
        SignalList.SendSignal(READY, C3, time);
        SignalList.SendSignal(READY, C4, time);
        */
        for (int i=0; i<5; i++)
        {
            SignalList.SendSignal(READY, cpList.get(i), time);
        }


    	// This is the main loop

    	while (time < 100000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
            /*
            // the system breaks down, reset the system
            if (actSignal.signalType==READY && brokenNumber==5){
                brokenNumber=0;
                SignalList.SendSignal(READY, C1, time);
                SignalList.SendSignal(READY, C2, time);
                SignalList.SendSignal(READY, C5, time);
                SignalList.SendSignal(READY, C3, time);
                SignalList.SendSignal(READY, C4, time);
                
            }
            // system works
            else{
                // if c1 breaks down, send dead signal to c2,c5
                if (actSignal.signalType==DEAD && actSignal.destination==C1) {
                    SignalList.SendSignal(DEAD, C2, time);
                    SignalList.SendSignal(DEAD, C5, time);
                }
                // if c3 breaks down, send dead signal to c4
                if (actSignal.signalType==DEAD && actSignal.destination==C3) {
                    SignalList.SendSignal(DEAD, C4, time);
                }
    		    actSignal.destination.TreatSignal(actSignal);
            }
            */
            if (actSignal.signalType==DEAD && actSignal.destination==cpList.get(0)) {
                    SignalList.SendSignal(DEAD, cpList.get(1), time);
                    SignalList.SendSignal(DEAD, cpList.get(4), time);
            }
            if (actSignal.signalType==DEAD && actSignal.destination==cpList.get(2)) {
                    SignalList.SendSignal(DEAD, cpList.get(3), time);
            }
            actSignal.destination.TreatSignal(actSignal);

    	}


    }
}