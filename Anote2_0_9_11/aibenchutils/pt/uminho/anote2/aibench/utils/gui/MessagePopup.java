package pt.uminho.anote2.aibench.utils.gui;

import javax.swing.Popup;

import es.uvigo.ei.aibench.workbench.Workbench;

public class MessagePopup extends Popup{
	
	public MessagePopup(String message, int x, int y){
		super(Workbench.getInstance().getMainFrame(), 
				new MessageComponent(message), x, y);
	}
}
