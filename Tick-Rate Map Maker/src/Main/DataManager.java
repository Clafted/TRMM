package Main;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Stack;

public class DataManager {
	private static DataManager instance;

	//Texture Palette variables
	private static Color[] defaultTexturePalette = new Color[]{new Color(20, 80, 10), new Color(20, 25, 35)};
	private static Color[] texturePalette;

	//Map Variables
	private static int[] defaultMapData = new int[] {
			0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			0, 0,-1,-1,-1, 0, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			0, 0, 0, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	};
	private static Map defaultMap = new Map(defaultMapData, 25, 15, defaultTexturePalette);
	private static Map map;



	//IO Variables
	private static FileWriter fileWriter;
	private static File currentFile;

	//Action variables
	private static Stack<Map> undoStack;
	private static Stack<Map> redoStack;

	//A class to load, edit, and save data of the map being drawn.
	public DataManager()
	{
		//LOAD ANY EXISTING DATA
		loadMap("Maps/Map.txt");
		undoStack = new Stack<>();
		redoStack = new Stack<>();
	}

	//------------------------------------------------------------------------------------

	//GETTTERS
	public static int[] MapData() {return map.data;}
	public static int MapWidth() {return map.Width();}
	public static int MapHeight() {return map.Height();}
	public static Color[] TexturePalette() { return texturePalette; }
	public static DataManager GetInstance()
	{
		if(instance == null) instance = new DataManager();
		return instance;
	}

	//------------------------------------------------------------------------------------

	//IO METHODS
	//Save map data to a file (return true if successful, false otherwise)
	public static boolean export()
	{
		String data = "";

		try {
			//Write data to the currently accessed map file
			fileWriter = new FileWriter(new File("Exports/MapExport.txt"));

			//Concatenate texture palette.
			data += "textPal: " + texturePalette.length + "\n";
			for(int i = 0; i < texturePalette.length; i++)
			{
				data += texturePalette[i].getRed() + " " + texturePalette[i].getGreen() + " " + texturePalette[i].getBlue() + "\n";
			}

			//Concatenate map dimensions.
			data += "W:" + map.Width() + "\tH:" + map.Height() + "\n";

			//Concatenate map data.
			data += "Map:\n";
			for(int i = 0; i < map.data.length; i++)
			{
				data += map.data[i] + ((map.data[i] > -1) ? "  " : " ");
				if(i % map.Width() == map.Width() - 1) data += "\n";
			}

			fileWriter.write(data);
			fileWriter.close();

			System.out.println("Successfully saved file :)");

		}catch(IOException e)
		{
			System.out.println("Unable to access " + currentFile.getName() + ". Failed to save data :(");
			return false;
		}
		return true;
	}

	//Load a map with a given file
	public static void loadMap(String file)
	{
		//Load map file
		try {
			currentFile = new File(file);
			if(currentFile.exists())
			{
				System.out.println("Loading existing map");
				FileInputStream fIS = new FileInputStream(currentFile);
				ObjectInputStream oIS = new ObjectInputStream(fIS);
				
				map = (Map) oIS.readObject();
				texturePalette = map.TexturePalette();
				
				oIS.close();
				fIS.close();
				return;
			}else {
				System.out.println("Using default map.");
			}
		}catch(IOException e)
		{
			System.out.println("Trouble accessing file!\nUsing default map.");
			e.printStackTrace();
		}catch(Exception e)
		{
			System.out.println("File is corrupt!\nUsing default map.");
			e.printStackTrace();
		}
		
		loadDefaultMap();
	}

	//Save the map object to a file
	public static void saveMap()
	{
		try
		{
			FileOutputStream fOS = new FileOutputStream(currentFile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);
			
			oOS.writeObject(map);
			oOS.close();
			fOS.close();
		}catch(Exception e)
		{
			System.out.println("Unable to save data!");
		}
	}
	
	//------------------------------------------------------------------------------------

	//MAP DRAWING METHODS
	//Change the type of tile at a given index
	public static void changeTile(int x, int y, int value)
	{
		if(x >= 0 && x < map.Width() && y >= 0 && y < map.Height())
			map.data[x + (y * map.Width())] = value;
	}

	//Load the default map.
	public static void loadDefaultMap()
	{
		map = defaultMap;
		texturePalette = map.TexturePalette();
	}

	//Resizes the map by the given values (REPLACES MAP)
	public static void resizeMap(int xInc, int yInc)
	{
		undoStack.add(map);
		if(xInc == 0 && yInc == 0) return;

		//Calculate and create new map dimensions and array;
		int newWidth = map.Width() + xInc;
		int newHeight = map.Height() + yInc;
		int[] newData = new int[newWidth * newHeight];

		//Transfer mapData to a newly size array at a specific index range
		for(int i = 0, o = (yInc < 0) ? -yInc * map.Width() : 0; i < newData.length; i++)
		{
			//Skip iteration if i isn't within 0 and (mapWidth * mapHeight) - 1
			if(i / newWidth < yInc || i % newWidth >= map.Width())
			{
				newData[i] = -1;
			}else{
				//Add existing mapData to newData;
				newData[i] = map.data[o];

				o++;
				if(xInc < 0 && i % newWidth == map.Width() - -xInc - 1) o += -xInc;
			}
		}

		map = new Map(newData, newWidth, newHeight, map.TexturePalette());
	}

	//Creates a new instance of Map and saves the previous state to the undoStack.
	public static void updateStack()
	{
		undoStack.add(new Map(map.data.clone(), map.Width(), map.Height(), map.TexturePalette()));
	}

	//Undo a previous edit to the map
	public static void undoAction()
	{
		if(undoStack.empty()) return;
		redoStack.add(new Map(map.data.clone(), map.Width(), map.Height(), map.TexturePalette()));
		map = undoStack.pop();
	}

	//Redo an undo
	public static void redoAction()
	{
		if(redoStack.empty()) return;
		undoStack.add(new Map(map.data.clone(), map.Width(), map.Height(), map.TexturePalette()));
		map = redoStack.pop();
	}
}
