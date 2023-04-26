package Pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import Main.*;
import Pages.TRMMFrame.PAGES;

public class MapSelectionPage extends Page implements MouseWheelListener, MouseListener, MouseMotionListener
{
	public static final long serialVersionUID = 439588;

	private final Color skyColor = new Color(120, 180, 230);
	private final Color borderColor = new Color(120, 80, 50);
	private final Color hoveredColor = new Color(150, 120, 110);
	private final Color selectedColor = new Color(120, 80, 70);

	private JButton newMapButton;
	private JButton loadMapButton;
	private JButton deleteButton;
	
	private int scrollAmount = 0;
	private int hoveredIndex = 0;
	private int selectedIndex = -1;

	public MapSelectionPage()
	{
		setBackground(backgroundColor);
		addMouseWheelListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		newMapButton = new JButton("New Map");
		newMapButton.setFocusable(false);
		newMapButton.setBounds(1100, 650, 90, 30);
		
		loadMapButton = new JButton("Load");
		loadMapButton.setFocusable(false);
		loadMapButton.setBounds(1000, 650, 90, 30);
		
		deleteButton = new JButton(new ImageIcon(getClass().getResource("/Images/Trash.png")));
		deleteButton.setFocusable(false);
		deleteButton.setBounds(1000 - deleteButton.getIcon().getIconWidth() - 30, 650, deleteButton.getIcon().getIconWidth() + 20, 30);

		newMapButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				DataManager.loadDefaultMap();
				TRMMFrame.changePage(PAGES.MAP_EDITOR);
			}

		});
		
		loadMapButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				DataManager.loadSavedMap(selectedIndex);
				TRMMFrame.changePage(PAGES.MAP_EDITOR);
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(selectedIndex < 0) return;
				DataManager.deleteMap(selectedIndex);
				selectedIndex = -1;
			}
		});

		add(newMapButton);
		add(loadMapButton);
		add(deleteButton);
	}

	public void paint(Graphics g)
	{
		super.paint(g);

		Map[] savedMaps = DataManager.SavedMaps();
		int xOffset;

		// if (selected map does not exist)
		if(selectedIndex >= 0 && selectedIndex < savedMaps.length)
		{
			g.setColor(selectedColor);
			g.fillRect((selectedIndex * 500) - (10 * scrollAmount), 0, 500, 700);
		}
		
		g.setColor(hoveredColor);
		g.fillRect((hoveredIndex * 500) - (10 * scrollAmount), 0, 500, 700);

		// Iterate through existing maps and display them
		for(int m = 0; m < savedMaps.length; m++)
		{
			xOffset = 50 + (m * 500) - (10 * scrollAmount);

			limitedMapDisplay(savedMaps[m], g, xOffset);
			
			//Draw borders
			g.setColor(borderColor);
			
			//Draw map border
			g.drawRect(xOffset - 1, 19, 400, 200);
			g.drawRect(xOffset - 2, 18, 402, 202);
			
			//Draw segment on the right
			g.fillRect(xOffset + 450, 0, 4, 700);
			
			//Draw map information
			g.setColor(Color.BLACK);
			g.setFont(displayFont);
			g.drawString(savedMaps[m].name, xOffset, 240);
			g.setColor(Color.DARK_GRAY);
			g.drawString("Width: " + savedMaps[m].width, xOffset, 280);
			g.drawString("Height: " + savedMaps[m].height, xOffset, 302);
		}

		paintChildren(g);
	}

	//Draws a map that is 400 x 200 pixels in size, starting at (offset, 20)
	public void limitedMapDisplay(Map map, Graphics g, int offset)
	{
		// Loop through y-axis
		for(int y = 20; y > 0; y--)
		{
			// Loop through x-axis
			for(int x = 0; x < 40; x++)
			{
				// if (current 'y' is higher than map height || current 'x is past map width || current tile is air)
				if(y > map.height || x >= map.width || map.data[x + (map.width * (map.height - y))] == -1)
				{
					g.setColor(skyColor);
				}else
				{
					// Use color indicated by map
					g.setColor(map.texturePalette.get(map.data[x + (map.width * (map.height - y))]));
				}

				// Draw tile at appropriate area
				g.fillRect((x * 10) + offset, ((20 - y) * 10) + 20, 10, 10);
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		// if (NOT (scrollAmount will be below 0 || no more maps to scroll through))
		if (!(scrollAmount + e.getWheelRotation() < 0 || (scrollAmount + e.getWheelRotation()) * 10 + 1200 > (DataManager.SavedMaps().length * 500)))
		{
			scrollAmount += e.getWheelRotation();
			
			hoveredIndex = ((e.getX() + (10 * scrollAmount)) / 500);
			
			// If trying to hover over a non-existent map
			if(hoveredIndex >= DataManager.SavedMaps().length)
			{
				hoveredIndex = DataManager.SavedMaps().length - 1;
			}
			
			repaint();
		} 
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		selectedIndex = hoveredIndex;
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		hoveredIndex = ((e.getX() + (10 * scrollAmount)) / 500);
		if(hoveredIndex >= DataManager.SavedMaps().length) hoveredIndex = DataManager.SavedMaps().length - 1;
		repaint();
	}
}
