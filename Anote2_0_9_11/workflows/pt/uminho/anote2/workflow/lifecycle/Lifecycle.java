/**
 * 
 */
package pt.uminho.anote2.workflow.lifecycle;

import org.platonos.pluginengine.PluginLifecycle;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManagerException;
import pt.uminho.anote2.workflow.settings.basic.WorkflowBasicsSettingsNode;
import pt.uminho.anote2.workflow.settings.general.WorkflowGeneralSettingsNode;
import pt.uminho.anote2.workflow.settings.query.WorkflowQuerySettingsNode;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.clipboard.ClipboardListener;

/**
 * @author paulo maia, 09/05/2007
 *
 */
public class Lifecycle extends PluginLifecycle {
	
	@Override
	public void start(){

		Core.getInstance().getClipboard().addClipboardListener(new ClipboardListener() {

			@Override
			public void elementRemoved(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(QueryInformationRetrievalExtension.class).size()==0)
				{
					Core.getInstance().disableOperation("operations.queryworkflow");
				}
			}

			@Override
			public void elementAdded(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(QueryInformationRetrievalExtension.class).size()>0)
				{
					Core.getInstance().enableOperation("operations.queryworkflow");
				}
			}
		});
		
		
		try {
			PropertiesManager.getPManager().registerNewProp(new WorkflowGeneralSettingsNode(), "conf/settings/workflow/general.conf");
			PropertiesManager.getPManager().registerNewProp(new WorkflowQuerySettingsNode(), "conf/settings/workflow/query.conf");
			PropertiesManager.getPManager().registerNewProp(new WorkflowBasicsSettingsNode(), "conf/settings/workflow/basic.conf");
		} catch (PropertiesManagerException e) {
			e.printStackTrace();
		}
	}
	
	
}
