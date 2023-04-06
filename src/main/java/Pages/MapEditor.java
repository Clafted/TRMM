package Pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;

import Main.DataManager;
import Pages.TRMMFrame.PAGES;

public class MapEditor extends Page implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
	public static final long serialVersionUID = 23445645;

	//Buttons
	private JButton saveButton = new JButton("Save");
	private JButton exportButton = new JButton("Export");

	//Colors
	Color backgroundColor;
	Color popUpColor;

	//Drawing variables
	private int brushSize = 3, tempbrushSize = brushSize;

	//INPUT variables
	//Mouse variables
	private boolean mouseLeftHeldDown, mouseRightHeldDown;
	private int mouseLastX, mouseLastY, mouseMoveX, mouseMoveY, mouseDragX, mouseDragY;
	private boolean widthSizerDragged = false, heightSizerDragged = false;
	private boolean brushSizerDragged = false;

	//Map variables
	private int xOffset = 600 - DataManager.MapWidth() * 20, yOffset = 350 - DataManager.MapHeight() * 20;
	private int tileType = 0;
	private int tileSize = 40;

	//Other
	private int resizeX, resizeY;

	//Texture 
	private Color[] texturePalette;

	//--------------------------------------------

	//Constructor
	public MapEditor()
	{
		backgroundColor = new Color(50, 150, 200);
		popUpColor = new Color(20, 30, 50, 195);

		//Configure visualPanel
		setBackground(backgroundColor);

		//Configure buttons
		saveButton.setBounds(1080, 660, 100, 20);
		saveButton.setFocusable(false);
		exportButton.setBounds(960, 660, 100, 20);
		exportButton.setFocusable(false);

		//Add actions to the buttons.
		saveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				DataManager.saveMap("Map");
			}
		});

		exportButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TRMMFrame.changePage(PAGES.EXPORT_PAGE);
			}
		});

		add(saveButton);
		add(exportButton);

		//Add ActionListeners
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
	}

	//Getters
	public int WidthSizerY() { return yOffset + (DataManager.MapHeight() * (tileSize / 2)) - 10; }
	public int WidthSizerX() { return xOffset + DataManager.MapWidth() * tileSize + 10; }
	public int HeightSizerY() { return yOffset - 10; }
	public int HeightSizerX() { return xOffset + (DataManager.MapWidth() * (tileSize / 2)) - 10; } 

	//Rendering method
	public void paint(Graphics g)
	{
		super.paint(g);

		//Set texture palette;
		texturePalette = DataManager.TexturePalette();

		//DRAW TILES
		for(int i = 0; i < DataManager.MapData().length; i++)
		{
			if(DataManager.MapData()[i] == -1) continue;
			//Change the type of the tile
			g.setColor(texturePalette[DataManager.MapData()[i]]);

			//Draw the tile
			g.fillRect(xOffset + (i % DataManager.MapWidth()) * tileSize, yOffset + ((i / DataManager.MapWidth()) * tileSize), tileSize, tileSize);
		}

		//DRAW GRID
		g.setColor(Color.white);
		//Draw border
		g.drawRect(xOffset, yOffset, DataManager.MapWidth() * tileSize, DataManager.MapHeight() * tileSize);
		//Draw Vertical Grid-Lines
		for(int i = 1; i < DataManager.MapWidth(); i++)
		{
			g.drawLine(xOffset + (i * tileSize), yOffset, xOffset + (i * tileSize), yOffset + (DataManager.MapHeight() * tileSize));
		}
		//Draw Horizontal Grid-Lines
		for(int i = 1; i < DataManager.MapHeight(); i++)
		{
			g.drawLine(xOffset, yOffset + (i * tileSize), xOffset + (DataManager.MapWidth() * tileSize), yOffset + (i * tileSize));
		}

		//DRAW RESIZE TABS
		g.fillRect(xOffset + (DataManager.MapWidth() * (tileSize / 2)) - 10, yOffset - 10, (tileSize / 2), 5);
		g.fillRect(xOffset + DataManager.MapWidth() * tileSize + 10, yOffset + (DataManager.MapHeight() * (tileSize / 2)) - 10, 5, (tileSize / 2));

		//DRAW TEXTURE PALETTE
		//Draw eraser
		g.fillRect(0, 0, 40, 40);
		g.setColor(Color.red);
		g.drawLine(0, 0, 40, 40);
		g.drawLine(40, 0, 0, 40);

		//Draw textures
		for(int i = 1; i <= texturePalette.length; i++)
		{
			g.setColor(texturePalette[i - 1]);
			g.fillRect(i * 40, 0, 40, 40);

			g.setColor(Color.black);
			g.drawLine( i * 40, 0, i * 40, 40);
		}

		//Draw texture palette border
		g.setColor(Color.black);
		g.drawRect(0, 0, (texturePalette.length + 1) * 40, 40);

		//Draw extra border for selected texture
		g.setColor(Color.white);
		g.drawRect(41 + (tileType * 40), 1, 38, 38);

		//Draw pop-ups
		drawToolBar(g);

		paintChildren(g);
	}

	//RENDER TOOLBAR
	public void drawToolBar(Graphics g)
	{
		//Draw background
		g.setColor(popUpColor);
		g.fillRect(0, 620, 300, 80);

		//Draw border
		g.setColor(Color.black);
		g.drawRect(-1, 619, 301, 82);

		//Draw brushSize slider
		g.setColor(Color.white);
		g.fillRect(20, 659, 100, 2);

		g.fillRect(20 + ((brushSize - 1) * 10), 650, 4, 20); 
	}

	//Move the map by a given offset
	public void offset(int x, int y)
	{
		xOffset += x;
		yOffset += y;
		repaint();
	}

	//Resize the map
	public void resizeMap(int xInc, int yInc)
	{
		DataManager.resizeMap(xInc, yInc);
		yOffset -= yInc * tileSize;
		repaint();
	}


	//--------------------------------------------------
	//ACTION LISTENERS
	public void mouseClicked(MouseEvent e) {
		//Handle left click.
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			//Set to tileType to a number between 0 and the number of textures.
			if(e.getX() < (DataManager.TexturePalette().length + 1) * 40 && e.getY() < 40)
			{
				tileType = (e.getX() / 40) - 1;

				//Repaint to show selected texture
				repaint();
			}
		}

		//Draw a tile at the mouse's position
		if(e.getButton() == MouseEvent.BUTTON3)
		{
			DataManager.updateStack();

			for(int i = -brushSize / 2; i <= brushSize / 2; i++)
			{
				for(int j = -brushSize / 2; j <= brushSize / 2; j++)
				{
					DataManager.changeTile((e.getX() - xOffset) / tileSize + i, (e.getY() - yOffset) / tileSize + j, tileType);
				}
			}

			repaint();
		}
	}
	public void mousePressed(MouseEvent e) {	
		if(e.getButton() == MouseEvent.BUTTON1 && !mouseLeftHeldDown)
		{
			mouseLeftHeldDown = true;
			mouseLastX = e.getX();
			mouseLastY = e.getY();

			//Check if width/height of map was resized
			widthSizerDragged = (e.getX() >= WidthSizerX() && e.getX() <= WidthSizerX() + 5 && e.getY() >= WidthSizerY() && e.getY() <= WidthSizerY() + 20);
			heightSizerDragged = (e.getX() >= HeightSizerX() && e.getX() <= HeightSizerX() + 20 && e.getY() >= HeightSizerY() && e.getY() <= HeightSizerY() + 5);
			brushSizerDragged = (e.getX() >= 20 + ((brushSize - 1) *  10) && e.getX() <= 24 + ((brushSize - 1) *  10) && e.getY() >= 650 && e.getY() <= 670);
			
		}
		else if(e.getButton() == MouseEvent.BUTTON3 && !mouseRightHeldDown)
		{
			DataManager.updateStack();
			mouseRightHeldDown = true;
		}
	}
	public void mouseReleased(MouseEvent e)
	{
		switch(e.getButton())
		{
		case MouseEvent.BUTTON1:
			mouseLeftHeldDown = false;

			//Reset tracking variables
			mouseLastX = 0;
			mouseLastY = 0;
			mouseDragX = 0;
			mouseDragY = 0;

			//Reset dragged states
			widthSizerDragged = false;
			heightSizerDragged = false;
			brushSizerDragged = false;
			tempbrushSize = brushSize;

			//Resize the map when needed
			if(resizeX != 0 || resizeY != 0)
			{
				resizeMap(resizeX, resizeY);
				resizeX = 0;
				resizeY = 0;
			}
			break;
		case MouseEvent.BUTTON3:
			mouseRightHeldDown = false;
			break;
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		if(mouseLeftHeldDown && !mouseRightHeldDown)
		{
			//Calculate the mouse's movement since being held down
			mouseMoveX = e.getX() - mouseLastX;
			mouseMoveY = e.getY() - mouseLastY;
			
			//Update total distance dragged
			mouseDragX += e.getX() - mouseLastX;
			mouseDragY += e.getY() - mouseLastY;
			
			//Update the mouse's last position
			mouseLastX = e.getX();
			mouseLastY = e.getY();

			//Move any dragged components
			if(widthSizerDragged) resizeX = (mouseDragX / tileSize);
			else if(heightSizerDragged) resizeY = (-mouseDragY / tileSize);
			else if(brushSizerDragged)
			{
				//Change brushSize by the mouse's movement
				brushSize = (tempbrushSize + mouseDragX / 10);
				
				//Limit the brushSize
				if(brushSize > 10) brushSize = 10;
				else if(brushSize < 1) brushSize = 1;
				
				repaint();
			}else offset(mouseMoveX, mouseMoveY);
			
			
		}else if(mouseRightHeldDown)
		{
			for(int i = -brushSize / 2; i <= brushSize / 2; i++)
			{
				for(int j = -brushSize / 2; j <= brushSize / 2; j++)
				{
					DataManager.changeTile((e.getX() - xOffset) / tileSize + i, (e.getY() - yOffset) / tileSize + j, tileType);
				}
			}

			repaint();
		}
	}
	public void mouseMoved(MouseEvent e){}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	//Zoom in and out
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		tileSize -= e.getWheelRotation();
		repaint();
	}

	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) 
	{
		switch(e.getKeyCode())
		{
		case 89:
			DataManager.redoAction();
			repaint();
			break;
		case 90:
			DataManager.undoAction();
			repaint();
			break;
		}
	}
	public void keyReleased(KeyEvent e) {}


}
