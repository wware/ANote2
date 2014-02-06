package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.exceptions.NonExistingConnection;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesProcess;
import pt.uminho.anote2.datastructures.utils.FileHandling;
import pt.uminho.anote2.datastructures.utils.Utils;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

@Operation()
public class AddPublicationToQueryOperation {
	
	private QueryInformationRetrievalExtension query;
	private IPublication pub;
	private File file;
	
	@Port(name="Query",direction=Direction.INPUT,order=1)
	public void getQuery( QueryInformationRetrievalExtension query){
		this.query=query;
	}
	
	@Port(name="Pub",direction=Direction.INPUT,order=2)
	public void getPub(IPublication pub){
		this.pub=pub;
	}
	
	@Port(name="File",direction=Direction.INPUT,order=3)
	public void getFile(File file){
		this.file=file;
	
	}
	
	@Port(name="Relevance",direction=Direction.INPUT,order=4)
	public void getRelevance(String relavance){
		
		int pubID = insertPublication();
		File fileDest;
		if(pub.getOtherID().equals(""))
		{
			fileDest = new File(PublicationManager.saveDocs+"id"+pubID+".pdf");
		}
		else
		{
			fileDest = new File(PublicationManager.saveDocs+"id"+pubID+"-"+pub.getOtherID()+".pdf");
		};
		try {
			addRelation(pubID);
			refreshQueryInfo();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(file!=null)
		{
			try {
				FileHandling.copy(file, fileDest);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(relavance!=null)
		{
			try {
				updateRelevance(pubID,relavance);
			} catch (NonExistingConnection e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		query.notifyViewObserver();
	}
	
	public void updateRelevance(int pubID,String relevance) throws NonExistingConnection{
		
		Connection connection = this.query.getPubManager().getDb().getConnection();

		if(connection == null)
			throw new NonExistingConnection("There is no Database connection");

		String statement = "INSERT INTO document_relevance " +
		"VALUES ( " +  pubID + "," + query.getID() + ",\'" + relevance + "\')";
		try {
			Statement stat = (Statement) connection.createStatement();
			stat.execute(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int insertPublication()
	{		
		int result = -1;
		
		HashMap<String, Integer> tablesSize;
		try {
			query.getPubManager().getDb().openConnection();
			PreparedStatement add_pub_stat = query.getPubManager().getDb().getConnection().prepareStatement(QueriesPublication.insertPublications);
			tablesSize = getDbColumnsInfo(query.getPubManager().getDb().getConnection());
			result = addPubToDB(add_pub_stat,pub,true,tablesSize );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	protected int addPubToDB(PreparedStatement p_statement,IPublication pub, boolean is_new,HashMap<String, Integer> tablesSize) throws NumberFormatException, SQLException{
	
		int pubID =  HelpDatabase.getNextInsertTableID(this.query.getPubManager().getDb(),"publications");
		// Pmid
		String pmid = pub.getOtherID();
		if(is_new)
			p_statement.setString(1,pmid);
		addToStatement(pub.getTitle(),2, p_statement, "title", "",tablesSize);	
		addToStatement(pub.getAuthors(),3, p_statement, "authors", "",tablesSize);
		String aux = pub.getDate();
		if(aux!=null && !aux.equals("null") && !aux.equals(""))
		{
			String[] tokens = aux.split("[ ;,-]");
			aux = tokens[0];
			p_statement.setInt(4, Integer.decode(aux));
		}
		else
			p_statement.setString(4,null);
		
		if(aux!=null && !aux.equals(""))
		{
			String dte = Utils.formatDate(aux+";");
			if(dte.length()>=tablesSize.get("fulldate"))
				dte = dte.substring(0,tablesSize.get("fulldate"));
			p_statement.setString(5, dte);
		}
		else
			p_statement.setString(5,null);
		addToStatement("",6, p_statement, "type", null,tablesSize);
		addToStatement(pub.getStatus(),7, p_statement, "status", null,tablesSize);
		addToStatement(pub.getJournal(),8, p_statement, "journal", null,tablesSize);
		addToStatement(pub.getVolume(),9, p_statement, "volume", null,tablesSize);
		addToStatement(pub.getIssue(),10, p_statement, "issue", null,tablesSize);
		addToStatement(pub.getPages(),11, p_statement, "pages", null,tablesSize);
		addToStatement(pub.getAbstractSection(),12, p_statement, "abstract", null,tablesSize);
	

		if(is_new)
		{
			// PMid represent the type of article not ID on database
			p_statement.setInt(13,pub.getID());
			p_statement.execute();
		}

		return pubID;

	}
	
	
	protected void addToStatement(String value, int j, PreparedStatement p_statement, String column, String default_value,HashMap<String, Integer> tablesSize) throws SQLException{
		if(value!=null && !value.equals(""))
		{
			if(value.length()>=tablesSize.get(column))
				value = value.substring(0,tablesSize.get(column));
			p_statement.setString(j, value);
		}
		else
			p_statement.setString(j, default_value);
	}
	

	/** Add the relation: (id_query,pmid) to database 
	 * @throws SQLException */
	protected void addRelation(int id) throws SQLException{
		String q2 = "INSERT IGNORE INTO `queries_has_publications` " + 
	    "SET `queries_idqueries` = ?, `publications_id` = ?";
		PreparedStatement add_rel_stat = this.query.getPubManager().getDb().getConnection().prepareStatement(q2);
		add_rel_stat.setInt(1,query.getID());
		add_rel_stat.setInt(2, id);
		add_rel_stat.execute();
	}
	
	/** Refresh query information 
	 * @param connection Database connection
	 * @param id_query Query id 
	 * @param n_pubs number of publications related to the query
	 * @param n_abs number of publications related to the query*/
	protected void refreshQueryInfo(){
		
	    if(query.getID()==-1)
		return;
	    query.setMatching_publications(query.getDocumentMathing()+1);
	    if(pub.getAbstractSection()!=null&&pub.getAbstractSection().length()>0)
	    {
	    	query.setAvailable_abstracts(query.getAvailable_abstracts()+1);
	    }
		Connection connection = query.getPubManager().getDb().getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(QueriesIRProcess.updateQueryInfo);
			statement.setInt(1,query.getDocumentMathing());
			statement.setInt(2,query.getAvailable_abstracts());
			statement.setInt(3,query.getID());
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
     
    protected static HashMap<String, Integer> getDbColumnsInfo(Connection c) throws SQLException{

		HashMap<String, Integer> db_columns = new HashMap<String, Integer>();
		Statement statement = c.createStatement();
		ResultSet res = statement.executeQuery(QueriesPublication.selectPublicationsResume);
		ResultSetMetaData rsmd = res.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		for(int i=1;i<=numberOfColumns;i++){
			db_columns.put(rsmd.getColumnName(i),rsmd.getPrecision(i));
		}
		return db_columns;
    }
    

}
