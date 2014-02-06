package pt.uminho.anote2.workflow.datastructures;

import java.util.Map;

public interface IChangeSettings {
	 	
	public abstract Map<String, Object> getProperties();
	public abstract boolean haveChanged();
	public abstract void defaultSettings();

}
