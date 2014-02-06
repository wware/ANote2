package pt.uminho.anote2.aibench.resources.datatypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import pt.uminho.anote2.aibench.utils.file.FileHandling;
import pt.uminho.anote2.core.configuration.ISaveModule;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.datatypes.annotation.Clipboard;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;

@Datatype(structure = Structure.COMPLEX,viewable=false,namingMethod="getName",removable=false,renamed=false)
public class Resources implements ISaveModule{
	
	
	private Dictionaries dictionaries;
	private LookupTables lookuptables;
	private RulesSet rules;
	private Ontologies ontologies;
	private LexicalWordsSet lexicalWordsSet;

	public Resources()
	{
		this.dictionaries=new Dictionaries(this);
		this.lookuptables=new LookupTables(this);
		this.rules=new RulesSet(this);
		this.setOntologies(new Ontologies(this));
		this.lexicalWordsSet = new LexicalWordsSet(this);
	}

	@Clipboard(name="Dictionaries",order=1)
	public Dictionaries getDictionaries() {
		return dictionaries;
	}

	@Clipboard(name="Lookup Tables",order=2)
	public LookupTables getLookuptables() {
		return lookuptables;
	}
	
	@Clipboard(name="Rules",order=3)
	public RulesSet getRules() {
		return rules;
	}
	
	@Clipboard(name="Ontologies",order=4)
	public Ontologies getOntologies() {
		return ontologies;
	}
	
	@Clipboard(name="Lexical Words",order=5)
	public LexicalWordsSet getLexicalWords() {
		return lexicalWordsSet;
	}
	
	public String getName()
	{
		return "Resources";
	}
	
	public ResultSet getResourceFielsByType(String type) throws SQLException, DatabaseLoadDriverException
	{
		PreparedStatement stat;
		stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceFilterByType);
		stat.setNString(1,type);
		ResultSet rs = stat.executeQuery();
		return rs;
	}
	
	
	public static void newResource(String name,String info,String resourceType) throws SQLException, DatabaseLoadDriverException 
	{	
		PreparedStatement stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertResource);
		int resourceTypeID = addResourceType(resourceType);
		stat.setNString(1,name);
		stat.setInt(2,resourceTypeID);
		stat.setNString(3,info);
		stat.execute();
		stat.close();

	}
	
	
	public boolean removeResource(int resourceID) 
	{	
		return false;
	}
	
	private static int addResourceType(String resourceType) throws SQLException, DatabaseLoadDriverException
	{
		int result = -1;
		{
			PreparedStatement stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceTypes);
			stat.setNString(1,resourceType);
			ResultSet rs = stat.executeQuery();
			if(rs.next())
			{
				result = rs.getInt(1);
			}
			else
			{
				PreparedStatement stat2 = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertResourceType);
				stat2.setNString(1,resourceType);
				stat2.execute();
				stat2.close();
				result = HelpDatabase.getNextInsertTableID(GlobalTablesName.resourcesType)-1;
			}
			rs.close();
			stat.close();
		}
		return result;	
	}
	
	public boolean saveFile(File file) throws IOException, JDOMException
	{
		if(!file.exists())
		{
			file.createNewFile();
			FileHandling.putSaveFileTags(file);
			putSaveSettings(file,GlobalOptions.resourcesName);
		}
		else if(FileHandling.testSaveFile(file))
		{
			file.delete();
			file.createNewFile();
			FileHandling.putSaveFileTags(file);
			putSaveSettings(file,GlobalOptions.resourcesName);
		}
		else if(!FileHandling.testOlderInformation(file,GlobalOptions.resourcesName))
		{
			putSaveSettings(file,GlobalOptions.resourcesName);
		}
		else
		{
			if(overwriteGUI())
			{
				putSaveSettings(file,GlobalOptions.resourcesName);
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	private void putSaveSettings(File file, String string) throws JDOMException, IOException {
	
		SAXBuilder builder = new SAXBuilder();
		Document doc = (Document) builder.build(file);
		Element rootNode = doc.getRootElement();
		rootNode.removeChild(GlobalOptions.resourcesName);
		Element pm = new Element(GlobalOptions.resourcesName);
		Element pmDic = new Element(GlobalOptions.resourcesDictionariesName);
		Element pmLookup = new Element(GlobalOptions.resourcesLookupTablesName);
		Element pmRules= new Element(GlobalOptions.resourcesRulesResourceName);
		Element pmOntologies = new Element(GlobalOptions.resourcesOntologiesName);
		Element pmLexicalWords = new Element(GlobalOptions.resourcesLexicalWordsSet);		
		Element pmSun;
		for(DictionaryAibench dic:getDictionaries().getDictionariesClipboard())
		{
			pmSun = new Element(GlobalOptions.resourcesDictionaryName);
			pmSun.setAttribute("id", String.valueOf(dic.getID()));
			pmDic.addContent(pmSun);
		}
		for(LookupTableAibench dic:getLookuptables().getLookuptablesClipboard())
		{
			pmSun = new Element(GlobalOptions.resourcesLookupTableName);
			pmSun.setAttribute("id", String.valueOf(dic.getID()));
			pmLookup.addContent(pmSun);
		}
		for(RulesAibench dic:getRules().getRulesClipboard())
		{
			pmSun = new Element(GlobalOptions.resourcesRuleSetName);
			pmSun.setAttribute("id", String.valueOf(dic.getID()));
			pmRules.addContent(pmSun);
		}
		for(OntologyAibench dic:getOntologies().getOntologiesClipboard())
		{
			pmSun = new Element(GlobalOptions.resourcesOntologyName);
			pmSun.setAttribute("id", String.valueOf(dic.getID()));
			pmOntologies.addContent(pmSun);
		}
		for(LexicalWordsAibench lexicalWords:getLexicalWords().getLexicalWordsSetClipboard())
		{
			pmSun = new Element(GlobalOptions.resourcesLexicalWords);
			pmSun.setAttribute("id", String.valueOf(lexicalWords.getID()));
			pmLexicalWords.addContent(pmSun);
		}
		pm.addContent(pmDic);
		pm.addContent(pmLookup);
		pm.addContent(pmRules);
		pm.addContent(pmOntologies);
		pm.addContent(pmLexicalWords);
		rootNode.addContent(pm);
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter(file));
	}
	
	private boolean overwriteGUI(){
		Object[] options = new String[]{"Overwrite","Cancel"};
		int opt = showOptionPane("Save Resources Information","The file already contain information about Resources \n Do You wish overwrite it ?", options);
		switch (opt) {
		case 0:
			return true;
		default:
			return false;
		}
	}
	
	/** Presents a option pane with the given title, question and options */
	public static int showOptionPane(String title, String question, Object[] options){
		JOptionPane option_pane = new JOptionPane(question);

		option_pane.setOptions(options);
		JDialog dialog = option_pane.createDialog(Workbench.getInstance().getMainFrame(), title);
		dialog.setVisible(true);
		
		Object choice = option_pane.getValue();
					
		for(int i=0; i<options.length; i++)
			if(options[i].equals(choice))
				return i;
		
		return -1;		
	}

	public void setOntologies(Ontologies ontologies) {
		this.ontologies = ontologies;
	}

	@SuppressWarnings("unchecked")
	public void loadFile(File file) throws JDOMException, IOException, SQLException, DatabaseLoadDriverException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = (Document) builder.build(file);
		Element rootNode = doc.getRootElement();
		Element pm = rootNode.getChild(GlobalOptions.resourcesName);
		Element dics = pm.getChild(GlobalOptions.resourcesDictionariesName);
		List<Element> dicList = dics.getChildren();
		for(Element dic:dicList)
		{
			String id = dic.getAttributeValue("id");
			int idint = Integer.valueOf(id);
			loadDictionary(idint);
		}
		Element lookups = pm.getChild(GlobalOptions.resourcesLookupTablesName);
		List<Element> lookList = lookups.getChildren();
		for(Element look:lookList)
		{
			String id = look.getAttributeValue("id");
			int idint = Integer.valueOf(id);
			loadLookupTable(idint);
		}
		Element rules = pm.getChild(GlobalOptions.resourcesRulesResourceName);
		List<Element> rulesList = rules.getChildren();
		for(Element rule:rulesList)
		{
			String id = rule.getAttributeValue("id");
			int idint = Integer.valueOf(id);
			loadRules(idint);
		}
		Element ontologies = pm.getChild(GlobalOptions.resourcesOntologiesName);
		List<Element> ontologiesList = ontologies.getChildren();
		for(Element ontology:ontologiesList)
		{
			String id = ontology.getAttributeValue("id");
			int idint = Integer.valueOf(id);
			loadOntologies(idint);
		}
		Element lexicalwWords = pm.getChild(GlobalOptions.resourcesLexicalWordsSet);
		List<Element> lexicalwWordsList = lexicalwWords.getChildren();
		for(Element lexicalw2ords:lexicalwWordsList)
		{
			String id = lexicalw2ords.getAttributeValue("id");
			int idint = Integer.valueOf(id);
			loadLexicalWords(idint);
		}
	}

	private void loadLexicalWords(int idint) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceInformtaionByID);
		stat.setInt(1,idint);
		ResultSet rs = stat.executeQuery();	
		if(rs.next())
		{
			LexicalWordsAibench lexical = new LexicalWordsAibench(idint, rs.getString(2),rs.getString(3));
			getLexicalWords().addLexicalWords(lexical);
		}
		rs.close();
		stat.close();
	}

	private void loadOntologies(int idint) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceInformtaionByID);
		stat.setInt(1,idint);
		ResultSet rs = stat.executeQuery();	
		if(rs.next())
		{
			OntologyAibench ontologies = new OntologyAibench( idint, rs.getString(2),rs.getString(3));
			getOntologies().addOntology(ontologies);
		}
		rs.close();
		stat.close();
	}

	private void loadRules(int idint) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceInformtaionByID);
		stat.setInt(1,idint);
		ResultSet rs = stat.executeQuery();	
		if(rs.next())
		{
			RulesAibench rules = new RulesAibench(idint, rs.getString(2),rs.getString(3));
			getRules().addRules(rules);
		}
		rs.close();
		stat.close();
	}

	private void loadLookupTable(int idint) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceInformtaionByID);
		stat.setInt(1,idint);
		ResultSet rs = stat.executeQuery();	
		if(rs.next())
		{
			LookupTableAibench look = new LookupTableAibench(idint, rs.getString(2),rs.getString(3));
			getLookuptables().addLookupTable(look);
		}
		rs.close();
		stat.close();
	}

	private void loadDictionary(int idint) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceInformtaionByID);
		stat.setInt(1,idint);
		ResultSet rs = stat.executeQuery();	
		if(rs.next())
		{

			DictionaryAibench dic = new DictionaryAibench(idint, rs.getString(2),rs.getString(3));
			getDictionaries().addDictionary(dic);
		}
		rs.close();
		stat.close();
	}
	
	/**
	 * Stats
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public static int getnumberTerms(int resourceId) throws SQLException, DatabaseLoadDriverException
	{	
		int result = 0;
		PreparedStatement totalTermsDic = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.totalResourceTerms);
		totalTermsDic.setInt(1,resourceId);
		ResultSet rsResultSet = totalTermsDic.executeQuery();
		if(rsResultSet.next())
		{
			result = rsResultSet.getInt(1);
		}
		rsResultSet.close();
		totalTermsDic.close();
		return result;
	}
	
	public static SortedMap<Integer,String> getResourceContentClasses(int resourceID) throws SQLException, DatabaseLoadDriverException
	{
		SortedMap<Integer,String> classSize = new TreeMap<Integer, String>();	
		PreparedStatement getclassContentPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectResourceclassContent);
		getclassContentPS.setInt(1,resourceID);
		ResultSet rs = getclassContentPS.executeQuery();
		while(rs.next())
		{
			classSize.put(rs.getInt(1), rs.getString(2));
		}
		rs.close();
		getclassContentPS.close();
		return classSize;
	}
	
	public static int getNumberSynonyms(int resourceID) throws DatabaseLoadDriverException, SQLException
	{
		int result = 0;
		Connection conn = Configuration.getDatabase().getConnection();	
		PreparedStatement totalTermsDic = conn.prepareStatement(QueriesResources.totalResourceSyn);
		totalTermsDic.setInt(1,resourceID);
		ResultSet rsResultSet = totalTermsDic.executeQuery();
		if(rsResultSet.next())
		{
			result = rsResultSet.getInt(1);
		}
		rsResultSet.close();
		totalTermsDic.close();
		return result;
	}
	
	public static Object[] getClassContentStats(int resourceID, int classID) throws SQLException, DatabaseLoadDriverException
	{		
		Object[] obj = new Object[3];
		obj[0] = classID;
		PreparedStatement totalTermsDic = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.totalResourceClassTerms);
		totalTermsDic.setInt(1,resourceID);
		totalTermsDic.setInt(2,classID);
		ResultSet rsResultSet = totalTermsDic.executeQuery();
		if(rsResultSet.next())
		{
			obj[1] = rsResultSet.getInt(1);
		}
		rsResultSet.close();
		totalTermsDic.close();
		PreparedStatement totalTermsDicSyn = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.totalResourceClassSyn);
		totalTermsDicSyn.setInt(1,resourceID);
		totalTermsDicSyn.setInt(2,classID);
		ResultSet rsResultSet2 = totalTermsDicSyn.executeQuery();
		if(rsResultSet2.next())
		{
			obj[2] = rsResultSet2.getInt(1);
		}
		rsResultSet2.close();
		totalTermsDicSyn.close();
		return obj;
	}
	
	public static  int getCandidateTerms(IResource<IResourceElement> ruleSnd, Set<Integer> classIDSource) throws SQLException, DatabaseLoadDriverException {
		int sum = 0; 
		for(int classID:classIDSource)
		{
			Object[] data = Resources.getClassContentStats(ruleSnd.getID(),classID);
			sum += (Integer) data[1];
		}
		return sum;
	}

	public static IResource<IResourceElement> getLastResource() throws DatabaseLoadDriverException, SQLException {
		int after = HelpDatabase.getNextInsertTableID(GlobalTablesName.resources)-1;
		PreparedStatement stat = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.getResourcesInfo);
		stat.setInt(1,after);
		ResultSet rs = stat.executeQuery();	
		if(rs.next())
		{
			String type = rs.getString(2);
			String name = rs.getString(3);
			String notes = rs.getString(3);
			return makeResource(after,type,name,notes);
		}
		return null;
	}
	
	private static IResource<IResourceElement> makeResource(int id, String type,String name, String note) {
		if(type.equals(GlobalOptions.resourcesDictionaryName))
		{
			return new DictionaryAibench(id, name, note);
		}
		else if(type.equals(GlobalOptions.resourcesLookupTableName))
		{
			return new LookupTableAibench(id, name, note);
		}
		else if(type.equals(GlobalOptions.resourcesOntologyName))
		{
			return new OntologyAibench(id, name, note);
		}
		else if(type.equals(GlobalOptions.resourcesRuleSetName))
		{
			return new RulesAibench( id, name, note);
		}
		else if(type.equals(GlobalOptions.resourcesLexicalWords))
		{
			return new LexicalWordsAibench( id, name, note);
		}
		return null;
	}

}
