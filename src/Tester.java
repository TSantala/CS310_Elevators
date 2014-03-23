
public class Tester {
	
	public Tester(){
		this.runTestPart1();
	}
	
	private void runTestPart1() {
		EventBarrier barrier = new EventBarrier();
		
		Consumer c1 = new Consumer(barrier,0);
		c1.start();
		
		Consumer c2 = new Consumer(barrier,1);
		c2.start();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		barrier.raise();
	}

}
