import java.io.*;

public class Building {
	
	private int numFloors;
	private Rider[] riders;
	private Elevator[] elevators;
	//private Elevator elevator;
	//private EventBarrier barrier;
	private int numRiders;
	private int numElevators;
	private int capacity;
	private BufferedWriter logfile;
	
	
	public Building(int f, int e, int r, int n, BufferedWriter log){
		numFloors = f;
		numElevators = e;
		numRiders = r;
		capacity = n;
		logfile = log;

		//barrier = new EventBarrier();
	}
	
	public void init() throws IOException {
		Elevator e;
		Rider r;
		elevators = new Elevator[numElevators];
		System.out.println("Elevator array initialized\n");
		for(int i = 0; i < numElevators; i++) {
			System.out.println("preerror" + numElevators + "\n");
			e = new Elevator(numFloors,i+1,capacity, logfile);
			logfile.write("New Elevator added!\n");
			System.out.println("New elevator added!");
			e.start();
			elevators[i] = e;
		}
		System.out.println("Created elevators!");
		riders = new Rider[numRiders];
		for(int j = 0; j < numRiders; j++) {
			r = new Rider(this, j+1, logfile); 
			riders[j] = r;
		}
		System.out.println("Finished building initialization!");
	}
	
	public void riderInput(int riderNum, int start, int dest) {
		//if(start position / request not already set)
		Rider r = riders[riderNum-1];
		r.setStartDest(start, dest);
		r.start(); //the function here is going to change. needs to start running and then wait for movement commands issued by the building
		//else store request for future.
	}
	

	public Elevator callUp(Rider r){
		return elevators[0];
	}
	
	public Elevator callDown(Rider r){
		return elevators[0];
	}
	
	public void writeLog(String message) throws IOException {
		synchronized(logfile) {
			logfile.write(message);
		}
	} 

}
