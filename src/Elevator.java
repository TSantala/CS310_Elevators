import java.util.PriorityQueue;

public class Elevator {
	
	private EventBarrier barrier;
	private PriorityQueue<Integer> floorList;
	private int currentFloor;
	private boolean isGoingUp = false;
	private int maxCapacity;
	
	public Elevator(int max){
		barrier = new EventBarrier();
		floorList = new PriorityQueue<Integer>();
		maxCapacity = max;
	}
	
	public synchronized void Enter(){
		
	}
	
	public synchronized void Exit(){
		
	}
	
	public void RequestFloor(int floor){
		
	}
	
	public boolean isGoingUp(){
		return isGoingUp;
	}
	
	public synchronized int getFloor(){
		return currentFloor;
	}

}
