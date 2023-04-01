package Main;

import Pages.TRMMFrame;

public class TRMMMain 
{
	public static void main(String[] args)
	{
		DataManager.GetInstance();
		TRMMFrame.getInstance();
	}
}
