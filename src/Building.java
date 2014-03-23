
public class Building {
	
	private int numFloors;
	private Elevator elevator;
	private EventBarrier barrier;
	
	public Building(int f){
		numFloors = f;
		elevator = new Elevator();
		barrier = new EventBarrier();
	}
	
	public Elevator callUp(int from){
		return elevator;
	}
	
	public Elevator callDown(int from){
		return elevator;
	}

}
