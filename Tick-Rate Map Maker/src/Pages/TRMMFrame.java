package Pages;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class TRMMFrame extends JFrame{
	private static final long serialVersionUID = 124352;

	private MapEditor mapEditor;
	
	//Constructor
	public TRMMFrame()
	{
		setSize(1200, 700);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Map Maker");
		setUndecorated(true);
		setIconImage(new ImageIcon("Images/TRMMIcon.png").getImage());
		
		//Set content-pane for graphics.
		mapEditor = new MapEditor();
		setContentPane(mapEditor);
		
		setVisible(true);
		
	}
}