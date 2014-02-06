package pt.uminho.anote2.datastructures.utils.conf.propertiesmanager;

import java.util.Map;

public interface IPropertiesPanel{

	public Map<String, Object> getProperties();
	public boolean haveChanged();
}
