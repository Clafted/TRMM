package Main;

import Pages.TRMMFrame;

public class TRMMMain 
{
	public static void main(String[] args)
	{
		DataManager.GetInstance();
		@SuppressWarnings("unused")
		TRMMFrame frame = new TRMMFrame();
	}
}
