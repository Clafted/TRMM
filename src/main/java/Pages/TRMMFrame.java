package Pages;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class TRMMFrame extends JFrame{
	private static final long serialVersionUID = 124352;
	
	public static TRMMFrame instance = null;
	
	public enum PAGES
	{
		MAP_EDITOR(new MapEditor()), EXPORT_PAGE(new ExportPage());
		
		public final Page page;
		
		private PAGES(Page page) { this.page = page; }
	}
	
	//Constructor
	private TRMMFrame()
	{
		setSize(1200, 700);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Map Maker");
		setUndecorated(true);
		setIconImage(new ImageIcon("./Images/Icon.png").getImage());
		
		//Set content-pane for graphics.
		setContentPane(PAGES.MAP_EDITOR.page);
		
		setVisible(true);
		
	}
	
	//Singleton method
	public static TRMMFrame getInstance()
	{
		if(instance == null) instance = new TRMMFrame();
		return instance;
	}
	
	public static void changePage(PAGES page)
	{
		instance.setContentPane(page.page);
	}
}