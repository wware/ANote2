package pt.uminho.anote2.aibench.curator.view.panes.tree;

import pt.uminho.anote2.core.annotation.IEventAnnotation;

public class EventNode {

	private IEventAnnotation event;
	
	public EventNode(IEventAnnotation event)
	{
		this.event= event;
	}
	
	public String toString()
	{
		String value = new String();
		value = "Relation/Event ID:"+event.getID();
		return value;
		
	}
	
	public IEventAnnotation getEvent() {
		return event;
	}

}
