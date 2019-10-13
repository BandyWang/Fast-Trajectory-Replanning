import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class FTR {
	
	/*
	 * Initialize a random instance for creating a maze
	 * Create a counter to remember current iteration of A*
	 */
	
	static Random rng = new Random();
	static int counter;
	static int ACTION_COST = 1;
	
	/*
	 * Set the dimensions of the grid world
	 */
	
	//static int knownMap[][];
	static int observableMap[][];
	static int newHValueMap[][];
	static State map[][];
	static int X_DIMENSION = 101;
	static int Y_DIMENSION = 101;
	static int num_invalid_maps = 0;
	
	static int start_x = rng.nextInt(X_DIMENSION-1);
	static int start_y = rng.nextInt(Y_DIMENSION-1);

	static int goal_x = rng.nextInt(X_DIMENSION-1);
	static int goal_y = rng.nextInt(Y_DIMENSION-1);
	
	
	/*
	 * Initialize a priority queue for the open list 
	 * and a HashSet for the closed list
	 */
	
	static PriorityQueue<State> OPEN;
	static HashSet<State> CLOSED;
	
	/*
	 * Count the expanded cells for each version of A*
	 */
	static int forwardAStarExpandedCells = 0;
	static int backwardAStarExpandedCells = 0;
	static int adaptiveAStarExpandedCells = 0;
	
	/*
	 * Where the main program will run
	 */
	
	public static void main(String[] args) {
		List<int[][]> list;
		int knownMap[][];
		int numMaps;
		
		//get map
		//repeated A star 50 times on same map with different start 
		//repeated A star 50 times on same map with different end 
		
		try {
			list = SavedMaps.getMaps();
			knownMap = list.get(0);
			start_x = rng.nextInt(X_DIMENSION-1);
			start_y = rng.nextInt(Y_DIMENSION-1);
			for (int i = 0; i < 50; i++) {
				repeatedForwardAStar(knownMap);
				list = SavedMaps.getMaps();
				knownMap = list.get(0);
				goal_x = rng.nextInt(X_DIMENSION-1);
				goal_y = rng.nextInt(Y_DIMENSION-1);
			}
			System.out.println("forward: " + forwardAStarExpandedCells/50);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			list = SavedMaps.getMaps();
			knownMap = list.get(0);
			start_x = rng.nextInt(X_DIMENSION-1);
			start_y = rng.nextInt(Y_DIMENSION-1);
			for (int i = 0; i < 50; i++) {
				repeatedBackwardsAStar(knownMap);
				list = SavedMaps.getMaps();
				knownMap = list.get(0);
				goal_x = rng.nextInt(X_DIMENSION-1);
				goal_y = rng.nextInt(Y_DIMENSION-1);
			}
			System.out.println("backward: " + backwardAStarExpandedCells/50);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		try {
//			list = SavedMaps.getMaps();
//			knownMap = list.get(3);
//			repeatedForwardAStar(knownMap);
//			printMap(knownMap);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			list = SavedMaps.getMaps();
//			knownMap = list.get(3);
//			repeatedAdaptiveAStar(knownMap);
//			printMap(knownMap);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println("Expanded cells forward: "+forwardAStarExpandedCells);
//		System.out.println("Expanded cells backward: "+adaptiveAStarExpandedCells);

		
		
//		try {
//			list = SavedMaps.getMaps();
//			numMaps = list.size();
//			for (int[][] m: list) {
//				knownMap = m;
//				repeatedForwardAStar(knownMap);
//				//System.out.println("Expanded cells: "+forwardAStarExpandedCells);
//				//printMap(knownMap);
//			}
//			int valid_maps = numMaps - num_invalid_maps;
//			
//			list = SavedMaps.getMaps();
//			numMaps = list.size();
//			for (int[][] m: list) {
//				knownMap = m;
//				repeatedBackwardsAStar(knownMap);
//				//System.out.println("Expanded cells: "+forwardAStarExpandedCells);
//				//printMap(knownMap);
//			}
//			list = SavedMaps.getMaps();
//			numMaps = list.size();
//			for (int[][] m: list) {
//				knownMap = m;
//				repeatedAdaptiveAStar(knownMap);
//				//System.out.println("Expanded cells: "+forwardAStarExpandedCells);
//				//printMap(knownMap);
//			}
//			System.out.println("Forward A Star Expanded cells: "+forwardAStarExpandedCells/valid_maps);
//			System.out.println("Backward A Star Expanded cells: "+backwardAStarExpandedCells/valid_maps);
//			System.out.println("Adaptive A Star Expanded cells: "+adaptiveAStarExpandedCells/valid_maps);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
	
	/*
	 * The heuristic function
	 */
	
	public static int manhattenValue(int x1, int y1, int x2, int y2) {
		return Math.abs(x1-x2) + Math.abs(y1-y2);
	}
	
	public static void repeatedForwardAStar(int knownMap[][]) {
		
		/*
		 * Manually configure the goal state for now
		 * Declare a stack to hold states belonging to shortest path
		 */
		
		State goal = new State(goal_x, goal_y, 0);
		//State goal = new State(X_DIMENSION-1, Y_DIMENSION-1, 0);

		Stack<State> path;
		
		/*
		 * Make the map
		 */
		
		makeMap(goal);
		
//		knownMap[3][4] = 1;
//		knownMap[2][2] = 1;
//		knownMap[2][3] = 1;
//		knownMap[2][1] = 1;
//		knownMap[2][4] = 1;
//		knownMap[2][0] = 1;
//		knownMap[2][3] = 1;
//		knownMap[3][4] = 1;
//		knownMap[2][2] = 1;
		
		/*
		 * Manually configure the start state for now
		 * Have our map remember the instance of goal declared above
		 * Set the current iteration of A* to 0
		 */
		State start = map[start_x][start_y];
		//State start = map[0][0];

		//System.out.println("Forward Start cell: "+ start.x + "," + start.y);
		//System.out.println("Forward goal cell: "+ goal.x + "," + goal.y);

		//map[X_DIMENSION-1][Y_DIMENSION-1] = goal;
		map[goal_x][goal_y] = goal;

		counter = 0;
		
		/*
		 * Check start cell neighbors
		 * Check if starting cell is a blocked cell
		 * Begin the repeated A Star
		 * Break the loop if the agent finds the goal
		 */
		
		initialCheck(start, knownMap);
		
		if (knownMap[start.x][start.y] == 1) {
			
			//System.out.println("Starting cell is block");
			num_invalid_maps++;
			return;
		}
		
		if (knownMap[goal.x][goal.y] == 1) {
			//System.out.println("goal cell is block");
			return;
		}
		
		
		while (!start.equals(goal)) {
			
			//printMap(knownMap);
			
			/*
			 * Use scanner to block once travelPath is called
			 * Press enter to unblock and move to next iteration of A*
			 * This will make analyzing each iteration of A* easier
			 */
			
			Scanner myObj = new Scanner(System.in);
			//String userName = myObj.nextLine();
			//printMap(knownMap);
			
			//COUNTER AND SEARCH IS NEVER USED IN THIS CODE
			//**********************************************************
			/*
			 * Counter will store the xth iteration of A*
			 * At the beginning of A* set the start state's g value to 0
			 * Set the goal state's g value to maximum since we do not know
			 * Set both state's search value to counter to mark it in the current iteration
			 */
			
			counter+=1;
			start.gValue = 0;
			goal.gValue = Integer.MAX_VALUE;
			
			//COUNTER AND SEARCH IS NEVER USED IN THIS CODE
			//**********************************************************
			
			/*
			 * Open list and closed list "forget" previous iteration of expanded cells
			 * Insert starting cell inside open list
			 */
			
			OPEN = new PriorityQueue<State>(( a, b ) -> 
					(100 * a.fValue - a.gValue) - (100* b.fValue - b.gValue));
			CLOSED = new HashSet<>();
			OPEN.add(start);
			
			/*
			 * Compute path will return true if a path was found, false if no path is found
			 */
			
			if (computePath(start, goal)) {
				
				/*
				 * Create path will return a stack
				 * Given that a path is found store the path in a stack
				 */
				
				//printMap();
				//Scanner myObj = new Scanner(System.in);
				//String userName = myObj.nextLine();
				//System.out.println("Check goal pointer: " + goal.pointer);
				
				path = createPath(goal);
			}
			else {
				
				/*
				 * Given that a path is not found, break the loop 
				 */
				
				//System.out.println("No path found");
				return;
			}
			
			/*
			 * If we reach this section the loop has not been broken and thus a path was found and stored 
			 * Have the agent travel the path
			 * Travel path will return which state the agent will end up
			 * If the agent reached the goal, travel path returns the goal state
			 * If the agent found a blocked cell in its path, return the state just before encountering 
			 */
			
			start = travelPath(start, path, knownMap);
			
			/*
			 * Mark the spot to remember the robot walked this cell
			 */
			
			knownMap[start.x][start.y] = -1;
			
			/*
			 * Set the state's pointer to 0 to avoid infinite loop
			 * EG: State A points up to B, State B points down to A
			 */
			
			start.pointer = 0;
			
		}
		
		//printMap(knownMap);		
		
	}
	
	public static void repeatedBackwardsAStar(int knownMap[][]) {
		
		State goal = new State(goal_x, goal_y, 0);
		
		Scanner myObj;
		
		makeMap(goal);
		map[goal_x][goal_y] = goal;
		
//		knownMap[2][3] = 1;
//		knownMap[3][4] = 1;
//		knownMap[2][2] = 1;
		
		State start = map[start_x][start_y];
		

		initialCheck(start, knownMap);
		
		if (knownMap[start.x][start.y] == 1) {
			//System.out.println("start cell is block");
			return;
		}
		if (knownMap[goal.x][goal.y] == 1) {
			//System.out.println("goal cell is block");
			return;
		}
		
		
		while (!start.equals(goal)) {
			
			//printMap(knownMap);
			
			myObj = new Scanner(System.in);
			//myObj.nextLine();
			//printMap(knownMap);
			
			start.gValue = Integer.MAX_VALUE;
			goal.gValue = 0;
			
			OPEN = new PriorityQueue<State>(( a, b ) -> 
			(100 * a.fValue - a.gValue) - (100* b.fValue - b.gValue));
			CLOSED = new HashSet<>();
			OPEN.add(goal);
			
			/*
			 * Backwards A star - path finding starts at goal and ends at start state
			 */
			if (computePathBackwards(goal, start)) {
				
				/*
				 * Don't need a stack to store the path
				 * Follow pointers from start state
				 */
				//System.out.println("what is start's pointer? " + start.pointer);
				start = followPointers(start, knownMap);
				
				
			}
			else {
				System.out.println("No path found");
				num_invalid_maps++;
				myObj.close();
				break;
			}
			
			
			knownMap[start.x][start.y] = -1;
			
			start.pointer = 0;
			//goal.pointer = 0;
			
		}
		
	}
	
	public static void repeatedAdaptiveAStar(int knownMap[][]) {
		
		State goal = new State(goal_x, goal_y, 0);
		Stack<State> path;
		newHValueMap = new int[X_DIMENSION][Y_DIMENSION];
		
		makeMap(goal);
		
//		knownMap[3][4] = 1;
//		knownMap[2][2] = 1;
//		knownMap[2][3] = 1;
//		knownMap[2][1] = 1;
//		knownMap[2][4] = 1;
//		knownMap[2][0] = 1;
//		knownMap[2][3] = 1;
//		knownMap[3][4] = 1;
//		knownMap[2][2] = 1;
//		knownMap[2][1] = 1;
		
		State start = map[start_x][start_y];
		
		//System.out.println("Adaptive Start cell: "+ start.x + "," + start.y);
		//System.out.println("Adaptive goal cell: "+ goal.x + "," + goal.y);
		
		start.hValue = manhattenValue(goal.x, goal.y, start.x, start.y);
		
		map[goal.x][goal.y] = goal;
		//map[X_DIMENSION-1][Y_DIMENSION-1] = goal;
		counter = 0;
		
		initialCheck(start, knownMap);
		
		if (knownMap[start.x][start.y] == 1) {
			System.out.println("Starting cell is block");
			return;
		}
		if (knownMap[goal.x][goal.y] == 1) {
			System.out.println("goal cell is block");
			return;
		}
		
		
		while (!start.equals(goal)) {
			
			//printMap(knownMap);
			
			Scanner myObj = new Scanner(System.in);
			//String userName = myObj.nextLine();
			//printMap(knownMap);
			
			counter+=1;
			start.gValue = 0;
			goal.gValue = Integer.MAX_VALUE;
			
			
			OPEN = new PriorityQueue<State>(( a, b ) -> 
					(100 * a.fValue - a.gValue) - (100 * b.fValue - b.gValue));
			CLOSED = new HashSet<>();
			OPEN.add(start);
			
			if (computePathAdaptive(start, goal)) {
				
				path = createPath(goal);
				Iterator<State> it = CLOSED.iterator();
				while (it.hasNext()) {
					State n = it.next();
					map[n.x][n.y].iteration = counter;
					newHValueMap[n.x][n.y] = goal.gValue - n.gValue;
				}
			}
			else {
				System.out.println("No path found");
				num_invalid_maps++;
				return;
			}
			
			start = travelPath(start, path, knownMap);
			
			knownMap[start.x][start.y] = -1;
			
			start.pointer = 0;
			start.gValue = 0;
			
		}
		
		//printMap(knownMap);
	}
	
	public static boolean computePath(State start, State goal) {
		
		/*
		 * Given a start state and a goal state, call A* and return true if a path is found
		 * Continue the loop given that the open list is not empty 
		 * 						AND
		 * ***************************************************************************
		 * Line below taken out.
		 * the g value of the goal is larger than the lowest f value in the open list / 
		 */
		
		while (OPEN.peek() != null) {
			
			//Scanner myObj = new Scanner(System.in);
			//String userName = myObj.nextLine();
			
			/*
			 * Retrieve and remove the head of the priority queue
			 * If the removed head is the goal state return true
			 * Add the retrieved state to the closed list
			 */
			
			State s = OPEN.poll();
			
			if (s.equals(goal)) {
				
				return true;
			}
			
			forwardAStarExpandedCells++;
			//System.out.println("Computing path, expanded cell " + s.x + "," +s.y);
			CLOSED.add(s);
			
//			System.out.println("Look inside open list: ");
//			Iterator<State> val = OPEN.iterator();
//			while (val.hasNext()) {
//				System.out.println("	" + val.next());
//	        } 
		
			//System.out.println("expanded cell position: " + s.x + " " + s.y);
			
			/*
			 * Loop through the possible actions the agent can take from state s
			 * Assume actions(s) takes care to ignore observed blocked states, states in the closed list
			 * and out of bounds states
			 */
			
			for (State n: actions(s)) {
				
				//myObj = new Scanner(System.in);
				//userName = myObj.nextLine();
				
				//THIS PART HAS BEEN TAKEN OUT.
				//***************************************************************
				/*
				 * If the search value of the neighbor state is not equal to counter 
				 * THEN
				 * the state has not been explored before in the current iteration of A*
				 * THUS
				 * set it's g value to infinity and mark it as belonging to the present iteration of A*
				 */
				
				/*
				if (n.searchValue < counter) {
					n.gValue = Integer.MAX_VALUE;
					n.searchValue = counter;
				}
				*/
				//***************************************************************

				/*
				 * Remove n from the open list if already found
				 */
				
				boolean found = false;
				State c = n;
				
				Iterator<State> value = OPEN.iterator();
				while (value.hasNext()) {
					c = value.next();
		            if (c.equals(n)) {
		            	found = true;
		            	break;
		            }
		        } 
				
				if ( (found == false) || (found == true && c.fValue > c.hValue + s.gValue + ACTION_COST)) {
					if (found == true) {
						OPEN.remove(c);
					}
					//System.out.println("set pointer of " + n.x + " " + n.y);
					
					/*
					 * Set the g value of the neighbor cell 
					 * Set c's pointer according to which direction, 
					 *  			UP, DOWN, LEFT, RIGHT
					 *  it moved from
					 */
					
					n.gValue = s.gValue + ACTION_COST;
					
					
					if (n.x == s.x+1) {
						n.pointer = 1;
					}
					else if (n.x == s.x-1) {
						n.pointer = 2;
					}
					else if (n.y == s.y+1) {
						n.pointer = 3;
					}
					else {
						n.pointer = 4;
					}
					
					/*
					 * Calculate and set c's f value and add state c to the open list
					 */
					n.fValue = n.gValue + n.hValue;	
					OPEN.add(n);
				}		
			}
				
		}
		
		return false;
	}
	
	public static boolean computePathBackwards(State start, State goal) {

		while (OPEN.peek() != null) {
			
			State s = OPEN.poll();
			
			if (s.equals(goal)) {
				return true;
			}
			
			backwardAStarExpandedCells++;
			
			//System.out.println("Computing path, expanded cell " + s.x + "," +s.y);
			CLOSED.add(s);
			
			//System.out.println("Look inside open list: ");
//			Iterator<State> val = OPEN.iterator();
//			while (val.hasNext()) {
//				System.out.println("	" + val.next());
//	        } 
			
			for (State n: actions(s)) {
				
				boolean found = false;
				State c = n;
				
				Iterator<State> value = OPEN.iterator();
				while (value.hasNext()) {
					c = value.next();
		            if (c.equals(n)) {
		            	found = true;
		            	break;
		            }
		        }
				
				if ( (found == false) || (found == true && c.fValue > c.hValue + s.gValue + ACTION_COST)) {
					if (found == true) {
						OPEN.remove(c);
					}
					
					n.gValue = s.gValue + ACTION_COST;
					
					if (n.x == s.x+1) {
						n.pointer = 1;
					}
					else if (n.x == s.x-1) {
						n.pointer = 2;
					}
					else if (n.y == s.y+1) {
						n.pointer = 3;
					}
					else {
						n.pointer = 4;
					}
					
					n.hValue = manhattenValue(goal.x, goal.y, n.x, n.y);
					n.fValue = n.gValue + n.hValue;	
					OPEN.add(n);
				}
				
			}
			
		}
		return false;
	}
	
	public static boolean computePathAdaptive(State start, State goal) {
		
		while (OPEN.peek() != null) {
			
			
			State s = OPEN.poll();
			
			if (s.equals(goal)) {
				return true;
			}
			
			s.iteration = counter;
			adaptiveAStarExpandedCells++;
			CLOSED.add(s);
			
			for (State n: actions(s)) {
				
				boolean found = false;
				boolean skip = false;
				State c;
				
				Iterator<State> value = OPEN.iterator();
//				while (value.hasNext()) {
//					System.out.println(value.next());
//		        } 
				value = OPEN.iterator();
				while (value.hasNext()) {
					c = value.next();
		            if (c.equals(n)) {
		            	//System.out.println("State n was found in open list");
		            	//System.out.println("n: " + n.x + "," + n.y);
		            	found = true;
		            	if (found == true ) {
		            		//System.out.println("Check counter: " + counter + " n.iteration: " + n.iteration);
							if (n.iteration == counter - 1) {
								if (n.fValue >= newHValueMap[n.x][n.y] + s.gValue + ACTION_COST) {
									//System.out.println("Removing c " + c.x + "," + c.y);
									OPEN.remove(c);
								}
								else {
									skip = true;
								}
							}
							else if (n.fValue >= s.gValue + ACTION_COST + n.hValue) {
								//System.out.println("Removing c " + c.x + "," + c.y);
								OPEN.remove(c);
							}
							else
								skip = true;
						}
		            	break;
		            }
		        } 
				
				if (skip == true) {
					//System.out.println("Skip n " + n.x + "," + n.y);
					continue;
				}
				
				
				if (n.equals(goal)) {
					n.hValue = 0;
					n.gValue = s.gValue + ACTION_COST;
					n.fValue = n.gValue;
				}
				else if (n.iteration == counter - 1) {
					n.gValue = s.gValue + ACTION_COST;
					n.fValue = n.gValue + newHValueMap[n.x][n.y];
					//System.out.println("new heuristic: "+ n.gValue + " + " + newHValueMap[n.x][n.y]);
				}
				
				else {
					n.gValue = s.gValue + ACTION_COST;
					//System.out.println("manhatten: "+ n.gValue + " + " + n.hValue);
					n.fValue = n.gValue + n.hValue;
				}
					
				
				if (n.x == s.x+1) {
					n.pointer = 1;
				}
				else if (n.x == s.x-1) {
					n.pointer = 2;
				}
				else if (n.y == s.y+1) {
					n.pointer = 3;
				}
				else {
					n.pointer = 4;
				}
				
				//System.out.println("Adding n " + n.x + "," + n.y);
				OPEN.add(n);
				
			}
				
		}
		
		return false;
	}
	
	private static ArrayList<State> actions(State s) {
		
		//System.out.println("actions");

		
		ArrayList<State> validNeighbors = new ArrayList<State>();
		int validPaths = 0;
		
		if (s.x+1 < X_DIMENSION && s.x+1 >= 0 && s.y < X_DIMENSION && s.y >= 0) {
			if (!CLOSED.contains(map[s.x+1][s.y]) && observableMap[s.x+1][s.y] == 0) {
					validNeighbors.add(map[s.x+1][s.y]);
					validPaths++;
			}
		}
		if (s.x-1 < X_DIMENSION && s.x-1 >= 0 && s.y < X_DIMENSION && s.y >= 0) {
			if (!CLOSED.contains(map[s.x-1][s.y]) && observableMap[s.x-1][s.y] == 0) {
					validNeighbors.add(map[s.x-1][s.y]);
					validPaths++;
			}
		}
		if (s.x < X_DIMENSION && s.x >= 0 && s.y+1 < X_DIMENSION && s.y+1 >= 0) {
			if (!CLOSED.contains(map[s.x][s.y+1]) && observableMap[s.x][s.y+1] == 0) {
					validNeighbors.add(map[s.x][s.y+1]);
					validPaths++;
			}
		}
		if (s.x < X_DIMENSION && s.x >= 0 && s.y-1 < X_DIMENSION && s.y-1 >= 0) {
			if (!CLOSED.contains(map[s.x][s.y-1]) && observableMap[s.x][s.y-1] == 0) {
					validNeighbors.add(map[s.x][s.y-1]);
					validPaths++;
			}
		}
		
		//System.out.println("For state " + s.x + "," + s.y);
		//System.out.println("	Found " + validPaths + " neighbors");
		
		return validNeighbors;
	}
	
	/*
	 * Create path follows "pointers" set in goal to follow a path back to the robot
	 * "Pointers" is a byte that checks for values 1,2,3,4
	 * 1 - move left
	 * 2 - move right
	 * 3 - move down
	 * 4 - move up
	 */
	
	private static Stack<State> createPath(State goal) {
		
		//System.out.println("createpath");

		
		Stack<State> path = new Stack<State>();
		State currentState = goal;
		path.add(currentState);
		
		
		while (currentState.pointer != 0) {
			//System.out.println("tracing path: " + currentState.x + "," + currentState.y);
			
			if (currentState.pointer == 1) {
				currentState = map[currentState.x - 1][currentState.y];
			}
			else if (currentState.pointer == 2) {
				currentState = map[currentState.x + 1][currentState.y];
			}
			else if (currentState.pointer == 3) {
				currentState = map[currentState.x][currentState.y - 1];
			}
			else {
				currentState = map[currentState.x][currentState.y + 1];
			}
			path.add(currentState);
		}
		return path;
	}
	
	private static State travelPath(State start, Stack<State> path, int knownMap[][]) {
		
		//System.out.println("travelpath");

		
		if (path.isEmpty())
			return start;
		while (!path.isEmpty()) {
			State n = path.pop();
			//System.out.print(n.x + "," + n.y + " ");
			if (knownMap[n.x][n.y] == 1) {
				//System.out.println("travelpath found blocked cell");
				observableMap[n.x][n.y] = 1;
				return start;
			}
			initialCheck(map[n.x][n.y], knownMap);
			knownMap[n.x][n.y] = -1;
			start = n;
		}
		//System.out.println();
		return start;
	}
	
	private static State followPointers(State start, int knownMap[][]) {

		State temp = start; int x,y;
		while (temp.pointer != 0) {
			//System.out.println("	Robot on cell " + temp.x + "," + temp.y + " ");
			if (temp.pointer == 1) {
				x = temp.x-1; y = temp.y;
			}
			else if (temp.pointer == 2) {
				x = temp.x+1; y = temp.y;
			}
			else if (temp.pointer == 3) {
				x = temp.x; y = temp.y-1;
			}
			else if (temp.pointer == 4) {
				x = temp.x; y = temp.y+1;
			}
			else {
				System.out.println("Uh oh...pointer set to invalid direction...");
				Scanner myObj = new Scanner(System.in);
				//myObj.nextLine();
				myObj.close();
				return null;
			}
			
			initialCheck(map[temp.x][temp.y], knownMap);
			knownMap[temp.x][temp.y] = -1;
			
			if (knownMap[x][y] == 1) {
				//System.out.println("	Robot tried to walk blocked cell " + x + "," + y + "!");
				observableMap[x][y] = 1;
				return temp;
			}
			
			temp = map[x][y];
		}
		return temp;
	}
	
	public static void printMap(int [][]map) {
		System.out.print(" ");
		for (int i = 0; i < X_DIMENSION; i++) {
			System.out.print("1");
		}
		System.out.println();
		for (int i = 0; i < X_DIMENSION; i++) {
			System.out.print("1");
			for (int j = 0; j < Y_DIMENSION; j++) {
				if (j == start_x && i == start_y) {
					System.out.print("S");
				}
				else if (j == goal_x && i == goal_y) {
					System.out.print("G");
				}
				else if (map[j][i] == 0)
					System.out.print(" ");
				else if (map[j][i] == 1)
					System.out.print("X");
				else 
					System.out.print(".");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
	/*
	 * From the starting cell, remember neighboring blocked cells before starting A*
	 */
	
	private static void initialCheck(State start, int knownMap[][]) {
		int x = start.x; int y = start.y;
		
		if (x+1 < X_DIMENSION && y < Y_DIMENSION && knownMap[x+1][y] == 1) {
			observableMap[x+1][y] = 1;
		}
		if (x-1 >=0 ) {
			if (x-1 < X_DIMENSION && y < Y_DIMENSION && knownMap[x-1][y] == 1) {
				observableMap[x-1][y] = 1;
			}
		}
		if (x < X_DIMENSION && y+1 < Y_DIMENSION && knownMap[x][y+1] == 1) {
			observableMap[x][y+1] = 1;
		}
		if (y-1 >= 0) {
			if (x < X_DIMENSION && y-1 < Y_DIMENSION && knownMap[x][y-1] == 1) {
				observableMap[x][y-1] = 1;
			}
		}
	}
	
	/* 
	 *  Initialize the "knownMap" by marking blocked cells as 1 with probability < 30
	 *  Store a "observableMap" for the robot to remember blocked cells
	 *  Create the "State map" to store states of each cell
	 */
	
	private static void makeMap(State goal) {
		//knownMap = new int[X_DIMENSION][Y_DIMENSION];
		observableMap = new int[X_DIMENSION][Y_DIMENSION];
		map = new State[X_DIMENSION][Y_DIMENSION];
		
		for (int i = 0; i < X_DIMENSION; i++) {
			for (int j = 0; j < Y_DIMENSION; j++) {
				//knownMap[i][j] = rng.nextInt(100) < 10 ? 1:0;
				observableMap[i][j] = 0;
				
				map[i][j] = new State(i, j, manhattenValue(i, j, goal.x, goal.y));
			}
		}
	}
	
	public static int[][] generateMap() {
		int[][] mapp= new int[101][101];
		for (int i = 0; i < 101; i++) {
			for (int j = 0; j < 101; j++) {
				mapp[i][j] = rng.nextInt(100) < 25 ? 1:0;
				
			}
		}
		mapp[0][0] = 0;
		mapp[100][100] = 0;
		return mapp;
	}
	
}
