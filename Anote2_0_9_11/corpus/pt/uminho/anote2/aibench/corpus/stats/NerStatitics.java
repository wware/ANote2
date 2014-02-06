package pt.uminho.anote2.aibench.corpus.stats;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.datastructures.annotation.ner.SimpleEntity;

public class NerStatitics
{	
	private int nerAnnotations;
	private SortedMap<SimpleEntity,EntityAndSynonymsStats> entityStatistics;
	private SortedMap<SimpleEntity,EntityDocuments> entityPerDocuments;
	private Map<Integer,SortedSet<SimpleEntity>> classSimpleEntities;	
	private Map<Integer,Integer> classOcurrences;	
	private Map<Integer,DocumentClassEntities> docClasses;
	private Map<Integer,Integer> docAnnotations;
	private Map<Integer,String> docIDDocsOtherID;

	
	public NerStatitics(Map<Integer,IAnnotatedDocument> docsAnnot) throws SQLException, DatabaseLoadDriverException
	{
		this.nerAnnotations=0;
		this.classSimpleEntities=new HashMap<Integer,SortedSet<SimpleEntity>>();
		this.entityStatistics=new TreeMap<SimpleEntity, EntityAndSynonymsStats>();
		this.docIDDocsOtherID = new HashMap<Integer, String>();
		this.entityPerDocuments = new TreeMap<SimpleEntity, EntityDocuments>();
		this.docClasses = new HashMap<Integer,DocumentClassEntities>();
		this.classOcurrences = new HashMap<Integer, Integer>();
		this.docAnnotations = new HashMap<Integer, Integer>();
		getnerstatitics(docsAnnot);
	}

	synchronized private void addEntity(IEntityAnnotation ent,IAnnotatedDocument doc)
	{
		SimpleEntity simpleEntity = new SimpleEntity(ent.getAnnotationValue(), ent.getClassAnnotationID(), ent.getResourceElementID());
		updateEntityStats(ent, simpleEntity);	
		updateDocumentInfo(doc, simpleEntity);	
		nerAnnotations++;
	}

	synchronized private void updateDocumentInfo(IAnnotatedDocument doc,SimpleEntity simpleEntity) {
		if(!this.entityPerDocuments.containsKey(simpleEntity))
			this.entityPerDocuments.put(simpleEntity, new EntityDocuments());
		this.entityPerDocuments.get(simpleEntity).addEntityToDocument(doc);	
		if(!docClasses.containsKey(doc.getID()))
		{
			DocumentClassEntities docClassEnt = new DocumentClassEntities();
			docClassEnt.update(simpleEntity);
			docClasses.put(doc.getID(),docClassEnt);	
			docAnnotations.put(doc.getID(), 1);
		}
		else
		{
			docClasses.get(doc.getID()).update(simpleEntity);
			int number = docAnnotations.get(doc.getID());
			docAnnotations.put(doc.getID(), ++number);
		}
		if(!docIDDocsOtherID.containsKey(doc.getID()))
			docIDDocsOtherID.put(doc.getID(), doc.getOtherID());
	}

	synchronized public Map<Integer, Integer> getDocAnnotations() {
		return docAnnotations;
	}

	synchronized private void updateEntityStats(IEntityAnnotation ent,SimpleEntity simpleEntity) {
		if(!this.entityStatistics.containsKey(simpleEntity))
		{
			EntityAndSynonymsStats stats = new EntityAndSynonymsStats(simpleEntity.getName());
			this.entityStatistics.put(simpleEntity, stats);
		}
		this.entityStatistics.get(simpleEntity).update(simpleEntity.getName());
		if(!this.classSimpleEntities.containsKey(simpleEntity.getClassID()))
		{
			this.classSimpleEntities.put(simpleEntity.getClassID(),new TreeSet<SimpleEntity>());
		}
		if(!this.classSimpleEntities.get(simpleEntity.getClassID()).contains(simpleEntity))
		{
			this.classSimpleEntities.get(simpleEntity.getClassID()).add(simpleEntity);		
		}		
		updateClassOcurrences(simpleEntity);
	}

	synchronized private void updateClassOcurrences(SimpleEntity simpleEntity) {
		if(this.classOcurrences.containsKey(simpleEntity.getClassID()))
		{
			int number = this.classOcurrences.get(simpleEntity.getClassID());
			this.classOcurrences.put(simpleEntity.getClassID(), ++number);
		}
		else
		{
			this.classOcurrences.put(simpleEntity.getClassID(), 1);
		}
	}

	

	synchronized private void getnerstatitics(Map<Integer, IAnnotatedDocument> docsAnnot) throws SQLException, DatabaseLoadDriverException
	{
		for(IAnnotatedDocument doc:docsAnnot.values())
		{
			List<IEntityAnnotation> ents = doc.getEntitiesAnnotations();
			for(IEntityAnnotation entAnnot:ents)
			{
				addEntity(entAnnot,doc);
			}
		}
	}		

	synchronized public int getNerAnnotations() {
		return nerAnnotations;
	}
	
	synchronized public Map<Integer, SortedSet<SimpleEntity>> getClassSimpleEntities() {
		return classSimpleEntities;
	}


	synchronized public Map<SimpleEntity, EntityAndSynonymsStats> getEntityStatistics() {
		return entityStatistics;
	}

	synchronized public Map<SimpleEntity, EntityDocuments> getEntityPerDocuments() {
		return entityPerDocuments;
	}


	synchronized public Map<Integer, DocumentClassEntities> getDocClasses() {
		return docClasses;
	}

	synchronized public Map<Integer, String> getDocIDDocsOtherID() {
		return docIDDocsOtherID;
	}
	
	synchronized public Map<Integer, Integer> getClassOcurrences() {
		return classOcurrences;
	}

	public void freeMemory() {
		this.nerAnnotations = 0;
		this.classSimpleEntities=null;
		this.entityStatistics=null;
		this.docIDDocsOtherID = null;
		this.entityPerDocuments = null;
		this.docClasses = null;
		this.classOcurrences = null;
		this.docAnnotations = null;
		
	}

}

