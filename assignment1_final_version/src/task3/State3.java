package task3;

import java.util.*;

import eventSchedulingMethod.Event;

class State3 extends task1.GlobalSimulation1{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public double interArrivalTime = 2; //interarrival time for Q1, to be changed between 2, 1.5 and 1.1 
	public int nbrInQ1 = 0, nbrInQ2 = 0, totNbrInQ = 0, nbrOfArrivals = 0,  nbrMeasurements = 0;
	public double  totTimeInSystem = 0, lastTime = 0; //last time an arrival or departure occurred
	public boolean Q1busy = false, Q2busy = false;

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
			case ARRIVAL:
				arrival();
				break;
			case QUEUECHANGE:
				queueChange();
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
	
	private void arrival(){ //arrival to Q1
		totTimeInSystem += (time-lastTime)*nbrCustomersInSystem();
		lastTime = time;
		nbrOfArrivals++;
		if(nbrInQ1 == 0 && !Q1busy){ //server 1 is free, customer can be served directly
			insertEvent(QUEUECHANGE, time + expRandom(1));
			Q1busy = true;
		}else { //server 1 is busy, customer has to wait in buffer 1
			nbrInQ1++;
		}
		insertEvent(ARRIVAL, time + interArrivalTime);
	}
	
	private void queueChange(){ //customer is done in server 1 and continues to Q2
		if(nbrInQ1 > 0){//another customer was waiting for and can now use server 1
			nbrInQ1 --;
			insertEvent(QUEUECHANGE, time + expRandom(1)); 			
		}else{
			Q1busy = false;
		}
		if(Q2busy){ //server 2 is busy, customer has to wait in buffer 2
			nbrInQ2++;
		} else{ //server 2 is free (and buffer 2 empty), customer can be served directly
			insertEvent(READY, time + expRandom(1));
			Q2busy = true;
		}
	}
	private void ready(){ //customer is done in server 2 and leaves the system
		totTimeInSystem += (time-lastTime)*nbrCustomersInSystem();
		lastTime = time;
		
		if (nbrInQ2 > 0){ //another customer was waiting for and can now use server 2
			nbrInQ2 --;
			insertEvent(READY, time + expRandom(1));
		}else{
			Q2busy = false;
		}
	}
	
	/* Measurement of total number of customers in the entire
	 * system since the simulation started
	 */
	private void measure(){ 
		totNbrInQ += nbrCustomersInSystem();
		nbrMeasurements++;
		insertEvent(MEASURE, time + expRandom(5));
	}
	/* Calculate the current number of customers in the 
	 * entire system
	 */
	private int nbrCustomersInSystem(){
		int nbrCustomersInSystem = nbrInQ1 + nbrInQ2;
		if(Q1busy){
			nbrCustomersInSystem++;
		}
		if(Q2busy){
			nbrCustomersInSystem++;
		}
		return nbrCustomersInSystem;
	}
}