package Pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import Main.DataManager;
import Pages.TRMMFrame.PAGES;

public class ExportPage extends Page
{
	public static final long serialVersionUID = 325430;

	//Buttons
	private JButton cancelButton;
	private JButton exportButton;

	//Text Input
	private JTextField name;

	public ExportPage()
	{
		setBackground(new Color(150, 100, 70));

		//BUTTONS
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TRMMFrame.changePage(PAGES.MAP_EDITOR);
			}
		});

		exportButton = new JButton("Export");
		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataManager.export(name.getText());
			}
		});

		//Configure buttons
		cancelButton.setBounds(1080, 660, 100, 20);
		cancelButton.setFocusable(false);
		exportButton.setBounds(960, 660, 100, 20);
		exportButton.setFocusable(false);


		//TEXT FIELD
		
		name = new JTextField("Map");
		name.setBounds(20, 20 + 10 * DataManager.MapHeight(), 100, 20);

		add(cancelButton);
		add(exportButton);
		add(name);

	}

	public void paint(Graphics g)
	{
		super.paint(g);

		//Set texture palette;
		Color[] texturePalette = DataManager.TexturePalette();
		Color skyColor = new Color(70, 100, 200);

		//DRAW TILES
		for(int i = 0; i < DataManager.MapData().length; i++)
		{
			//Change either to the sky color or texture color
			g.setColor((DataManager.MapData()[i] == -1) ? skyColor : texturePalette[DataManager.MapData()[i]]);

			//Draw the tile
			g.fillRect((i % DataManager.MapWidth()) * 10 + 20, ((i / DataManager.MapWidth()) * 10 + 20), 10, 10);
		}
		
		//Draw a border around the map
		g.setColor(Color.white);
		g.drawRect(20, 20, 10 * DataManager.MapWidth(), 10 * DataManager.MapHeight());
		
		name.setLocation(20, 20 + 10 * DataManager.MapHeight());
		
		paintChildren(g);
	}

}
