package task3;

import java.io.*;

public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that should be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL, 0);  
        insertEvent(MEASURE, 5);
        
        // The main simulation loop
    	while (actState.nbrMeasurements < 5000){//1000 enough
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
    	
    	// Printing the result of the simulation, in this case a mean value
    	System.out.print("Mean number of customers in the queuing network: ");
    	System.out.println(1.0*actState.totNbrInQ/actState.nbrMeasurements);
    	System.out.print("Mean time spend in the queuing network: ");
    	System.out.println();
    }
}