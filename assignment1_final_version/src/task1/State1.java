package task1;

import java.util.*;

import eventSchedulingMethod.Event;

class State1 extends GlobalSimulation1{
	
	/* Here follows the state variables and other variables that might be needed
	 e.g. for measurements*/
	public int interArrivalTime = 5; //interarrival time for Q1, to be changed between 1, 2 and 5 
	public int nbrInQ1 = 0, nbrInQ2 = 0, totNbrInQ2 = 0,  nbrMeasurements = 0, 
			nbrRejected = 0, nbrCustomers = 0;
	public boolean Q1busy = false, Q2busy = false;

	Random slump = new Random(); // This is just a random number generator
	
	/*Function to pick a random number from an exponential distribution with 
	mean mu*/ 
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
		nbrCustomers++;
		if(nbrInQ1 >= 10){ //Q1 is full
			nbrRejected++;
		}
		else if(nbrInQ1 == 0 && !Q1busy){ //server 1 is free
			insertEvent(QUEUECHANGE, time + expRandom(2.1));
			Q1busy = true;
		}else { //server 1 is busy, customer have to stay in queue
			nbrInQ1++;
		}
		insertEvent(ARRIVAL, time + interArrivalTime); //schedule new arrival to Q1
	}
	
	private void queueChange(){ //customer done in server 1 and continues to Q2
		if(nbrInQ1 > 0){	//another customer waiting in Q1 can now get served
			nbrInQ1 --;
			insertEvent(QUEUECHANGE, time + expRandom(2.1)); 			
		}else{
			Q1busy = false;
		}
		if(Q2busy){ //server 2 is busy, customer has to stay in queue
			nbrInQ2++;
		} else{ 	//server 2 is free
			insertEvent(READY, time + 2);
			Q2busy = true;
		}
	}
	private void ready(){ //customer done in server 2 and can leave the system
		if (nbrInQ2 > 0){ //another customer waiting in Q2 can now get served
			nbrInQ2 --;
			insertEvent(READY, time + 2);
		}else{
			Q2busy = false;
		}
	}
	
	private void measure(){ //measurement of number in queue + server in Q2 since start of the simulation
		totNbrInQ2 += nbrInQ2;
		if(Q2busy){
			totNbrInQ2++;
		}
		nbrMeasurements++;
		insertEvent(MEASURE, time + expRandom(5));
	}
}