package Pages;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Main.DataManager;
import Pages.TRMMFrame.PAGES;

public class ExportPage extends Page implements ChangeListener
{
	public static final long serialVersionUID = 325430;

	Color skyColor = new Color(70, 100, 200);
	JPanel options;
	Color[] texturePalette;
	
	//Buttons
	private JButton cancelButton;
	private JButton exportButton;

	//Text Input
	private JTextField saveDirectory, name;
	private JLabel saveDirectoryLabel, nameLabel;

	//Map info inclusions
	private JCheckBox texturePaletteCBox, widthCBox, heightCBox;
	boolean includeTexturePalette, includeWidth, includeHeight;

	public ExportPage()
	{
		options = new JPanel(new FlowLayout());
		options.setBounds(300, 0,  900, 700);
		options.setBackground(backgroundColor);
		
		setBackground(new Color(150, 100, 70));

		//Save directory textField
		saveDirectory = new JTextField(DataManager.getExportPath(), 20);
		saveDirectory.setLocation(20, 20 );

		//Filename text-field
		name = new JTextField(DataManager.MapName(), 10);
		name.setBounds(50, 50 + (10 * DataManager.MapHeight()), 200, 40);
		
		nameLabel = new JLabel("File Name");
		saveDirectoryLabel = new JLabel("Export Directory");
			
		//JCheckboxes
		texturePaletteCBox = new JCheckBox("Texture Palette");
		widthCBox = new JCheckBox("Width");
		heightCBox = new JCheckBox("Height");

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
				DataManager.updateExportDirectory(saveDirectory.getText());
				DataManager.export(name.getText(), false, true, true);
			}
		});

		//Configure buttons
		cancelButton.setBounds(100, 660, 100, 20);
		cancelButton.setFocusable(false);
		exportButton.setBounds(60, 660, 100, 20);
		exportButton.setFocusable(false);

		//Add components
		options.add(saveDirectoryLabel);
		options.add(saveDirectory);
		options.add(nameLabel);
		options.add(name);
		
		// Check-boxes
		options.add(texturePaletteCBox);
		options.add(widthCBox);
		options.add(heightCBox);
		
		options.add(exportButton);
		options.add(cancelButton);
		
		add(options);
	}

	public void paint(Graphics g)
	{
		paintComponent(g);

		//Set texture palette;
		texturePalette = DataManager.TexturePalette();

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

		paintChildren(g);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		//		if(e.getSource() == texturePalletteCBox) includeTexturePallette = 
	}

}
