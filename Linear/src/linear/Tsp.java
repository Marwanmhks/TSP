package linear;

import gurobi.*; 


public class Tsp extends GRBCallback {
  private final GRBVar[][] vars;

  public Tsp(GRBVar[][] xvars) {
    vars = xvars;
  }

  // Subtour elimination callback.  Whenever a feasible solution is found,
  // find the subtour that contains node 0, and add a subtour elimination
  // constraint if the tour doesn't visit every node.

  @Override
  protected void callback() {
    try {
      if (where == GRB.CB_MIPSOL) {
        // Found an integer feasible solution - does it visit every node?
        int n = vars.length;
        int[] tour = findsubtour(getSolution(vars));

        if (tour.length < n) {
          // Add subtour elimination constraint
          GRBLinExpr expr = new GRBLinExpr();
          for (int i = 0; i < tour.length; i++)
            for (int j = i+1; j < tour.length; j++)
              expr.addTerm(1.0, vars[tour[i]][tour[j]]);
          addLazy(expr, GRB.LESS_EQUAL, tour.length-1);
        }
      }
    } catch (GRBException e) {
      System.out.println("Error code: " + e.getErrorCode() + ". " +
          e.getMessage());
    }
  }

  // Given an integer-feasible solution 'sol', return the smallest
  // sub-tour (as a list of node indices).

  protected static int[] findsubtour(double[][] sol)
  {
    int n = sol.length;
    boolean[] seen = new boolean[n];
    int[] tour = new int[n];
    int bestind, bestlen;
    int i, node, len, start;

    for (i = 0; i < n; i++)
      seen[i] = false;

    start = 0;
    bestlen = n+1;
    bestind = -1;
    node = 0;
    while (start < n) {
      for (node = 0; node < n; node++)
        if (!seen[node])
          break;
      if (node == n)
        break;
      for (len = 0; len < n; len++) {
        tour[start+len] = node;
        seen[node] = true;
        for (i = 0; i < n; i++) {
          if (sol[node][i] > 0.5 && !seen[i]) {
            node = i;
            break;
          }
        }
        if (i == n) {
          len++;
          if (len < bestlen) {
            bestlen = len;
            bestind = start;
          }
          start += len;
          break;
        }
      }
    }

    int result[] = new int[bestlen];
    for (i = 0; i < bestlen; i++)
      result[i] = tour[bestind+i];
    return result;
  }

  // Euclidean distance between points 'i' and 'j'

  protected static double distance(double[] x, double[] y, int i, int j) {
    double dx = x[i]-x[j];
    double dy = y[i]-y[j];
    return Math.sqrt(dx*dx+dy*dy);
  }

  
}


