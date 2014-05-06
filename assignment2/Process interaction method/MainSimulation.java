import java.util.*;
import java.io.*;


public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {
    	// The signal list is started and actSignal is declaree. actSignal is the latest signal that has been fetched from the 
    	// signal list in the main loop below.

    	Signal actSignal;
    	new SignalList();

    	// Here process instances are created (two queues and one generator) and their parameters are given values. 
        ArrayList<QS> qsList = new ArrayList<QS>();

    	
        for (int i=0; i<5 ; i++) 
        {
            QS Q = new QS();
            Q.sendTo = null;
            qsList.add(Q);
        }
        Random genNum = new Random();

    	Gen Generator = new Gen();
    	Generator.lambda = 9;  //Generator shall generate 9 customers per second
    	Generator.sendTo = null;   // The generated customers shall be sent to Q1

    	//To start the simulation the first signals are put in the signal list

    	SignalList.SendSignal(READY, Generator, time);
    	SignalList.SendSignal(MEASURE, qsList.get(0), time);


    	// This is the main loop

    	while (time < 100000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
            if (actSignal.destination==Generator)
            {   
                int index = genNum.nextInt(qsList.size());
                Generator.sendTo = qsList.get(index);   
            }
    		actSignal.destination.TreatSignal(actSignal);
    	}

    	//Finally the result of the simulation is printed below:

    	System.out.println("Mean number of customers in queuing system: " + 1.0*qsList.get(0).accumulated/qsList.get(0).noMeasurements);

    }
}