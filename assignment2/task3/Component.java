import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class Component extends Proc{
	Random slump = new Random();
	public Proc sendTo;
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case DEAD:{
				brokenNumber+=1;
				if (brokenNumber==5) {
					SignalList.SendSignal(READY, this, time);
					System.out.println(time-previousTime);
					previousTime=time;
				}
			} break;
			case READY:{
				SignalList.SendSignal(DEAD, this, time+1+4*slump.nextDouble());
			} break;
			
		}
	}
}