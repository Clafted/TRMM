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

import javax.swing.JButton;

import Main.*;
import Pages.TRMMFrame.PAGES;

public class MapSelectionPage extends Page implements MouseWheelListener, MouseListener, MouseMotionListener
{
	public static final long serialVersionUID = 439588;

	private final Color skyColor = new Color(120, 180, 230);
	private final Color borderColor = new Color(120, 80, 50);
	private final Color hoveredColor = new Color(150, 120, 110);

	private JButton newMapButton;
	private int scrollAmount = 0;
	private int hoveredIndex = 0;

	public MapSelectionPage()
	{
		setBackground(backgroundColor);
		addMouseWheelListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		newMapButton = new JButton("New Map");
		newMapButton.setFocusable(false);
		newMapButton.setBounds(1100, 650, 90, 30);

		newMapButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				DataManager.loadDefaultMap();
				TRMMFrame.changePage(PAGES.MAP_EDITOR);
			}

		});

		add(newMapButton);
	}

	public void paint(Graphics g)
	{
		super.paint(g);

		Map[] savedMaps = DataManager.SavedMaps();
		int xOffset;

		g.setColor(hoveredColor);
		g.fillRect((hoveredIndex * 500) - (10 * scrollAmount), 0, 500, 700);

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
			g.drawString(savedMaps[m].Name(), xOffset, 240);
			
			g.setColor(Color.DARK_GRAY);
			g.drawString("Width: " + savedMaps[m].Width(), xOffset, 280);
			g.drawString("Height: " + savedMaps[m].Height(), xOffset, 302);
		}

		paintChildren(g);
	}

	//Draws a map that is 400 x 200 pixels in size, starting at (offset, 20)
	public void limitedMapDisplay(Map map, Graphics g, int offset)
	{
		for(int y = 20; y > 0; y--)
		{
			for(int x = 0; x < 40; x++)
			{
				if(y > map.Height() || x >= map.Width() || map.data[x + (map.Width() * (map.Height() - y))] == -1)
				{
					g.setColor(skyColor);
				}else
				{
					g.setColor(map.TexturePalette()[map.data[x + (map.Width() * (map.Height() - y))]]);
				}

				g.fillRect((x * 10) + offset, ((20 - y) * 10) + 20, 10, 10);
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		//Prevent scrolling
		if (scrollAmount + e.getWheelRotation() < 0 || (scrollAmount + e.getWheelRotation()) * 10 + 1200 > (DataManager.SavedMaps().length * 500))
		{
			return;
		} 
		
		scrollAmount += e.getWheelRotation();
		
		hoveredIndex = ((e.getX() + (10 * scrollAmount)) / 500);
		if(hoveredIndex >= DataManager.SavedMaps().length) hoveredIndex = DataManager.SavedMaps().length - 1;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(hoveredIndex >= (DataManager.SavedMaps().length)) return;
		DataManager.loadSavedMap((e.getX() + (10 * scrollAmount)) / 500);
		TRMMFrame.changePage(PAGES.MAP_EDITOR);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		hoveredIndex = ((e.getX() + (10 * scrollAmount)) / 500);
		if(hoveredIndex >= DataManager.SavedMaps().length) hoveredIndex = DataManager.SavedMaps().length - 1;
		repaint();
	}
}
