import java.io.*;
import java.util.*;

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

	private List<Request> requests = new ArrayList<Request>();


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
		System.out.println("Created riders!");
	}

	public void riderRequestInput(int riderNum, int start, int dest) {
		Request r = new Request(riderNum, start, dest);
		requests.add(r);
		riders[riderNum-1].addRequest(r);
	}
	
	public void startRiders(){
		System.out.println("*** Riders started!");
		for(Rider r : riders)
			r.start();
	}

	public Elevator callUp(Rider r){
		int startFloor = r.getFrom();
		while(true) {
			for(int i = 0; i < elevators.length; i++) {
				Elevator e = elevators[i];
				if(!e.isInTransit()) {
					return e;
				}
				else if(e.isGoingUp() && e.getFloor()<startFloor) {
					return e;
				}
			}
		}
	}

	public Elevator callDown(Rider r){
		int startFloor = r.getFrom();
		while(true) {
			for(int i = 0; i < elevators.length; i++) {
				Elevator e = elevators[i];
				if(!e.isInTransit()) {
					return e;
				}
				else if(!e.isGoingUp() && e.getFloor()>startFloor) {
					return e;
				}
			}
		}
	}

	public void writeLog(String message) throws IOException {
		synchronized(logfile) {
			logfile.write(message);
		}
	} 

}
