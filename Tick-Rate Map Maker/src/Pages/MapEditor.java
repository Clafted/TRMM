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

import javax.swing.JButton;

import Main.DataManager;

public class MapEditor extends Page implements MouseListener, MouseMotionListener, KeyListener
{
	public static final long serialVersionUID = 23445645;

	//Buttons
	private JButton saveButton = new JButton("Save");
	private JButton exportButton = new JButton("Export");

	//Texture 
	private Color[] texturePalette;

	//Map variables
	private int xOffset = 20, yOffset = 100;

	//INPUT variables
	//Mouse variables
	private boolean mouseLeftHeldDown, mouseRightHeldDown;
	private int mouseLastX, mouseLastY, mouseMoveX, mouseMoveY, mouseDragX, mouseDragY;

	//Input variables
	private boolean widthSizerDragged = false, heightSizerDragged = false;

	//Other
	private int tileType = 1;
	private int resizeX, resizeY;

	//--------------------------------------------

	//Constructor
	public MapEditor()
	{
		//Configure visualPanel
		setBackground(new Color(50, 150, 200));

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
				DataManager.saveMap();
			}
		});

		exportButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				DataManager.export();
			}
		});

		add(saveButton);
		add(exportButton);

		//Add ActionListeners
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}

	//Getters
	public int WidthSizerY() { return yOffset + (DataManager.MapHeight() * 20) - 10; }
	public int WidthSizerX() { return xOffset + DataManager.MapWidth() * 40; }
	public int HeightSizerY() { return yOffset - 10; }
	public int HeightSizerX() { return xOffset + (DataManager.MapWidth() * 20) - 10; } 

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
			g.fillRect(xOffset + (i % DataManager.MapWidth()) * 40, yOffset + ((i / DataManager.MapWidth()) * 40), 40, 40);
		}

		//DRAW GRID
		g.setColor(Color.white);
		//Draw border
		g.drawRect(xOffset, yOffset, DataManager.MapWidth() * 40, DataManager.MapHeight() * 40);
		//Draw Vertical Grid-Lines
		for(int i = 1; i < DataManager.MapWidth(); i++)
		{
			g.drawLine(xOffset + (i * 40), yOffset, xOffset + (i * 40), yOffset + (DataManager.MapHeight() * 40));
		}
		//Draw Horizontal Grid-Lines
		for(int i = 1; i < DataManager.MapHeight(); i++)
		{
			g.drawLine(xOffset, yOffset + (i * 40), xOffset + (DataManager.MapWidth() * 40), yOffset + (i * 40));
		}

		//DRAW RESIZE TABS
		g.fillRect(xOffset + (DataManager.MapWidth() * 20) - 10, yOffset - 10, 20, 5);
		g.fillRect(xOffset + DataManager.MapWidth() * 40, yOffset + (DataManager.MapHeight() * 20) - 10, 5, 20);

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

		paintChildren(g);
	}
	//Move the map by a given offset
	public void offset(int x, int y)
	{
		xOffset += x;
		yOffset += y;
		repaint();
	}
	//Draw onto the map
	public void drawTile(int mouseX, int mouseY, int value)
	{
		DataManager.changeTile((mouseX - xOffset) / 40, (mouseY - yOffset) / 40, value);
		repaint();
	}
	//Resize the map
	public void resizeMap(int xInc, int yInc)
	{
		DataManager.resizeMap(xInc, yInc);
		yOffset -= yInc * 40;
		repaint();
	}


	//--------------------------------------------------
	//ACTION LISTENERS
	public void mouseClicked(MouseEvent e) {
		//Handle left click.
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			//Set to tileType to a number between 0 and the number of textures.
			if(e.getX() < (DataManager.TexturePalette().length + 1)* 40 && e.getY() < 40)
			{
				tileType = (e.getX() / 40) - 1;
				System.out.println(tileType);
			}
		}

		//Draw a tile at the mouse's position
		if(e.getButton() == MouseEvent.BUTTON3)
		{
			DataManager.updateStack();
			drawTile(e.getX(), e.getY() - 40, tileType);			
		}
	}
	public void mousePressed(MouseEvent e) {	
		if(e.getButton() == MouseEvent.BUTTON1 && !mouseLeftHeldDown)
		{
			mouseLeftHeldDown = true;
			mouseLastX = e.getX();
			mouseLastY = e.getY();

			//Check if width/height of map was resized
			if(e.getX() >= WidthSizerX() && e.getX() <= WidthSizerX() + 5 && e.getY() >= WidthSizerY() && e.getY() <= WidthSizerY() + 20)
				widthSizerDragged = true;
			else if(e.getX() >= HeightSizerX() && e.getX() <= HeightSizerX() + 20 && e.getY() >= HeightSizerY() && e.getY() <= HeightSizerY() + 5)
				heightSizerDragged = true;
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

			widthSizerDragged = false;
			heightSizerDragged = false;

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
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
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

			if(widthSizerDragged || heightSizerDragged)
			{
				if(widthSizerDragged)
				{
					resizeX = (mouseDragX / 40);
				}else if(heightSizerDragged)
				{
					resizeY = (-mouseDragY / 40);
				}
			}else
			{
				//Offset the map by the change in position
				offset(mouseMoveX, mouseMoveY);
			}
		}else if(mouseRightHeldDown)
		{
			drawTile(e.getX() - 8, e.getY() - 32, tileType);
		}
	}
	public void mouseMoved(MouseEvent e){}

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
