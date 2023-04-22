package Pages;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class TRMMFrame extends JFrame{
	private static final long serialVersionUID = 124352;
	
	public static TRMMFrame instance = null;
	
	public enum PAGES
	{
		MAP_SELECTION(new MapSelectionPage()), MAP_EDITOR(new MapEditor()), EXPORT_AND_SAVE_PAGE(new ExportPage());
		
		public final Page page;
		
		private PAGES(Page page) { this.page = page; }
	}
	
	//Constructor
	private TRMMFrame(){}
	
	//Singleton method
	public static TRMMFrame getInstance()
	{
		
		if(instance == null) instance = new TRMMFrame();
		
		instance.setSize(1200, 700);
		instance.setResizable(false);
		instance.setDefaultCloseOperation(EXIT_ON_CLOSE);
		instance.setTitle("Map Maker");
		instance.setUndecorated(true);
		try { instance.setIconImage(new ImageIcon(instance.getClass().getResource("/Images/Icon.png")).getImage()); }
		catch(Exception e) { e.printStackTrace(); }
		
		//Set content-pane for graphics.
		instance.setContentPane(PAGES.MAP_SELECTION.page);
		instance.setVisible(true);
		
		return instance;
	}
	
	public static void changePage(PAGES page)
	{
		instance.setContentPane(page.page);
	}
}