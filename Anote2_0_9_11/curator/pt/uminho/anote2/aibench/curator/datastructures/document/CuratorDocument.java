package pt.uminho.anote2.aibench.curator.datastructures.document;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.curator.datastructures.document.parts.CuratorDataBaseAnnotationsManagement;
import pt.uminho.anote2.aibench.curator.datastructures.document.parts.CuratorDocumentAnnotations;
import pt.uminho.anote2.aibench.curator.datastructures.document.parts.CuratorDocumentHTMLAndText;
import pt.uminho.anote2.aibench.curator.datastructures.document.parts.CuratorDocumentPreviousState;
import pt.uminho.anote2.aibench.curator.document.ParsingUtils;
import pt.uminho.anote2.aibench.curator.operation.help.CallLookupTablesOp;
import pt.uminho.anote2.aibench.curator.view.CuratorView;
import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.annotation.log.AnnotationLog;
import pt.uminho.anote2.datastructures.annotation.log.AnnotationLogTypeEnum;
import pt.uminho.anote2.datastructures.annotation.ner.EntityAnnotation;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.schema.DatabaseTablesName;
import pt.uminho.anote2.datastructures.exceptions.process.manualcuration.ManualCurationException;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.utils.HTMLCodes;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public class CuratorDocument{
	
	public static final String SEPARATOR = "[ '<>/(),\n\\.]+";

	
	/**
	 * Save clean text and html text of annotated text
	 */
	private CuratorDocumentHTMLAndText documentText;
	
	private CuratorDocumentAnnotations annotations;
	
	/**
	 * Save last structure
	 */
	private CuratorDocumentPreviousState previousData;
	
	/**
	 * Database Prepare Statment
	 * 
	 * @param annotatedDocument
	 */
	private CuratorDataBaseAnnotationsManagement annotManagenment;
	
	/**
	 * 
	 * @param annotatedDocument
	 * @throws SQLException
	 */
	private NERDocumentAnnotation annotatedDocument;
	

	private CuratorView curator;
	
	
	public CuratorDocument(CuratorView curator,NERDocumentAnnotation ner) throws SQLException, DatabaseLoadDriverException{
		this.annotatedDocument=ner;
		this.curator = curator;
		annotations = new CuratorDocumentAnnotations(ner);
		documentText = new CuratorDocumentHTMLAndText(ner.getDocumentAnnotationTextHTML(),gerateHtmlForEntitiess(ner));
		annotManagenment = new CuratorDataBaseAnnotationsManagement(ner);
	}

	public CuratorDocument(CuratorView curator,REDocumentAnnotation re) throws SQLException, DatabaseLoadDriverException{
		this.annotatedDocument=re;
		this.curator = curator;
		annotations = new CuratorDocumentAnnotations(annotatedDocument);
		documentText = new CuratorDocumentHTMLAndText(re.getDocumetAnnotationText(),gerateHtmlForEntitiess(re),gerateHtmlForEntitiessAndClues(re));
		annotManagenment = new CuratorDataBaseAnnotationsManagement(re);
	}
	
	/**
	 * Add An Entity Annotation 
	 * 
	 * @param posC
	 * @param annotation
	 * @param lookuptable
	 * @throws DatabaseLoadDriverException
	 * @throws SQLException
	 * @throws ManualCurationException
	 */
	public void addOneEntityAnnotation(AnnotationPosition posC,IEntityAnnotation annotation,IResource<IResourceElement> lookuptable) throws DatabaseLoadDriverException, SQLException, ManualCurationException
	{
		// If Document have relations verify if exist an clue position on this offset range
		if(annotatedDocument instanceof REDocumentAnnotation && ((REDocumentAnnotation) annotatedDocument).getEventAnnotationPositions().containsConflits(posC))
		{
			throw new ManualCurationException("Note: Already have an Relation annotation on selected position");
		}
		// Verify if Position already have any entity annotation
		else if(!annotatedDocument.getEntityAnnotationPositions().containsKey(posC))
		{	
			IResourceElement elem;
			// Add Annotation if don´t have any conflit
			if(annotatedDocument.getEntityAnnotationPositions().addAnnotationWhitConflicts(posC, annotation))
			{
				// Add On DAtabase
				annotManagenment.addEntityAnnotation(posC, annotation);
				// Add to Entity List
				annotatedDocument.getEntitiesAnnotations().add(annotation);
				// Add Entity to Relation Curator Panel
				if(annotatedDocument instanceof REDocumentAnnotation)
				{
//					((REDocumentAnnotation)annotatedDocument).getEntityRelationSet().put(posC, new HashSet<IEventAnnotation>());
				}
				String notes = new String();
				// Test IF exist a lookup associated
				if(lookuptable==null){}
				else
				{
					// Add Element To Lookup Table associated
					elem = new ResourceElement(-1,annotation.getAnnotationValue(),annotation.getClassAnnotationID(),"");
					int id = getResourceElementWhitNaneANdCLass(elem,lookuptable);
					annotation.setResourceElementID(id);
					// Add Notes to Log message 
					notes = "Annotation added to Lookup Table "+ lookuptable.getName() + "( " + lookuptable.getID() + " )";
				}
				// Get Class Name
				String termClass = ClassProperties.getClassIDClass().get(annotation.getClassAnnotationID());
				// Add Entity to statistics
				getAnnotations().addAnnotation(annotation,termClass);
				// Add A Log Add Entity
				String newStr = annotation.getAnnotationValue() + " ("+ termClass + ") Text Offset: "+posC.getStart()+ "-"+posC.getEnd();
				AnnotationLog log = new AnnotationLog(-1, annotation.getID(), annotatedDocument.getCorpus().getID(), annotatedDocument.getProcess().getID(), annotatedDocument.getID(),
						AnnotationLogTypeEnum.ENTITYADD, "", newStr, notes);
				curator.addAnnotationLog(log);
				// Notify Observers
				annotatedDocument.notifyViewObservers();
			}
			else
			{
				throw new ManualCurationException("Note: Already have an annotation on selected position");
			}
		}
		else
		{
			throw new ManualCurationException("Note: Another annotation already annotated in selected position range");
		}
	}
	
	
	/**
	 * Add all Text matching annotations
	 * 
	 * @param annotation
	 * @param lookuptable
	 * @param caseSensitive
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 * @throws ManualCurationException
	 */
	public void addAllEntityAnnotations(IEntityAnnotation annotation,IResource<IResourceElement> lookuptable, boolean caseSensitive) throws SQLException, DatabaseLoadDriverException, ManualCurationException
	{
		// Get Selected Clean Text
		String cleanTExt = annotatedDocument.getDocumetAnnotationText();
		Map<String,Integer> termResourceID = new HashMap<String, Integer>();
		// Get All postion with Clean Text
		List<AnnotationPosition> annot = searchTermInText(annotation.getAnnotationValue(),cleanTExt,caseSensitive);
		// Get Term Class
		String termClass = ClassProperties.getClassIDClass().get(annotation.getClassAnnotationID());
		IEntityAnnotation entAnnotation;
		String notesBase = "Multiple annotation with ";
		int addedEntities = 0;
		// Add Notes to Log Message
		if(caseSensitive)
		{
			notesBase = notesBase + " case sensitive as true. ";
		}
		else
		{
			notesBase = notesBase + " case sensitive as false. ";
		}
		int nextAnnotationID = HelpDatabase.getNextInsertTableID(DatabaseTablesName.annotation);
		//Test if each search annotation could be inserted
		for(AnnotationPosition pos:annot)
		{
			String notes = notesBase;
			ResourceElement elem = new ResourceElement(-1,pos.getOriginalText(),annotation.getClassAnnotationID(),"");
			// Test if lookup is not null and if term already has on lookup
			if(lookuptable !=null && !termResourceID.containsKey(pos.getOriginalText()))
			{
				int id = getResourceElementWhitNaneANdCLass(elem,lookuptable);
				annotation.setResourceElementID(id);
				termResourceID.put(pos.getOriginalText(), id);
			}
			// Add Term to lookup
			else if(lookuptable !=null )
			{
				int id = termResourceID.get(pos.getOriginalText());
				annotation.setResourceElementID(id);
				notes = notes + "Annotation added to Lookup Table "+lookuptable.getName() + "( "+ lookuptable.getID() + ")";
			}
			
			entAnnotation = new EntityAnnotation(nextAnnotationID, pos.getStart(), pos.getEnd(), annotation.getClassAnnotationID(), annotation.getResourceElementID(), pos.getOriginalText(), pos.getOriginalText());
			// If Document have relations verify if exist an clue position on this offset range
			if(annotatedDocument instanceof REDocumentAnnotation && ((REDocumentAnnotation) annotatedDocument).getEventAnnotationPositions().containsConflits(pos))
			{
				//throw new ManualCurationException("Note: Already have an Relation annotation on selected position");
			}
			// Test if position already has annotation
			else if(!annotatedDocument.getEntityAnnotationPositions().getAnnotations().containsKey(pos))
			{
				// Test if position contains conflits
				if(annotatedDocument.getEntityAnnotationPositions().addAnnotationWhitConflicts(pos, entAnnotation))
				{
					// And to Database
					annotManagenment.addEntityAnnotation(pos, entAnnotation);
					// Add To Statitics
					getAnnotations().addMoreAnnotion(entAnnotation,termClass,1);
					// Add Entity to Relation Curator Panel
					if(annotatedDocument instanceof REDocumentAnnotation)
					{
//						((REDocumentAnnotation)annotatedDocument).getEntityRelationSet().put(pos, new HashSet<IEventAnnotation>());
					}
					// Add Log message
					String newStr = annotation.getAnnotationValue() + " ("+ termClass + ") Text Offset: "+pos.getStart()+ "-"+pos.getEnd();
					AnnotationLog log = new AnnotationLog(-1, annotation.getID(), annotatedDocument.getCorpus().getID(), annotatedDocument.getProcess().getID(), annotatedDocument.getID(),
							AnnotationLogTypeEnum.ENTITYADD, "", newStr, notes);
					curator.addAnnotationLog(log);
					addedEntities ++;
					nextAnnotationID ++;
				}
			}
		}
		// IF any annotation was added notify views
		if(addedEntities!=0)
		{
			annotatedDocument.notifyViewObservers();
		}
		else
		{
			throw new ManualCurationException("Note: Already have all annotation for this term");
		}
	}
	
	private List<AnnotationPosition> searchTermInText(String term, String text, boolean caseSensitive){
		List<AnnotationPosition> positions = new ArrayList<AnnotationPosition>();
		String termExp = ParsingUtils.textToRegExp(term);
		
		Pattern p = caseSensitive ? Pattern.compile(SEPARATOR + "("+termExp+")" + SEPARATOR) : Pattern.compile(SEPARATOR + "("+termExp+")" + SEPARATOR, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1),text.substring(m.start(1),  m.end(1)));
			positions.add(pos);
		}
		
		p = caseSensitive ? Pattern.compile("^" + "("+termExp+")" + SEPARATOR) : Pattern.compile("^" + "("+termExp+")" + SEPARATOR, Pattern.CASE_INSENSITIVE);
		m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1),text.substring(m.start(1),  m.end(1)));
			positions.add(pos);
		}
		
		p = caseSensitive ? Pattern.compile(SEPARATOR + "("+termExp+")" + "$") : Pattern.compile(SEPARATOR + "("+termExp+")" + "$", Pattern.CASE_INSENSITIVE);
		m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1),text.substring(m.start(1),  m.end(1)));
			positions.add(pos);
		}
		return positions;
	}
	
	/**
	 * Remove an entity ANnotation
	 * 
	 * @param posC
	 * @throws SQLException
	 * @throws ManualCurationException
	 * @throws DatabaseLoadDriverException
	 */
	public void removeEntityAnnotation(AnnotationPosition posC) throws SQLException, ManualCurationException, DatabaseLoadDriverException
	{
		// Test if position contains an annotation 
		if(annotatedDocument.getEntityAnnotationPositions().getAnnotations().keySet().contains(posC))
		{
			// Get Entity Annotation
			IAnnotation annoatation = annotatedDocument.getEntityAnnotationPositions().getAnnotations().get(posC);
			IEntityAnnotation entityAnnotation = (IEntityAnnotation) annoatation;
			String notes = new String();
			// Remove Entity Annotation
			annotatedDocument.getEntityAnnotationPositions().getAnnotations().remove(posC);
			// Remove annotation from database
			annotManagenment.removeAnnotation(annoatation.getID());
			// Remove Entity Form Document List
			annotatedDocument.getEntitiesAnnotations().remove(entityAnnotation);
			// Get Class Name
			String termClass = ClassProperties.getClassIDClass().get(entityAnnotation.getClassAnnotationID());
			String oldStr = entityAnnotation.getAnnotationValue() + " ("+ termClass + ") Text Offset: "+posC.getStart()+ "-"+posC.getEnd();
			// Add Log Message
			AnnotationLog log = new AnnotationLog(-1, entityAnnotation.getID(), annotatedDocument.getCorpus().getID(), annotatedDocument.getProcess().getID(), annotatedDocument.getID(),
					AnnotationLogTypeEnum.ENTITYREMOVE, oldStr,"", notes);
			curator.addAnnotationLog(log);
			// Remove all event associated to Annotation Entity removed
			removeEventAssociatedWithEntity(posC, entityAnnotation);
			annotatedDocument.notifyViewObservers();
		}
		else
		{
			throw new ManualCurationException("Note: The select text not contains any annotation");
		}
	}

	/**
	 * Remove Event/Relation associated to removed Entity
	 * 
	 * @param posC
	 * @param entityAnnotation
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	private void removeEventAssociatedWithEntity(AnnotationPosition posC,IEntityAnnotation entityAnnotation) throws SQLException, DatabaseLoadDriverException {
		
		//If REProcess the relations associated to entity will be inactivated
		if(annotatedDocument instanceof REDocumentAnnotation)
		{
			// Get Class Name
			String termClass = ClassProperties.getClassIDClass().get(entityAnnotation.getClassAnnotationID());
			// For Log Message
			String entityRemoved = entityAnnotation.getAnnotationValue() + " ("+ termClass + ") Text Offset: "+posC.getStart()+ "-"+posC.getEnd();
			REDocumentAnnotation reDoc = (REDocumentAnnotation) annotatedDocument;
			// Test if Entity has relations associated
			if(reDoc.getEntityRelationSet().containsKey(posC))
			{
				// Get Relation associated to removed Entity
				Set<IEventAnnotation> relationToInactivate = reDoc.getEntityRelationSet().get(posC);
				for(IEventAnnotation event : relationToInactivate )
				{
					String noteEvent = "Relation/Event removed because Entity removed "+entityRemoved;
					String oldRelation = event.toString();
					// Remove Event from database
					annotManagenment.removeAnnotation(event.getID());
					// Remove reference to Entity associated to Event
					reDoc.removeAllEventReferences(event);
					// Remove Relation/Event From Document
					reDoc.getEventAnnotations().remove(event);
					// Add Log
					AnnotationLog logEvent = new AnnotationLog(-1, event.getID(), annotatedDocument.getCorpus().getID(), annotatedDocument.getProcess().getID(), annotatedDocument.getID(),
							AnnotationLogTypeEnum.RELATIONREMOVE, oldRelation,"", noteEvent);
					curator.addAnnotationLog(logEvent);
				}
				if(relationToInactivate.size()>0)
				{
					// Remove All Relation associated to Entity
					reDoc.getEntityRelationSet().remove(posC);
					// Clean all Clue postions without relations
					reDoc.cleanEmptyClues();
				}
			}
		}
	}

	/**
	 * Remove All Annotation 
	 * 
	 * @param term
	 * @param lookuptable
	 * @param casesensitive
	 * @return
	 * @throws DatabaseLoadDriverException
	 * @throws SQLException
	 * @throws ManualCurationException
	 */
	public List<AnnotationPosition> removeAllEntityAnnotations(String term, IResource<IResourceElement> lookuptable, boolean casesensitive) throws DatabaseLoadDriverException, SQLException, ManualCurationException
	{
		List<AnnotationPosition> listToRemove = new ArrayList<AnnotationPosition>();
		String notesBase = "Multiple annotation with ";
		if(casesensitive)
		{
			notesBase = notesBase + " case sensitive as true. ";
		}
		else
		{
			notesBase = notesBase + " case sensitive as false. ";
		}
		// Find in all annotations tthe annotation to remove
		for(AnnotationPosition pos:annotatedDocument.getEntityAnnotationPositions().getAnnotations().keySet())
		{	
			String notes = notesBase;
			IEntityAnnotation elem = (IEntityAnnotation) annotatedDocument.getEntityAnnotationPositions().getAnnotations().get(pos);
			// If annotation term as same as term ( case sensitive or not) remove entity
			if(!casesensitive && elem.getAnnotationValue().equalsIgnoreCase(term) || casesensitive && elem.getAnnotationValue().equals(term))
			{
				// Get Entity Annotation
				IAnnotation annotation = annotatedDocument.getEntityAnnotationPositions().getAnnotations().get(pos);
				IEntityAnnotation entityAnnotation = (IEntityAnnotation) annotation;
				String termClass = ClassProperties.getClassIDClass().get(entityAnnotation.getClassAnnotationID());
				String oldStr = entityAnnotation.getAnnotationValue() + " ("+ termClass + ") Text Offset: "+pos.getStart()+ "-"+pos.getEnd();
				// Reomve Annotation From Database
				annotManagenment.removeAnnotation(annotation.getID());
				// Remove Annotation From Document
				annotatedDocument.getEntitiesAnnotations().remove(entityAnnotation);
				pos.setOriginalText(elem.getAnnotationValue());
				// Add To list to remove in Statistics
				listToRemove.add(pos);
				// Remove all Event associated to Entity Annotation
				removeEventAssociatedWithEntity(pos, entityAnnotation);
				// If lookup is available remove term
				if(lookuptable!=null)
				{
					IResourceElement elemToInactive = lookuptable.getFirstTermByName(elem.getAnnotationValue());
					if(elemToInactive!=null)
					{
						lookuptable.inactiveElement(elemToInactive);
						notes = notes + "Annotation removed from Lookup Table "+lookuptable.getName() + "( "+ lookuptable.getID() + ")";
					}
				}
				// Add Log Message
				AnnotationLog log = new AnnotationLog(-1, entityAnnotation.getID(), annotatedDocument.getCorpus().getID(), annotatedDocument.getProcess().getID(), annotatedDocument.getID(),
						AnnotationLogTypeEnum.ENTITYREMOVE, oldStr,"", notes);
				curator.addAnnotationLog(log);
			}

		}
		if(lookuptable instanceof LookupTable)
			CallLookupTablesOp.updateLookupTableElements(lookuptable);
		if(listToRemove.size()==0)
		{
			throw new ManualCurationException("Note: Term not found in annotations set! Ignoring");
		}
		else
		{
			// Remove all Annotation Annotation Position
			for(AnnotationPosition pos:listToRemove)
			{
				annotatedDocument.getEntityAnnotationPositions().getAnnotations().remove(pos);
			}
			annotatedDocument.notifyViewObservers();
			return listToRemove;
		}
	}
	
	public void updateOneEntityAnnotation(AnnotationPosition pos, int classID) throws ManualCurationException, SQLException, DatabaseLoadDriverException {
		// Test inf Postion has Annotation
		if(annotatedDocument.getEntityAnnotationPositions().containsKey(pos))
		{
			List<AnnotationPosition> list = new ArrayList<AnnotationPosition>();
			list.add(pos);
			String notes = new String();
			// Update Entity Annotation in Database
			annotManagenment.updateEntityAnnotations(list,classID);
			IEntityAnnotation elem = (IEntityAnnotation) annotatedDocument.getEntityAnnotationPositions().get(pos);
			IEntityAnnotation old = elem.clone();
			// Update Entity Class in memory
			elem.setClass(classID);
			// Add Log Message
			String termClassOld = ClassProperties.getClassIDClass().get(old.getClassAnnotationID());
			String termClassNew = ClassProperties.getClassIDClass().get(classID);
			String oldStr = old.getAnnotationValue() + " ("+ termClassOld + ") Text Offset: "+pos.getStart()+ "-"+pos.getEnd();
			String newStr = elem.getAnnotationValue() + " ("+ termClassNew + ") Text Offset: "+pos.getStart()+ "-"+pos.getEnd();
			AnnotationLog log = new AnnotationLog(-1, old.getID(), annotatedDocument.getCorpus().getID(), annotatedDocument.getProcess().getID(), annotatedDocument.getID(),
					AnnotationLogTypeEnum.ENTITYUPDATE, oldStr,newStr, notes);
			curator.addAnnotationLog(log);
			annotatedDocument.notifyViewObservers();
		}
		else
		{
			throw new ManualCurationException("Note: The select text not contains any annotation");
		}
	}
	
	public List<AnnotationPosition> updateAllEntiyAnnotations(String term, int classID,boolean casesensitive, IResource<IResourceElement> lookuptable) throws DatabaseLoadDriverException, SQLException, ManualCurationException {
		
		IEntityAnnotation elem=null;
		List<AnnotationPosition> list = new ArrayList<AnnotationPosition>();
		String notesBase = "Multiple annotation with ";
		if(casesensitive)
		{
			notesBase = notesBase + " case sensitive as true. ";
		}
		else
		{
			notesBase = notesBase + " case sensitive as false. ";
		}
		// Find updated Term in Annotation
		for(AnnotationPosition pos:annotatedDocument.getEntityAnnotationPositions().getAnnotations().keySet())
		{	
			String notes = notesBase;
			elem = (IEntityAnnotation) annotatedDocument.getEntityAnnotationPositions().get(pos);
			pos.setOriginalText(elem.getAnnotationValue());
			// If annotation term as same as term ( case sensitive or not) remove entity
			if(!casesensitive && elem.getAnnotationValue().equalsIgnoreCase(term) || casesensitive && elem.getAnnotationValue().equals(term))
			{
				IEntityAnnotation lem = (IEntityAnnotation) annotatedDocument.getEntityAnnotationPositions().get(pos);
				IEntityAnnotation oldElemCopy = lem.clone();
				lem.setClass(classID);
				// Update Term
//				annotatedDocument.getEntityAnnotationPositions().getAnnotations().put(pos, newElem);
//				// Update Entity List
//				annotatedDocument.getEntitiesAnnotations().remove(oldElem);
//				annotatedDocument.getEntitiesAnnotations().add(newElem);
				list.add(pos);
				// Test if lookup table is active
				if(lookuptable!=null)
				{
					IResourceElement elemToInactive = lookuptable.getFirstTermByName(elem.getAnnotationValue());
					if(elemToInactive!=null)
					{
						notes = notes + "Annotation edit from Lookup Table "+lookuptable.getName() + "( "+ lookuptable.getID() + ")";
						elemToInactive.changeTermClassID(classID);
						lookuptable.updateElement(elemToInactive);
					}
					else
					{
						notes = notes + "Annotation Added to Lookup Table "+lookuptable.getName() + "( "+ lookuptable.getID() + ")";
						lookuptable.addElement(new ResourceElement(elem.getAnnotationValue(),classID));
					}
				}
				// Add Log
				String termClassOld = ClassProperties.getClassIDClass().get(elem.getClassAnnotationID());
				String termClassNew = ClassProperties.getClassIDClass().get(classID);
				String oldStr = elem.getAnnotationValue() + " ("+ termClassOld + ") Text Offset: "+pos.getStart()+ "-"+pos.getEnd();
				String newStr = oldElemCopy.getAnnotationValue() + " ("+ termClassNew + ") Text Offset: "+pos.getStart()+ "-"+pos.getEnd();
				AnnotationLog log = new AnnotationLog(-1, elem.getID(), annotatedDocument.getCorpus().getID(), annotatedDocument.getProcess().getID(), annotatedDocument.getID(),
						AnnotationLogTypeEnum.ENTITYUPDATE, oldStr,newStr, notes);
				curator.addAnnotationLog(log);
			}
		}
		if(list.size()>0)
		{
			annotManagenment.updateEntityAnnotations(list,classID);
			annotatedDocument.notifyViewObservers();
		}
		else
		{
			throw new ManualCurationException("Note: Term not found in annotations set! Ignoring");
		}
		if(lookuptable instanceof LookupTable)
			CallLookupTablesOp.updateLookupTableElements(lookuptable);
		return list;
	}
	

	public void addEventAnnotation(IEventAnnotation event) throws SQLException, DatabaseLoadDriverException {
		REDocumentAnnotation reDocument = (REDocumentAnnotation) annotatedDocument;
		// Add relation In database
		this.annotManagenment.addRelationsDB(event);	
		// Add Event List in memory
		reDocument.getEventAnnotations().add(event);
		// Add all references to Event
		reDocument.addEventReferences(event);
		// Add To Log
		String newRelation = event.toString();
		AnnotationLog logEvent = new AnnotationLog(-1, event.getID(), annotatedDocument.getCorpus().getID(), annotatedDocument.getProcess().getID(), annotatedDocument.getID(),
				AnnotationLogTypeEnum.RELATIONADD, "",newRelation, "");
		curator.addAnnotationLog(logEvent);
		// Notify View
		annotatedDocument.notifyViewObservers();

	}
	
	public void editEventAnnotation(IEventAnnotation eventToEdit,IEventAnnotation eventupdated) throws SQLException, DatabaseLoadDriverException {
		String oldrelation = eventToEdit.toString();
		REDocumentAnnotation reDocument = (REDocumentAnnotation) annotatedDocument;
		// Edit relation in Database
		this.annotManagenment.editRelationsDB(eventToEdit);	
		// remove all references to Event
		reDocument.removeAllEventReferences(eventToEdit);
		// Edit Relation in Memory
		eventToEdit.setEntityAtLeft(eventupdated.getEntitiesAtLeft());
		eventToEdit.setEntityAtRight(eventupdated.getEntitiesAtRight());
		eventToEdit.setEventProperties(eventupdated.getEventProperties());
		// Add all references to Event
		reDocument.addEventReferences(eventToEdit);
		// Clean Structures
		reDocument.cleanEmptyClues();
		// Add To Log
		String newRelation = eventupdated.toString();
		AnnotationLog logEvent = new AnnotationLog(-1, eventToEdit.getID(), annotatedDocument.getCorpus().getID(), annotatedDocument.getProcess().getID(), annotatedDocument.getID(),
				AnnotationLogTypeEnum.RELATIONUPDATE, oldrelation,newRelation, "");
		curator.addAnnotationLog(logEvent);
		// Notify View
		annotatedDocument.notifyViewObservers();
	}
	
	public void removeEventAnnotation(IEventAnnotation event) throws SQLException, DatabaseLoadDriverException, ManualCurationException
	{
		removeEventFromStructures(event);	
		annotatedDocument.notifyViewObservers();
	}
	
	private void removeEventFromStructures(IEventAnnotation event) throws SQLException, DatabaseLoadDriverException, ManualCurationException {
		AnnotationPosition eventAnnotationPosition = new AnnotationPosition((int)event.getStartOffset(), (int)event.getEndOffset());
		REDocumentAnnotation reDocument = (REDocumentAnnotation) annotatedDocument;
		if(reDocument.getEventAnnotationPositions().containsKey(eventAnnotationPosition) || reDocument.getEventAnnotations().contains(event))
		{
			// Remove From Database
			 this.annotManagenment.removeAnnotation(event.getID());
			// Remove form Event List in memory
			reDocument.getEventAnnotations().remove(event);
			// remove all references to Event
			reDocument.removeAllEventReferences(event);
			// Clean Structures
			reDocument.cleanEmptyClues();
			// Add To Log
			String oldRelation = event.toString();
			AnnotationLog logEvent = new AnnotationLog(-1, event.getID(), annotatedDocument.getCorpus().getID(), annotatedDocument.getProcess().getID(), annotatedDocument.getID(),
					AnnotationLogTypeEnum.RELATIONREMOVE, oldRelation,"", "");
			curator.addAnnotationLog(logEvent);
		}
		else
			throw new ManualCurationException("Note: no Relation/Event Found on position");
		
	}
	
	
	public void removeAllEventFromClue(AnnotationPosition position) throws ManualCurationException, SQLException, DatabaseLoadDriverException {
		REDocumentAnnotation reDocument = (REDocumentAnnotation) annotatedDocument;
		if(reDocument.getVerbRelations().containsKey(position))
		{
			Set<IEventAnnotation> setRelations = reDocument.getVerbRelations().get(position);
			List<IEventAnnotation> list = new ArrayList<IEventAnnotation>(setRelations);
			for(IEventAnnotation relation:list)
			{
				 removeEventFromStructures(relation);
			}
			reDocument.getVerbRelations().remove(position);
			annotatedDocument.notifyViewObservers();
		}
		else
			throw new ManualCurationException("Note: No Clue Found");
		
	}
	
	public void removeAllEntityEventFromClue(AnnotationPosition position) throws SQLException, DatabaseLoadDriverException, ManualCurationException {
		REDocumentAnnotation reDocument = (REDocumentAnnotation) annotatedDocument;
		if(reDocument.getEntityRelationSet().containsKey(position))
		{
			Set<IEventAnnotation> setRelations = reDocument.getEntityRelationSet().get(position);
			List<IEventAnnotation> list = new ArrayList<IEventAnnotation>(setRelations);
			for(IEventAnnotation relation:list)
			{
				 removeEventFromStructures(relation);
			}
			reDocument.getVerbRelations().remove(position);
			annotatedDocument.notifyViewObservers();
		}
		else
			throw new ManualCurationException("Note: No Entity Found");
		
	}
	
	public void removeAllEventFromRange(AnnotationPosition position)  throws SQLException, DatabaseLoadDriverException, ManualCurationException {
		Set<IEventAnnotation> relationsToRemove = getEventFromSubSet(position);
		if(relationsToRemove.size()>0)
		{
			List<IEventAnnotation> list = new ArrayList<IEventAnnotation>(relationsToRemove);
			for(IEventAnnotation relation:list)
			{
				removeEventFromStructures(relation);
			}
			annotatedDocument.notifyViewObservers();
		}
		else
			throw new ManualCurationException("Note: No Relations Found on range");
	}

	public Set<IEventAnnotation> getEventFromSubSet(AnnotationPosition position) throws SQLException,
			DatabaseLoadDriverException {
		REDocumentAnnotation reDocument = (REDocumentAnnotation) annotatedDocument;
		Set<IEventAnnotation> setRelations = new HashSet<IEventAnnotation>();
		AnnotationPositions entityPositions = reDocument.getEntityAnnotationPositions().getAnnotationPositionsSubSet(position.getStart(), position.getEnd());
		for(AnnotationPosition pos:entityPositions.getAnnotations().keySet())
		{
			if(reDocument.getEntityRelationSet().containsKey(pos))
			{
				setRelations.addAll(reDocument.getEntityRelationSet().get(pos));
			}
		}
		AnnotationPositions eventPositions = reDocument.getEventAnnotationPositions().getAnnotationPositionsSubSet(position.getStart(), position.getEnd());
		for(AnnotationPosition pos:eventPositions.getAnnotations().keySet())
		{
			if(reDocument.getVerbRelations().containsKey(pos))
			{
				setRelations.addAll(reDocument.getVerbRelations().get(pos));
			}
		}
		return setRelations;
	}

	
	public void rebuildHtml(int zoom) throws SQLException, DatabaseLoadDriverException
	{
		String newHtml = textAsHtmlForEntities(annotatedDocument.getDocumetAnnotationText(), annotatedDocument.getEntityAnnotationPositions());
		newHtml = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:"+zoom+"pt\" align=\"justify\">" + newHtml + "</DIV>";
		documentText.setHtmlTextForEntities(newHtml);
		if(this.annotatedDocument instanceof REDocumentAnnotation)
		{
			String newHtmlClues = textAsHtmlForEntitiesANdClues(annotatedDocument.getDocumetAnnotationText(),annotatedDocument.getEntityAnnotationPositions(), ((REDocumentAnnotation)annotatedDocument).getEventAnnotationPositions());
			newHtmlClues = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:"+zoom+"pt\" align=\"justify\">" + newHtmlClues + "</DIV>";
			documentText.setHtmlTextForEntitiesAndClues(newHtmlClues);
		}
	}

	public String textAsHtmlForEntities(String text, AnnotationPositions annotationsPositions) throws SQLException, DatabaseLoadDriverException{
		StringBuffer htmlText = new StringBuffer();
		text = HTMLCodes.textToHTML(text);
		int pointer=0;
		for(AnnotationPosition pos : annotationsPositions.getAnnotations().keySet())
		{
			IAnnotation annotation = annotationsPositions.getAnnotations().get(pos);
			if(annotation instanceof IEntityAnnotation)
			{
				IEntityAnnotation annot = (IEntityAnnotation) annotation;
				if(pointer<pos.getStart())
					htmlText.append(text.substring(pointer, pos.getStart()));			
				String entity = "<FONT COLOR=" + CorporaProperties.getCorporaClassColor(annot.getClassAnnotationID()).getColor() + "><b>";
				htmlText.append(entity);
				htmlText.append(text.substring(pos.getStart(), pos.getEnd()));
				htmlText.append("</b></FONT>");
				pointer = pos.getEnd();
			}
		}
		if(pointer<text.length())
			htmlText.append(text.substring(pointer, text.length()));
		return htmlText.toString();
	}
	
	public String textAsHtmlForEntitiesANdClues(String text, AnnotationPositions annotationsEntities, AnnotationPositions events) throws SQLException, DatabaseLoadDriverException{
		StringBuffer htmlText = new StringBuffer();
		int pointer=0;
		AnnotationPositions annotationsPositions = new AnnotationPositions();
		for(AnnotationPosition pos:annotationsEntities.getAnnotations().keySet())
		{
			annotationsPositions.addAnnotationWhitConflicts(pos, annotationsEntities.getAnnotations().get(pos));
		}
		for(AnnotationPosition pos:events.getAnnotations().keySet())
		{
			annotationsPositions.addAnnotationWhitConflicts(pos, events.getAnnotations().get(pos));
		}
		for(AnnotationPosition pos : annotationsPositions.getAnnotations().keySet())
		{
			IAnnotation annotation = annotationsPositions.getAnnotations().get(pos);
			String annotKey = new String();
			if(pointer<pos.getStart())
				htmlText.append(text.substring(pointer, pos.getStart()));		
			if(annotation instanceof IEventAnnotation)
			{
				annotKey = "<FONT COLOR=" + OtherConfigurations.getVerbColor() + "><b  bgcolor=\""+OtherConfigurations.getVerbColorBackGround()+"\">";
			}
			else
			{
				IEntityAnnotation annot = (IEntityAnnotation) annotationsPositions.getAnnotations().get(pos);
				annotKey = "<FONT COLOR=" + CorporaProperties.getCorporaClassColor(annot.getClassAnnotationID()).getColor() + "><b>";
			}
			htmlText.append(annotKey);
			htmlText.append(text.substring(pos.getStart(), pos.getEnd()));
			htmlText.append("</b></FONT>");
			pointer = pos.getEnd();
		}
		if(pointer<text.length())
			htmlText.append(text.substring(pointer, text.length()));
		return htmlText.toString();
	}
	
	public String gerateHtmlForEntitiess(NERDocumentAnnotation nerDoc) throws SQLException, DatabaseLoadDriverException {		
		String htmlText = textAsHtmlForEntities(nerDoc.getDocumetAnnotationText(),nerDoc.getEntityAnnotationPositions());
		htmlText = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:12pt\" align=\"justify\">" + htmlText + "</DIV>";
		return htmlText;
	}
	
	public String gerateHtmlForEntitiessAndClues(REDocumentAnnotation nerDoc) throws SQLException, DatabaseLoadDriverException {		
		String htmlText = textAsHtmlForEntitiesANdClues(nerDoc.getDocumetAnnotationText(),nerDoc.getEntityAnnotationPositions(),nerDoc.getEventAnnotationPositions());
		htmlText = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:12pt\" align=\"justify\">" + htmlText + "</DIV>";
		return htmlText;
	}

	public AnnotationPositions getNERAnnotations() throws SQLException, DatabaseLoadDriverException {
		return annotatedDocument.getEntityAnnotationPositions();
	}
	
	public int getResourceElementWhitNaneANdCLass(IResourceElement elem,IResource<IResourceElement> look) throws DatabaseLoadDriverException, SQLException
	{	
		int nextResourceID = HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements);
		if(((LookupTable) look).addSimpleElement(elem))
		{
			if(look instanceof LookupTable)
			{
				CallLookupTablesOp.updateLookupTableElements(look);
			}
			if(!look.getClassContent().contains(elem.getTermClassID()))
			{
				look.addResourceContent(elem.getTermClassID());
			}
			return nextResourceID;
		}
		else{
			return look.getFirstTermByName(elem.getTerm()).getID();
		}
	}
	
	public NERDocumentAnnotation getAnnotatedDocument() {
		return annotatedDocument;
	}
	
	public CuratorView getCuratorView() {
		return curator;
	}
	
	public CuratorDocumentHTMLAndText getDocumentText() {return documentText;}
	public void setDocumentText(CuratorDocumentHTMLAndText documentText) {this.documentText = documentText;}
	public CuratorDocumentAnnotations getAnnotations() {return annotations;}
	public void setAnnotations(CuratorDocumentAnnotations annotations) {this.annotations = annotations;}
	public CuratorDocumentPreviousState getPreviousData() {return previousData;}
	public void setPreviousData(CuratorDocumentPreviousState previousData) {this.previousData = previousData;}



}
