package task2;

import java.io.IOException;
import java.util.*;

import eventSchedulingMethod.Event;

class State2 extends GlobalSimulation2{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int version = 0;
	public double xA = 0.002, xB = 0.004, lambda = 150, d = 1;
	public int nbrAinBuffer = 0, nbrBinBuffer = 0, nbrSamples = 0, totNbrJobs = 0;
	public boolean Qbusy = false;

	Random slump = new Random(); // This is just a random number generator
	
	//Function to pick a random number from an exponential distribution with 
	//mean mu 
	public double expRandom(double mu){
		double u = slump.nextDouble();
		return -1*mu*Math.log(1-u);
	}
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVALA:
				arrivalA();
				break;
			case DELAY:
				delay();
				break;
			case ARRIVALB:
				arrivalB();
				break;
			case READY:
				ready();
				break;
			case MEASURE:
				measure();
				break;
		}
	}
	
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	private void arrivalA(){	//arrival of a job of type A
		if(Qbusy){	//server is busy, the job has to wait in the buffer
			nbrAinBuffer++;
		}else{	//server is free (and buffer is empty), customer can be served directly
			Qbusy = true;
			insertEvent(DELAY, time + xA);
		}
		insertEvent(ARRIVALA, time + expRandom((1.0/lambda)));
		
	}
	
	private void delay(){	//customer of type A is served and is now placed in delay
		if(version == 1 || version == 3){
			insertEvent(ARRIVALB, time + d); //task 1 & 3
		}else{	
			insertEvent(ARRIVALB, time + expRandom(d)); //task 2
		}
		if(version == 1 || version == 2){//B has higher priority (task 1 & 2)
			if(nbrBinBuffer > 0){ //there are jobs of type B in buffer, serve them first
				insertEvent(READY, time + xB);
				nbrBinBuffer --;
			} 
			else if(nbrAinBuffer > 0){//no jobs of type B so jobs of type A can be served
				insertEvent(DELAY, time + xA);
				nbrAinBuffer --; 
			}else{	//buffer is empty
				Qbusy = false;
			}
		}else{ //A has higher priority (task 3)
			if(nbrAinBuffer >0){	//there are jobs of type A in buffer, serve them first
				insertEvent(DELAY, time + xA);
				nbrAinBuffer --;
			}
			else if(nbrBinBuffer >0){//no jobs of type A so jobs of type B can be served
				insertEvent(READY, time + xB);
				nbrBinBuffer --;
			}else{ //buffer is empty
				Qbusy = false;
			}

		}
	}
	private void arrivalB(){ //arrival of job of type B to the buffer
		if(Qbusy){ //server is busy, the job has to wait in the buffer
			nbrBinBuffer++;
		}else{ //server is free, the job 
			Qbusy = true;
			insertEvent(READY, time+xB);
		}
	}
	
	/* Work exactly as delay except the job done in server is of type B
	 * and thus leave the system instead of turn up in delay
	 */
	private void ready(){
		if(version == 1 || version == 2){
			if(nbrBinBuffer > 0){
				insertEvent(READY, time + xB);
				nbrBinBuffer --;
			} 
			else if(nbrAinBuffer > 0){
				insertEvent(DELAY, time + xA);
				nbrAinBuffer --; 
			}else{
				Qbusy = false;
			}
		}else{
			if(nbrAinBuffer >0){
				insertEvent(DELAY, time + xA);
				nbrAinBuffer --;
			}
			else if(nbrBinBuffer >0){
				insertEvent(READY, time + xB);
				nbrBinBuffer --;
			}else{
				Qbusy = false;
			}
		}
	}
	/* Measurement of the total number of jobs in the
	 * buffer since the simulation started. The current number
	 * of jobs is written to the output file.
	 */
	private void measure(){  
		totNbrJobs = totNbrJobs + nbrAinBuffer + nbrBinBuffer;
		nbrSamples++;
		insertEvent(MEASURE, time + 0.1);
		try {
			GlobalSimulation2.job_writer.write((nbrAinBuffer + nbrBinBuffer) + " ");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}