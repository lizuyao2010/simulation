package task2;

import java.io.*;

import eventSchedulingMethod.Event;


public class MainSimulation2 extends GlobalSimulation2{
 
    public static void main(String[] args) throws IOException {
    	for(int i = 1; i <= 3; i++){	//three different cases for the task 1 - 3
    		switch(i){
    		case 1:
    			job_output = new FileOutputStream("task21.txt");
    			break;
    		case 2:
    			job_output = new FileOutputStream("task22.txt");
    			break;
    		case 3:
    			job_output = new FileOutputStream("task23.txt");
    			break;
    		}

    		job_writer = new BufferedWriter(new OutputStreamWriter(job_output, "utf-8"));
            
    		Event actEvent;
    		State2 actState = new State2(); // The state that should be used
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
    	
    		// Printing the result of the simulation, in this case a mean number of customers in the buffer
    		System.out.print("Mean number of jobs in buffer, task " + i + ": " );
    		System.out.println(1.0*actState.totNbrJobs/actState.nbrSamples);
    		job_writer.close();
    	}
    }
}