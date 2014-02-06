/**
 * 
 */
package pt.uminho.anote2.aibench.resources.lifecycle;

import org.platonos.pluginengine.PluginLifecycle;

import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.resources.datatypes.OntologyAibench;
import pt.uminho.anote2.aibench.resources.datatypes.RulesAibench;
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
			if (def.getID().equals("operations.initresources")){			
				Workbench.getInstance().executeOperation(def);
				break;
			}
		}
	
		Core.getInstance().getClipboard().addClipboardListener(new ClipboardListener() {
			
			@Override
			public void elementRemoved(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(DictionaryAibench.class).size()==0)
				{
					Core.getInstance().disableOperation("operations.updateDic");
					Core.getInstance().disableOperation("operations.loaddicfromcsv");
					
				}
				if(Core.getInstance().getClipboard().getItemsByClass(DictionaryAibench.class).size()<2)
				{
					Core.getInstance().disableOperation("operations.mergedics");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(LookupTableAibench.class).size()==0)
				{
					Core.getInstance().disableOperation("operations.addelementtolookup");
					Core.getInstance().disableOperation("operations.savecsvlookup");
					Core.getInstance().disableOperation("operations.loadcsvlookup");			
				}
				if(Core.getInstance().getClipboard().getItemsByClass(LookupTableAibench.class).size()<2)
				{
					Core.getInstance().disableOperation("operations.mergelook");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(LexicalWordsAibench.class).size()==0)
				{
					Core.getInstance().disableOperation("operations.addelementtolexicalwords");
					Core.getInstance().disableOperation("operations.savecsvlexicalwords");
					Core.getInstance().disableOperation("operations.loadcsvlexicalwords");			
				}
				if(Core.getInstance().getClipboard().getItemsByClass(LexicalWordsAibench.class).size()<2)
				{
					Core.getInstance().disableOperation("operations.mergelexicalwords");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(RulesAibench.class).size()==0)
				{
					Core.getInstance().disableOperation("operations.createrule");			
				}
				if(Core.getInstance().getClipboard().getItemsByClass(RulesAibench.class).size()<2)
				{
					Core.getInstance().disableOperation("operations.mergerules");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(OntologyAibench.class).size()==0)
				{
					Core.getInstance().disableOperation("operations.updateontology");
					Core.getInstance().disableOperation("operations.updateontology12");
				}
			}
			
			@Override
			public void elementAdded(ClipboardItem arg0) {
				if(Core.getInstance().getClipboard().getItemsByClass(DictionaryAibench.class).size()>0)
				{
					Core.getInstance().enableOperation("operations.updateDic");
					Core.getInstance().enableOperation("operations.loaddicfromcsv");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(DictionaryAibench.class).size()>1)
				{
					Core.getInstance().enableOperation("operations.mergedics");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(LookupTableAibench.class).size()>0)
				{
					Core.getInstance().enableOperation("operations.addelementtolookup");
					Core.getInstance().enableOperation("operations.savecsvlookup");
					Core.getInstance().enableOperation("operations.loadcsvlookup");			
				}
				if(Core.getInstance().getClipboard().getItemsByClass(LookupTableAibench.class).size()>1)
				{
					Core.getInstance().enableOperation("operations.mergelook");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(LexicalWordsAibench.class).size()>0)
				{
					Core.getInstance().enableOperation("operations.addelementtolexicalwords");
					Core.getInstance().enableOperation("operations.savecsvlexicalwords");
					Core.getInstance().enableOperation("operations.loadcsvlexicalwords");			
				}
				if(Core.getInstance().getClipboard().getItemsByClass(LexicalWordsAibench.class).size()>1)
				{
					Core.getInstance().enableOperation("operations.mergelexicalwords");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(RulesAibench.class).size()>0)
				{
					Core.getInstance().enableOperation("operations.createrule");			
				}
				if(Core.getInstance().getClipboard().getItemsByClass(RulesAibench.class).size()>1)
				{
					Core.getInstance().enableOperation("operations.mergerules");
				}
				if(Core.getInstance().getClipboard().getItemsByClass(OntologyAibench.class).size()>0)
				{
					Core.getInstance().enableOperation("operations.updateontology");
					Core.getInstance().enableOperation("operations.updateontology12");
				}
			}
		});
		
	}
}
