import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class FTR {
	
	/*
	 * Set the dimensions of the grid world
	 */
	
	static int knownMap[][];
	static int observableMap[][];
	static State map[][];
	static int X_DIMENSION = 10;
	static int Y_DIMENSION = 10;
	
	/*
	 * Initialize a priority queue for the open list 
	 * and a HashSet for the closed list
	 */
	
	static PriorityQueue<State> OPEN;
	static HashSet<State> CLOSED;
	
	/*
	 * Initialize a random instance for creating a maze
	 * Create a counter to remember current iteration of A*
	 */
	
	static Random rng = new Random();
	static int counter;
	static int ACTION_COST = 1;
	
	/*
	 * Where the main program will run
	 */
	
	public static void main(String[] args) {
		repeatedForwardAStar();
	}
	
	/*
	 * The heuristic function
	 */
	
	public static int manhattenValue(int x1, int y1, int x2, int y2) {
		return Math.abs(x1-x2) + Math.abs(y1-y2);
	}
	
	public static void repeatedForwardAStar() {
		
		/*
		 * Manually configure the goal state for now
		 * Declare a stack to hold states belonging to shortest path
		 */
		
		State goal = new State(X_DIMENSION-1, Y_DIMENSION-1, 0);
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
		
		/*
		 * Manually configure the start state for now
		 * Have our map remember the instance of goal declared above
		 * Set the current iteration of A* to 0
		 */
		
		State start = map[2][4];
		map[X_DIMENSION-1][Y_DIMENSION-1] = goal;
		counter = 0;
		
		/*
		 * Check start cell neighbors
		 * Check if starting cell is a blocked cell
		 * Begin the repeated A Star
		 * Break the loop if the agent finds the goal
		 */
		
		initialCheck(start);
		
		if (knownMap[start.x][start.y] == 1) {
			System.out.println("Starting cell is block");
			return;
		}
		
		while (!start.equals(goal)) {
			
			/*
			 * Use scanner to block once travelPath is called
			 * Press enter to unblock and move to next iteration of A*
			 * This will make analyzing each iteration of A* easier
			 */
			
			Scanner myObj = new Scanner(System.in);
			String userName = myObj.nextLine();
			printMap();
			
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
			start.searchValue = counter;
			goal.searchValue = counter;
			
			//COUNTER AND SEARCH IS NEVER USED IN THIS CODE
			//**********************************************************
			
			/*
			 * Open list and closed list "forget" previous iteration of expanded cells
			 * Insert starting cell inside open list
			 */
			
			OPEN = new PriorityQueue<State>(( a, b ) -> 
					(100 * a.fValue - a.gValue) - (100 * b.fValue - b.gValue));
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
				
				System.out.println("No path found");
				return;
			}
			
			/*
			 * If we reach this section the loop has not been broken and thus a path was found and stored 
			 * Have the agent travel the path
			 * Travel path will return which state the agent will end up
			 * If the agent reached the goal, travel path returns the goal state
			 * If the agent found a blocked cell in its path, return the state just before encountering 
			 */
			
			start = travelPath(start, path);
			
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
		
		printMap();		
		
	}
	
	public static boolean computePath(State start, State goal) {
		
		//System.out.println("computepath");
		
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
				//myObj.close();
				return true;
			}
			
			CLOSED.add(s);
		
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
				
				State c = map[n.x][n.y];
				if (OPEN.contains(n))
					OPEN.remove(n);
				
				//System.out.println("set pointer of " + n.x + " " + n.y);
				
				/*
				 * Set the g value of the neighbor cell 
				 * Set c's pointer according to which direction, 
				 *  			UP, DOWN, LEFT, RIGHT
				 *  it moved from
				 */
				
				c.gValue = s.gValue + ACTION_COST;
				
				
				if (c.x == s.x+1) {
					c.pointer = 1;
				}
				else if (c.x == s.x-1) {
					c.pointer = 2;
				}
				else if (c.y == s.y+1) {
					c.pointer = 3;
				}
				else {
					c.pointer = 4;
				}
				
				/*
				 * Calculate and set c's f value and add state c to the open list
				 */
				c.fValue = c.gValue + c.hValue;			
				OPEN.add(c);
				
			}
				
		}
		
		return false;
	}
	
	private static ArrayList<State> actions(State s) {
		
		//System.out.println("actions");

		
		ArrayList<State> validNeighbors = new ArrayList<State>();
		
		if (s.x+1 < X_DIMENSION && s.x+1 >= 0 && s.y < X_DIMENSION && s.y >= 0)
			if (!CLOSED.contains(map[s.x+1][s.y]) && observableMap[s.x+1][s.y] == 0)
					validNeighbors.add(map[s.x+1][s.y]);
		if (s.x-1 < X_DIMENSION && s.x-1 >= 0 && s.y < X_DIMENSION && s.y >= 0)
			if (!CLOSED.contains(map[s.x-1][s.y]) && observableMap[s.x-1][s.y] == 0)
					validNeighbors.add(map[s.x-1][s.y]);
		if (s.x < X_DIMENSION && s.x >= 0 && s.y+1 < X_DIMENSION && s.y+1 >= 0)
			if (!CLOSED.contains(map[s.x][s.y+1]) && observableMap[s.x][s.y+1] == 0)
					validNeighbors.add(map[s.x][s.y+1]);
		if (s.x < X_DIMENSION && s.x >= 0 && s.y-1 < X_DIMENSION && s.y-1 >= 0)
			if (!CLOSED.contains(map[s.x][s.y-1]) && observableMap[s.x][s.y-1] == 0)
					validNeighbors.add(map[s.x][s.y-1]);
		
		for (State o: validNeighbors) {
			//System.out.println("neighbors added: " + o.x + " " + o.y);
		}
		
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
	
	private static State travelPath(State start, Stack<State> path) {
		
		//System.out.println("travelpath");

		
		if (path.isEmpty())
			return start;
		while (!path.isEmpty()) {
			State n = path.pop();
			System.out.print(n.x + ", " + n.y + " ");
			if (knownMap[n.x][n.y] == 1) {
				System.out.println("travelpath found blocked cell");
				observableMap[n.x][n.y] = 1;
				return start;
			}
			knownMap[n.x][n.y] = -1;
			start = n;
		}
		System.out.println();
		return start;
	}
	
	private static void printMap() {
		System.out.print(" ");
		for (int i = 0; i < X_DIMENSION; i++) {
			System.out.print(i);
		}
		System.out.println();
		for (int i = 0; i < X_DIMENSION; i++) {
			System.out.print(i);
			for (int j = 0; j < Y_DIMENSION; j++) {
				if (knownMap[j][i] == 0)
					System.out.print(" ");
				else if (knownMap[j][i] == 1)
					System.out.print("X");
				else 
					System.out.print("S");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/*
	 * From the starting cell, remember neighboring blocked cells before starting A*
	 */
	
	private static void initialCheck( State start) {
		int x = start.x; int y = start.y;
		
		if (x+1 < X_DIMENSION && y < Y_DIMENSION && knownMap[x+1][y] == 1) {
			observableMap[x+1][y] = 1;
		}
		if (x-1 < X_DIMENSION && y < Y_DIMENSION && knownMap[x-1][y] == 1) {
			observableMap[x-1][y] = 1;
		}
		if (x < X_DIMENSION && y+1 < Y_DIMENSION && knownMap[x][y+1] == 1) {
			observableMap[x][y+1] = 1;
		}
		if (x < X_DIMENSION && y-1 < Y_DIMENSION && knownMap[x][y-1] == 1) {
			observableMap[x][y-1] = 1;
		}
	}
	
	/* 
	 *  Initialize the "knownMap" by marking blocked cells as 1 with probability < 30
	 *  Store a "observableMap" for the robot to remember blocked cells
	 *  Create the "State map" to store states of each cell
	 */
	
	private static void makeMap(State goal) {
		knownMap = new int[X_DIMENSION][Y_DIMENSION];
		observableMap = new int[X_DIMENSION][Y_DIMENSION];
		map = new State[X_DIMENSION][Y_DIMENSION];
		
		for (int i = 0; i < X_DIMENSION; i++) {
			for (int j = 0; j < Y_DIMENSION; j++) {
				knownMap[i][j] = rng.nextInt(100) < 10 ? 1:0;
				observableMap[i][j] = 0;
				
				map[i][j] = new State(i, j, manhattenValue(i, j, goal.x, goal.y));
			}
		}
	}
	

	
}
