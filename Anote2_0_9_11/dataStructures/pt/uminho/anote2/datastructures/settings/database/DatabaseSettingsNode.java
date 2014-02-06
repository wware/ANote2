package pt.uminho.anote2.datastructures.settings.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.datastructures.database.DatabaseArchive;
import pt.uminho.anote2.datastructures.database.DatabaseFactory;
import pt.uminho.anote2.datastructures.database.DatabaseManager;
import pt.uminho.anote2.datastructures.database.startdatabase.StartDatabase;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.AbstractPropertyNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;

public class DatabaseSettingsNode extends AbstractPropertyNode{

	
	private static final String treepath = "General.Database";
	
	private Map<String, Object> defaultOptions;
	private boolean firstRun = true;

	public DatabaseSettingsNode() {
		super(treepath);
	}


	@Override
	public Map<String, Object> getDefaultProperties() {
		if(defaultOptions == null){
			defaultOptions = new HashMap<String, Object>();
			defaultOptions.put(DatabaseDefaultSettings.DATABASE_ARCHIVE, new DatabaseArchive());
			defaultOptions.put(DatabaseDefaultSettings.DATABASE,new DatabaseManager(new StartDatabase()));
		}
		return defaultOptions;
	}


	@Override
	public IPropertiesPanel getPropertiesPanel() {
		if(firstRun)
		{
			firstRun = false;
			return new DatabaseChangeSettingsGUI();		
		}
		return new DatabaseChangeSettingsGUI(properties,defaultOptions);
	}


	@Override
	public Object revert(String propId, String propValue) {
		if(propId.equals(DatabaseDefaultSettings.DATABASE))
		{
			if(propValue.isEmpty())
			{
				return new DatabaseManager(new StartDatabase());
			}
			return DatabaseFactory.createDatabaseManager(propValue);
		}
		else if(propId.equals(DatabaseDefaultSettings.DATABASE_ARCHIVE))
		{
			return new DatabaseArchive(propValue);
		}
		return null;
	}


	@Override
	public String convert(String propId, Object propValue) {
		return propValue.toString();
	}


	@Override
	public Set<String> getNotExportableProperties() {
		Set<String> notAllowExportableProperties = new HashSet<String>(); 
		notAllowExportableProperties.add(DatabaseDefaultSettings.DATABASE_ARCHIVE);
		notAllowExportableProperties.add(DatabaseDefaultSettings.DATABASE);
		return notAllowExportableProperties;
	}
	
	

}
