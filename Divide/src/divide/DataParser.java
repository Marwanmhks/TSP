package divide;



import java.util.Scanner;
import java.util.*;
import java.io.*;


public class DataParser {
	/**
     * @param filename
	 * @return: int[] 2D-array of int
     * @throws java.io.FileNotFoundException
	 */
	public static double[][] parse(String filename) throws FileNotFoundException {
		// Init file
		File inputFile = new File(filename);
		Scanner sc = new Scanner(inputFile);

		// Skip 2 lines
		String name = sc.nextLine();
		String comment = sc.nextLine();
		String dimension = sc.nextLine();

		// parse dimension
		int size = parseDimension(dimension);

		// Skip 2 lines
		String weightType = sc.nextLine();
		String nodeSection = sc.nextLine();

		// Read node list
		double[] nodeList = new double[size * 2];
		for (int i = 0; i < size; i++) {
			String[] splitedLine = sc.nextLine().split(" ");
			nodeList[i * 2] = Double.parseDouble(splitedLine[1]);
			nodeList[i * 2 + 1] = Double.parseDouble(splitedLine[2]);
		}
		return parseNodesTo2DDoubleArray(nodeList, size);
	}

	private static double[][] parseNodesTo2DDoubleArray(double[] nodes, int size) {
		double[][] nodesMap = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				nodesMap[i][j] = getEucDis(nodes[i * 2], nodes[i * 2 + 1], nodes[j * 2], nodes[j * 2 + 1]);
			}
		}
		return nodesMap;
	}

	public static int[][] parseNodesTo2DIntArray(double[][] geoMap) {
		int size = geoMap.length;
		int[][] nodesMap = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				nodesMap[i][j] = (int) Math.round(geoMap[i][j]);
			}
		}
		return nodesMap;
	}

	private static double getEucDis(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	private static int parseDimension(String line) {
		String num = line.split(": ")[1];
		return Integer.parseInt(num);
	}

	/**
     * write result into file
     * @param tsp tsp traversal of a given graph
     * @param geoMap original graph
     * @param args
     */
    public static void writeOutput(List<Integer> tsp, int[][] geoMap, String args[]) {
        int weight = getWeight(tsp, geoMap);
        PrintWriter output;
        String name = "ERROR";
        if (args[1].equals("NN") || args[1].equals("MSTApprox")) {
            name = args[0] + "_" + args[1];
        } else {
            name = args[0] + "_" + args[1] + "_" + args[2] + "_" + args[3];
        }
        String outputPath = "../output/" + name + ".sol";
        try {
            output = new PrintWriter(outputPath, "UTF-8");
            output.println(weight);
            for (int i = 1; i < tsp.size(); i++) {
                int source = tsp.get(i - 1);
                int target = tsp.get(i);
                output.println(source + "\t" + target + "\t" + (int) geoMap[source][target]);
            }
            System.out.println("Write finished");
            output.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
        }
    }

    public static void writeTrace(List<Long> trace, String args[]) {
        PrintWriter output;
        String name = "ERROR";
        if (args[1].equals("NN") || args[1].equals("MSTApprox")) {
            name = args[0] + "_" + args[1];
        } else {
            name = args[0] + "_" + args[1] + "_" + args[2] + "_" + args[3];
        }
        String outputPath = "../output/" + name + ".trace";
        try {
            output = new PrintWriter(outputPath, "UTF-8");
            for (int i = 0; i < trace.size(); i+=2) {
                double time = (double) trace.get(i);
                if (args[1].equals("NN") || args[1].equals("MSTApprox")) {
                     time = time / 10000;
                } else {
                    time = time / 1000;
                }
                long quality = trace.get(i + 1);
                output.println(time + "," + quality + "\t");
            }
            System.out.println("Write finished");
            output.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
        }
    }

    /**
     * calculate the weight of a given tsp
     * @param tsp tsp traversal of a given graph
     * @param geoMap original graph
     * @return the weight of a given tsp
     */
    public static int getWeight(List<Integer> tsp, int[][] geoMap) {
        int res = 0;
        for (int i = 0; i < tsp.size() - 1; i++) {
            res += geoMap[tsp.get(i)][tsp.get(i + 1)];
        }
        System.out.println(res);
        return res;
    }
}


