package task1;

import java.io.IOException;

import eventSchedulingMethod.Event;

public class MainSimulation1 extends GlobalSimulation1{
		 
	public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State1 actState = new State1(); // The state that should be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL, 0);  
        insertEvent(MEASURE, 5);
        
        // The main simulation loop
    	while (actState.nbrMeasurements < 5000){//1000 enough
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
    	
    	/* Printing the result of the simulation, in this case a mean number of  customers in the queue and
    	 * the probability that a customer is rejected
    	 */
    	System.out.print("Mean number of customers in Q2: ");
    	System.out.println(1.0*actState.totNbrInQ2/actState.nbrMeasurements);
    	System.out.print("Probability of rejection: ");
    	System.out.println(1.0*actState.nbrRejected/actState.nbrCustomers);
   	}
}

