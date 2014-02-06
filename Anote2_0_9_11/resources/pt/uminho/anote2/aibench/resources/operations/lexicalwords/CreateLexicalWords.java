package pt.uminho.anote2.aibench.resources.operations.lexicalwords;

import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsSet;
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
public class CreateLexicalWords {
	
	private LexicalWordsSet lexical;
	private String name;

	@Port(name="lexicalwords",direction=Direction.INPUT,order=1)
	public void setDicionaries(LexicalWordsSet lexical)
	{
		this.lexical=lexical;
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
			Resources.newResource(name, info,GlobalOptions.resourcesLexicalWords);
			lexical.notifyViewObservers();
			new ShowMessagePopup("Create Lexical Words .");
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(LexicalWordsSet.class);
			LexicalWordsSet lws = (LexicalWordsSet) cl.get(0).getUserData();	
			IResource<IResourceElement> lw = Resources.getLastResource();
			lws.addLexicalWords((LexicalWordsAibench) lw);
		} catch (SQLException e) {
			new ShowMessagePopup("Create Lexical Words Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Create Lexical Words Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
			return;
		}

	}


}
