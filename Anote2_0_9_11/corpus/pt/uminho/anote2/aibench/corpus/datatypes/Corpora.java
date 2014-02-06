package pt.uminho.anote2.aibench.corpus.datatypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
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
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.corpora.QueriesCorpora;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.process.IProcess;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.manualcuration.ManualCurationEnum;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;

@Datatype(structure = Structure.LIST,namingMethod="getNameProject",removable=false,renamed=false)
public class Corpora extends Observable implements Serializable,ISaveModule{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7424015800573471356L;
	private List<ICorpus> allCorpus;
	private List<ICorpus> corpusAibench;
	
	public Corpora()
	{
		this.corpusAibench = new ArrayList<ICorpus>();
		this.allCorpus = null;
	}

	@ListElements(modifiable=true)
	public List<ICorpus> getCorpusSet() {
		return corpusAibench;
	}
	
	public Corpus createCorpus(String description,Properties prop) throws DatabaseLoadDriverException, SQLException
	{
		int nextCorpus;
		Connection con = Configuration.getDatabase().getConnection();
		nextCorpus = HelpDatabase.getNextInsertTableID(GlobalTablesName.corpus);
		PreparedStatement insertCorpus = con.prepareStatement(QueriesCorpora.insertCorpus);
		insertCorpus.setNString(1,description);
		insertCorpus.execute();
		insertCorpus.close();
		PreparedStatement insertCorpusProperties = con.prepareStatement(QueriesCorpora.insertCorpusProperties);
		insertCorpusProperties.setInt(1, nextCorpus);
		for(String pro:prop.stringPropertyNames())
		{
			String value = prop.getProperty(pro);
			insertCorpusProperties.setNString(2,pro);
			insertCorpusProperties.setNString(3,value);
			insertCorpusProperties.execute();
		}
		insertCorpusProperties.close();	
		return new Corpus(nextCorpus,description,this,prop);
	}
	
	public void addCorpus(ICorpus corpus)
	{
		if(alreadyExist(corpus))
		{
			new ShowMessagePopup("Corpus Already In Clipboard");
		}
		else
		{
			this.corpusAibench.add(corpus);
			new ShowMessagePopup("Corpus Added To Clipboard.");
			notifyViewObservers();
		}
	}
	
	private boolean alreadyExist(ICorpus corpus) {
		for(ICorpus cor:corpusAibench)
		{
			if(cor.getID()==corpus.getID())
			{
				return true;
			}
		}
		return false;	
	}

	public void notifyViewObservers()
	{
		this.setChanged();
		this.notifyObservers();
	}
	
	
	public List<ICorpus> getAllCorpus() throws DatabaseLoadDriverException, SQLException {
		if(allCorpus==null)
		{
			findCorpus();
		}
		return allCorpus;
	}

	public void findCorpus() throws DatabaseLoadDriverException, SQLException
	{
		allCorpus = new ArrayList<ICorpus>();
		Connection con = Configuration.getDatabase().getConnection();	
		PreparedStatement findCorpus = con.prepareStatement(QueriesCorpora.selectCorpora);
		PreparedStatement findCorpusProperties = con.prepareStatement(QueriesCorpora.selectCorpusProperties);
		ResultSet rs = findCorpus.executeQuery();
		int corpusID;
		String corpusDescription;
		Corpus corpus;
		Properties prop = new Properties();
		while(rs.next())
		{
			corpusID = rs.getInt(1);
			corpusDescription = rs.getString(2);
			findCorpusProperties.setInt(1, corpusID);
			ResultSet rs2 = findCorpusProperties.executeQuery();
			while(rs2.next())
			{
				prop.put(rs2.getString(2), rs2.getString(3));
			}
			rs2.close();
			corpus = new Corpus(corpusID,corpusDescription,this,prop);
			allCorpus.add(corpus);
			prop = new Properties();
		}
		rs.close();
		findCorpus.close();
		findCorpusProperties.close();
	}
	
	public String getNameProject()
	{
		return "Corpora";
	}
	
	public List<IProcess> getListProcess() throws SQLException, DatabaseLoadDriverException
	{
		List<IProcess> list = new ArrayList<IProcess>();
		PreparedStatement findProc;
		findProc = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectProcesses);
		PreparedStatement finProcProp = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectProcessProperties);
		ResultSet rs = findProc.executeQuery();
		int id;
		String name,type;
		Properties prop;
		ResultSet rs2;
		while(rs.next())
		{
			id = rs.getInt(1);
			name = rs.getString(2);
			type = rs.getString(3);
			prop = new Properties();
			finProcProp.setInt(1,id);
			rs2 = finProcProp.executeQuery();
			while(rs2.next())
			{
				prop.put(rs2.getString(1), rs2.getString(2));
			}
			rs2.close();
			IIEProcess process = new IEProcess(id,null,name,type,prop);
			list.add(process);
		}
		rs.close();
		findProc.close();
		finProcProp.close();
		return list;
	}

	public void updateCorpus() throws DatabaseLoadDriverException, SQLException {
		allCorpus = new ArrayList<ICorpus>();
		findCorpus();
	}
	
	public boolean saveFile(File file) throws IOException, JDOMException
	{
		if(!file.exists())
		{
			file.createNewFile();
			FileHandling.putSaveFileTags(file);
			putSaveSettings(file,GlobalOptions.corporaName);
		}
		else if(FileHandling.testSaveFile(file))
		{
			file.delete();
			file.createNewFile();
			FileHandling.putSaveFileTags(file);
			putSaveSettings(file,GlobalOptions.corporaName);
		}
		else if(!FileHandling.testOlderInformation(file,GlobalOptions.corporaName))
		{
			putSaveSettings(file,GlobalOptions.corporaName);
		}
		else
		{
			if(overwriteGUI())
			{
				putSaveSettings(file,GlobalOptions.corporaName);
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
		rootNode.removeChild(GlobalOptions.corporaName);
		Element pm = new Element(GlobalOptions.corporaName);
		Element corpusElemt;
		for(ICorpus corpus: getCorpusSet())
		{
			corpusElemt = new Element(GlobalOptions.corporacorpusName);
			corpusElemt.setAttribute("id",String.valueOf(corpus.getID()));
			processCorpusProcesses((Corpus)corpus,corpusElemt);
			pm.addContent(corpusElemt);			
		}
		rootNode.addContent(pm);
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter(file));
	}
	
	private void processCorpusProcesses(Corpus corpus, Element corpusElemt) {
		Element processesElement;
		for(IIEProcess processes:corpus.getIEProcesses())
		{
			processesElement = new Element(GlobalOptions.corporaProcessName);
			processesElement.setAttribute("id", String.valueOf(processes.getID()));
			processProcess(processes,processesElement);
			corpusElemt.addContent(processesElement);
		}
	}

	private void processProcess(IIEProcess processes, Element processesElement) {
		Element annotatedDocument;		
		if(processes instanceof RESchema)
		{
			RESchema process = (RESchema) processes;
			for(IAnnotatedDocument doc : process.getAnnotatedDocs())
			{
				annotatedDocument = new Element(GlobalOptions.corporaAnnotatedDocument);
				annotatedDocument.setAttribute("id",String.valueOf(doc.getID()));
				processesElement.addContent(annotatedDocument);
			}
		}
		else
		{
			NERSchema nerProcess = (NERSchema) processes;
			for(IAnnotatedDocument doc : nerProcess.getAnnotatedDocs())
			{
				annotatedDocument = new Element(GlobalOptions.corporaAnnotatedDocument);
				annotatedDocument.setAttribute("id",String.valueOf(doc.getID()));
				processesElement.addContent(annotatedDocument);
			}
		}

	}

	private boolean overwriteGUI(){
		Object[] options = new String[]{"Overwrite","Cancel"};
		int opt = showOptionPane("Save Corpora Information","The file already contain information about Corpora \n Do You wish overwrite it ?", options);
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
		Element pm = rootNode.getChild(GlobalOptions.corporaName);
		List<Element> elems = pm.getChildren();
		for(Element elem:elems)
		{
			String id = elem.getAttributeValue("id");
			int idint = Integer.valueOf(id);
			insertCorpus(idint,elem);
		}
		
	}

	private void insertCorpus(int corpusID, Element elem) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement findCorpus = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectCorporaInformationByID);
		findCorpus.setInt(1, corpusID);
		PreparedStatement findCorpusProperties = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectCorpusProperties);
		ResultSet rs = findCorpus.executeQuery();
		String corpusDescription;
		Corpus corpus;
		Properties prop = new Properties();
		if(rs.next())
		{
			corpusDescription = rs.getString(2);
			findCorpusProperties.setInt(1, corpusID);
			ResultSet rs2 = findCorpusProperties.executeQuery();
			while(rs2.next())
			{
				prop.put(rs2.getString(2), rs2.getString(3));
			}
			rs2.close();
			corpus = new Corpus(corpusID,corpusDescription,this,prop);
			putCorpusProcesses(corpus,elem);
			getCorpusSet().add(corpus);
		}
		rs.close();
		findCorpus.close();
		findCorpusProperties.close();	
	}

	@SuppressWarnings("unchecked")
	private void putCorpusProcesses(Corpus corpus, Element elem) throws SQLException, DatabaseLoadDriverException {
		List<Element> processesList = elem.getChildren();
		for(Element process:processesList)
		{
			String id = process.getAttributeValue("id");
			int idint = Integer.valueOf(id);
			putProcess(idint,process,corpus);
		}
	}

	private void putProcess(int processID, Element processElem, Corpus corpus) throws SQLException, DatabaseLoadDriverException {
				
		PreparedStatement findCorpusProcesses = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectProcessInfoByID);
		PreparedStatement findProcessProperties = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectProcessProperties);		
		findCorpusProcesses.setInt(1,processID);
		ResultSet rs = findCorpusProcesses.executeQuery();
		String processDescription,processType;
		if(rs.next())
		{
			Properties prop = new Properties();
			String propertyKey,propertyValue;
			processDescription = rs.getString(2);
			processType = rs.getString(3);
			findProcessProperties.setInt(1, processID);
			ResultSet rsProperties = findProcessProperties.executeQuery();
			while(rsProperties.next())
			{
				propertyKey = rsProperties.getString(1);
				propertyValue = rsProperties.getString(2);
				prop.put(propertyKey, propertyValue);
			}
			rsProperties.close();
			IEProcess processLoad = new IEProcess(processID,corpus,processDescription,processType,prop);
			if(processLoad.getType().equals(GlobalNames.ner)||processLoad.getType().equals(ManualCurationEnum.NER.getProcessName()))
			{
				NERSchema nerProcess = new NERSchema(processLoad.getID(),corpus, processLoad.getName(),GlobalNames.ner , processLoad.getProperties());
				putNERnnotatedDocuments(processID,processElem,corpus,nerProcess);
				corpus.addProcess(nerProcess);
			}
			else if(processLoad.getType().equals(GlobalNames.re)||processLoad.getType().equals(ManualCurationEnum.RE.getProcessName()))
			{
				RESchema reProcess = new RESchema(processLoad.getID(),corpus, processLoad.getName(),GlobalNames.re , processLoad.getProperties());
				putREAnnotatedDocuments(processID,processElem,corpus,reProcess);		
				corpus.addProcess(reProcess);
			}
		}
		rs.close();
		findCorpusProcesses.close();
		findProcessProperties.close();
	}

	@SuppressWarnings("unchecked")
	private void putREAnnotatedDocuments(int processID, Element processElem,Corpus corpus, RESchema reProcess) throws SQLException, DatabaseLoadDriverException {
		if(processElem!=null)
		{
			List<Element> annotDocs = processElem.getChildren();
			for(Element annotElement:annotDocs)
			{
				String id = annotElement.getAttributeValue("id");
				int idint = Integer.valueOf(id);
				putREDocumentAnnotatedDocuments(processID,corpus,reProcess,idint);
			}	
		}	
	}

	private void putREDocumentAnnotatedDocuments(int processID, Corpus corpus,RESchema reProcess, int idint) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement findPub = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectPublicationsbyID);
		findPub.setInt(1,idint);
		ResultSet rs = findPub.executeQuery();
		IPublication pub = null;
		if(rs.next())
		{
			pub = new 	Publication(rs.getInt(1), 
					rs.getString(2),//int pmid
					rs.getString(3),//String title
					rs.getString(4),//String authors
					rs.getString(5),//int date
					rs.getString(6),//String status
					rs.getString(7),//String journal
					rs.getString(8),//String volume
					rs.getString(9),//String issue
					rs.getString(10),//String pages
					rs.getString(11),
					rs.getString(12),//String aBstract
					rs.getBoolean(13));
		}
		rs.close();
		findPub.close();
		IAnnotatedDocument annotDoc = new REDocumentAnnotation(reProcess, corpus, pub);
		reProcess.addAnnotatedDocument((REDocumentAnnotation) annotDoc);		
	}

	@SuppressWarnings("unchecked")
	private void putNERnnotatedDocuments(int processID, Element processElem,Corpus corpus, NERSchema nerProcess) throws SQLException, DatabaseLoadDriverException {
		if(processElem!=null)
		{
			List<Element> annotDocs = processElem.getChildren();
			for(Element annotElement:annotDocs)
			{
				String id = annotElement.getAttributeValue("id");
				int idint = Integer.valueOf(id);
				putNERDocumentAnnotatedDocuments(processID,corpus,nerProcess,idint);
			}	
		}
	}

	private void putNERDocumentAnnotatedDocuments(int processID, Corpus corpus,NERSchema nerProcess, int idint) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement findPub = Configuration.getDatabase().getConnection().prepareStatement(QueriesCorpora.selectPublicationsbyID);
		findPub.setInt(1,idint);
		ResultSet rs = findPub.executeQuery();
		IPublication pub = null;
		if(rs.next())
		{
			pub = new 	Publication(rs.getInt(1), 
					rs.getString(2),//int pmid
					rs.getString(3),//String title
					rs.getString(4),//String authors
					rs.getString(5),//int date
					rs.getString(6),//String status
					rs.getString(7),//String journal
					rs.getString(8),//String volume
					rs.getString(9),//String issue
					rs.getString(10),//String pages
					rs.getString(11),
					rs.getString(12),//String aBstract
					rs.getBoolean(13));
		}
		rs.close();
		findPub.close();
		IAnnotatedDocument annotDoc = new NERDocumentAnnotation(nerProcess, corpus, pub);
		nerProcess.addAnnotatedDocument((NERDocumentAnnotation) annotDoc);	
	}

	@Override
	public String getName() {
		return "Corpora";
	}

	public void removeCorpus(Corpus corpus) {
		corpusAibench.remove(corpus);
	}
	
}
