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
		currentFloor = 1;
	}

	public void run(){

		if(floorList.size()<=0) {
			try {
				synchronized(this) {
					inTransit = false;
					this.wait();
					System.out.println("******* E"+ this.getID()+" now in transit!");
					inTransit = true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		this.VisitFloor(getNextFloor());

	}

	/**
	 * Elevator control interface: invoked by Elevator thread.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	/* Signal incoming and outgoing riders */
	public void OpenDoors() {

		writeLog("E" + elevatorId + " on F" + currentFloor+ " has opened\n");
		synchronized(this){
			this.notifyAll();
		}
		System.out.println("****** Waiting to get on = "+numGettingOn.get(currentFloor) + " Waiting to get off = "+numGettingOff.get(currentFloor));
		while(numGettingOn.get(currentFloor)>0 || numGettingOff.get(currentFloor) > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.CloseDoors();
	}

	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void CloseDoors() {

		writeLog("E" + elevatorId + " on F" + currentFloor+ " has closed\n");
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
		this.VisitFloor(getNextFloor());
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
			return floorList.remove(0);
		}
		else {
			return floorList.remove(floorList.size()-1);
		}
	}

	/* Go to a requested floor */
	public synchronized void VisitFloor(int floor) {
		String direction;
		int printfloor = currentFloor;
		if(floor>currentFloor){
			printfloor++;
			goingUp = true;
			direction = "up";
			while(printfloor<=floor){
				writeLog("E" + elevatorId + " moves " + direction + " to F" + printfloor + "\n");
				printfloor++;
			}
		}
		else {
			printfloor--;
			goingUp = false;
			direction = "down";
			while(printfloor>=floor){
				writeLog("E" + elevatorId + " moves " + direction + " to F" + printfloor + "\n");
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
	public synchronized boolean Enter(Rider r) {

		this.writeLog("R"+r.getID()+" enters "+"E"+this.getID()+" on F"+currentFloor+"\n");

		if(ridersOn==maxOccupancyThreshold) {
			numGettingOn.set(currentFloor, 0);
			this.CloseDoors();
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
		return true;
	}

	/* Exit the elevator */
	public synchronized void Exit(Rider r) {
		this.writeLog("R"+r.getID()+" exits "+"E"+this.getID()+" on F"+currentFloor+"\n");
		numGettingOff.set(currentFloor, numGettingOff.get(currentFloor-1));
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

}
