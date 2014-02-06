package pt.uminho.anote2.aibench.utils.operations;

import java.awt.Color;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;

import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.configuration.ISaveModule;
import es.uvigo.ei.aibench.Launcher;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation()
public class ExitOperation {
	
	ArrayList<ISaveModule> arrayList;
	
	@Port(name="savemodule",direction=Direction.INPUT,order=1)
	public void quit(ArrayList<ISaveModule> prjs) throws Exception{
		arrayList = prjs;
	}

	@Port(name="file",direction=Direction.INPUT,order=2)
	public void file(File file) throws Exception
	{
		if(arrayList!=null && arrayList.size()!=0 && file!=null)
		{
			for(ISaveModule p:arrayList){
				p.saveFile(file);
			}
			new ShowMessagePopup("Projects Save.");
		}
		JDialog dialog = new JDialog((Frame) null, "Shutdown");
		JLabel lbl = new JLabel("Shutting down @note2...");
		int borderSize = 16;
		lbl.setOpaque(true);
		lbl.setBackground(Color.WHITE);
		lbl.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		lbl.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));
		dialog.getContentPane().add(lbl);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		new ShowMessagePopup("@Note2 Closed.");
		new Thread(){ //killer thread
			public void run(){						
				Launcher.getPluginEngine().shutdown();						
				System.exit(0);			
			}
		}.start();
	}
}
