import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that should be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVALA, 0);  
        insertEvent(MEASURE, 0.1);
        
        // The main simulation loop
    	while (actState.nbrSamples < 2000){//1000 enough
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
    	
    	// Printing the result of the simulation, in this case a mean value
    	System.out.print("Mean number of customers in buffer: ");
    	System.out.println(actState.totNbrJobs + "/" + actState.nbrSamples);
    	System.out.println(1.0*actState.totNbrJobs/actState.nbrSamples);
    	System.out.println("A: " + actState.testerA + " B: " + actState.testerB);
    }
}