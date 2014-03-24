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
	

	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold, BufferedWriter log) {
		this.numFloors = numFloors;
		this.elevatorId = elevatorId;
		this.maxOccupancyThreshold = maxOccupancyThreshold;
		this.riderList = new ArrayList<Rider>();
		this.floorList = new ArrayList<Integer>();
		logfile = log;
		this.numOn = new ArrayList<Integer>(numFloors+1);
		this.numOff = new ArrayList<Integer>(numFloors+1);
	}

	/**
	 * Elevator control inferface: invoked by Elevator thread.
	 */

	/* Signal incoming and outgoing riders */
	public synchronized void OpenDoors() {
		System.out.println("Doors have opened!");
		for(Rider r : riderList){
			r.notify();
		}
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

	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
	 */
	public synchronized void CloseDoors() {
		System.out.println("Doors have closed!");
	}

	/* Go to a requested floor */
	public synchronized void VisitFloor(int floor) {
		if(goingUp){
			currentFloor = floorList.remove(0);
		}
		else{
			currentFloor = floorList.remove(floorList.size()-1);
		}
		System.out.println("Now visiting floor: "+currentFloor);
		this.OpenDoors();
	}


	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
	 */

	/* Enter the elevator */
	public synchronized boolean Enter() {
		if(ridersOn==maxOccupancyThreshold) {
			System.out.println("Max occupancy... doors closing!");
			this.CloseDoors();
			return false;
		}
		ridersOn++;
		System.out.println("Rider has entered the elevator!");
		return true;
	}

	/* Exit the elevator */
	public synchronized void Exit() {
		ridersOn--;
		System.out.println("Rider has left the elevator!");
	}

	/* Request a destination floor once you enter */
	public synchronized void RequestFloor(int floor) {
		int numRidersOff = numOff.get(floor);
		numOff.set(floor, numRidersOff++);
		floorList.add(floor);
	}

	/* Other methods as needed goes here */

	public synchronized int getFloor(){
		return currentFloor;
	}

}
