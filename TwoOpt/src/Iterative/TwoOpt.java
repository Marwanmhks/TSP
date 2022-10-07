package Iterative;

import java.io.FileNotFoundException;

public class TwoOpt {

    /**
     *
     * @param args
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
   
        double[][] p = DataParser.parse("C:\\Users\\Marwan\\Documents\\GitHub\TSP\\Input Samples\\Atlanta.tsp");
        int[][] IntArray = DataParser.parseNodesTo2DIntArray(p);
        
  
       System.out.print(TwoOptExchange.solve(IntArray).solution);
       System.out.print(TwoOptExchange.solve(IntArray).trace);

  }
}
