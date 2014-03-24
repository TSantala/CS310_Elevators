import java.io.*;
public class Main {
	
	public static void main(String[] args) throws IOException{
		System.out.println("Started");
		String line = null;
		Building b;
		String fileName;
		BufferedWriter bw = null;
		
		
			String[] params;
			int floors, elevators, capacity, riders;
			int rider, from, to;
			
			File logfile = new File("logfile.txt");
			if (!logfile.exists()) 
				logfile.createNewFile();
			FileWriter fw;

			fw = new FileWriter(logfile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
			
			if(args.length > 0) {
				fileName = args[0];
			}
			else {
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
			
			
			bw.write("Simulation Complete");
			bw.close();
	}

}
