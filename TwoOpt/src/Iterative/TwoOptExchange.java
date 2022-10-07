package Iterative;

import java.util.Collections;
import java.util.*;

public class TwoOptExchange {
    public static TSPAnswer solve(int[][] graph) {
        List<Integer> solution = generateRandomSolution(graph, 0);
        
        int parentBestDis = getDis(graph, solution);
        System.out.println(parentBestDis);
        Boolean updated = true;
        final long start = System.currentTimeMillis();
        List<Long> traceList = new ArrayList<>();
        int sb = 0;
        while (updated) {
            updated = false;
            int iterBestDis = parentBestDis;
            List<Integer> iterBestSolution = solution;
            for (int i = 0; i < solution.size(); i++) {
                for (int j = i + 1; j < solution.size(); j++) {
                    List<Integer> newList = ((List<Integer>) ((ArrayList) solution).clone());

                    // Reverse the list from i to j
                    int c = 0;
                    for (int k = i; k <= ((i + j) / 2); k++) {
                        int t = newList.get(k);
                        newList.set(k, newList.get(j - c));
                        newList.set(j - c, t);
                        c++;
                    }
                    int dis = getDis(graph, newList);
                    if (dis < iterBestDis) {
                        iterBestDis = dis;
                        iterBestSolution = newList;
                    }
                }
            }
            if (iterBestDis < parentBestDis) {
                updated = true;
                parentBestDis = iterBestDis;
                solution = iterBestSolution;
                long timeStamp = System.currentTimeMillis() - start;
                traceList.add(timeStamp);
                traceList.add((long) iterBestDis);
               
                if (655454 * 1.1 > parentBestDis && sb == 0) {
                    System.out.println("---------");
                    System.out.println((double) (System.currentTimeMillis() - start) / 1000);
                    sb = 1;
                }
            }
        }
     
        TSPAnswer myAns = new TSPAnswer();
        solution.add(solution.get(0));
        myAns.solution = ((List<Integer>) ((ArrayList) solution).clone());
        myAns.trace = traceList;
        System.out.println((double) (System.currentTimeMillis() - start) / 1000);
        return myAns;
    }

    private static List<Integer> generateRandomSolution(int[][] graph, long seed) {
        int totalNumber = graph.length;
        List<Integer> solution = new ArrayList<>(totalNumber);

        for(int i = 0; i < totalNumber; i++) solution.add(i);

        Random rand = new Random(seed);
        Collections.shuffle(solution, rand);
        return solution;
    }

    private static int getDis(int[][] graph, List<Integer> solution) {
        int ans = 0;
        for (int i = 0; i < solution.size() - 1; i++) {
            ans += graph[solution.get(i)][solution.get(i + 1)];
        }
        ans += graph[solution.get(solution.size() - 1)][0];
        return ans;
    }
}