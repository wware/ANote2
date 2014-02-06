/**
 * 
 */
package pt.uminho.anote2.aibench.utils.lifecycle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import org.platonos.pluginengine.PluginLifecycle;

import pt.uminho.anote2.aibench.utils.settings.database.DatabaseManagementGUI;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.UpdateDatabaseHelp;
import pt.uminho.anote2.datastructures.database.startdatabase.StartDatabase;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * @author paulo maia, 09/05/2007
 *
 */
public class Lifecycle extends PluginLifecycle {


    
    
	@Override
	public void start(){
		IDatabase db  = Configuration.getDatabase();
		if(db == null || db instanceof StartDatabase)
		{
			new DatabaseManagementGUI();
		}
		else
		{

			if(UpdateDatabaseHelp.isDatabaseOutOfDate())
			{
				for (OperationDefinition<?> def : Core.getInstance().getOperations()){
					if (def.getID().equals("operations.updatedb")){	
						Workbench.getInstance().executeOperation(def);
						break;
					}
				}	
			}
		}

		Workbench.getInstance().getMainFrame().addWindowListener(new WindowListener(){

			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
			}

			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
			}


			public void windowClosing(WindowEvent e) {
				Workbench.getInstance().getMainFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				for (OperationDefinition<?> def : Core.getInstance().getOperations()){
					if (def.getID().equals("operations.exit")){	
						Workbench.getInstance().executeOperation(def);
						return;
					}
				}	
			}

			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
			}

		});
//		String os = System.getProperty("os.name").toLowerCase();
//
//		/**
//		 * Just for mac os
//		 */
//		if (os.indexOf( "mac" ) >= 0) {
//
//			Application thisapp = Application.getApplication();
//			thisapp.addApplicationListener(new OptFluxAdapter());
//		} 
	}
	
//	class Anote2Adapter extends ApplicationAdapter{
//
//		public Anote2Adapter(){
//
//		}
//		
//		public void handleQuit(ApplicationEvent e)
//		{
//			for (OperationDefinition<?> def : Core.getInstance().getOperations()){
//				if (def.getID().equals("operations.exit")){			
//					Workbench.getInstance().executeOperation(def);
//					return;
//				}
//			}			
//		}
//
//
//	}
}
