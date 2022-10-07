package dynamic;

import java.io.FileNotFoundException;

public class Dynamic {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
    double[][] p = DataParser.parse("C:\\Users\\Marwan\\Documents\\GitHub\TSP\\Input Samples\\Atlanta.tsp");
   
    int startNode = 0;
    TspDynamicProgrammingIterative solver = new TspDynamicProgrammingIterative(startNode, p);

    System.out.println("Tour: " + solver.getTour());
    System.out.println("Tour cost: " + solver.getTourCost());
    }
    
}
