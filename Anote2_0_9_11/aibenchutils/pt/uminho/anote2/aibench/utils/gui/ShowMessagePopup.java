package pt.uminho.anote2.aibench.utils.gui;

import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import es.uvigo.ei.aibench.workbench.Workbench;

public class ShowMessagePopup extends Thread{

	private String msg;
	
	public ShowMessagePopup(String message){
		this.msg = message;
		this.start();
	}
	
	public void run() {
		
		MessagePopup popup = new MessagePopup(msg, 
				Workbench.getInstance().getMainFrame().getX()+Workbench.getInstance().getMainFrame().getWidth()-315, 
				Workbench.getInstance().getMainFrame().getY()+30);
		
		popup.show();
		try {
			Thread.sleep(GlobalGUIOptions.popupMassagesTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		popup.hide();
	}
}
