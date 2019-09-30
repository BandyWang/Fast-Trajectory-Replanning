import classes.Gridworld;
import classes.GridworldGenerator;

public class testing {
	public static void main(String[] a){
		Gridworld test = GridworldGenerator.generate();
		test.printGridworld();
		//System.out.println("no inf loop");
	}

}
