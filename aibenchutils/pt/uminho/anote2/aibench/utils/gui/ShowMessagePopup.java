package pt.uminho.anote2.aibench.utils.gui;

import es.uvigo.ei.aibench.workbench.Workbench;

public class ShowMessagePopup extends Thread{

	private String msg;
	
	public ShowMessagePopup(String message){
		this.msg = message;
		this.start();
	}
	
	public void run() {
		
		MessagePopup popup = new MessagePopup(msg, 
				Workbench.getInstance().getMainFrame().getX()+7, 
				Workbench.getInstance().getMainFrame().getY()+30);
		
		popup.show();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		popup.hide();
	}
}
