/**
 * 
 */
package pt.uminho.anote2.aibench.utils.lifecycle;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import org.platonos.pluginengine.PluginLifecycle;

import pt.uminho.anote2.aibench.utils.firstrun.StartWizart1;
import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * @author paulo maia, 09/05/2007
 *
 */
public class Lifecycle extends PluginLifecycle {
	
	@Override
	public void start(){
		Workbench.getInstance().getMainFrame().addWindowListener(new WindowListener(){

			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			
			public void windowClosing(WindowEvent e) {
//				Workbench.getInstance().getMainFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//				//find quit
//				for (OperationDefinition def : Core.getInstance().getOperations()){
//					if (def.getID().equals("operations.quit")){			
//						Workbench.getInstance().executeOperation(def);
//						return;
//					}
//				}
				
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
				File file = new File("conf/settings.conf");
				if(!file.exists())
				{	
//					StartupMethods.configurePerl();
					new StartWizart1(600,350,new ArrayList<Object>());
				}
			}
			
		});
		
	}
}
