package linear;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import java.io.FileNotFoundException;
import static linear.Tsp.distance;
import static linear.Tsp.findsubtour;


public class Linear {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
public static void main(String[] args) throws FileNotFoundException {

  
double[][] p = DataParser.parse("C:\\Users\\Marwan\\Documents\\GitHub\TSP\\Input Samples\\Cincinnati.tsp");
        
    int n = p.length;

    try {
      GRBEnv   env   = new GRBEnv();
      GRBModel model = new GRBModel(env);

      // Must set LazyConstraints parameter when using lazy constraints

      model.set(GRB.IntParam.LazyConstraints, 1);

      double[] x = new double[n];
      double[] y = new double[n];

      for (int i = 0; i < n; i++) {
          for(int j=0;j<n-1;j++){
                x[i] = p[i][j];
                y[i] = p[i][j+1];
            }
      }

      // Create variables

      GRBVar[][] vars = new GRBVar[n][n];

      for (int i = 0; i < n; i++)
        for (int j = 0; j <= i; j++) {
          vars[i][j] = model.addVar(0.0, 1.0, distance(x, y, i, j),
                                    GRB.BINARY,
                                  "x"+String.valueOf(i)+"_"+String.valueOf(j));
          vars[j][i] = vars[i][j];
        }

      // Degree-2 constraints
      final long start = System.currentTimeMillis();
      for (int i = 0; i < n; i++) {
        GRBLinExpr expr = new GRBLinExpr();
        for (int j = 0; j < n; j++)
          expr.addTerm(1.0, vars[i][j]);
        model.addConstr(expr, GRB.EQUAL, 2.0, "deg2_"+String.valueOf(i));
      }

      // Forbid edge from node back to itself
      
      for (int i = 0; i < n; i++)
        vars[i][i].set(GRB.DoubleAttr.UB, 0.0);
      
      model.setCallback(new Tsp(vars));
      model.optimize();

      if (model.get(GRB.IntAttr.SolCount) > 0) {
        int[] tour = findsubtour(model.get(GRB.DoubleAttr.X, vars));
        assert tour.length == n;
        
        System.out.print("Time: ");
        System.out.println((double) (System.currentTimeMillis() - start) / 1000);
        System.out.print("Tour: ");
        
        for (int i = 0; i < tour.length; i++)
          System.out.print(String.valueOf(tour[i]) + " ");
        
        System.out.println();
      }

      // Dispose of model and environment
      model.dispose();
      env.dispose();

    } catch (GRBException e) {
      System.out.println("Error code: " + e.getErrorCode() + ". " +
          e.getMessage());
    }
  }
}
