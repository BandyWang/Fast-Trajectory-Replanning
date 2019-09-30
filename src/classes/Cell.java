package classes;

public class Cell {
	private boolean isBlocker;
	
	public void makeBlocker() {
		this.isBlocker = true;
	}
	
	public boolean isBlocker() {
		return this.isBlocker;
	}
}
