
public class Rider extends Thread{
	
	private Building building;
	private int from;
	private int to;
	private int id;
	
	public Rider(int floorFrom, int floorTo, Building b, int i){
		building = b;
		from = floorFrom;
		to = floorTo;
		id = i;
	}
	
	@Override
	public synchronized void run(){
		System.out.println("Running: id = "+id);
		Elevator e = (to > from) ? building.callUp() : building.callDown();
		System.out.println("Elevator has been assigned to this rider! = "+id);
		
		while(e.getFloor() != to){
			try {
				this.wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		e.Enter();
	}

}
