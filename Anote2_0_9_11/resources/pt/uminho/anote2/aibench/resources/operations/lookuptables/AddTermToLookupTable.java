package pt.uminho.anote2.aibench.resources.operations.lookuptables;

import java.sql.SQLException;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.schema.TableResourcesElements;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation(enabled=false)
public class AddTermToLookupTable {
	
	private LookupTableAibench lookup;
	private String name;

	@Port(name="lookuptable",direction=Direction.INPUT,order=1)
	public void setDicionaries(LookupTableAibench lookup)
	{
		this.lookup=lookup;
	}

	@Port(name="name",direction=Direction.INPUT,order=2)
	public void setName(String name)
	{
		this.name=name;
	}

	@Port(name="class",direction=Direction.INPUT,order=3)
	public void getInfo(String classe)
	{
		System.gc();
		int elementClass;
		try {
			elementClass = lookup.addElementClass(classe);

			IResourceElement rule = new ResourceElement(-1, name, elementClass, classe);
			if(lookup.verifyIfTermExist(rule))
			{
				new ShowMessagePopup("The term \""+rule.getTerm()+"\" is already in Lexical Word Resource");	
				Workbench.getInstance().warn("The term \""+rule.getTerm()+"\" is already in Lexical Word Resource");
			}
			else if(lookup.addElement(rule))
			{
				lookup.addResourceContent(classe);
				lookup.notifyViewObservers();
				new ShowMessagePopup("Term Added .");
			}
			else
			{
				if(name.length()<2)
				{
					new ShowMessagePopup("The term must contain more than one character.");	
					Workbench.getInstance().warn("The term must contain more than one character.");
				}
				else if(name.length()>TableResourcesElements.elementSize)
				{		
					new ShowMessagePopup("the term must not contain more than "+TableResourcesElements.elementSize+" character.");	
					Workbench.getInstance().warn("the term must not contain more than "+TableResourcesElements.elementSize+" character.");
				}
				else
				{
					new ShowMessagePopup("The term \""+name+"\" is already in Lexical Word Resource");	
					Workbench.getInstance().warn("The term \""+name+"\" is already in Lexical Word Resource");
				}
			}
		} catch (SQLException e) {
			new ShowMessagePopup("Term Added Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			new ShowMessagePopup("Term Added Fail.");
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
}
