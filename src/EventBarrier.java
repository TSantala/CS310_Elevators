
public class EventBarrier extends AbstractEventBarrier {

	private boolean gateOpen = false;
	private int numWaiting = 0;
	
	public EventBarrier(){}
	
	@Override
	public synchronized void arrive() {
		numWaiting++;
		System.out.println("Consumer has arrived: numWaiting = " + numWaiting);
		while(!gateOpen){
			try {
				this.wait();
				System.out.println("Notified of opened gate");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	@Override
	public synchronized void raise() {
		gateOpen = true;
		this.notifyAll();		
		
		System.out.println("Gate has opened!");
		
		
	}

	
	@Override
	public synchronized void complete() {
		numWaiting--;
		System.out.println("Complete called: numWaiting = "+numWaiting);
		if(numWaiting==0){
			gateOpen = false;
		}
	}

	@Override
	public synchronized int waiters() {
		System.out.println("Waiters called: numwaiting = " +numWaiting);
		return numWaiting;
	}

}
