import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;


public class Elevator extends Thread{

	//private int numFloors; 
	private int elevatorId;
	private int maxOccupancyThreshold;
	//private EventBarrier barrier;
	private List<Integer> floorList;
	private List<Integer> numGettingOn;
	private List<Integer> numGettingOff;
	private int currentFloor;
	private boolean goingUp;
	private int ridersOn;
	private BufferedWriter logfile;
	private boolean inTransit;
	private boolean isOpen;


	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold, BufferedWriter log) {
		//this.numFloors = numFloors;
		this.elevatorId = elevatorId;
		this.maxOccupancyThreshold = maxOccupancyThreshold;
		this.floorList = new ArrayList<Integer>();
		logfile = log;
		this.numGettingOn = new ArrayList<Integer>();
		this.numGettingOff = new ArrayList<Integer>();
		for(int i = 0; i <= numFloors+1; i++){
			numGettingOn.add(0);
			numGettingOff.add(0);
		}
		inTransit = false;
		isOpen = false;
		currentFloor = 1;
		ridersOn = 0;
	}

	public void run(){

		if(floorList.size()==0) {
			try {
				synchronized(this) {
					inTransit = false;
					this.wait();
					System.out.println("******* E"+ this.getID()+" now in transit!");
					inTransit = true;
				}
			} catch (InterruptedException e) {
				return;
			}
		}

		this.VisitFloor(getNextFloor());

	}

	
	/* Signal incoming and outgoing riders */
	public synchronized void OpenDoors() {
		isOpen = true;
		writeLog("E" + elevatorId + " on F" + currentFloor+ " has opened\n");
		synchronized(this){
			this.notifyAll();
		}
		System.out.println("****** Waiting to get on = "+numGettingOn.get(currentFloor) + " Waiting to get off = "+numGettingOff.get(currentFloor));
		while(numGettingOn.get(currentFloor)>0 || numGettingOff.get(currentFloor) > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		this.CloseDoors();
	}


	public synchronized void CloseDoors() {
		isOpen = false;
		writeLog("E" + elevatorId + " on F" + currentFloor+ " has closed\n");
		this.run();
	}

	public synchronized boolean isGoingUp() {
		return goingUp;
	}

	public synchronized void addRequest(Rider newRider) {
		int floorFrom = newRider.getFrom();
		int numRidersOn = numGettingOn.get(floorFrom);
		numGettingOn.set(floorFrom, ++numRidersOn);
		if(!floorList.contains(floorFrom)) {
			floorList.add(floorFrom);
			Collections.sort(floorList);
		}
		this.notifyAll();
	}

	private int getNextFloor() {
		if(goingUp) {
			return floorList.get(0);
		}
		else {
			return floorList.get(floorList.size()-1);
		}
	}

	/* Go to a requested floor */
	public synchronized void VisitFloor(int floor) {
		if(floor>currentFloor){
			goingUp = true;
			while(currentFloor<floor){
				currentFloor++;
				writeLog("E" + elevatorId + " moves up to F" + currentFloor + "\n");
				floor = this.getNextFloor();	// In case pick-up on the way.
			}
		}
		else {
			goingUp = false;
			while(currentFloor>floor){
				currentFloor--;
				writeLog("E" + elevatorId + " moves down to F" + currentFloor + "\n");
				floor = this.getNextFloor();	// In case pick-up on the way.
			}
		}
		currentFloor = floor;
		floorList.remove((Integer) currentFloor);
		this.OpenDoors();
	}



	/* Enter the elevator */
	public synchronized boolean Enter(Rider r) {
		//System.out.println("Start of enter: ridersOn = "+ridersOn+" max occ = "+maxOccupancyThreshold);
		if(this.atMaxCapacity()) {
			numGettingOn.set(currentFloor, 0);
			this.writeLog("At max capacity! R"+r.getID()+" cannot enter.");
			this.notifyAll();
			return false;
		}
		else {
			ridersOn++;
			numGettingOn.set(currentFloor, (numGettingOn.get(currentFloor)-1));
			synchronized(r){
				this.RequestFloor(r.getTo());
			}
		}
		if(numGettingOff.get(currentFloor)==0 && numGettingOn.get(currentFloor) == 0) {
			this.notifyAll();
		}
		this.writeLog("R"+r.getID()+" enters "+"E"+this.getID()+" on F"+currentFloor+"\n");
		return true;
	}

	/* Exit the elevator */
	public synchronized void Exit(Rider r) {
		this.writeLog("R"+r.getID()+" exits "+"E"+this.getID()+" on F"+currentFloor+"\n");
		ridersOn--;
		numGettingOff.set(currentFloor, numGettingOff.get(currentFloor)-1);
		if(numGettingOff.get(currentFloor)==0 && numGettingOn.get(currentFloor) ==0) {
			this.notifyAll();
		}
	}

	/* Request a destination floor once you enter */
	public synchronized void RequestFloor(int floor) {
		int numRidersOff = numGettingOff.get(floor);
		numGettingOff.set(floor, ++numRidersOff);
		if(!floorList.contains(floor)) {
			floorList.add(floor);
			Collections.sort(floorList);
		}
	}

	/* Other methods as needed goes here */

	public int getFloor(){
		return currentFloor;
	}

	public int getID(){
		return elevatorId;
	}

	public boolean isInTransit() {
		return inTransit;
	}
	
	public boolean isOpen(){
		return isOpen;
	}
	
	public boolean atMaxCapacity(){
		return ridersOn == maxOccupancyThreshold;
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

}
