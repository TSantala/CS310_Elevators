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
					System.out.println("******* Now in transit!");
					inTransit = true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		VisitFloor(getNextFloor());

	}

	/**
	 * Elevator control interface: invoked by Elevator thread.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	/* Signal incoming and outgoing riders */
	public synchronized void OpenDoors() {

		writeLog("E" + elevatorId + " on F" + currentFloor+ " has opened\n");
		this.notifyAll();
		System.out.println("****** Waiting to get on = "+numGettingOn.get(currentFloor));
		while(numGettingOn.get(currentFloor)>0 || numGettingOff.get(currentFloor) > 0) {
			// waiting...
		}
		CloseDoors();

	}

	public synchronized boolean isGoingUp() {
		return goingUp;
	}

	public synchronized void addRequest(Rider newRider) {
		int floor = newRider.getFrom();
		int numRidersOn = numGettingOn.get(floor);
		numGettingOn.set(floor, ++numRidersOn);
		if(!floorList.contains(floor)) {
			floorList.add(floor);
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

	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public synchronized void CloseDoors() {

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
		VisitFloor(getNextFloor());
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
	public synchronized boolean Enter(int floor) {
		System.out.println("ENTER WAS CALLED!");
		if(ridersOn==maxOccupancyThreshold) {
			numGettingOn.set(currentFloor, 0);
			CloseDoors();
			return false;
		}
		else {
			ridersOn++;
			numGettingOn.set(currentFloor, (numGettingOn.get(currentFloor)-1));

			RequestFloor(floor);
		}
		if(numGettingOff.get(currentFloor)==0 && numGettingOn.get(currentFloor) == 0) {
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
		numGettingOff.set(currentFloor, numGettingOff.get(currentFloor-1));
		if(numGettingOff.get(currentFloor)==0 && numGettingOn.get(currentFloor) ==0) {
			this.notify();
		}
		System.out.println("Rider has left the elevator!");
		return currentFloor;
	}

	/* Request a destination floor once you enter */
	public synchronized void RequestFloor(int floor) {
		int numRidersOff = numGettingOff.get(floor);
		numGettingOff.set(floor, numRidersOff++);
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
		System.out.print(message);
		synchronized(logfile) {
			try {
				logfile.write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 

//	private void safeWait(){
//		try {
//			this.wait();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

}
