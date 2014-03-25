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

		if(myRequests.isEmpty()){
			System.out.println("Rider "+id+" has finished!");
			return;
		}

		Request next = myRequests.remove();
		this.setStartDest(next.getFrom(),next.getTo());

		//System.out.println("Running: id = "+id);

		Elevator e;
		if(to > from){
			this.writeLog("R"+id+" pushes "+"U"+from+"\n");
			e =	building.callUp(this);
		}
		else {
			this.writeLog("R"+id+" pushes "+"D"+from+"\n");
			e = building.callDown(this);
		}

		System.out.println("******* Elevator: "+e.getID()+" has been assigned to this rider! Rider ID = "+id);
		e.getFloor();
		System.out.println("******* beep again...");

		synchronized(e){
			while(e.getFloor() != from){				// || e.isGoingUp() != (to > from)){
				System.out.println("////////// I'd like to be notified...");
				try {
					e.wait();
					System.out.println("/////////// I've been notified!");

				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		if(!e.Enter(this)){
			System.out.println("***** Enter returned false, trying again...");
			this.run();
		}

		synchronized(e){
			while(e.getFloor() != to){
				try {
					e.wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		e.Exit(this);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		this.run();

	}

	public int getFrom(){
		return from;
	}

	public int getTo(){
		return to;
	}	
	
	public int getID(){
		return id;
	}

	public void addRequest(Request r) {
		myRequests.add(r);
	}

	public void writeLog(String message) {
		System.out.print(message);
		synchronized(logfile) {
			try {
				logfile.write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 

	//	public synchronized void safeWait(){
	//		try {
	//			this.wait();
	//		} catch (InterruptedException e) {
	//			e.printStackTrace();
	//		}
	//	}

}
