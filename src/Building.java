import java.io.*;

public class Building {
	
	private int numFloors;
	private Rider[] riders;
	private Elevator[] elevators;
	private Elevator elevator;
	private EventBarrier barrier;
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

		barrier = new EventBarrier();
	}
	
	public void init() throws IOException {
		Elevator e;
		Rider r;
		elevators = new Elevator[numElevators];
		for(int i = 0; i < numElevators; i++) {
			e = new Elevator(numFloors,i+1,capacity, logfile);
			logfile.write("New Elevator added!");
			e.run();
			elevators[i] = e;
		}
		logfile.close();
		for(int j = 0; j < numRiders; j++) {
			r = new Rider(this, j+1, logfile);
			r.run(); //the function here is going to change. needs to start running and then wait for movement commands issued by the building 
			riders[j] = r;
		}
	}
	
	public void riderInput(int riderNum, int start, int dest) {
		//if the rider isnt on a journey already
		Rider r = riders[riderNum-1];
		r.setStartDest(start, dest);
		
		
	}
	

	public Elevator callUp(Rider r){
		return elevator;
	}
	
	public Elevator callDown(Rider r){
		return elevator;
	}
	
	public void writeLog(String message) throws IOException {
		synchronized(logfile) {
			logfile.write(message);
		}
	} 

}
