package Pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Main.DataManager;
import Pages.TRMMFrame.PAGES;

public class ExportPage extends Page implements ChangeListener
{
	public static final long serialVersionUID = 325430;

	//Buttons
	private JButton cancelButton;
	private JButton exportButton;

	//Text Input
	private JTextField name;
	
	//Map info inclusions
	private JCheckBox texturePaletteCBox, widthCBox, heightCBox;
	boolean includeTexturePalette, includeWidth, includeHeight;

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
				//CHANGE TO WORK WITH CHECK BOXES
				DataManager.export(name.getText(), false, true, true);
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
		
		//JCheckboxes
		texturePaletteCBox = new JCheckBox("Texture Palette");
		widthCBox = new JCheckBox("Width");
		heightCBox = new JCheckBox("Height");
		
		//Add components
		add(cancelButton);
		add(exportButton);
		add(name);
		add(texturePaletteCBox);
		add(widthCBox);
		add(heightCBox);
		

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

	@Override
	public void stateChanged(ChangeEvent e) {
//		if(e.getSource() == texturePalletteCBox) includeTexturePallette = 
	}

}
