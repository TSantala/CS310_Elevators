
public class Building {
	
	private int numFloors;
	private Elevator elevator;
	private EventBarrier barrier;
	
	public Building(int f){
		numFloors = f;
		elevator = new Elevator();
		barrier = new EventBarrier();
	}
	
<<<<<<< HEAD
	public Elevator callUp(){
		return elevator;
	}
	
	public Elevator callDown(){
=======
	public Elevator callUp(int from){
		return elevator;
	}
	
	public Elevator callDown(int from){
>>>>>>> af44cbe5c038e4b1b4ef5c05fa3029362fea40c9
		return elevator;
	}

}
