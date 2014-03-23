
public class Building {
	
	private int numFloors;
	private Elevator elevator;
	private EventBarrier barrier;
	
	public Building(int f, int max){
		numFloors = f;
		elevator = new Elevator(max);
		barrier = new EventBarrier();
	}
	
	public Elevator callUp(Rider r){
		return elevator;
	}
	
	public Elevator callDown(Rider r){
		return elevator;
	}

}
