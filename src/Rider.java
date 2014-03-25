import java.io.*;
import java.util.*;

public class Rider extends Thread{

	private Building building;
	private int from;
	private int to;
	private int id;
	private BufferedWriter logfile;
	private Queue<Request> myRequests = new LinkedList<Request>();

	int count = 0;


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
			this.writeLog("R"+id+" has finished!\n");
			building.checkCompletion();
			return;
		}

		Request next = myRequests.remove();
		this.setStartDest(next.getFrom(),next.getTo());

		Elevator e;
		if(to > from){
			this.writeLog("R"+id+" pushes "+"U"+from+"\n");
			e =	building.callUp(this);
		}
		else {
			this.writeLog("R"+id+" pushes "+"D"+from+"\n");
			e = building.callDown(this);
		}

		this.writeLog("R"+this.getID()+" has been assigned E"+e.getID()+"\n");

		synchronized(e){
			while((e.getFloor() != from) || !e.isOpen()){
				this.safeWait(e);
			}
		}

		if(!e.Enter(this)){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			this.addRequestToFront(new Request(id,from,to));
			this.run();
		}

		synchronized(e){
			while(e.getFloor() != to || !e.isOpen()){
				this.safeWait(e);
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

	public void addRequestToFront(Request r) {
		myRequests.add(r);
		for(int i = 0; i<myRequests.size()-1; i++)
			myRequests.add(myRequests.remove());
	}

	public void writeLog(String message) {
		System.out.print(message);
		synchronized(logfile) {
			try {
				logfile.write(message);
				logfile.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 

	private void safeWait(Object o){
		try {
			o.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
