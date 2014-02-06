package pt.uminho.anote2.aibench.ner.datastructures;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.ner.EntityAnnotation;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.textprocessing.TermSeparator;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.dictionary.IDictionary;
import pt.uminho.anote2.resource.lookuptables.ILookupTable;
import pt.uminho.anote2.resource.ontologies.IOntology;
import pt.uminho.anote2.resource.rules.IRule;

public class ElementToNer {
	
	private List<IEntityAnnotation> terms;
	private List<IResourceElement> rules;
	private Map<Integer,String> resourceIdsOptions; 
	private ResourcesToNerAnote resourceToNER;
	private boolean termNormalization;
	private boolean usingOtherResourceInfoToImproveRuleAnnotstions;

	public ElementToNer(ResourcesToNerAnote resourceToNER,boolean termNormalization) throws DatabaseLoadDriverException, SQLException
	{
		this.resourceToNER = resourceToNER;
		terms = new ArrayList<IEntityAnnotation>();
		rules = new ArrayList<IResourceElement>();
		resourceIdsOptions = new HashMap<Integer, String>();
		this.termNormalization = termNormalization;
		this.usingOtherResourceInfoToImproveRuleAnnotstions = resourceToNER.isUseOtherResourceInformationInRules();
		processingINfo();
	}
	
	private void processingINfo() throws DatabaseLoadDriverException, SQLException {

		for(int i=0;i<resourceToNER.getList().size();i++)
		{
			Set<Integer> selected = resourceToNER.getList().get(i).getZ();
			Set<Integer> all = resourceToNER.getList().get(i).getY();
			if(selected.equals(all))
			{
				addResource(resourceToNER.getList().get(i).getX(),termNormalization);
			}
			else
			{
				addResource(resourceToNER.getList().get(i).getX(),new ArrayList<Integer>(selected),termNormalization);
			}
		}
	}
	
	public List<IResource<IResourceElement>> getResourcesForRulesInfo()
	{
		List<IResource<IResourceElement>> resources =  new ArrayList<IResource<IResourceElement>>();
		for(int i=0;i<resourceToNER.getList().size();i++)
		{
			IResource<IResourceElement> resource = resourceToNER.getList().get(i).getX();
			if(resource instanceof IDictionary)
			{
				resources.add(resource);
			}
			else if(resource instanceof IOntology)
			{
				resources.add(resource);
			}
		}
		return resources;
	}

	public void addResource(IResource<IResourceElement> resource, List<Integer> arrayList, boolean normalization) throws DatabaseLoadDriverException, SQLException
	{
		if(resource instanceof IDictionary) 
		{
			addResources((IDictionary) resource,arrayList,normalization);
		}
		else if(resource instanceof ILookupTable)
		{
			addResources((ILookupTable) resource,arrayList,normalization);
		}
		else if(resource instanceof IRule)
		{
			addResources((IRule) resource,arrayList);
		}
		else if(resource instanceof IOntology)
		{
			addResources((IOntology) resource,arrayList,normalization);
		}
		else
		{
			System.err.println(" Undifined Resource");
		}
	}
	
	public void addResource(IResource<IResourceElement> resource,boolean normalization) throws DatabaseLoadDriverException, SQLException
	{
		if(resource instanceof IDictionary)
		{
			addResources((IDictionary) resource,normalization);
		}
		else if(resource instanceof ILookupTable)
		{
			addResources((ILookupTable) resource,normalization);
		}
		else if(resource instanceof IRule)
		{
			addResources((IRule) resource);
		}
		else if(resource instanceof IOntology)
		{
			addResources((IOntology) resource,normalization);
		}
		else
		{
			System.err.println(" Undifined Resource");
		}
	}
	
	private void addResources(IDictionary dictionary,boolean normalization) throws DatabaseLoadDriverException, SQLException
	{
		if(normalization)
		{
			IResourceElementSet<IResourceElement> elems = dictionary.getAllTermsAndSynonyms();
			for(IResourceElement elem : elems.getElements())
			{
				IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), TermSeparator.termSeparator(elem.getTerm()),NormalizationForm.getNormalizationForm(elem.getTerm()));		
				terms.add(annot);
			}
		}
		else
		{
			IResourceElementSet<IResourceElement> elems = dictionary.getAllTermsAndSynonyms();
			for(IResourceElement elem : elems.getElements())
			{
				IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), elem.getTerm(),NormalizationForm.getNormalizationForm(elem.getTerm()));		
				terms.add(annot);
			}
		}
		resourceIdsOptions.put(dictionary.getID(),GlobalNames.allclasses);
		
	}
	
	private void addResources(ILookupTable lookupTable,boolean normalization) throws DatabaseLoadDriverException, SQLException
	{
		if(normalization)
		{
			IResourceElementSet<IResourceElement> elems = lookupTable.getResourceElements();
			for(IResourceElement elem : elems.getElements())
			{
				IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(),TermSeparator.termSeparator(elem.getTerm()),NormalizationForm.getNormalizationForm(elem.getTerm()));		
				terms.add(annot);
			}
		}
		else
		{
			IResourceElementSet<IResourceElement> elems = lookupTable.getResourceElements();
			for(IResourceElement elem : elems.getElements())
			{
				IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), elem.getTerm(),NormalizationForm.getNormalizationForm(elem.getTerm()));		
				terms.add(annot);
			}
		}
		resourceIdsOptions.put(lookupTable.getID(),GlobalNames.allclasses);
	}

	private void addResources(IOntology ontology, boolean normalization) throws SQLException, DatabaseLoadDriverException
	{
		if(normalization)
		{
			IResourceElementSet<IResourceElement> elems = ontology.getAllTermsAndSynonyms();
			for(IResourceElement elem : elems.getElements())
			{
				IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(),TermSeparator.termSeparator(elem.getTerm()),NormalizationForm.getNormalizationForm(elem.getTerm()));		
				terms.add(annot);
			}
		}
		else
		{
			IResourceElementSet<IResourceElement> elems = ontology.getAllTermsAndSynonyms();
			for(IResourceElement elem : elems.getElements())
			{
				IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), elem.getTerm(),NormalizationForm.getNormalizationForm(elem.getTerm()));		
				terms.add(annot);
			}
		}
		resourceIdsOptions.put(ontology.getID(),GlobalNames.allclasses);
	}

	/**
	 * 
	 * @param rules
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException 
	 */
	private void addResources(IRule rules) throws DatabaseLoadDriverException, SQLException
	{
		IResourceElementSet<IResourceElement> elems = rules.getResourceElements();
		for(IResourceElement elem : elems.getElements())
		{
			this.rules.add(elem);
		}
		resourceIdsOptions.put(rules.getID(),GlobalNames.allclasses);
	}
	
	private void addResources(IDictionary dictionary,List<Integer> termClassesID,boolean normalization) throws DatabaseLoadDriverException, SQLException
	{
		String classes = new String();
		if(normalization)
		{
			for(int classID:termClassesID)
			{
				IResourceElementSet<IResourceElement> elems = dictionary.getAllTermByClass(classID);
				for(IResourceElement elem : elems.getElements())
				{
					IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), TermSeparator.termSeparator(elem.getTerm()),NormalizationForm.getNormalizationForm(elem.getTerm()));		
					terms.add(annot);
				}
				classes = classes.concat(classID+",");
			}
		}
		else
		{
			for(int classID:termClassesID)
			{
				IResourceElementSet<IResourceElement> elems = dictionary.getAllTermByClass(classID);
				for(IResourceElement elem : elems.getElements())
				{
					IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), elem.getTerm(),NormalizationForm.getNormalizationForm(elem.getTerm()));		
					terms.add(annot);
				}
				classes = classes.concat(classID+",");
			}
		}
		resourceIdsOptions.put(dictionary.getID(),classes);
	}
	
	private void addResources(IOntology ontology,List<Integer> termClassesID,boolean normalization) throws SQLException, DatabaseLoadDriverException
	{
		String classes = new String();
		if(normalization)
		{
			for(int classID:termClassesID)
			{
				IResourceElementSet<IResourceElement> elems = ontology.getAllTermByClass(classID);
				for(IResourceElement elem : elems.getElements())
				{
					IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(),TermSeparator.termSeparator(elem.getTerm()),NormalizationForm.getNormalizationForm(elem.getTerm()));		
					terms.add(annot);
				}
				classes = classes.concat(classID+",");
			}
		}
		else
		{
			for(int classID:termClassesID)
			{
				IResourceElementSet<IResourceElement> elems = ontology.getAllTermByClass(classID);
				for(IResourceElement elem : elems.getElements())
				{
					IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), elem.getTerm(),NormalizationForm.getNormalizationForm(elem.getTerm()));		
					terms.add(annot);
				}
				classes = classes.concat(classID+",");
			}
		}
		resourceIdsOptions.put(ontology.getID(),classes);
	}
	
	private void addResources(ILookupTable lookupatabele,List<Integer> termClassesID,boolean normalization) throws DatabaseLoadDriverException, SQLException
	{
		String classes = new String();
		if(normalization)
		{
			for(int classID:termClassesID)
			{
				IResourceElementSet<IResourceElement> elems = lookupatabele.getTermByClass(classID);
				for(IResourceElement elem : elems.getElements())
				{
					IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(),TermSeparator.termSeparator(elem.getTerm()),NormalizationForm.getNormalizationForm(elem.getTerm()));		
					terms.add(annot);
				}
				classes = classes.concat(classID+",");
			}
		}
		else
		{
			for(int classID:termClassesID)
			{
				IResourceElementSet<IResourceElement> elems = lookupatabele.getTermByClass(classID);
				for(IResourceElement elem : elems.getElements())
				{
					IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), elem.getTerm(),NormalizationForm.getNormalizationForm(elem.getTerm()));		
					terms.add(annot);
				}
				classes = classes.concat(classID+",");
			}
		}
		resourceIdsOptions.put(lookupatabele.getID(),classes);
	}
	
	private void addResources(IRule rules,List<Integer> termClassesID) throws DatabaseLoadDriverException, SQLException
	{
		String classes = new String();
		for(int classID:termClassesID)
		{
			IResourceElementSet<IResourceElement> elems = rules.getTermByClass(classID);
			for(IResourceElement elem : elems.getElements())
			{
				this.rules.add(elem);
			}
			classes = classes.concat(classID+",");
		}
		resourceIdsOptions.put(rules.getID(),classes);
	}
	
	public List<IEntityAnnotation> getTerms() {
		return terms;
	}

	public List<IResourceElement> getRules() {
		return rules;
	}

	public void setRules(List<IResourceElement> rules) {
		this.rules = rules;
	}

	public void setResourceIdsOptions(Map<Integer,String> resourceIdsOptions) {
		this.resourceIdsOptions = resourceIdsOptions;
	}

	public Map<Integer,String> getResourceIdsOptions() {
		return resourceIdsOptions;
	}	
	
	public ResourcesToNerAnote getResourceToNER() {
		return resourceToNER;
	}
	
	public boolean isUsingOtherResourceInfoToImproveRuleAnnotstions() {
		return usingOtherResourceInfoToImproveRuleAnnotstions;
	}

}
