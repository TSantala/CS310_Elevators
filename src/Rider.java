import java.io.*;
import java.util.*;

public class Rider extends Thread{

	private Building building;
	private int from;
	private int to;
	private int id;
	private BufferedWriter logfile;
	private Queue<Request> myRequests = new LinkedList<Request>();

	public Rider(int floorFrom, int floorTo, Building b, int i, BufferedWriter log){
		building = b;
		from = floorFrom;
		to = floorTo;
		id = i;
		logfile = log;
	}

	public Rider(Building b, int i, BufferedWriter log) {
		building = b;
		id = i;
		logfile = log;
	}

	public void setStartDest(int start, int dest) {
		from = start;
		to = dest;
	}

	@Override
	public synchronized void run(){

		System.out.println("Running: id = "+id);

		Elevator e = (to > from) ? building.callUp(this) : building.callDown(this);
		System.out.println("Elevator: "+e.getID()+" has been assigned to this rider! Rider ID = "+id);

		while(e.getFloor() != from || e.isGoingUp() != (to > from)){
			this.safeWait();
		}

		System.out.println("Rider "+id+" is about to call Enter!");

		e.Enter(to);

		while(e.getFloor() != to){
			this.safeWait();
		}

		e.Exit();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		if(!myRequests.isEmpty()){
			Request next = myRequests.remove();
			this.setStartDest(next.getFrom(),next.getTo());
			this.run();
		}

		System.out.println("Rider "+id+" has finished!");

	}

	public int getFrom(){
		return from;
	}

	public int getTo(){
		return to;
	}	

	public void addRequest(Request r) {
		myRequests.add(r);
	}

	public void writeLog(String message) throws IOException {
		synchronized(logfile) {
			logfile.write(message);
		}
	} 

	public void safeWait(){
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
