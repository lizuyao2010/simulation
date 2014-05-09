package task3;

import java.io.*;

import eventSchedulingMethod.Event;

public class MainSimulation3 extends task1.GlobalSimulation1{ //the same GlobalSimulation as in task1
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State3 actState = new State3(); // The state that should be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL, 0);  
        insertEvent(MEASURE, 5);
        
        // The main simulation loop
    	while (actState.nbrMeasurements < 1000){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
    	
    	/* Printing the result of the simulation, in this case a mean
    	 *  number of customers in the whole system and the mean time a customer
    	 *  spends in the system 
    	 */
    	System.out.print("Mean number of customers in the queuing network: ");
    	System.out.println(1.0*actState.totNbrInQ/actState.nbrMeasurements);
    	System.out.print("Mean time spend in the queuing network: ");
    	System.out.print(1.0*actState.totTimeInSystem/actState.nbrOfArrivals);
    	System.out.println(" seconds");
    }
}