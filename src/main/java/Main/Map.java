package Main;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable
{
	public static final long serialVersionUID = 32436654;

	public int[] data;
	private ArrayList<Color> texturePalette;
	private final int width, height;
	private String name;

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

	public String Name() { return name; }
	public int Width() { return width; }
	public int Height() { return height; }
	public int Size() { return width * height; }
	
	public void setName(String name) { this.name = name; }

	public void addTexture(int r, int g, int b) { texturePalette.add(new Color(r, g, b)); }
	public Color[] TexturePalette()
	{
		Color[] tP = new Color[texturePalette.size()];
		for(int i = 0; i < tP.length; i++) tP[i] = texturePalette.get(i);
		return tP;
	}
}
