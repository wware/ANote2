package pt.uminho.anote2.aibench.curator.documentstructuring;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentStructuring {

	/* Fields in the database to structure in the text. */
	private List<String> dbPublicationFields;
	private File docsDir;
	//private DBSession dbSession;
	
	public DocumentStructuring(File docsDir) {
		this.setDocsDir(docsDir);
		this.setDbPublicationFields(null);
	}

//	public DocumentStructuring(File docsDir, List<String> dbPublicationFields, DBSession dbSession) {
//		this.docsDir = docsDir;
//		this.dbPublicationFields = dbPublicationFields;
//		//this.dbSession = dbSession;
//	}
	
	
//	public void structureTexts(DBSession dbSession) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{
//		DBPublicationInformation dbPubInfo = null;
//		if(dbSession!=null)
//			dbPubInfo = new DBPublicationInformation(dbSession.getHost(), dbSession.getPort(), dbSession.getSchema(), dbSession.getUser(), dbSession.getPass());
//		for(File doc : docsDir.listFiles())
//		{
//			String pmid = doc.getName().replaceFirst("\\..{3}$", "");
//			String docText = FileHandling.textFromFile(doc);
//			if(dbPubInfo!=null)
//			{
//				Map<String, String> pieces = dbPubInfo.getPieces(pmid, dbPublicationFields);
//				docText = annotatePublicationFields(docText, pieces);
//			}
//			
//			
//		}
//	}
	
	public List<String> dbPublicationFields(){
		List<String> pieces = new ArrayList<String>();
		pieces.add("title");
		pieces.add("authors");
		pieces.add("journal");
		pieces.add("abstract");
		return pieces;
	}
	
	public String annotatePublicationFields(String pubText, Map<String, String> fields){
		String resultText = pubText;
		
		for(String field : fields.keySet())
		{
			String token = field.toUpperCase();
			String replace = "<"+token+">" + fields.get(field) +"</"+token+">"; 
			resultText = resultText.replace(fields.get(field), replace);
		}
		
		return resultText;
	}

	public void setDbPublicationFields(List<String> dbPublicationFields) {
		this.dbPublicationFields = dbPublicationFields;
	}

	public List<String> getDbPublicationFields() {
		return dbPublicationFields;
	}

	public void setDocsDir(File docsDir) {
		this.docsDir = docsDir;
	}

	public File getDocsDir() {
		return docsDir;
	}
	
}
