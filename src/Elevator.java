import java.util.ArrayList;
import java.util.List;


public class Elevator {
	
	private EventBarrier barrier;
	private List<Integer> floorList;
	private int currentFloor;
	
	public Elevator(){
		barrier = new EventBarrier();
		floorList = new ArrayList<Integer>();
	}
	
	public void Enter(){
		
	}
	
	public void Exit(){
		
	}
	
	public void RequestFloor(){
		
	}
	
	public synchronized int getFloor(){
		return currentFloor;
	}

}
