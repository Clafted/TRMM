package Pages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class Page extends JPanel implements ActionListener{
	public static final long serialVersionUID = 325432;
	
	
	public Page()
	{
		setBounds(0, 0, 1200, 700);
		setLayout(null);
	}


	@Override
	public void actionPerformed(ActionEvent e) {}
}
