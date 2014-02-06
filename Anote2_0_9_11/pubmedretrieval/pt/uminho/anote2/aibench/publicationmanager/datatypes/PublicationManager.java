package pt.uminho.anote2.aibench.publicationmanager.datatypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import pt.uminho.anote2.aibench.utils.file.FileHandling;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.configuration.ISaveModule;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
/**
 * Aibench Datatype
 * 
 * This class serves to manage publication groping by queries.
 * 
 * @author Hugo Costa
 *
 */
@Datatype(structure = Structure.LIST,namingMethod="getName",removable=false,renamed=false)
public class PublicationManager extends Observable implements Serializable,ISaveModule {
	
	private static final long serialVersionUID = -6847950783302249246L;

	/**
	 * List Of Queries on Clipboard
	 */
	private List<QueryInformationRetrievalExtension> queries;
		
	public static int searchYearStarting = 1900;
	
	public PublicationManager() {
		queries=new ArrayList<QueryInformationRetrievalExtension>();
	}

	public void notifyViewObserver(){
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * This MEthod return a list of Queries present on Clipboard ( Aibench )
	 * 
	 * @return
	 */
	@ListElements(modifiable=true)
	public List<QueryInformationRetrievalExtension> getQueries()
	{
		return this.queries;
	}
	
	/**
	 * This MEthod that add an Query to Clipboard ( Aibench )
	 * 
	 * @return
	 */
	public void addQueryInformationRetrievalExtension(QueryInformationRetrievalExtension query)
	{
		if(compare(query))
		{
			new ShowMessagePopup("Query Already Exist on Clipboard");
		}
		else
		{
			this.queries.add(query);
			new ShowMessagePopup("Query Added");
			this.notifyViewObserver();
		}
	}
	
	public boolean compare(QueryInformationRetrievalExtension query)
	{
		for(QueryInformationRetrievalExtension q:queries)
		{
			if(q.getID()==query.getID())
			{
				return true;
			}			
		}
		return false;
	}
	
	public static void removeQuery(int queryID) throws SQLException, DatabaseLoadDriverException
	{
		PreparedStatement removeQuery = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.removeQuery);
		PreparedStatement removePublicationsQuery = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.removeQueryPublicationLinking);
		PreparedStatement removeQueryProperties = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.removeQueryProperties);
		removeQuery.setInt(1,queryID);
		removePublicationsQuery.setInt(1,queryID);
		removeQueryProperties.setInt(1,queryID);
		removePublicationsQuery.execute();
		removePublicationsQuery.close();
		removeQueryProperties.execute();
		removeQueryProperties.close();
		removeQuery.execute();
		removeQuery.close();
	}

	public String getName()
	{
		return "Publication Manager";
	}
	
	public boolean saveFile(File file) throws IOException, JDOMException
	{
		if(!file.exists())
		{
			file.createNewFile();
			FileHandling.putSaveFileTags(file);
			putSaveSettings(file,GlobalOptions.publicationManagerName);
		}
		else if(FileHandling.testSaveFile(file))
		{
			file.delete();
			file.createNewFile();
			FileHandling.putSaveFileTags(file);
			putSaveSettings(file,GlobalOptions.publicationManagerName);
		}
		else if(!FileHandling.testOlderInformation(file,GlobalOptions.publicationManagerName))
		{
			putSaveSettings(file,GlobalOptions.publicationManagerName);
		}
		else
		{
			if(overwriteGUI())
			{
				putSaveSettings(file,GlobalOptions.publicationManagerName);
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
		rootNode.removeChild(GlobalOptions.publicationManagerName);
		Element pm = new Element(GlobalOptions.publicationManagerName);
		Element pmSun;
		List<QueryInformationRetrievalExtension> listqueries = getQueries();
		for(QueryInformationRetrievalExtension queryID:listqueries)
		{
			pmSun = new Element(GlobalOptions.publicationManagerQuery);
			pmSun.setAttribute("id", String.valueOf(queryID.getID()));
			pm.addContent(pmSun);
		}
		rootNode.addContent(pm);
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter(file));
	}
	
	private boolean overwriteGUI(){
		Object[] options = new String[]{"Overwrite","Cancel"};
		int opt = showOptionPane("Save Publication Manager information","The file already contain information about Publication Manager \n Do You wish overwrite it ?", options);
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

	@SuppressWarnings("unchecked")
	public void loadFile(File file) throws JDOMException, IOException, SQLException, DatabaseLoadDriverException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = (Document) builder.build(file);
		Element rootNode = doc.getRootElement();
		Element pm = rootNode.getChild(GlobalOptions.publicationManagerName);
		List<Element> elems = pm.getChildren();
		for(Element elem:elems)
		{
			String id = elem.getAttributeValue("id");
			int idint = Integer.valueOf(id);
			insertQuery(idint);
		}
	}

	private void insertQuery(int idint) throws SQLException, DatabaseLoadDriverException {
		Properties prop = null;
		PreparedStatement queryInfo = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.selectQueriesInformationByID);
		PreparedStatement queryProperties = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.selectQueryProperties);
		queryInfo.setInt(1,idint);
		ResultSet rs = queryInfo.executeQuery();
		QueryInformationRetrievalExtension query = null;
		prop = new Properties();
		queryProperties.setInt(1,idint);
		ResultSet rs2 = queryProperties.executeQuery();	
		while(rs2.next())
		{
			prop.put(rs2.getString(1),rs2.getString(2));
		}
		if(rs.next())
		{
			query = new QueryInformationRetrievalExtension(
					rs.getInt(1),
					rs.getString(2),
					rs.getDate(3),
					rs.getString(4),
					rs.getString(5),
					rs.getInt(6),
					rs.getInt(7),
					0,
					rs.getBoolean(8),
					prop,
					this);
		}
		rs.close();
		rs2.close();	
		queryInfo.close();
		queryProperties.close();
		queries.add(query);
	}
	
	public static QueryInformationRetrievalExtension getLastQuery(PublicationManager pm) throws DatabaseLoadDriverException, SQLException
	{
		QueryInformationRetrievalExtension query = null;
			PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.selectQueryProperties);
			PreparedStatement statement = Configuration.getDatabase().getConnection().prepareStatement(QueriesProcess.selectLastQueriesPubmed);
			ResultSet rs = statement.executeQuery();
			if(rs.next())
			{	
				ps.setInt(1,rs.getInt(1));
				Properties prop = new Properties();
				ResultSet rs2 = ps.executeQuery();		
				while(rs2.next())
				{
					prop.put(rs2.getString(1),rs2.getString(2));
				}
				rs2.close();
				query = new QueryInformationRetrievalExtension(
						rs.getInt(1),
						rs.getString(2),
						rs.getTime(3),
						rs.getString(4),
						rs.getString(5),
						rs.getInt(6),
						rs.getInt(7),
						0,
						rs.getBoolean(8),
						prop,
						pm
						);
			}
			rs.close();
			statement.close();
			ps.close();
		return query;
	}

}
