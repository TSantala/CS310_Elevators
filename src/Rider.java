import java.io.*;
public class Rider extends Thread{
	
	private Building building;
	private int from;
	private int to;
	private int id;
	private BufferedWriter logfile;
	
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
		System.out.println("Elevator has been assigned to this rider! = "+id);
		
		while(e.getFloor() != from || e.isGoingUp() != (to > from)){
			this.safeWait();
		}
		
		e.Enter();
		e.RequestFloor(to);

		while(e.getFloor() != to){
			this.safeWait();
		}
		e.Exit();
	}
	
	public int getFrom(){
		return from;
	}
	
	public int getTo(){
		return to;
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
