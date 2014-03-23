
public class Consumer extends Thread {
	
	private EventBarrier barrier;
	private int id;
	
	public Consumer(EventBarrier eb, int i){
		barrier = eb;
		id = i;
	}
	
<<<<<<< HEAD
=======
	@Override
>>>>>>> af44cbe5c038e4b1b4ef5c05fa3029362fea40c9
	public synchronized void run(){
		System.out.println("Running: id = "+id);
		barrier.arrive();
		System.out.println("After arrive, before complete: Consumer ln 12, id = "+id);
		barrier.complete();		
	}

}
