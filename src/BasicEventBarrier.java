
public class BasicEventBarrier extends AbstractEventBarrier{

	/**
	 * Instantiate the stateless AbstractEventBarrier.
	 */
<<<<<<< HEAD
	public BasicEventBarrier() {
=======
	public AbstractEventBarrier() {
>>>>>>> af44cbe5c038e4b1b4ef5c05fa3029362fea40c9
	}

	/**
	 * Arrive at the barrier and wait until an event is signaled. Return
	 * immediately if already in the signaled state.
	 */
<<<<<<< HEAD
	public void arrive() {
=======
	public abstract void arrive() {
>>>>>>> af44cbe5c038e4b1b4ef5c05fa3029362fea40c9
		
	}

	/**
	 * Signal the event and block until all threads that wait for this
	 * event have responded. The EventBarrier returns to an unsignaled state
	 * before raise() returns.
	 */	
<<<<<<< HEAD
	public void raise() {
=======
	public abstract void raise() {
>>>>>>> af44cbe5c038e4b1b4ef5c05fa3029362fea40c9
		
	}

	/**
	 * Indicate that the calling thread has finished responding to a
	 * signaled event, and block until all other threads that wait for 
	 * this event have also responded.
	 */
<<<<<<< HEAD
	public void complete() {
=======
	public abstract void complete() {
>>>>>>> af44cbe5c038e4b1b4ef5c05fa3029362fea40c9
		
	}

	/**
	 * Return a count of threads that are waiting for the event or that
	 * have not responded yet.
	 */
<<<<<<< HEAD
	public int waiters() {
		return 0;
=======
	public abstract int waiters() {
		
>>>>>>> af44cbe5c038e4b1b4ef5c05fa3029362fea40c9
	}
}
