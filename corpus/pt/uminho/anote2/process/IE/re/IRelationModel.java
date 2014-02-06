package pt.uminho.anote2.process.IE.re;

import java.util.List;
import java.util.Set;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.datastructures.utils.GenericPair;

public interface IRelationModel {
	
	/**
	 * Method that return a list of char that possible ends a relation
	 * Examples . , :
	 * This endRelations are used in posTagger  
	 * 
	 * @return
	 */
	public abstract Set<String> getRelationTerminations();
	
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
	public abstract List<IEventAnnotation> extractSentenceRelation(List<IEntityAnnotation> semanticLayer,GenericPair<List<IVerbInfo>, List<Long>> sintaticLayer);

}
