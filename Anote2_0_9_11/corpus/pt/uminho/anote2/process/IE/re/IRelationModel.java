package pt.uminho.anote2.process.IE.re;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.utils.IGenericPair;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;

public interface IRelationModel {
	
	/**
	 * Method that return a list of char that possible ends a relation
	 * Examples . , :
	 * This endRelations are used in posTagger  
	 * 
	 * @return
	 */
	public Set<String> getRelationTerminations();
	
	/**
	 * This method receive a information about posTagging ( Sintatic LAyer) verbs,verb chunker,
	 * endRelations connectors.
	 * 
	 * and information about biological entities ( Semantic Layer ) and return
	 * all relations of the sentence
	 * @param sintaticLayer sintatic Annotations
	 * @param semanticLayer semantic annotations
	 * 
	 * @return List of relations
	 */
	public List<IEventAnnotation> extractSentenceRelation(IAnnotatedDocument document,List<IEntityAnnotation> semanticLayer,IGenericPair<List<IVerbInfo>, List<Long>> sintaticLayer) throws SQLException, DatabaseLoadDriverException;

}
