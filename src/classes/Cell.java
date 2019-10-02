package classes;

public class Cell {

	private boolean isBlocker;
	
	private boolean isTraversed;
	
	public void makeBlocker() {
		this.isBlocker = true;
	}
	
	public boolean isBlocker() {
		return this.isBlocker;
	}

	public boolean isTraversed() {
		return isTraversed;
	}

	public void setTraversed() {
		this.isTraversed = true;
	}
}
