package asgn04;

import java.util.ArrayList;
import java.util.Random;

public class Program
{

	final static int NUM_PROCS = 6; // How many concurrent processes
	final static int TOTAL_RESOURCES = 30; // Total resources in the system
	final static int MAX_PROC_RESOURCES = 13; // Highest amount of resources any process could need
	final static int ITERATIONS = 30; // How long to run the program
	static Random rand = new Random();
	
	public static void main(String[] args)
	{
		
		// The list of processes:
		ArrayList<Proc> processes = new ArrayList<Proc>();
		for (int i = 0; i < NUM_PROCS; i++)
			processes.add(new Proc(MAX_PROC_RESOURCES - rand.nextInt(3))); // Initialize to a new Proc, with some small range for its max
		
		// Run the simulation:
		for (int i = 0; i < ITERATIONS; i++)
		{
			// loop through the processes and for each one get its request
			for (int j = 0; j < processes.size(); j++)
			{
				if (processes.get(j) != null){
					// Get the request
					int currRequest = processes.get(j).resourceRequest(TOTAL_RESOURCES - getTotalHeldResources(processes));
	
					// just ignore processes that don't ask for resources
					if (currRequest == 0)
						continue;
					
					// Here you have to enter code to determine whether or not the request can be granted,
					// and then grant the request if possible. Remember to give output to the console 
					// this indicates what the request is, and whether or not its granted.
					else if (currRequest < 0){
						System.out.println("Process " + j +  " completed. " + currRequest * -1 + " released.");
					}
					else if (currRequest > 0){
					//else{	
					
					
						boolean safe = evaluateRequest(processes, TOTAL_RESOURCES - getTotalHeldResources(processes), currRequest, j);
						if (safe){
							processes.get(j).addResources(currRequest);
							System.out.println("Process " + j + " requested " + currRequest + ", granted");
						}
						else {
							System.out.println("Process " + j + " requested " + currRequest + ", not granted");
						}
						
		
						// At the end of each iteration, give a summary of the current status:
						System.out.println("\n***** STATUS *****");
						System.out.println("Total Available: " + (TOTAL_RESOURCES - getTotalHeldResources(processes)));
						for (int k = 0; k < processes.size(); k++)
							System.out.println("Process " + k + " holds: " + processes.get(k).getHeldResources() + ", max: " +
									processes.get(k).getMaxResources() + ", claim: " + 
									(processes.get(k).getMaxResources() - processes.get(k).getHeldResources()));
						System.out.println("***** STATUS *****\n");
					}
				}
				
			}
		}

	}
	
	public static int getTotalHeldResources(ArrayList<Proc> processes){
		int total = 0;
		for(Proc process : processes){
			total += process.getHeldResources();
		}
		return total;
	}
	
	//process is the process making the request
	public static boolean evaluateRequest(ArrayList<Proc> procs, int available, int currRequest, int process){
		ArrayList<Proc> processes = new ArrayList<Proc>();
		for (int i = 0; i < procs.size(); i++){
			processes.add(procs.get(i));
		}
		//boolean found = false;
		while(!processes.isEmpty()){
			for (int i = 0; i < processes.size(); i++){
				if (processes.get(i) != null){
					if (processes.get(i).getMaxResources() - processes.get(i).getHeldResources() <= available - currRequest 
							||i == process &&
								processes.get(i).getMaxResources() - processes.get(i).getHeldResources() - currRequest 
									<= available - currRequest){
						//proc can obtain all it needs. assume it does so, terminates, and
						//releases what it already has.
						available += processes.get(i).getHeldResources();
						processes.remove(processes.get(i));
						//found = true;
						return true;
					}
				}
			}
//			if (!found){
//				return false;
//			}
			return false;
		}
		//return true;
		return false;
	}

}

