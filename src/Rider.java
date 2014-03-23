
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
		Elevator e = (to > from) ? building.callUp(from) : building.callDown(from);
		System.out.println("Elevator has been assigned to this rider! = "+id);
		
		while(e.getFloor() != from || e.isGoingUp() != (to > from)){
			try {
				this.wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		e.Enter();
		while(e.getFloor() != to){
			try {
				this.wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		e.Exit();
	}
	
	public int getFrom(){
		return from;
	}
	
	public int getTo(){
		return to;
	}

}
