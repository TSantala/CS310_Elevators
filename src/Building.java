
public class Building {
	
	private int numFloors;
	private Elevator elevator;
	private EventBarrier barrier;
	
	public Building(int f){
		numFloors = f;
		elevator = new Elevator(0,0,0);
		barrier = new EventBarrier();
	}
	

	public Elevator callUp(Rider r){
		return elevator;
	}
	
	public Elevator callDown(Rider r){
		return elevator;
	}

}
