package pt.uminho.anote2.aibench.publicationmanager.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.RelevanceData;
import pt.uminho.anote2.aibench.utils.exceptions.NonExistingConnection;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesDocument;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;

/**
 * Aibench Operation -- Operation for changing relevance publication in a query
 * 
 * @author Hugo Costa
 *
 */
@Operation(name="Relevance of the Publication", description="Select the Relevance of the Publication")
public class SelectRelevance {
	private IPublication pubResume;
	private IDatabase db;
	
	@Port(name="database",direction=Direction.INPUT,order=1)
	public void setDb(IDatabase db){
		this.db=db;
	}
	
	@Port(name="publication",direction=Direction.INPUT,order=2)
	public void setPubResule(IPublication pubResume){
		this.pubResume = pubResume;
	}
	
	@Port(name="listchangedata",direction=Direction.INPUT,order=3)
	public void setRelevance(List<RelevanceData> listChangeData) throws NonExistingConnection{
		for(RelevanceData data:listChangeData)
		{
			updateRelevance(data);
		}
	}
		
	public void updateRelevance(RelevanceData data) throws NonExistingConnection{
				
		db.openConnection();
		Connection connection = db.getConnection();
		
		if(connection == null)
			throw new NonExistingConnection("There is no Database connection");
		
		if(data.getPreviousRelevance()==null||data.getPreviousRelevance().equals(""))
		{			
			try {
				Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(QueriesDocument.insertRelevanceInDocInQuery);
				ps.setInt(1,this.pubResume.getID());
				ps.setInt(2,data.getIdQuery() );
				ps.setString(3,data.getRelevance());
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(QueriesDocument.updateRelevanceDocInQuery);
				ps.setString(1,data.getRelevance());
				ps.setInt(2,data.getIdQuery() );
				ps.setInt(3,this.pubResume.getID());
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				db.closeConnection();
			}
		}
		db.closeConnection();
	}
	
}
