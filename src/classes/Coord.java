package classes;

public class Coord {
	private int x;
	private int y;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	@Override
	public boolean equals(Object c) {
		if(this == c)
			return true;
		if(this == null || c == null)
			return false;
		if (this.getClass() != c.getClass()) 
			return false;
		Coord cc = (Coord) c;
					
		if(this.getX() == cc.getX() && this.getY() == cc.getY()){
			return true;
		}
		return false;
	}
	
	//hashcode found here: https://stackoverflow.com/questions/22826326/good-hashcode-function-for-2d-coordinates
	public int hashCode() {
	      int tmp = (y +  ((x+1)/2));
	      return x +(tmp*tmp);
	}
	

}
