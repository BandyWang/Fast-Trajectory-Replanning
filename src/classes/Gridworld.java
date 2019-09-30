package classes;

public class Gridworld {
	private Cell[][] gridworld = new Cell[101][101];
	
	private Coord startingCoord;
	
	public Gridworld() {
		for(int i = 0 ; i < 101; i++){
			for(int j = 0; j < 101; j++) {
				gridworld[i][j] = new Cell();
			}
		}
	}
	public void setStartingCoord(int x, int y) {
		this.startingCoord = new Coord(x, y);
	}
	
	public Coord getStartingCoord() {
		return this.startingCoord;
	}
	
	public void setBlocker(Coord c) {
		gridworld[c.getX()][c.getY()].makeBlocker();
	}
	
	public void printGridworld(){
		
		for(int i = 0; i < 101; i++) {
			System.out.println();
			for(int j = 0; j < 101; j++) {
				if(this.gridworld[i][j].isBlocker()){
					System.out.print("#");
				}else {
					System.out.print(" ");
				}
			}
		}
		
	}
}
