package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceReport;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.schema.TableResourcesElements;
import pt.uminho.anote2.datastructures.report.resources.PortResourceUpdateReport;
import pt.uminho.anote2.datastructures.report.resources.ResourceMergeConflits;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class DictionaryLoaderHelp extends PortResourceUpdateReport{
	
	
	/**
	 * Logger
	 */
	static Logger logger = Logger.getLogger(DictionaryLoaderHelp.class.getName());
	
	private int nextResourceElementID;
	private IDictionary resource;
	private Map<String,Integer> sourceNameSourceID;
	private Map<String,Integer> entityClassEntityID;
	
	public DictionaryLoaderHelp(IDictionary resource,String font)
	{
		super(resource,font);
		this.setResource(resource);
		this.sourceNameSourceID = new HashMap<String, Integer>();
		this.entityClassEntityID = new HashMap<String, Integer>();
	}

	/**
	 * 
	 * @param term
	 * @param termSynomns
	 * @param externalID
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException 
	 */
	public void addTermsAndSyn(String term,String classe, Set<String> termSynomns,List<IExternalID> externalIDs) throws DatabaseLoadDriverException, SQLException {
		if(!entityClassEntityID.containsKey(classe))
		{
			int newClassID = resource.addElementClass(classe);
			entityClassEntityID.put(classe, newClassID);
		}
		int classDatabaseID = entityClassEntityID.get(classe);
		if(term.isEmpty()|| term.length()<TableResourcesElements.mimimumElementSize)
		{
			term = getSynonym(termSynomns);
		}		
		if(!term.isEmpty() && term.length() >= TableResourcesElements.mimimumElementSize && term.length()< TableResourcesElements.elementSize)
		{
			IResourceElement elem = new ResourceElement(nextResourceElementID,term,classDatabaseID,classe);
			if(((Dictionary) getResource()).getShortElems().containsKey(term))
			{
				elem = new ResourceElement(((Dictionary) getResource()).getShortElems().get(term).getY(),term,classDatabaseID,classe);
				updateWhenTermAlreadyExist(elem,term,classe,classDatabaseID, termSynomns, externalIDs);
			}
			else if(getResource().addElement(elem))
			{
				updateNewTerm(elem,classe,classDatabaseID,termSynomns, externalIDs);
				nextResourceElementID++;
			}
			else
			{
				reportupdateTermConflit(getReport(),elem);
			}
		}
	}

	private void updateNewTerm(IResourceElement elem, String classe, int classDatabaseID, Set<String> termSynomns, List<IExternalID> externalIDs) throws DatabaseLoadDriverException, SQLException {
		getReport().addTermAdding(1);
		updateSynonyms(elem,classe,classDatabaseID,termSynomns);
		updateExternalIds(elem, externalIDs);
	}

	private void updateWhenTermAlreadyExist(IResourceElement elem,String term,String classe,int classDatabaseID,Set<String> termSynomns, List<IExternalID> externalIDs ) throws DatabaseLoadDriverException, SQLException {
		IInsertConflits conflit;
		if(((Dictionary) getResource()).getShortElems().get(term).getX()==classDatabaseID)
		{
			updateSynonyms(elem,classe,classDatabaseID,termSynomns);
			updateExternalIds(elem, externalIDs);
			conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elem, elem);
		}
		else
		{
			conflit = new ResourceMergeConflits(ConflitsType.TermInDiferentClasses, elem, new ResourceElement(-1,"",((Dictionary) getResource()).getShortElems().get(elem.getTerm()).getX(),""));
		}
		getReport().addConflit(conflit);
	}

	private void updateSynonyms(IResourceElement elem, String classe,int classeDatabaseID, Set<String> termSynomns) {
		Set<String> synsSet = new HashSet<String>();
		IResourceElement newElem;
		for(String syn:termSynomns)
		{
			if(syn.length()>= TableResourcesElements.mimimumSynonymSize && syn.length()<TableResourcesElements.synonymSize && !synsSet.contains(syn.toUpperCase()))
			{
				newElem = new ResourceElement(elem.getID(),syn,classeDatabaseID,classe);
				if(getResource().addSynomyn(newElem))
				{
					getReport().addSynonymsAdding(1);
					synsSet.add(syn.toUpperCase());
				}
				else
				{
					IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveSynonyms, newElem, newElem);
					getReport().addConflit(conflit);
				}
			}
		}
	}

	private void updateExternalIds(IResourceElement elem,List<IExternalID> externalIDs) throws DatabaseLoadDriverException, SQLException {
		Set<String> extIDsSet = new HashSet<String>();
		for(IExternalID externalIDField:externalIDs)
		{
			String externalID = externalIDField.getExternalID();
			if(!sourceNameSourceID.containsKey(externalIDField.getSource()))
			{
				sourceNameSourceID.put(externalIDField.getSource(),  resource.addSource(externalIDField.getSource()));
			}
			if(!externalID.isEmpty()&&externalID.length()<TableResourcesElements.externalIDSize && !extIDsSet.contains(externalID.toUpperCase()))
			{
				if(getResource().addExternalID(elem.getID(), externalID,sourceNameSourceID.get(externalIDField.getSource())))
				{
					getReport().addExternalIDs(1);
					extIDsSet.add(externalID.toUpperCase());
				}
				else
				{
					IResourceElement newElem = new ResourceElement(-1, "", 1, "", new ExternalID(externalID, externalIDField.getSource(), sourceNameSourceID.get(externalIDField.getSource())));
					IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlteradyHaveExternalID, newElem,newElem);
					getReport().addConflit(conflit);
				}
			}
			else if(extIDsSet.contains(externalID.toUpperCase()))
			{
				IResourceElement newElem = new ResourceElement(-1, "", 1, "", new ExternalID(externalID, externalIDField.getSource(), sourceNameSourceID.get(externalIDField.getSource())));
				IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlteradyHaveExternalID, newElem,newElem);
				getReport().addConflit(conflit);
			}
		}
	}
	
	private void reportupdateTermConflit(IResourceReport report,IResourceElement elem) {
		IInsertConflits conflit;
		if(((Dictionary) getResource()).getSynonyms().get(elem.getTerm())!=null && elem.getTermClassID()==((Dictionary) getResource()).getSynonyms().get(elem.getTerm()).getX())
		{
			conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elem, elem);
		}
		else
		{
			Dictionary dic = ((Dictionary) getResource());
			Map<String, GenericPair<Integer, Integer>> synonyms = dic.getSynonyms();
			GenericPair<Integer, Integer> syn = synonyms.get(elem.getTerm());
			conflit = new ResourceMergeConflits(ConflitsType.TermInDiferentClasses, elem, new ResourceElement(-1,"",syn.getX(),""));
		}
		report.addConflit(conflit);
	}
	
	
	public int getNextResourceElementID() {
		return nextResourceElementID;
	}

	public void setNextResourceElementID(int nextResourceElementID) {
		this.nextResourceElementID = nextResourceElementID;
	}


	private String getSynonym(Set<String> termSynomns) {
		if(termSynomns==null || termSynomns.size()<1)
		{
			return new String();
		}
		else
		{
			for(String syn : termSynomns)
			{
				if(syn.length() >= TableResourcesElements.mimimumElementSize)
				{
					termSynomns.remove(syn);
					return syn;
				}
			}
		}
		return new String();
	}


	public IDictionary getResource() {
		return resource;
	}

	public void setResource(IDictionary resource) {
		this.resource = resource;
	}
	
	
}
