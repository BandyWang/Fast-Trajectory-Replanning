package classes;

public class Gridworld {
	
	private Cell[][] gridworld;
	
	private Coord startingCoord;
	
	
	public Gridworld(int x, int y) {
		gridworld = new Cell[x][y]; 
		for(int i = 0 ; i < x; i++){
			for(int j = 0; j < y; j++) {
				gridworld[i][j] = new Cell();
			}
		}
	}
	
	//testing constructor
	public Gridworld() {

		gridworld = new Cell[5][6];
		for(int i = 0 ; i < 5; i++){
			for(int j = 0; j < 6; j++) {
				gridworld[i][j] = new Cell();
			}
		}
		this.setBlocker(new Coord(1,0));
		this.setBlocker(new Coord(1,2));
		this.setBlocker(new Coord(2,2));
		this.setBlocker(new Coord(1,4));
		
		this.setBlocker(new Coord(2,4));
		this.setBlocker(new Coord(3,4));
		
		this.setBlocker(new Coord(3,1));
		this.setBlocker(new Coord(3,2));
		this.setBlocker(new Coord(3,3));
		this.setBlocker(new Coord(3,5));
		
	}
	
	public Cell getCell(Coord c) {
		return gridworld[c.getX()][c.getY()];
	}
	
	public void setStartingCoord(int x, int y) {
		this.startingCoord = new Coord(x, y);
	}
	
	public Coord getStartingCoord() {
		return this.startingCoord;
	}
	
	public boolean isCellABlocker(Coord c) {
		return gridworld[c.getX()][c.getY()].isBlocker();
	}
	
	public void setBlocker(Coord c) {
		gridworld[c.getX()][c.getY()].makeBlocker();
	}
	
	public void setTraversed(Coord c) {
		gridworld[c.getX()][c.getY()].setTraversed();
	}
	
	public int getXDimention(){
		return this.gridworld.length;
	}
	public int getYDimention(){
		return this.gridworld[0].length;
	}
	
	public void printGridworld(){
		System.out.println();
		for(int i = 0; i <= gridworld[0].length;i++) {
			if(i == 0  ){
				System.out.print(" ");
			}else {
				System.out.print("-");
			}
		}
		
		for(int i = 0; i < gridworld.length; i++) {
			System.out.println();
			System.out.print("|");
			for(int j = 0; j < gridworld[i].length; j++) {
				if(this.gridworld[i][j].isBlocker()){
					System.out.print("#");
				}else if (this.gridworld[i][j].isTraversed()) {
					System.out.print("0");
				}else {
					System.out.print(" ");
				}
			}
			System.out.print("|");
		}
		System.out.println();
		for(int i = 0; i <= gridworld[0].length;i++) {
			if(i == 0  ){
				System.out.print(" ");
			}else {
				System.out.print("-");
			}
		}
		System.out.println();
	}
}
