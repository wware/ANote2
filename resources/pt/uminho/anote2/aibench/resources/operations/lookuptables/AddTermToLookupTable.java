package pt.uminho.anote2.aibench.resources.operations.lookuptables;

import pt.uminho.anote2.aibench.resources.datatypes.LookupTableAibench;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation
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
		int elementClass = lookup.addElementClass(classe);
		IResourceElement rule = new ResourceElement(-1, name, elementClass, classe);
		lookup.addElement(rule);
		lookup.addResourceContent(classe);
		lookup.notifyViewObservers();
		new ShowMessagePopup("Term Added !!!");
	}
}
