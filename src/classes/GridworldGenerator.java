package classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

public class GridworldGenerator {
	
	private static Gridworld gridworld;
	private static HashSet<Coord> notVisitedCells;
	private static Stack<Coord> stack;
	private static Random rng = new Random();
	
	static int GRIDWORLD_X_DIMENTION;
	static int GRIDWORLD_Y_DIMENTION;
	
	public static Gridworld generate(int x, int y) {
		GRIDWORLD_X_DIMENTION = x;
		GRIDWORLD_Y_DIMENTION = y;
		gridworld = new Gridworld(GRIDWORLD_X_DIMENTION,GRIDWORLD_Y_DIMENTION);
		stack = new Stack<>();
	
		hydrateNotVisitedCells();
		
		Coord curr = new Coord(rng.nextInt(GRIDWORLD_X_DIMENTION), rng.nextInt(GRIDWORLD_Y_DIMENTION));
		notVisitedCells.remove(curr);

		while(true) {
			curr = traverse(curr);
			
			if(curr == null) {
			
				if(notVisitedCells.size() == 0) {
					break;
				}
				int i = rng.nextInt(notVisitedCells.size());
				curr = (Coord) notVisitedCells.toArray()[i];
			}
		}
		return gridworld;
	}
	
	private static Coord traverse(Coord curr) {
		Coord nextMove = generateNextValidMove(curr);
		//dead-end condition checking
		if(nextMove == null){
			notVisitedCells.remove(curr);
			return stack.size() != 0 ? stack.pop() : null;
		}
		//determine if a random neighbor is a blocker or not based on set probability.
		boolean isCellABlocker = rng.nextInt(101) < 20 ? true : false;
		
		if(isCellABlocker) {
			gridworld.setBlocker(nextMove);
		}else {
			stack.add(curr);
		}
		notVisitedCells.remove(nextMove);
	
		return isCellABlocker ? curr : nextMove;
	}
	
	private static void hydrateNotVisitedCells() {
		notVisitedCells = new HashSet<>();
		for (int i = 0 ; i < GRIDWORLD_X_DIMENTION; i++) {
			for (int j = 0; j < GRIDWORLD_Y_DIMENTION; j++) {
				notVisitedCells.add(new Coord(i,j));
			}
		}
	}
	
	private static Coord generateNextValidMove(Coord curr) {
		//neighbors are just the coords of the 4 neighbors relative to curr.
		ArrayList<Coord> neighbors = new ArrayList<>();
		int x = curr.getX();
		int y = curr.getY();
		neighbors.add(new Coord(x+1, y));
		neighbors.add(new Coord(x-1, y));
		neighbors.add(new Coord(x, y+1));
		neighbors.add(new Coord(x, y-1));
		
		//legalMoves are neighbors that are in bound and are not visited yet.
		ArrayList<Coord> legalMoves = new ArrayList<>();
		for (Coord c : neighbors) {
			if(c.getX() > 0 || c.getY() > 0 || c.getX() < GRIDWORLD_X_DIMENTION || c.getY() < GRIDWORLD_Y_DIMENTION ) {
				if(notVisitedCells.contains(c)) {
					legalMoves.add(c);
				}
			}
		}
		
		return legalMoves.size() > 0 ? legalMoves.get(rng.nextInt(legalMoves.size())) : null;		
	}
	
}
