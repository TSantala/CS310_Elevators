import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;


public class Elevator extends Thread{

	protected int numFloors; 
	protected int elevatorId;
	protected int maxOccupancyThreshold;
	//private EventBarrier barrier;
	private List<Rider> riderList;
	private List<Integer> floorList;
	private List<Integer> numOn;
	private List<Integer> numOff;
	private int currentFloor;
	private boolean goingUp;
	private int ridersOn;
	private BufferedWriter logfile;
	private boolean inTransit;


	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold, BufferedWriter log) {
		this.numFloors = numFloors;
		this.elevatorId = elevatorId;
		this.maxOccupancyThreshold = maxOccupancyThreshold;
		this.riderList = new ArrayList<Rider>();
		this.floorList = new ArrayList<Integer>();
		logfile = log;
		this.numOn = new ArrayList<Integer>();
		this.numOff = new ArrayList<Integer>();
		for(int i = 0; i <= numFloors+1; i++){
			numOn.add(0);
			numOff.add(0);
		}
		inTransit = false;
		currentFloor = 1;
	}
	
	public void run(){
			if(floorList.size()<=0) {
				try {
					synchronized(this) {
						inTransit = false;
						this.wait();
						inTransit = true;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			VisitFloor(getNextFloor());

	}

	/**
	 * Elevator control inferface: invoked by Elevator thread.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	/* Signal incoming and outgoing riders */
	public synchronized void OpenDoors() {

		System.out.println("Doors have opened!");
		writeLog("E" + elevatorId + " on F" + currentFloor+ "has opened");
		for(Rider r: riderList) {
			synchronized(r) {
				r.notify();
			}
		}
		if(numOn.get(currentFloor)>0 || numOff.get(currentFloor) > 0) {
			this.safeWait();
		}
		CloseDoors();

	}

	public synchronized boolean isGoingUp() {
		return goingUp;
	}

	public synchronized void addRequest(Rider newRider) {
		riderList.add(newRider);
		int floor = newRider.getFrom();
		int numRidersOn = numOn.get(floor);
		numOn.set(floor, numRidersOn++);
		if(!floorList.contains(floor)) {
			floorList.add(floor);
			Collections.sort(floorList);
		}
	}

	private int getNextFloor() {
		if(goingUp) {
			return floorList.remove(0);
		}
		else {
			return floorList.remove(floorList.size()-1);
		}
	}

	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public synchronized void CloseDoors() {
		System.out.println("Doors have closed!");
		writeLog("E" + elevatorId + " on F" + currentFloor+ "has closed");
		if(floorList.size()<=0) {
			try {
				inTransit = false;
				this.wait();
				inTransit = true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		VisitFloor(getNextFloor());
	}

	/* Go to a requested floor */
	public synchronized void VisitFloor(int floor) {
		String direction;
		int printfloor = currentFloor;
		if(floor>currentFloor){
			goingUp = true;
			direction = "up";
			while(printfloor<=floor){
				writeLog("E" + elevatorId + " moves " + direction + " to F" + printfloor);
				printfloor++;
			}
		}
		else {
			goingUp = false;
			direction = "down";
			while(printfloor>=floor){
				writeLog("E" + elevatorId + " moves " + direction + " to F" + printfloor);
				printfloor--;
			}
		}
		
		currentFloor = floor;
		this.OpenDoors();
	}


	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	/* Enter the elevator */
	public synchronized boolean Enter(int floor) {

		if(ridersOn==maxOccupancyThreshold) {
			numOn.set(currentFloor, 0);
			CloseDoors();
			return false;
		}
		else {
			ridersOn++;
			System.out.println("Set = "+ currentFloor + " numOn size = "+numOn.size());
			numOn.set(currentFloor, (numOn.get(currentFloor)-1));
			RequestFloor(floor);
		}
		if(numOff.get(currentFloor)==0 && numOn.get(currentFloor) ==0) {
			this.notify();
		}
		return true;
		/*if(ridersOn==maxOccupancyThreshold) {
			System.out.println("Max occupancy... doors closing!");
			this.CloseDoors();
			return false;
		}
		ridersOn++;
		System.out.println("Rider has entered the elevator!");
		return true;*/
	}

	/* Exit the elevator */
	public synchronized int Exit() {
		numOff.set(currentFloor, numOff.get(currentFloor-1));
		if(numOff.get(currentFloor)==0 && numOn.get(currentFloor) ==0) {
			this.notify();
		}
		System.out.println("Rider has left the elevator!");
		return currentFloor;
	}

	/* Request a destination floor once you enter */
	public synchronized void RequestFloor(int floor) {
		int numRidersOff = numOff.get(floor);
		numOff.set(floor, numRidersOff++);
		if(!floorList.contains(floor)) {
			floorList.add(floor);
		}
	}

	/* Other methods as needed goes here */

	public synchronized int getFloor(){
		return currentFloor;
	}

	public synchronized int getID(){
		return elevatorId;
	}

	public synchronized boolean isInTransit() {
		return inTransit;
	}

	public void writeLog(String message) {
		System.out.println(message);
		synchronized(logfile) {
			try {
				logfile.write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 

	private void safeWait(){
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
