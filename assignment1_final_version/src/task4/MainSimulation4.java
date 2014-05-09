package task4;

import java.io.*;

import eventSchedulingMethod.Event;


public class MainSimulation4 extends GlobalSimulation4{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State4 actState = new State4(); // The state that should be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL, 0);  
        insertEvent(MEASURE, actState.T);
        
        output = new FileOutputStream("task4.txt");
        writer = new BufferedWriter(new OutputStreamWriter(output, "utf-8"));            
        
        // The main simulation loop
    	while (actState.nbrMeasurements < actState.M){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
    	writer.close();
    }
}