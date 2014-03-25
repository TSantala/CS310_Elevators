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

	private int completedRiders = 0;

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
		System.out.println("Elevator array initialized");

		for(int i = 0; i < numElevators; i++) {
			e = new Elevator(numFloors,i+1,capacity, logfile);
			e.start();
			elevators[i] = e;
		}
		System.out.println("Created elevators!");

		riders = new Rider[numRiders];
		for(int j = 0; j < numRiders; j++) {
			r = new Rider(this, j+1, logfile); 
			riders[j] = r;
		}
		System.out.println("Created riders!");
	}

	public void riderRequestInput(int riderNum, int start, int dest) {
		riders[riderNum-1].addRequest(new Request(riderNum, start, dest));
	}

	public void startRiders(){
		System.out.println("*** Riders started!");
		for(Rider r : riders)
			r.start();
	}
	
	public void checkCompletion(){
		completedRiders++;
		if(completedRiders == numRiders){
			this.writeLog("Simulation completed!\n");
			try {
				logfile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(Elevator e : elevators){
				e.interrupt();
			}
		}
	}

	public Elevator callUp(Rider r){
		int startFloor = r.getFrom();
		while(true) {
			for(Elevator e : elevators) {
				synchronized(e){
					if(e.isGoingUp() && e.getFloor()<startFloor) {
						e.addRequest(r);
						return e;
					}
					else if(!e.isInTransit()) {
						e.addRequest(r);
						return e;
					}
				}
			}
		}
	}

	public Elevator callDown(Rider r){
		int startFloor = r.getFrom();
		while(true) {
			for(Elevator e : elevators) {
				synchronized(e){
					if(!e.isGoingUp() && e.getFloor()>startFloor) {
						e.addRequest(r);
						return e;
					}
					else if(!e.isInTransit()) {
						e.addRequest(r);
						return e;
					}
				}
			}
		}
	}
	
	public void writeLog(String message) {
		System.out.print(message);
		synchronized(logfile) {
			try {
				logfile.write(message);
				logfile.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 

}
