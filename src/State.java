
public class State {
	
	int gValue;
	int hValue;
	int fValue;
	int x, y;
	int iteration = Integer.MIN_VALUE;
	byte pointer;
	
	public State(int x, int y, int gValue, int hValue) {
		this.x = x;
		this.y = y;
		this.gValue = gValue;
		this.hValue = hValue;
	}
	
	public State(int x, int y, int hValue) {
		this(x, y, 0, hValue);
	}
	
	public String toString(){
		return "[x: " + x + " y: "+ y + "] with f: " + fValue + " with g: " + gValue + "with h: " + hValue;
	}
	
	@Override
	public boolean equals(Object c) {
		if(this == null || c == null)
			return false;
		if (this.getClass() != c.getClass()) 
			return false;
		State cc = (State) c;
					
		if(this.x == cc.x && this.y == cc.y){
			return true;
		}
		return false;
	}
	
}
