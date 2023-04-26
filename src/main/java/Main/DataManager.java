package Main;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Stack;


public class DataManager {
	private static DataManager instance;

	//Map Variables
	private static Map map;

	//IO Variables
	private static FileWriter fileWriter;

	//File directories
	private static File MapMakerRoot, exportsFolder, savedMapsFile;

	//Existing save files
	private static ArrayList<Map> savedMaps;

	//Properties
	private static Properties properties;
	private static File configFile;

	//Action variables
	private static Stack<Map> undoStack;
	private static Stack<Map> redoStack;

	//A class to load, edit, and save data of the map being drawn.
	private DataManager(){}

	//------------------------------------------------------------------------------------

	//GETTTERS
	public static String MapName() { return map.name; }
	public static int[] MapData() {return map.data;}
	public static int MapWidth() {return map.width;}
	public static int MapHeight() {return map.height;}
	public static Color[] TexturePalette() {
		Color[] list = new Color[map.texturePalette.size()];
		map.texturePalette.toArray(list);
		return list; }
	public static Map[] SavedMaps() 
	{
		Map[] mapList = new Map[savedMaps.size()];
		savedMaps.toArray(mapList);
		return mapList; 
	}
	public static String getExportPath()
	{
		try {
			properties.load(new FileInputStream(configFile));
			return properties.getProperty("preferredExportDirectory");
		}catch(Exception e)
		{
			System.out.println("Unable to read configurations!");
			e.printStackTrace();

			return null;
		}
	}

	public static DataManager getInstance()
	{
		if(instance == null)
		{
			instance = new DataManager();

			//Create action stack
			undoStack = new Stack<>();
			redoStack = new Stack<>();

			savedMaps = new ArrayList<>();
			properties = new Properties();

			//Load any existing user-made folders
			MapMakerRoot = new File(System.getenv("APPDATA") + "/MapMaker");
			exportsFolder = new File(MapMakerRoot.getAbsolutePath() + "/Exports");
			savedMapsFile = new File(MapMakerRoot.getAbsolutePath() + "/SaveMaps.txt");
			configFile = new File(MapMakerRoot.getAbsolutePath() + "/config.properties");

			updateConfigurations();
			try
			{
				updateMaps();
			}catch(Exception e)
			{
				e.printStackTrace();
			}

			//LOAD ANY EXISTING DATA
			if(savedMaps.size() > 0)
			{
				loadSavedMap(savedMaps.size() - 1);
			}else loadDefaultMap();
		}

		return instance;
	}

	//------------------------------------------------------------------------------------

	//IO METHODS
	//Save map data to a file (return true if successful, false otherwise), with the given options
	public static boolean export(String name, boolean includeTxtPal, boolean includeWidth, boolean includeHeight)
	{
		String data = "";

		try {
			//Create a new file with the given name
			File exportFile = new File(exportsFolder.getAbsolutePath() + "/" + name + ".txt");

			System.out.println("Exporting file: " + exportFile);
			exportFile.createNewFile();

			//Write data to the currently accessed map file
			fileWriter = new FileWriter(exportFile);

			//Concatenate texture palette.
			if(includeTxtPal == true)
			{
				data += "textPal: " + map.texturePalette.size() + "\n";
				for(int i = 0; i < map.texturePalette.size(); i++) 
					data += map.texturePalette.get(i).getRed() 
					+ " " + map.texturePalette.get(i).getGreen() 
					+ " " + map.texturePalette.get(i).getBlue() + "\n";
			}

			//Concatenate map dimensions.
			if(includeWidth == true) data += "\nW: " + map.width;
			if(includeHeight == true) data += "\nH: " + map.height + "\n";

			//Concatenate map data.
			data += "Map:\n";
			for(int i = 0; i < map.data.length; i++)
			{
				data += map.data[i] + ((map.data[i] > -1) ? "  " : " ");
				if(i % map.width == map.width - 1) data += "\n";
			}

			//Write the data an close it
			fileWriter.write(data);
			fileWriter.close();

		}catch(IOException e)
		{
			System.out.println("Failed to export file :(");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//Load a map and its texturePalette with a given file
	public static void loadSavedMap(int index)
	{
		if(savedMaps.size() > 0) map = savedMaps.get(index);
		else loadDefaultMap();
	}

	@SuppressWarnings("unchecked")
	public static void updateMaps() throws IOException, ClassNotFoundException
	{
		if(savedMapsFile.exists())
		{
			FileInputStream fileReader = new FileInputStream(savedMapsFile);
			ObjectInputStream objectReader = new ObjectInputStream(fileReader);

			savedMaps = (ArrayList<Map>) objectReader.readObject();

			objectReader.close();
			fileReader.close();
		}
	}

	//Creates folders for saved, exported, and other files
	public static void createAppDataFolder() throws IOException
	{
		if(MapMakerRoot.exists()) return;

		MapMakerRoot.mkdirs();
		exportsFolder.mkdir();
		savedMapsFile.createNewFile(); 
		configFile.createNewFile();

		// Write fields to new configuration file
		properties.put("preferredExportDirectory", exportsFolder.getAbsolutePath());
		properties.store(new FileOutputStream(configFile), null);
	}

	//Load any existing user configurations
	private static void updateConfigurations()
	{
		if(configFile.exists())
		{
			try
			{
				properties.load(new FileInputStream(configFile));
				exportsFolder = new File(properties.getProperty("preferredExportDirectory"));
			}catch(IOException e)
			{
				System.out.println("Unable to load cofiguration!");
				e.printStackTrace();
			}
		}else {
			System.out.println("App Data Folder not found. Creating new folders...");
			try
			{
				createAppDataFolder();
			}catch(Exception e)
			{
				System.out.println("Unable to create root folder!");
				e.printStackTrace();
			}
		}
	}

	// Change the directory of the exported Maps
	public static void updateExportDirectory(String path)
	{
		if(configFile.exists() == false) return;

		properties.setProperty("preferredExportDirectory", path);

		try { properties.store(new FileOutputStream(configFile), null); System.out.println("Changed directory to: " + path);}
		catch (IOException e) { e.printStackTrace(); }
	}

	//Saves the current Map Object to a txt file.
	public static void saveMap(String name) throws IOException
	{
		map.name = name;
		savedMaps.add(map);
		
		FileOutputStream fOS = new FileOutputStream(savedMapsFile);
		ObjectOutputStream oOS = new ObjectOutputStream(fOS);

		oOS.writeObject(savedMaps);
		oOS.close();
		fOS.close();
	}
	
	//Delete a map stored at the given index
	public static void deleteMap(int index)
	{
		if(index >= savedMaps.size()) return;
		
		savedMaps.remove(index);
		
		try
		{
			FileOutputStream fOS = new FileOutputStream(savedMapsFile);
			ObjectOutputStream oOS = new ObjectOutputStream(fOS);

			oOS.writeObject(savedMaps);
			oOS.close();
			fOS.close();
		}catch(IOException e)
		{
			System.out.println("Trouble deleting map!");
			e.printStackTrace();
		}
	}

	//------------------------------------------------------------------------------------

	//MAP DRAWING METHODS
	//Change the type of tile at a given index
	public static void changeTile(int x, int y, int value)
	{
		if(x >= 0 && x < map.width && y >= 0 && y < map.height)
			map.data[x + (y * map.width)] = value;
	}

	//Load the default map.
	public static void loadDefaultMap()
	{
		try
		{
			ObjectInputStream objectInputStream = new ObjectInputStream(instance.getClass().getResourceAsStream("/DefaultMap.txt"));

			map = (Map) objectInputStream.readObject();
			map.name = "Map_" + savedMaps.size();

			objectInputStream.close();
		}catch(Exception e)
		{
			System.out.println("Trouble loading default map!");
			e.printStackTrace();
		}
	}

	//Resizes the map by the given values (REPLACES MAP)
	public static void resizeMap(int xInc, int yInc)
	{
		undoStack.add(map);
		if(xInc == 0 && yInc == 0) return;

		//Calculate and create new map dimensions and array;
		int newWidth = map.width + xInc;
		int newHeight = map.height + yInc;

		//Limit map shrinking
		if(newWidth < 3)
		{
			xInc += 3 - newWidth;
			newWidth = 3;
		}
		if(newHeight < 3)
		{
			yInc += 3 - newHeight;
			newHeight = 3;
		}

		int[] newData = new int[newWidth * newHeight];

		//Transfer mapData to a newly size array at a specific index range
		for(int i = 0, o = (yInc < 0) ? -yInc * map.width : 0; i < newData.length; i++)
		{
			//Skip iteration if i isn't within 0 and (mapWidth * mapHeight) - 1
			if(i / newWidth < yInc || i % newWidth >= map.width)
			{
				newData[i] = -1;
			}else{
				//Add existing mapData to newData;
				newData[i] = map.data[o];

				o++;
				if(xInc < 0 && i % newWidth == map.width - -xInc - 1) o += -xInc;
			}
		}

		map = new Map(map.name, newData, newWidth, newHeight, map.texturePalette);
	}

	//---------------------------------------------------------------------

	//UNDO AND REDO
	//Creates a new instance of Map and saves the previous state to the undoStack.
	public static void updateStack()
	{
		undoStack.add(new Map(map.name,map.data.clone(), map.width, map.height, map.texturePalette));
	}

	//Undo a previous edit to the map
	public static void undoAction()
	{
		if(undoStack.empty()) return;
		redoStack.add(new Map(map.name,map.data.clone(), map.width, map.height, map.texturePalette));
		map = undoStack.pop();
	}

	//Redo an undo
	public static void redoAction()
	{
		if(redoStack.empty()) return;
		undoStack.add(new Map(map.name,map.data.clone(), map.width, map.height, map.texturePalette));
		map = redoStack.pop();
	}
}
