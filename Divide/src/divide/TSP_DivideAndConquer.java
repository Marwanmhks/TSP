package divide;

/*Attempts to solve the traveling salesman problem using a divide and conquer approach.
 * The greedy algorithm will get stuck at local minima, which prevents it from reaching the global minimum.
 * Using this approach, I hope to possibly avoid some local minima and see if and how it changes the results compared to the greedy approach.
 * Every iteration, I will get the 5 shortest distances to the current city. 
 * I will use a brute force approach for these 5 cities (including the initial distance of the current city, to the first picked city).
 * The brute force will get me the definite minimum, and I will add it, and update the last city visited by this approach as the new current city.
 * If there are less than 5 cities left, I will default to a greedy algorithm to clean up the rest and return to the origin city. */

import java.io.FileNotFoundException;
import java.util.*;

//Holds information about x,y coordinates.
class Point
{
	double x;
	double y;

	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public int getDistance(Point P0)
	{
		double xDiff = this.x - P0.x;
		double yDiff = this.y - P0.y;
		return (int)Math.sqrt(xDiff*xDiff + yDiff*yDiff);
	}
}

//Holds information about each city when determining which to visit next.
class City implements Comparable<City>
{
	int col;
	int distance;

	public City(int col, int distance)
	{
		this.col = col;
		this.distance = distance;
	}

	@Override
	public int compareTo(City compareCity)
	{
		if(this.distance < compareCity.distance) return -1;
		if(this.distance > compareCity.distance) return 1;
		return 0;
	}

}
	
public class TSP_DivideAndConquer
{
//***************************************************METHODS FOR GATHERING LIST OF COMBOS********************************************************

	//Checks to see if current number can be uniquely placed.
	public static boolean isClear(int[] list, int target)
	{
		for(int i = 0; i < list.length; i++)
		{
			if(list[i] == target) return false;
		}
		return true;
	}

	//Replaces all elements in array with -1 (Used to "prime" the array for the method).
	public static void negOneOut(int[] list)
	{
		for(int i = 0; i < list.length; i++) list[i] = -1;
	}

	//Creates a deep copy of the array so the current unique combinations can be added to the list.
	public static int[] deepCopy(int[] list)
	{
		int[] newList = new int[list.length];
            System.arraycopy(list, 0, newList, 0, list.length);
		return newList;
	}

	//Generates all possible pathways for the amount of points supplied (The length of list). 
	public static void generatePaths(ArrayList<int[]> combos, int[] list, int index)
	{
		if(index == list.length)
		{
			int[] listToAdd = deepCopy(list);
			combos.add(listToAdd);
			return;
		}

		for(int i = 0; i < list.length; i++)
		{
			if(!isClear(list,i)) continue;
			list[index] = i;
			generatePaths(combos,list,index + 1);
			list[index] = -1;
		}
	}
	
//*************************************************END OF METHODS FOR GETTING COMBOS***************************************************


	//Kills the current column with -1's
	public static void killColumn(int[][] matrix, int target)
	{
            for (int[] matrix1 : matrix) {
                matrix1[target] = -1;
            }
	}

	//Calls killColumn to kill the entire list of columns supplied (Remember to supply the COL number of first 5 numbers in the sorted list).
	public static void killAll(int[][] matrix, int[] targets)
	{
		for(int i = 0; i < targets.length; i++) killColumn(matrix,targets[i]);
	}

	//Checks to see if current index is contained the in supplied array.
	public static boolean contains(int columns[], int target)
	{
		for(int i = 0; i < columns.length; i++)
		{
			if(columns[i] == target) return true;
		}
		return false;
	}

	//Generates the smaller matrix that will be brute forced. (Needs an array of the shortest distances columns in SORTED order.)
	public static int[][] getSubMatrix(int[][] matrix, int[] columns)
	{
		int[][] subMatrix = new int[columns.length][columns.length];
		for(int i = 0; i < columns.length; i++)
		{
			int row = columns[i];
			int col = 0;
			for(int j = 0; j < matrix[0].length; j++)
			{	
				if(contains(columns,j)) subMatrix[i][col++] = matrix[row][j];
			}
		}
		return subMatrix;
	}

	//Generate default minimum distance
	public static int initialMinimum(int[][] subMatrix)
	{
		int retval = 0;
            for (int[] subMatrix1 : subMatrix) {
                for (int j = 0; j < subMatrix[0].length; j++) {
                    retval += subMatrix1[j];
                }
            }
		return retval;
	}

	//Uses brute force method to find the minimum path for current sub matrix. Need the minimum distance and the index of the last choice.
	public static int[] bruteForce(int[][] matrix, int[][] subMatrix, ArrayList<int[]> combinations, int[] columns, int currentRow)
	{
		//Set minimum distance as something that everything will have to be lower than.
		int minDistance = initialMinimum(subMatrix);
		int minIndex = 0;
		for(int i = 0; i < combinations.size(); i++)
		{
			//Getting the initial distance to go to this first point from origin point.
			int[] currentCombo = combinations.get(i);
			int col = columns[currentCombo[0]];
			int currentDistance = matrix[currentRow][col];

			//Iterating the rest of the submatrix and bruce forcing. 
			for(int j = 0; j < currentCombo.length - 1; j++) currentDistance += subMatrix[currentCombo[j]][currentCombo[j+1]];
			if(currentDistance < minDistance)
			{
				minDistance = currentDistance;
				minIndex = i;
			}
		}

		//Get the index of the last column in original city matrix that the brute force method ended in. (columns hold indexes of the main matrix). 
		int lastIndex = combinations.get(0).length - 1;
		int[] retval = {minDistance, columns[combinations.get(minIndex)[lastIndex]]};
		
		//KILL ALL THESE INDEXES HERE
		killAll(matrix,columns);
		
		return retval;
	}

//*******************************************************************MAIN METHOD*************************************************************************
	public static void main(String[] args) throws FileNotFoundException
	{
		//Generate all the paths.
		ArrayList<int[]> combinations = new ArrayList<>();
		int[] path = new int[5];
		negOneOut(path);
		generatePaths(combinations,path,0);
                int flag =1;

		//Generate the matrix, get the first spot, and save a temp of origin column before killing it.
                double[][] p = DataParser.parse("C:\\Users\\Marwan\\Documents\\GitHub\TSP\\Input Samples\\Cincinnati.tsp");//file path here
		int[][] cities = DataParser.parseNodesTo2DIntArray(p);
		int originCity = (int)(Math.random()*cities.length);
		int[] tempOriginCity = new int[cities.length];
		for(int i = 0; i < tempOriginCity.length; i++) tempOriginCity[i] = cities[i][originCity];
		killColumn(cities,originCity);

		//Set other initial variables.
                final long startTime = System.currentTimeMillis();
		int distanceTraveled = 0;
		int currentCity = originCity;
		int cityCount = 1;
                System.out.println("Starting Point: "+currentCity);
                System.out.println("Path: ");
		//Begin iterating the matrix and getting shortest 5 cities, solving them, and combining results.
		while(cityCount != cities.length)
		{
			//Get array of shortest distances sorted.
			ArrayList<City> currentDistances = new ArrayList<>();
			for(int col = 0; col < cities[0].length; col++)
			{
				if(cities[currentCity][col] != -1) currentDistances.add(new City(col,cities[currentCity][col]));
			}
                        
			//Sort the array.
			Collections.sort(currentDistances);
                        
                  
			//If there are at least 5 elements, do the brute force approach.
			if(currentDistances.size() >= 5)
			{
				//Grab the list of the columns for the shortest, and sort the column indexes at the end!!!!!!!!!!
				int[] bruteForceColumn = new int[5];
				for(int i = 0; i < bruteForceColumn.length; i++) bruteForceColumn[i] = currentDistances.get(i).col;
				Arrays.sort(bruteForceColumn);
                               System.out.println(Arrays.toString(bruteForceColumn));
				//Generate the sub matrix from these columns.
				int[][] subMatrix = getSubMatrix(cities,bruteForceColumn);

				//Now feed this to the brute force algorithm, and save the distance traveled, plus the new currentCity.
				int[] distANDcurrent = bruteForce(cities,subMatrix,combinations,bruteForceColumn,currentCity);
                                

				//Add to the distance, and set the new current city
				distanceTraveled += distANDcurrent[0];
				currentCity = distANDcurrent[1];
                                
				cityCount += 5;
			}
			//Otherwise begin doing a greedy approach for the last elements.
			else
			{
                            
                            if(flag==1){
                            int[] Column = new int[currentDistances.size()];
				for(int i = 0; i < Column.length; i++) Column[i] = currentDistances.get(i).col;
                                Arrays.sort(Column);
                                System.out.println(Arrays.toString(Column));
                                flag=0;
                            }
				int min = 10000000;
				int minIndex = 0;
				for(int i = 0; i < cities[0].length; i++)
				{
					if(cities[currentCity][i] != -1)
					{
						if(cities[currentCity][i] < min)
						{
							min = cities[currentCity][i];
							minIndex = i;
						}
					}
				}

				//Updating
				distanceTraveled += min;
				currentCity = minIndex;                                
				killColumn(cities,currentCity);
				cityCount++;
			}
			//If only the origin city is left, add the distance from the current city back to it.
			if(cityCount == cities.length)
			{
				distanceTraveled += tempOriginCity[currentCity];
			}
		}
		System.out.println("Best Cost: "+distanceTraveled);
                System.out.println("Time: " + (double) (System.currentTimeMillis() - startTime)+ " ms");
	}
}

