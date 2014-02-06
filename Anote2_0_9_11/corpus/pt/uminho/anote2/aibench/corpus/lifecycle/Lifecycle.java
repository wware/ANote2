/**
 * 
 */
package pt.uminho.anote2.aibench.corpus.lifecycle;

import org.platonos.pluginengine.PluginLifecycle;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.settings.corpora.CorporaSettingsNode;
import pt.uminho.anote2.aibench.corpus.settings.cytoscape.CytoscapeSettingsNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManagerException;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.clipboard.ClipboardListener;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;


public class Lifecycle extends PluginLifecycle {
	
	@Override
	public void start(){
	
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.initproject")){			
				Workbench.getInstance().executeOperation(def);
				break;
			}
		}
		
		Core.getInstance().getClipboard().addClipboardListener(new ClipboardListener() {
			
			@Override
			public void elementRemoved(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(RESchema.class).size()==0){
					Core.getInstance().disableOperation("operations.exportretoxgmml");
					Core.getInstance().disableOperation("operations.exportretocsv");						
				}
				if(Core.getInstance().getClipboard().getItemsByClass(Corpus.class).size()==0){
					Core.getInstance().disableOperation("operations.mergenerschemas");			
				}
				if(Core.getInstance().getClipboard().getItemsByClass(NERSchema.class).size()==0){
					Core.getInstance().disableOperation("operations.exportnetocsv");			
				}	
			}
			
			
			@Override
			public void elementAdded(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(RESchema.class).size()>0){
					Core.getInstance().enableOperation("operations.exportretoxgmml");
					Core.getInstance().enableOperation("operations.exportretocsv");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(Corpus.class).size()>0){
					Core.getInstance().enableOperation("operations.mergenerschemas");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(NERSchema.class).size()>0){
					Core.getInstance().enableOperation("operations.exportnetocsv");			
				}
			}
		});
		
		try {
			PropertiesManager.getPManager().registerNewProp(new CorporaSettingsNode(), "conf/settings/corpora/corpora.conf");
		} catch (PropertiesManagerException e) {
			e.printStackTrace();
		}
		
		try {
			PropertiesManager.getPManager().registerNewProp(new CytoscapeSettingsNode(), "conf/settings/corpora/cytoscape.conf");
		} catch (PropertiesManagerException e) {
			e.printStackTrace();
		}
		
		
	}
}
