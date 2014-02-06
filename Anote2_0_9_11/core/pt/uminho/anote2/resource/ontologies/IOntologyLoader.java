package pt.uminho.anote2.resource.ontologies;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;


public interface IOntologyLoader {
	
	public boolean validateFile(File file) throws IOException;
	public IResourceUpdateReport processOntologyFile(File file) throws IOException, DatabaseLoadDriverException, SQLException;
	public IOntology getOntology();

}
