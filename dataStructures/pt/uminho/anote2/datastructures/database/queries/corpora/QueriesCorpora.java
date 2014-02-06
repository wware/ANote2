package pt.uminho.anote2.datastructures.database.queries.corpora;

public class QueriesCorpora {
	
	public static final String insertCorpus = "INSERT INTO corpus (name) VALUES(?)";
	
	public static final String insertCorpusProperties = "INSERT INTO corpus_properties " +
														"VALUES(?,?,?)";
	
	public static final String selectCorpora = "SELECT * FROM corpus";
	
	public static final String selectCorpusProperties  = "SELECT * FROM corpus_properties " +
														 "WHERE corpus_idcorpus=?";
	
	public static final String selectProcesses = "SELECT idprocesses,name,type "+
													   "FROM processes t JOIN processes_type s ON t.processes_type_idprocesses_type = s.idprocesses_type ";
	
	public static final String selectCorpusProcesses = "SELECT idprocesses, name, type "+
											"FROM processes p "+
											"JOIN corpus_has_processes c ON p.idprocesses = c.processes_idprocesses "+
											"JOIN processes_type t ON p.processes_type_idprocesses_type = t.idprocesses_type "+
											"WHERE c.corpus_idcorpus=? ";

	public static final String selectProcessProperties = "SELECT property_name,property_value FROM process_properties " +
														 "WHERE processes_idprocesses=? ";
	
	public static final String insertCorpusPublication = "INSERT INTO corpus_has_publications (corpus_idcorpus,publications_id) " +
														 "VALUES(?,?)";
	
	public static final String insertCorpusProcess = "INSERT INTO corpus_has_processes(corpus_idcorpus,processes_idprocesses) VALUES (?,?) ";
	
	public static final String selectCorpusPublications = "SELECT publications.id, publications.other_id,publications.title, publications.authors, publications.date, publications.status, "+
														  "publications.journal, publications.volume, publications.issue, publications.pages,publications.external_links, publications.abstract, publications.available_pdf "+
														  "FROM corpus_has_publications AS q JOIN publications ON (q.publications_id=id) "+
														  "WHERE q.corpus_idcorpus=? ";


}
