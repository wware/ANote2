package pt.uminho.anote2.aibench.resources.operations.dics;

import pt.uminho.anote2.aibench.resources.datatypes.Dictionaries;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation()
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
		dics.getResources().newResource(name, info,"dictionary");
		dics.notifyViewObservers();
		new ShowMessagePopup("Dictionary Created !!!");
	}
	
}
