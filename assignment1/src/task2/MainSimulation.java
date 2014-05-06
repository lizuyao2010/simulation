package task2;

import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	for(int i = 1; i <= 3; i++){
    		switch(i){
    		case 1:
    			output = new FileOutputStream("task21.txt");
    			break;
    		case 2:
    			output = new FileOutputStream("task22.txt");
    			break;
    		case 3:
    			output = new FileOutputStream("task23.txt");
    			break;
    		}
    		
            writer = new BufferedWriter(new OutputStreamWriter(output, "utf-8"));            
            
    		Event actEvent;
    		State actState = new State(); // The state that should be used
    		actState.version = i;
    		// Some events must be put in the event list at the beginning
        	insertEvent(ARRIVALA, 0);  
        	insertEvent(MEASURE, 0.1);
        
        	// The main simulation loop
    		while (actState.nbrSamples < 1000){
    			actEvent = eventList.fetchEvent();
    			time = actEvent.eventTime;
    			actState.treatEvent(actEvent);
    		}
    	
    		// Printing the result of the simulation, in this case a mean value
    		System.out.print("Mean nbr of customers in buffer, task " + i + ": " );
    		System.out.println(1.0*actState.totNbrJobs/actState.nbrSamples);
    		writer.close();
    	}
    }
}