package nearest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Nearest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                /**
         *  Variable declaration
         */
        String filename = "";
        ArrayList<Integer> shortestTour;
        
        filename="C:\\Users\\Marwan\\Documents\\GitHub\TSP\\Input Samples\\Atlanta.tsp";
        
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
                NearestNeighborSolver nn = new NearestNeighborSolver(tour);
                shortestTour = nn.getShortestTour();
                String answer2 = tour.printTour(shortestTour);
                System.out.println(answer2);
                long endTime   = System.currentTimeMillis();
            	long totalTime = endTime - startTime;
            	System.out.println("Total time to execute: "+totalTime+" ms");
   
    }//end of main()
    
}
