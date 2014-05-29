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

    	QS Q1 = new QS();
    	Q1.sendTo = null;

    	Gen Generator = new Gen();
    	Generator.lambda = 4; //Generator shall generate 4 customers per hour
    	Generator.sendTo = Q1;   // The generated customers shall be sent to Q1
        Generator.numberofcustomers = 0; 

    	//To start the simulation the first signals are put in the signal list

    	SignalList.SendSignal(READY, Generator, time);
    	//SignalList.SendSignal(MEASURE, Q1, time);


    	// This is the main loop

    	while (time < 100000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

    	//Finally the result of the simulation is printed below:
        System.out.println("numberofcustomers:" +numberofcustomers);
        System.out.println("arrival:" +Q1.acc_arrival);
        System.out.println("leave:" +Q1.acc_leave);

    	System.out.println("average time: " + 1.0*(Q1.acc_leave-Q1.acc_arrival)/numberofcustomers);

    }
}