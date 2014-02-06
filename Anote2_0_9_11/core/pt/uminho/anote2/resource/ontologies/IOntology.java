package pt.uminho.anote2.resource.ontologies;

import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;

public interface IOntology extends IResource<IResourceElement>{
	
	public boolean isOntologyFill() throws SQLException, DatabaseLoadDriverException;
	public IResourceElementSet<IResourceElement> getAllTermsAndSynonyms()  throws SQLException, DatabaseLoadDriverException;
	public IResourceElementSet<IResourceElement> getAllTermByClass(String termClass)  throws SQLException, DatabaseLoadDriverException;
	public IResourceElementSet<IResourceElement> getAllTermByClass(int termClassID)  throws SQLException, DatabaseLoadDriverException;
	public boolean addTermNoRestritions(IResourceElement elem);
	public boolean addSynonymsNoRestritions(IResourceElement elem);

}
