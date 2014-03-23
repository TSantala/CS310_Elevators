
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
		System.out.println("*** Gate has opened!");
		this.notifyAll();
		while(true){
			if(numWaiting > 0) {
				
			}
			else {
				gateOpen = true;
				break;
			}
		}
	}

	
	@Override
	public synchronized void complete() {
		numWaiting--;
		System.out.println("Complete called: numWaiting = "+numWaiting);
		if(numWaiting==0){
			gateOpen = false;
			System.out.println("*** Gate closed");
		}
	}

	@Override
	public synchronized int waiters() {
		System.out.println("Waiters called: numwaiting = " +numWaiting);
		return numWaiting;
	}

}
