package pt.uminho.anote2.aibench.curator.datastructures.document.parts;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.curator.datastructures.ShortAnnotation;
import pt.uminho.anote2.aibench.curator.datastructures.TermComparator;
import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.ner.EntityAnnotation;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.OrderedMap;

public class CuratorDocumentAnnotations implements Serializable {

	private static final long serialVersionUID = 9027206399419826768L;

	/** Key: Entity Class
	 * Values: Key: Term, Values: Term information */
	private Map<String,HashMap<String,ShortAnnotation>> classTermAnnotations;
	
	/** Map ordered by the size of the term, this map associates the term with its class */
	private OrderedMap<String,String> sortTermClass;

	
	@SuppressWarnings("unchecked")
	public CuratorDocumentAnnotations(){
		this.classTermAnnotations = new HashMap<String, HashMap<String,ShortAnnotation>>();
		@SuppressWarnings("rawtypes")
		TermComparator comparator = new TermComparator<Object>();
		this.sortTermClass = new OrderedMap<String, String>(comparator);
	}
	
	public CuratorDocumentAnnotations(Map<String, HashMap<String,ShortAnnotation>> annotations, OrderedMap<String,String> term_tag, List<String> relations){
		this.classTermAnnotations = annotations;
		this.sortTermClass = term_tag;
	}
	
	@SuppressWarnings("unchecked")
	public CuratorDocumentAnnotations(NERDocumentAnnotation nerDocAnnot) throws SQLException, DatabaseLoadDriverException{
		this.classTermAnnotations = new HashMap<String, HashMap<String,ShortAnnotation>>();
		@SuppressWarnings("rawtypes")
		TermComparator comparator = new TermComparator<Object>();
		this.sortTermClass = new OrderedMap<String, String>(comparator);
		fillTermStructures(nerDocAnnot);
	}

	
	private void fillTermStructures(NERDocumentAnnotation nerDocAnnot) throws SQLException, DatabaseLoadDriverException {
		
		for(AnnotationPosition pos:nerDocAnnot.getEntityAnnotationPositions().getAnnotations().keySet())
		{
			IAnnotation annot = nerDocAnnot.getEntityAnnotationPositions().getAnnotations().get(pos);
			if(annot!=null && annot instanceof IEntityAnnotation)
			{
				IEntityAnnotation entAnnot = (IEntityAnnotation) nerDocAnnot.getEntityAnnotationPositions().getAnnotations().get(pos);
				if(entAnnot != null)
				{
					String entityName = entAnnot.getAnnotationValue();
					String classe =  ClassProperties.getClassIDClass().get(entAnnot.getClassAnnotationID());		
					ShortAnnotation annotAnnot = new ShortAnnotation(1,entityName,entityName,entAnnot.getResourceElementID(), "",entityName);
					if(classTermAnnotations.containsKey(classe))
					{
						if(classTermAnnotations.get(classe).containsKey(entityName))
						{
							classTermAnnotations.get(classe).get(entityName).incCount();
						}
						else
						{
							classTermAnnotations.get(classe).put(entityName, annotAnnot);
						}	
					}
					else
					{
						HashMap<String,ShortAnnotation> hash = new HashMap<String, ShortAnnotation>();
						if(hash.containsKey(entityName))
						{
							hash.get(entityName).incCount();
						}
						else
						{
							hash.put(entityName,annotAnnot);
							classTermAnnotations.put(classe,hash);
						}
					}
					sortTermClass.put(entityName, classe);
				}
			}
		}
	}
	
	public void addAnnotation(IEntityAnnotation elem, String termClass){
		String entity = elem.getAnnotationValue();
		HashMap<String,ShortAnnotation> map = this.classTermAnnotations.get(termClass);
		ShortAnnotation annot = new ShortAnnotation(1,entity,entity,elem.getResourceElementID(),entity,entity);
		if(map==null)
		{
			map = new HashMap<String, ShortAnnotation>();
			this.classTermAnnotations.put(termClass, map);
		}
		else
		{
			if(map.containsKey(entity))
			{
				map.get(entity).incCount();
			}
			else
			{
				map.put(entity, annot);	
			}
		}
		this.sortTermClass.put(entity, termClass);
	}
	
	public void addMoreAnnotion(IEntityAnnotation elem, String termClass,int numberTimes){
		String entity = elem.getAnnotationValue();
		HashMap<String,ShortAnnotation> map = this.classTermAnnotations.get(termClass);
		ShortAnnotation annot = new ShortAnnotation(1,entity,entity,elem.getResourceElementID(),entity,entity);
		if(map==null)
		{
			map = new HashMap<String, ShortAnnotation>();
			this.classTermAnnotations.put(termClass, map);
		}
		if(map.containsKey(entity))
		{
			map.get(entity).incCount(numberTimes);
		}
		else
		{
			map.put(entity, annot);
			map.get(entity).incCount(numberTimes-1);		
		}
		this.sortTermClass.put(entity, termClass);
	}
	
	/** Removes an annotation if it exists. If so, it will also return the previous class of the removed term */
	public void removeOneAnnotation(String term){
		
		String termClass = null;
		if(this.sortTermClass.containsKey(term))
		{
			termClass = this.sortTermClass.get(term);
			HashMap<String,ShortAnnotation> terms = this.classTermAnnotations.get(termClass);
			if(terms.get(term).getCount()==1)
			{
				sortTermClass.remove(term);
				terms.remove(term);
				if(terms.size()==0)
					this.classTermAnnotations.remove(termClass);
			}
			else
			{
				terms.get(term).decCount();
			}
		}	
	}
	
	public void removeAllAnnotation(String term){
		
		String termClass = null;
		if(this.sortTermClass.containsKey(term))
		{
			termClass = this.sortTermClass.get(term);
			this.sortTermClass.remove(term);
			HashMap<String,ShortAnnotation> terms = this.classTermAnnotations.get(termClass);
			terms.remove(term);
			if(terms.size()==0)
				this.classTermAnnotations.remove(termClass);
		}	
	}
	
	public void correctAnnotation(String term, String termClass,int resourceTErmID,int numberTimes){
		removeAllAnnotation(term);
		addMoreAnnotion(new EntityAnnotation(0,0,0,ClassProperties.getClassClassID().get(termClass),resourceTErmID,term,NormalizationForm.getNormalizationForm(term)),termClass,numberTimes);
	}

	
	public void updateCount(String term, String termClass){
		Map<String,ShortAnnotation> map = this.classTermAnnotations.get(termClass);
		ShortAnnotation annotation = map.get(term);
		annotation.incCount();
	}
	
	public String filterTags(String htmlText, List<String> tags) throws SQLException, DatabaseLoadDriverException{
		String resultext = htmlText, token, newTag;
				
		for(String tag: classTermAnnotations.keySet())
		{
			int tagID = ClassProperties.getClassClassID().get(tag);
			if(!tags.contains(tag))
			{	
				token = "<FONT COLOR=" + CorporaProperties.getCorporaClassColor(tagID).getColor() + "><b>";
				newTag = "<!--<FONT COLOR=" + CorporaProperties.getCorporaClassColor(tagID).getColor() + "><b>-->";
				resultext = resultext.replaceAll(token, newTag);
			}
			else
			{
				newTag = "<FONT COLOR=" + CorporaProperties.getCorporaClassColor(tagID).getColor() + "><b>";
				token = "<!--<FONT COLOR=" + CorporaProperties.getCorporaClassColor(tagID).getColor() + "><b>-->";
				resultext = resultext.replaceAll(token, newTag);
			}
		}
		return resultext;
	}
	
	public CuratorDocumentAnnotations clone(){
		Map<String,HashMap<String,ShortAnnotation>> cloneClassTermAnnotations = new HashMap<String, HashMap<String,ShortAnnotation>>();
		OrderedMap<String,String> cloneOrderedTermClass = new OrderedMap<String, String>(this.sortTermClass.getComparator());
		List<String> cloneRelations = new ArrayList<String>();
		
		for(String cls: this.classTermAnnotations.keySet())
		{
			HashMap<String,ShortAnnotation> classAnnotations = new HashMap<String, ShortAnnotation>();
			for(String term: this.classTermAnnotations.get(cls).keySet())
			{
				ShortAnnotation annotation = this.classTermAnnotations.get(cls).get(term).clone();
				classAnnotations.put(term, annotation);
			}
			cloneClassTermAnnotations.put(cls,classAnnotations);
		}
		
		for(String term : this.sortTermClass.keySet())
			cloneOrderedTermClass.put(term, this.sortTermClass.get(term));
		
		return new CuratorDocumentAnnotations(cloneClassTermAnnotations, cloneOrderedTermClass, cloneRelations);
	}

	
	public Map<String, HashMap<String,ShortAnnotation>> getClassTermAnnotations() {return classTermAnnotations;}
	public void setClassTermAnnotations(Map<String, HashMap<String,ShortAnnotation>> annotations) {this.classTermAnnotations = annotations;}
	public OrderedMap<String, String> getTermClass() {return sortTermClass;}
	public void setTermClass(OrderedMap<String, String> term_tag) {this.sortTermClass = term_tag;}
}
