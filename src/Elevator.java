<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;


public class Elevator extends Thread{

	protected int numFloors; 
	protected int elevatorId;
	protected int maxOccupancyThreshold;
	private EventBarrier barrier;
	private List<Rider> riderList;
	private List<Integer> floorList;
	private List<Integer> numOn;
	private List<Integer> numOff;
	private int currentFloor;
	private boolean goingUp;
	private int ridersOn;
	
	

	
	/**
	 * Other variables/data structures as needed goes here 
	 */

	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		this.numFloors = numFloors;
		this.elevatorId = elevatorId;
		this.maxOccupancyThreshold = maxOccupancyThreshold;
		this.riderList = new ArrayList<Rider>();
		this.floorList = new ArrayList<Integer>();
		this.numOn = new ArrayList<Integer>(numFloors+1);
		this.numOff = new ArrayList<Integer>(numFloors+1);
	}

	/**
	 * Elevator control inferface: invoked by Elevator thread.
	 */

	/* Signal incoming and outgoing riders */
	public void OpenDoors() { 	
		
	}
	
	public boolean isGoingUp() {
		return goingUp;
	}
	
	public void addRequest(Rider newRider) {
		riderList.add(newRider);
		int floor = newRider.getFrom()
		int numRidersOn = numOn.get(floor);
		numOn.set(floor, numRidersOn++);
		if(!floorList.contains(floor)) {
			floorList.add(floor);
		}
	}

	/**
	 * When capacity is reached or the outgoing riders are exited and
	 * incoming riders are in. 
	 */
	public void ClosedDoors() {
		
	}

	/* Go to a requested floor */
	public void VisitFloor(int floor) {
		
	}


	/**
	 * Elevator rider interface (part 1): invoked by rider threads. 
	 */

	/* Enter the elevator */
	public synchronized boolean Enter() {
		if(ridersOn==maxOccupancyThreshold) {
			ClosedDoors();
			return false;
		}
		
		return true;
	}

	/* Exit the elevator */
	public void Exit() {
		
	}

	/* Request a destination floor once you enter */
	public void RequestFloor(int floor) {
		int numRidersOff = numOff.get(floor);
		numOff.set(floor, numRidersOff++);
	}

	/* Other methods as needed goes here */


	

=======
import java.util.PriorityQueue;

public class Elevator {
	
	private EventBarrier barrier;
	private PriorityQueue<Integer> floorList;
	private int currentFloor;
	private boolean isGoingUp = false;
	
	public Elevator(){
		barrier = new EventBarrier();
		floorList = new PriorityQueue<Integer>();
	}
	
	public synchronized void Enter(){
		
	}
	
	public synchronized void Exit(){
		
	}
>>>>>>> af44cbe5c038e4b1b4ef5c05fa3029362fea40c9
	
	public void RequestFloor(){
		
	}
	
<<<<<<< HEAD
=======
	public boolean isGoingUp(){
		return isGoingUp;
	}
	
>>>>>>> af44cbe5c038e4b1b4ef5c05fa3029362fea40c9
	public synchronized int getFloor(){
		return currentFloor;
	}

}
