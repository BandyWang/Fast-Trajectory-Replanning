package classes;
/**
 *  Object that holds the f value of a specfic coordinate.
 *  Used for maxHeap in A*
 * @author Bandy Wang
 *
 */
public class fValueObject {
	Coord coord;
	int fValue;
	
	public fValueObject(Coord coord, int f){
		this.coord = coord;
		this.fValue = f;
	}
	
	public Coord getCoord() {
		return this.coord;
	}
	
	public int getFValue(){
		return this.fValue;
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
					
		if(this.getCoord().equals(cc)){
			return true;
		}
		return false;
	}
	
	public String toString(){
		return "Coord: " + coord.toString() + " f: "+ fValue;
	}
}
