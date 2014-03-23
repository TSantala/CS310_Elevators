
public class Tester {
	
	public Tester(){
		this.runTestPart1();
	}
	
	private void runTestPart1() {
		EventBarrier barrier = new EventBarrier();
		
		barrier.raise();
		
		this.pause(100);
		
		Consumer c0 = new Consumer(barrier,0);
		c0.start();
		
		Consumer c1 = new Consumer(barrier,1);
		c1.start();
		
		this.pause(100);
		
		barrier.raise();
		
		this.pause(100);
		
		Consumer c2 = new Consumer(barrier,2);
		c2.start();
		
		Consumer c3 = new Consumer(barrier,3);
		c3.start();
		
		Consumer c4 = new Consumer(barrier,4);
		c4.start();
		
		Consumer c5 = new Consumer(barrier,5);
		c5.start();
		
		barrier.waiters();
		barrier.raise();
		
		this.pause(100);
		
		barrier.waiters();
				
	}
	
	private void pause(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
