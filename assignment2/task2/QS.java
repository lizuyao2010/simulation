import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc{
	public int numberInQueue = 0, acc_arrival=0,acc_leave=0;
	public Proc sendTo;
	Random slump = new Random();

	public void TreatSignal(Signal x){
		switch (x.signalType){

			case ARRIVAL:{
				numberInQueue++;
                System.out.println("arrival:"+time%24);
				acc_arrival+=time%24;
				if (numberInQueue == 1){
					SignalList.SendSignal(READY,this, time + 1.0/6+1.0/6*slump.nextDouble());
				}
			} break;

			case READY:{
				if (numberInQueue>0){
					numberInQueue--;
                    System.out.println("leave:"+time%24);
					acc_leave+=time%24;
				}
				
				/*
				if (sendTo != null){
					SignalList.SendSignal(ARRIVAL, sendTo, time);
				}
				*/
				if (numberInQueue > 0){
					double dealtime=1.0/6+1.0/6*slump.nextDouble();
					SignalList.SendSignal(READY, this, time + dealtime);
				}
				if (numberInQueue == 0 && time%24>17) {
					System.out.println(time%24);
					//accumulated += (time%24) - 9;
				}
			} break;
			/*
			case MEASURE:{
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + 2*slump.nextDouble());
			} break;
			*/
		}
	}
}