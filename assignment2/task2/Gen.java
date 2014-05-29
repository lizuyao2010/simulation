import java.util.*;
import java.io.*;

//It inherits Proc so that we can use time and the signal names without dot notation 

class Gen extends Proc{

	//The random number generator is started:
	Random slump = new Random();

	//There are two parameters:
	public Proc sendTo;   //Where to send customers
	public double lambda;   //How many to generate per hour
	public double expRandom(double mu){
		double u = slump.nextDouble();
		return -1*mu*Math.log(1-u);
	}


	//What to do when a signal arrives
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				if (time%24>=9 && time%24<=17){
					SignalList.SendSignal(ARRIVAL, sendTo, time);
					numberofcustomers++;
				}
				SignalList.SendSignal(READY, this, time + expRandom(1.0/lambda));
			}
				break;
		}
	}
}