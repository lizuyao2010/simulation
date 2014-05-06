import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that should be used
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL, 0);  
        insertEvent(MEASURE, actState.T);
        
        //Outfile
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