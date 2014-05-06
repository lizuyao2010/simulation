import java.io.IOException;
import java.util.*;

class State extends GlobalSimulation{
	
	/* Here follows the state variables and other variables that might be needed
	 e.g. for measurements*/ 
	public int lambda = 8, x = 100, T = 1, M = 1000, N = 1000, nbrBusyServers = 0, nbrMeasurements = 0;

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
		if(nbrBusyServers < N){ //otherwise rejected
			nbrBusyServers++;
			insertEvent(READY, time + x);
		}
		insertEvent(ARRIVAL, time + expRandom(1.0/lambda));
	}
	
	private void ready(){
		nbrBusyServers --;
	}
	
	private void measure(){
		nbrMeasurements++;
		try {
			GlobalSimulation.writer.write(nbrBusyServers + " ");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		insertEvent(MEASURE, time + T);
	}
}