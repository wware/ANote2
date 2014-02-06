/**
 * 
 */
package pt.uminho.anote2.corpusloaders.lifecycle;

import org.platonos.pluginengine.PluginLifecycle;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
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
				if(Core.getInstance().getClipboard().getItemsByClass(Corpora.class).size()==0){
					Core.getInstance().disableOperation("operations.ccbyaimed");
					Core.getInstance().disableOperation("operations.ccbyanote");
					Core.getInstance().disableOperation("operations.ccbygeniaevent");
				}
			}
			
			@Override
			public void elementAdded(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(Corpora.class).size()>0){
					Core.getInstance().enableOperation("operations.ccbyaimed");
					Core.getInstance().enableOperation("operations.ccbyanote");
					Core.getInstance().enableOperation("operations.ccbygeniaevent");

				}
			}
		});
		
	}
}
