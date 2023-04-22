package Pages;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class Page extends JPanel implements ActionListener{
	public static final long serialVersionUID = 325432;
	
	protected static final Color backgroundColor = new Color(220, 190, 180);
	protected static final Font displayFont = new Font(Font.MONOSPACED, Font.BOLD, 20); 
	
	public Page()
	{
		setBounds(0, 0, 1200, 700);
		setLayout(null);
		setFocusable(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
