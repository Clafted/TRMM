package Main;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable
{
	public static final long serialVersionUID = 32436654;

	public int[] data;
	public ArrayList<Color> texturePalette;
	public final int width, height;
	public String name;

	public Map(String name, int[] data, int width, int height, Color[] texturePalette)
	{
		this.name = name;
		this.data = data;
		this.width = width;
		this.height = height;

		this.texturePalette = new ArrayList<>();
		
		//Add colors to texture palette.
		for(Color color : texturePalette) this.texturePalette.add(color);
	}
	
	public Map(String name, int[] data, int width, int height, ArrayList<Color> texturePalette)
	{
		this.name = name;
		this.data = data;
		this.width = width;
		this.height = height;

		this.texturePalette = texturePalette;
	}
}
