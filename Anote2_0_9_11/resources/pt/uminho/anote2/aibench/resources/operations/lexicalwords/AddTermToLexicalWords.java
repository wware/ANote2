package pt.uminho.anote2.aibench.resources.operations.lexicalwords;

import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.schema.TableResourcesElements;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(enabled=false)
public class AddTermToLexicalWords {
	
	private LexicalWordsAibench lexical;

	@Port(name="lexicalwords",direction=Direction.INPUT,order=1)
	public void setDicionaries(LexicalWordsAibench lexical)
	{
		this.lexical=lexical;
	}

	@Port(name="name",direction=Direction.INPUT,order=2)
	public void setName(String name)
	{
		System.gc();
		IResourceElement word = new ResourceElement(name);
		if(lexical.verifyIfTermExist(word))
		{
			new ShowMessagePopup("The term \""+word.getTerm()+"\" is already in Lexical Word Resource");	
			Workbench.getInstance().warn("The term \""+word.getTerm()+"\" is already in Lexical Word Resource");
		}
		else if(lexical.addElement(word))
		{
			try {
				lexical.getLexicalWords().add(word.getTerm());
				int datbaseID = HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements);
				lexical.getLexicalWordDatabaseID().put(word.getTerm(), datbaseID-1);
				lexical.notifyViewObservers();
				new ShowMessagePopup("Term Added .");
			} catch (SQLException e) {
				new ShowMessagePopup("Term Added Fail .");
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				new ShowMessagePopup("Term Added Fail .");
				TreatExceptionForAIbench.treatExcepion(e);
			}	
		}
		else
		{
			if(word.getTerm().length()<2)
			{
				new ShowMessagePopup("The term must contain more than one character.");	
				Workbench.getInstance().warn("The term must contain more than one character.");
			}
			else if(word.getTerm().length()>TableResourcesElements.elementSize)
			{		
				new ShowMessagePopup("the term must not contain more than "+TableResourcesElements.elementSize+" character.");	
				Workbench.getInstance().warn("the term must not contain more than "+TableResourcesElements.elementSize+" character.");
			}
			else
			{
				new ShowMessagePopup("The term \""+word.getTerm()+"\" is already in Lexical Word Resource");	
				Workbench.getInstance().warn("The term \""+word.getTerm()+"\" is already in Lexical Word Resource");
			}
		}

	}
}
