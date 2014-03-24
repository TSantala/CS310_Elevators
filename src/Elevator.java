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
	
	public void run(){
			if(floorList.size()<=0) {
				try {
					synchronized(this) {
						this.wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				VisitFloor(getNextFloor());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/**
	 * Elevator control inferface: invoked by Elevator thread.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	/* Signal incoming and outgoing riders */
	public synchronized void OpenDoors() throws InterruptedException, IOException {
		
		System.out.println("Doors have opened!");
		writeLog("E" + elevatorId + " on F" + currentFloor+ "has opened");
		for(Rider r: riderList) {
			synchronized(r) {
				r.notify();
			}
		}
		synchronized(this) {
			if(numOn.get(currentFloor)>0 || numOff.get(currentFloor) > 0) {
				this.wait();
			}
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
	public synchronized void CloseDoors() throws IOException, InterruptedException {
		System.out.println("Doors have closed!");
		writeLog("E" + elevatorId + " on F" + currentFloor+ "has closed");
		if(floorList.size()<=0) {
			this.wait();
		}
		VisitFloor(getNextFloor());
	}

	/* Go to a requested floor */
	public synchronized void VisitFloor(int floor) throws InterruptedException, IOException {
		currentFloor = floor;
		System.out.println("Now visiting floor: "+currentFloor);
		this.OpenDoors();
	}


	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	/* Enter the elevator */
	public synchronized boolean Enter() throws IOException, InterruptedException {
		
		if(ridersOn==maxOccupancyThreshold) {
			numOn.set(currentFloor, 0);
			CloseDoors();
			return false;
		}
		else {
			ridersOn++;
			numOn.set(currentFloor, (numOn.get(currentFloor)-1));
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
	public synchronized void Exit() {
		numOff.set(currentFloor, numOff.get(currentFloor-1));
		if(numOff.get(currentFloor)==0 && numOn.get(currentFloor) ==0) {
			this.notify();
		}
		System.out.println("Rider has left the elevator!");
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
	
	public void writeLog(String message) throws IOException {
		synchronized(logfile) {
			logfile.write(message);
		}
	} 

}
