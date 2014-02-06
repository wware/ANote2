package pt.uminho.anote2.aibench.resources.operations.dics;

import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.aibench.resources.datatypes.Dictionaries;
import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation(enabled=true)
public class CreateDictionary{
	
	private Dictionaries dics;
	private String name;
	
	@Port(name="dictionaries",direction=Direction.INPUT,order=1)
	public void setDicionaries(Dictionaries dics)
	{
		this.dics=dics;
	}
	
	@Port(name="name",direction=Direction.INPUT,order=2)
	public void setName(String name)
	{
		this.name=name;
	}
	
	@Port(name="info",direction=Direction.INPUT,order=3)
	public void getInfo(String info)
	{
		System.gc();
		try {
			Resources.newResource(name, info,GlobalOptions.resourcesDictionaryName);
			dics.notifyViewObservers();
			new ShowMessagePopup("Dictionary Created .");
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(Dictionaries.class);
			Dictionaries dics = (Dictionaries) cl.get(0).getUserData();	
			IResource<IResourceElement> dic = Resources.getLastResource();
			dics.addDictionary((DictionaryAibench) dic);
		} catch (SQLException e) {
			new ShowMessagePopup("Dictionary Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Dictionary Fail .");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}

	}
	
}
