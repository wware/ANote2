/**
 * 
 */
package pt.uminho.anote2.relation.lifecycle;

import org.platonos.pluginengine.PluginLifecycle;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManagerException;
import pt.uminho.anote2.relation.settings.RERelationSettingsNode;
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
				if(Core.getInstance().getClipboard().getItemsByClass(Corpus.class).size()==0){
					Core.getInstance().disableOperation("operations.rerelation");			
				}
			}
			
			@Override
			public void elementAdded(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(Corpus.class).size()>0){
					Core.getInstance().enableOperation("operations.rerelation");
				}
			}
		});
		
		try {
			PropertiesManager.getPManager().registerNewProp(new RERelationSettingsNode(), "conf/settings/re/re.conf" );
		} catch (PropertiesManagerException e) {
			e.printStackTrace();
		}	
		
	}
}
