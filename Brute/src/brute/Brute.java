package brute;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Brute {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
             /**
         *  Variable declaration
         */
        String filename = "";
    
        ArrayList<Integer> shortestTour;
        
        
        /** 
         * Parse arguments and switches
         */
   
            
            try ( // Prompt the user for the .tsp filename.
                    Scanner scanner = new Scanner(System.in)) {
               
                System.out.print("Enter the file name: ");
                System.out.flush();
                filename = scanner.nextLine();
                
        
            }
        

        Tour tour = new Tour();
        tour.setFilename(filename);
        
        try {
            String readLine = "";
            // Read-in the .tsp file line-by-line
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                // Read-in the .tsp file line-by-line
                while ((readLine = br.readLine()) != null) {
                    tour.parseLine(readLine);
                } // end while 
            }
        } // end try // end try
        catch (IOException e) {
            System.err.println("Error: " + e);
        }

        
  
        		long startTime = System.currentTimeMillis();
                BruteForceSolver bruteForce = new BruteForceSolver(tour);
                bruteForce.generatePermutations();
                shortestTour = bruteForce.getShortestTour();
                
                String answer = tour.printTour(shortestTour);
                System.out.println(answer);
                System.out.println("Solution :" + bruteForce.getShortestTour()
                      + ", Dist.: " + bruteForce.getShortestDistance() + "\n");

                long endTime   = System.currentTimeMillis();
            	long totalTime = endTime - startTime;
            	System.out.println("Total time to execute: "+totalTime+" ms");
               
              
            
    }
    
}
