import java.io.*;
public class Main {

	public static void main(String[] args) throws IOException{

		System.out.println("Started");
		String line = null;
		Building b;
		String fileName;


		String[] params;
		int floors, elevators, capacity, riders;
		int rider, from, to;

		File logfile = new File("logfile.txt");
		if (!logfile.exists()) 
			logfile.createNewFile();
		
		FileWriter fw = new FileWriter(logfile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		if(args.length > 0) {
			fileName = args[0];
		}
		else {
			System.out.println("Using standard commands file");
			fileName = "standardCommands.txt";
		}

		try {
			FileReader fileReader = 
					new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = 
					new BufferedReader(fileReader);
			line = bufferedReader.readLine();
			params = line.split(" ");

			floors = Integer.parseInt(params[0]);
			elevators = Integer.parseInt(params[1]);
			riders = Integer.parseInt(params[2]);
			capacity = Integer.parseInt(params[3]);
			b = new Building(floors, elevators, riders, capacity, bw);
			System.out.println("Starting building initialization!");
			b.init();

			while((line = bufferedReader.readLine()) != null) {
				params = line.split(" ");
				rider = Integer.parseInt(params[0]);
				from = Integer.parseInt(params[1]);
				to = Integer.parseInt(params[2]);

				b.riderInput(rider, from, to);
			}

		}
		catch(FileNotFoundException e) {
			e.printStackTrace();				
		}
		catch(IOException e) {				
			e.printStackTrace();
		}

		System.out.println("Simulation complete!\n");
		bw.write("Simulation Complete");
		bw.close();
	}

}
