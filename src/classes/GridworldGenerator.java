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
	
	public static Gridworld generate() {
		gridworld = new Gridworld();
		stack = new Stack<>();
	
		hydrateNotVisitedCells();
		
		Coord curr = new Coord(rng.nextInt(101), rng.nextInt(101));
		notVisitedCells.remove(curr);

		while(true) {
			curr = traverse(curr);
			
			if(curr == null) {
				int i = rng.nextInt(notVisitedCells.size());
				if(i == 0) {
					break;
				}
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
		
		//determine if a  random neighbor is a blocker or not based on 70/30 probability.
		boolean isCellABlocker = rng.nextInt(101) < 25 ? true : false;
		
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
		for (int i = 0 ; i < 101; i++) {
			for (int j = 0; j < 101; j++) {
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
			if(c.getX() > 0 || c.getY() > 0 || c.getX() < 101 || c.getY() < 101 ) {
				if(notVisitedCells.contains(c)) {
					legalMoves.add(c);
				}
			}
		}
		
		return legalMoves.size() > 0 ? legalMoves.get(rng.nextInt(legalMoves.size())) : null;		
	}
	
}
