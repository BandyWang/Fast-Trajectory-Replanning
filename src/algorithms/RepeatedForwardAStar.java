package algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import classes.Coord;
import classes.Gridworld;
import classes.GridworldGenerator;
import classes.fValueObject;

public class RepeatedForwardAStar {
	static Gridworld grid;
	
	static private int[][] g;
	static private int[][] search;
	
	static PriorityQueue<fValueObject> OPEN;
	static HashSet<Coord> CLOSED;
	static Hashtable<Coord, Coord> tree;
	static Stack<Coord> path;
	
	//names used in the pseudeocode in to documents. Start actually means current cell.
	static Coord start;
	static Coord goal;
	static int counter;
	
	static int ACTION_COST = 1;
	
	//set grid dimentions here
	static int GRIDWORLD_X_DIMENTION = 25;
	static int GRIDWORLD_Y_DIMENTION = 25;
	
	public static void main(String[] args) {
		Gridworld test = GridworldGenerator.generate(GRIDWORLD_X_DIMENTION, GRIDWORLD_Y_DIMENTION);
		test.printGridworld();
		enact(test, new Coord(0,0), new Coord (GRIDWORLD_X_DIMENTION - 1,GRIDWORLD_Y_DIMENTION -1));

		/*enact(new Gridworld(), new Coord(0,5),
				new Coord (GRIDWORLD_X_DIMENTION - 1,GRIDWORLD_Y_DIMENTION -1));
		System.out.println("\nDone");
		*/
	}
	
	public static void enact(Gridworld grid, Coord init, Coord goal) {
		RepeatedForwardAStar.goal = goal;
		RepeatedForwardAStar.grid = grid;
		RepeatedForwardAStar.start = init;
		counter = 0;

		path = new Stack<Coord>();
		intalizeDataStructures(goal);
		
		while(!start.equals(goal)) {
			counter = counter + 1;
			setGValue(start, 0);
			setSearchValue(start, counter);
			setGValue(goal, Integer.MAX_VALUE);
			setSearchValue(goal, counter);
			OPEN = new PriorityQueue<fValueObject>(( x, y ) -> x.getFValue() - y.getFValue());
			CLOSED = new HashSet<>();
			
			int f = getGValue(start) != Integer.MAX_VALUE ? 
					getGValue(start) + getHValue(start) :  Integer.MAX_VALUE;
			
			OPEN.add(new fValueObject(start, f));
		
			ComputePath();			
			if(OPEN.isEmpty()) {
				System.out.println("Can not find target");
				break;
			}
			createPath();
			
			start = movePath();
		}
		grid.printGridworld();
	}
	
	private static void ComputePath() {
		while(OPEN.peek() != null && getGValue(goal) > OPEN.peek().getFValue()) {
			fValueObject polled = OPEN.poll();
			Coord s = polled.getCoord();
			CLOSED.add(s);
			for(Coord a : actions(polled.getCoord())) {
				if (getSearchValue(a) < counter) {
					setGValue(a , Integer.MAX_VALUE);
					setSearchValue(a, counter);
				}	
				if (getGValue(a) > getGValue(s) + ACTION_COST) {
					setGValue(a, getGValue(s) + ACTION_COST);
					tree.put(a, s);
					
					if(OPEN.contains(a)) {
						OPEN.remove(a);
					}
					OPEN.add(new fValueObject(a, getGValue(a) + getHValue(a) ));
				}
			}
		}
	}
	
	private static void intalizeDataStructures(Coord goal) {
		search = new int[GRIDWORLD_X_DIMENTION][GRIDWORLD_Y_DIMENTION];
		g = new int[GRIDWORLD_X_DIMENTION][GRIDWORLD_Y_DIMENTION];
	
		for(int i = 0; i < GRIDWORLD_X_DIMENTION; i++) {
			for(int j = 0; j < GRIDWORLD_Y_DIMENTION; j++) {
				g[i][j] = Integer.MAX_VALUE;
				search[i][j] = 0;
			}
		}
		tree = new Hashtable<>();
		tree.put(start, start);		
	}
	
	private static ArrayList<Coord> actions(Coord coord) {
		ArrayList<Coord> neighbors = new ArrayList<>();
		int x = coord.getX();
		int y = coord.getY();
		neighbors.add(new Coord(x+1, y));
		neighbors.add(new Coord(x-1, y));
		neighbors.add(new Coord(x, y+1));
		neighbors.add(new Coord(x, y-1));
		ArrayList<Coord> legalMoves = new ArrayList<>();
		for (Coord c : neighbors) {
			if(c.getX() >= 0 && c.getY() >= 0 && c.getX() < GRIDWORLD_X_DIMENTION && c.getY() < GRIDWORLD_Y_DIMENTION ) {
				if(!grid.isCellABlocker(c) && !CLOSED.contains(c)) {
					legalMoves.add(c);
				}
			}
		}	
		return legalMoves;
	}
	
	private static void createPath() {
		Coord ptr = goal;
		path.push(goal);

		while(ptr != start){
			Coord value = tree.get(ptr);
			path.push(value);
			ptr = value;
		}
	}
	
	private static Coord movePath() {
		Coord curr = start;
		while(!path.isEmpty()){
			curr = path.pop();
			grid.setTraversed(curr);
		}
		return curr;
	}
	
	// Calculates the Manhatten distance between 2 points. 
	private static int getHValue(Coord p1) {
		return Math.abs(p1.getX() - goal.getX()) +  
				Math.abs(p1.getY() - goal.getY());
	}
	
	private static void setSearchValue(Coord s, int value) {
		search[s.getX()][s.getY()] = value;
	}
	
	private static int getSearchValue(Coord c) {
		return search[c.getX()][c.getY()];
	}

	private static void setGValue(Coord s, int value) {
		g[s.getX()][s.getY()] = value;
	}
	
	private static int getGValue(Coord c) {
		return g[c.getX()][c.getY()];
	}
}
