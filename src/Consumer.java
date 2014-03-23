
public class Consumer extends Thread {
	
	private EventBarrier barrier;
	private int id;
	
	public Consumer(EventBarrier eb, int i){
		barrier = eb;
		id = i;
	}
	
	public synchronized void run(){
		System.out.println("Running: id = "+id);
		barrier.arrive();
		System.out.println("After arrive, before complete: Consumer ln 12, id = "+id);
		barrier.complete();		
	}

}
