import java.io.IOException;
import java.util.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int version = 0;
	public double xA = 0.002, xB = 0.004, lambda = 150, d = 1;
	public int nbrAinQ = 0, nbrBinQ = 0, nbrSamples = 0, totNbrJobs = 0, testerA = 0, testerB = 0;
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
	
	private void arrivalA(){
		testerA++;
		if(Qbusy){
			nbrAinQ++;
		}else{
			Qbusy = true;
			insertEvent(DELAY, time + xA);
		}
		insertEvent(ARRIVALA, time + expRandom((1.0/lambda)));
		
	}
	
	private void delay(){
		if(version == 1 || version == 3){
			insertEvent(ARRIVALB, time + d); //task 1 & 3
		}else{	
			insertEvent(ARRIVALB, time + expRandom(d)); //task 2
		}
		if(version == 1 || version == 2){//B has higher priority
			if(nbrBinQ > 0){
				insertEvent(READY, time + xB);
				nbrBinQ --;
			} 
			else if(nbrAinQ > 0){
				insertEvent(DELAY, time + xA);
				nbrAinQ --; 
			}else{
				Qbusy = false;
			}
		}else{ //A has higher priority
			if(nbrAinQ >0){
				insertEvent(DELAY, time + xA);
				nbrAinQ --;
			}
			else if(nbrBinQ >0){
				insertEvent(READY, time + xB);
				nbrBinQ --;
			}else{
				Qbusy = false;
			}

		}
	}
	private void arrivalB(){
		testerB++;
		if(Qbusy){
			nbrBinQ++;
		}else{
			Qbusy = true;
			insertEvent(READY, time+xB);
		}
	}
	private void ready(){
		if(version == 1 || version == 2){
			if(nbrBinQ > 0){
				insertEvent(READY, time + xB);
				nbrBinQ --;
			} 
			else if(nbrAinQ > 0){
				insertEvent(DELAY, time + xA);
				nbrAinQ --; 
			}else{
				Qbusy = false;
			}
		}else{
			if(nbrAinQ >0){
				insertEvent(DELAY, time + xA);
				nbrAinQ --;
			}
			else if(nbrBinQ >0){
				insertEvent(READY, time + xB);
				nbrBinQ --;
			}else{
				Qbusy = false;
			}
		}
	}
	
	private void measure(){
		totNbrJobs = totNbrJobs + nbrAinQ + nbrBinQ;
		nbrSamples++;
		insertEvent(MEASURE, time + 0.1);
		try {
			GlobalSimulation.writer.write(totNbrJobs + " ");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}