package pt.uminho.anote2.datastructures.utils.conf;

import java.sql.SQLException;
import java.text.DecimalFormat;

import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.settings.database.DatabaseSettingsNode;
import pt.uminho.anote2.datastructures.settings.proxy.ProxySettingsNode;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManagerException;

public class GlobalOptions {
	
	static{
		try {
			PropertiesManager.getPManager().registerNewProp(new ProxySettingsNode(), "conf/settings/global.conf" );
			PropertiesManager.getPManager().registerNewProp(new DatabaseSettingsNode(), "conf/settings/global.conf" );
			anote2DatabaseVersion = HelpDatabase.getMaxDatabaseVersion();
		} catch (PropertiesManagerException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseLoadDriverException e) {		
			e.printStackTrace();
		}
	}
	
	public static final String anote2Version = "0.9.10";
	
	public static final String mysqlDatabase = "anote_db";
	
	public static final String configurationFile = "conf/settings.conf";
	
	public static final String sessionFile = "conf/sessionfile.ssf";
	
	public static int anote2DatabaseVersion;
	
	public static final String mysqlDatabaseFile = "./conf/database/mysql_anote2_db.sql";
	
	public static final String mysqlDatabaseVersionFile = "./conf/database/anote2_db_version.xml";
	
	public static final String mysqlDatbaseUpdateStartNameFile = "anote2_db_update_";
	
	public static final String mysqlDatbaseUpdateEndNameFile = ".sql";
	
	public static final String mysqlDatbaseUpdateEndNameInfoFile = ".info";
	
	public static final String mysqlDatbaseUpdateFolder = "./conf/database";	
	
	public static final String wikiGeneralLink = "http://darwin.di.uminho.pt/anote2/wiki/index.php/";
	
	public static final String checkForUpdatesURL = "http://sourceforge.net/projects/anote2/files/";
	
	public static final String saveDocDirectoty = "Docs/";
		
	public static final String rootName = "Anote2";
	
	public static final String publicationManagerName = "publicationmanager";
	
	public static final String publicationManagerQuery = "query";
	
	public static final String resourcesName = "resources";
	
	public static final String resourcesDictionariesName = "dictionaries";
	
	public static final String resourcesDictionaryName = "dictionary";
	
	public static final String resourcesLookupTablesName = "lookuptables";
	
	public static final String resourcesLookupTableName = "lookuptable";
	
	public static final String resourcesRulesResourceName = "rules";
	
	public static final String resourcesRuleSetName = "rule";
	
	public static final String resourcesOntologiesName = "ontologies";
	
	public static final String resourcesOntologyName = "ontology";
	
	public static final String corporaName = "corpora";
	
	public static final String corporacorpusName = "corpus";
	
	public static final String corporaProcessName = "process";
	
	public static final String corporaAnnotatedDocument = "annotateddoc";

	public static final String resourcesLexicalWordsSet = "lexicalwordsset";

	public static final String resourcesLexicalWords = "lexicalwords";
	
	public final static int superWidth = 1024;
	
	public final static int superHeight = 680;
	
	public final static int generalWidth= 800;
	
	public final static int generalHeight= 600;
	
	public final static int smallWidth = 600;
	
	public final static int smallHeight = 400;

	public static Integer threadsNumber = null;
	
	public final static DecimalFormat decimalformat = new DecimalFormat("0.00");

	public final static String otherConfigurationFile = "conf/othersettings.xml";	
	
	public final static String pluginManagerFile = "conf/pluginmanager.conf";
	
	public static String verbColor = null;
	
	public static String verbColorBackground = null;
	
	public static String tmpDocs = "Docs/tmp/";

	public static Boolean freeFullTextOnly = null;
	
	public static IDatabase database = null;

	public static IProxy proxy = null;

	public static Boolean dictionaryViewSearchForSynonyms = null;

	public static String highlightColor = null;

	public static Boolean usingTitleInAbstract = null;
	
	public static String currentdirectory = null;

	public static Integer getDoublePrecision() {
		return 2;
	}

}
