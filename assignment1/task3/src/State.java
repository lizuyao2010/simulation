import java.util.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public double interArrivalTime = 1.1; //interarrival time for Q1 
	public int nbrInQ1 = 0, nbrInQ2 = 0, totNbrInQ = 0,  nbrMeasurements = 0, 
			nbrCustomers = 0;
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
	
	private void arrival(){
		nbrCustomers++;
		
		if(nbrInQ1 == 0 && !Q1busy){
			insertEvent(QUEUECHANGE, time + expRandom(1));
			Q1busy = true;
		}else {
			nbrInQ1++;
		}
		insertEvent(ARRIVAL, time + interArrivalTime);
	}
	
	private void queueChange(){
		if(nbrInQ1 > 0){
			nbrInQ1 --;
			insertEvent(QUEUECHANGE, time + expRandom(1)); 			
		}else{
			Q1busy = false;
		}
		if(Q2busy){
			nbrInQ2++;
		} else{
			insertEvent(READY, time + expRandom(1));
			Q2busy = true;
		}
	}
	private void ready(){
		if (nbrInQ2 > 0){
			nbrInQ2 --;
			insertEvent(READY, time + expRandom(1));
		}else{
			Q2busy = false;
		}
	}
	
	private void measure(){
		totNbrInQ = totNbrInQ + nbrInQ1 +  nbrInQ2; //r�kna med den som �r servern?
		nbrMeasurements++;
		insertEvent(MEASURE, time + expRandom(5));
	}
}