/**
 * 
 */
package pt.uminho.anote2.nergate.lifecycle;

import org.platonos.pluginengine.PluginLifecycle;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.clipboard.ClipboardListener;

/**
 *
 */
public class Lifecycle extends PluginLifecycle {
	
	@Override
	public void start(){
		
		Core.getInstance().getClipboard().addClipboardListener(new ClipboardListener() {
			
			@Override
			public void elementRemoved(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(Corpus.class).size()==0){
					Core.getInstance().disableOperation("operations.nerabner");			
				}
				if(Core.getInstance().getClipboard().getItemsByClass(Corpus.class).size()==0){
					Core.getInstance().disableOperation("operations.nerChemistryTagger");			
				}
			}
			
			@Override
			public void elementAdded(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(Corpus.class).size()>0){
					Core.getInstance().enableOperation("operations.nerabner");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(Corpus.class).size()>0){
					Core.getInstance().enableOperation("operations.nerChemistryTagger");
				}
			}
		});
		
	}
}
