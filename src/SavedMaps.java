import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

	/*
	 * visible functions:  SavedMaps.getMaps()
	 * 					  
	 */
public class SavedMaps {
	private static String FILENAME = "savedMaps.txt";
	
	/**
	 * Retrieves all saved maps from local file. If there is nothing saved, or if there 
	 * are not 50 maps found, generate 50 new maps and save them to the local file.
	 * @return	Returns an arraylist of maps in form of 2dArrays.
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static List<int[][]> getMaps() throws IOException  {
		 List<int[][]> maps = null;
		try {
			FileInputStream file = new FileInputStream(FILENAME); 
	        ObjectInputStream in = new ObjectInputStream(file);
	        maps = (List<int[][]>) in.readObject();
	        in.close();
	        file.close();
		} catch (Exception e) {
			maps = generate50Maps();
        	serialize(maps);
		}
        
        if(maps == null || maps.size() != 50) {
        	maps = generate50Maps();
        	serialize(maps);
        }
        
        return maps;
	}
	
	private static void serialize(Object o) throws IOException {
		FileOutputStream file = new FileOutputStream(FILENAME); 
        ObjectOutputStream out = new ObjectOutputStream(file); 
        out.writeObject(o); 
      
        out.close(); 
        file.close();
	}
	
	private static List<int[][]> generate50Maps() throws IOException {
		List<int[][]> maps = new ArrayList<>();
		for(int i = 0; i < 50; i++) {
			maps.add(FTR.generateMap());
		}
		return maps;
	}
	
	/*
	 * Testing purposes only
	 */
	public static void main(String[] args) throws Throwable {
		List<int[][]> maps = getMaps();
		for(int[][] m : maps) {
			FTR.printMap(m);
		}
	}
	
}
