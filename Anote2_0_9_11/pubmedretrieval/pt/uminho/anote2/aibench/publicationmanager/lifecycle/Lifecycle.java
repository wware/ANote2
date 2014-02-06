/**
 * 
 */
package pt.uminho.anote2.aibench.publicationmanager.lifecycle;

import org.platonos.pluginengine.PluginLifecycle;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.clipboard.ClipboardListener;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;

/**
 * @author paulo maia, 09/05/2007
 *
 */
public class Lifecycle extends PluginLifecycle {
	
	@Override
	public void start(){
		
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.initreferencemanager")){			
				Workbench.getInstance().executeOperation(def);
				break;
			}
		}
				
		Core.getInstance().getClipboard().addClipboardListener(new ClipboardListener() {
			
			@Override
			public void elementRemoved(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(QueryInformationRetrievalExtension.class).size()==0)
				{
					Core.getInstance().disableOperation("operations.addpublicationquery");
					Core.getInstance().disableOperation("operations.journalretrievalmuldoc");
					Core.getInstance().disableOperation("operations.pubmedsearchupdate");
				}
			}
			
			@Override
			public void elementAdded(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(QueryInformationRetrievalExtension.class).size()>0)
				{
					Core.getInstance().enableOperation("operations.addpublicationquery");
					Core.getInstance().enableOperation("operations.journalretrievalmuldoc");	
					Core.getInstance().enableOperation("operations.pubmedsearchupdate");
				}
			}
		});
		
	}
}
