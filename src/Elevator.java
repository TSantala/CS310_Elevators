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
	
	public void RequestFloor(){
		
	}
	
	public boolean isGoingUp(){
		return isGoingUp;
	}
	
	public synchronized int getFloor(){
		return currentFloor;
	}

}
