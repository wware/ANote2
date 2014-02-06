package pt.uminho.anote2.aibench.ner.datastructures;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.datastructures.annotation.EntityAnnotation;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.datastructures.utils.OrderedMap;
import pt.uminho.anote2.ner.comparators.TermComparator;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.dictionary.IDictionary;
import pt.uminho.anote2.resource.lookuptables.ILookupTable;
import pt.uminho.anote2.resource.rules.IRule;

public class ResourcesToOrderMap {
	
	private OrderedMap<String, IEntityAnnotation> terms;
	private List<IResourceElement> rules;
	private Map<Integer,String> resourceIdsOptions; 
	
	public ResourcesToOrderMap()
	{
		terms = new OrderedMap<String, IEntityAnnotation>(new TermComparator<String>());
		rules = new ArrayList<IResourceElement>();
		resourceIdsOptions = new HashMap<Integer, String>();
	}
	
	public void addResource(IResource<IResourceElement> resource)
	{
		long start = GregorianCalendar.getInstance().getTimeInMillis();
		if(resource instanceof IDictionary) 
		{
			addResources((IDictionary) resource);
		}
		else if(resource instanceof ILookupTable)
		{
			addResources((ILookupTable) resource);
		}
		else if(resource instanceof IRule)
		{
			addResources((IRule) resource);
		}
		else
		{
			System.out.println(" Recurso de nenhum tipo");
		}
		long end = GregorianCalendar.getInstance().getTimeInMillis();
		long diff = end-start;
		System.out.println("Add Resource "+resource.getId()+" "+diff);
	}
	
	public void addResource(IResource<IResourceElement> resource,List<Integer> termClassesID)
	{
		if(resource instanceof IDictionary)
		{
			addResources((IDictionary) resource,termClassesID);
		}
		else if(resource instanceof ILookupTable)
		{
			addResources((ILookupTable) resource,termClassesID);
		}
		else if(resource instanceof IRule)
		{
			addResources((IRule) resource,termClassesID);
		}
		else
		{
			System.out.println(" Recurso de nenhum tipo");
		}
	}
	
	private void addResources(IDictionary dictionary)
	{
		IResourceElementSet<IResourceElement> elems = dictionary.getAllTermsAndSynonyms();
		for(IResourceElement elem : elems.getElements())
		{
			IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), elem.getTerm(),NormalizationForm.getNormalizationForm(elem.getTerm()));		
			terms.put(elem.getTerm(), annot);
		}
		resourceIdsOptions.put(dictionary.getId(),"all");
	}
	
	private void addResources(ILookupTable lookupTable)
	{
		IResourceElementSet<IResourceElement> elems = lookupTable.getResourceElements();
		for(IResourceElement elem : elems.getElements())
		{
			IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), elem.getTerm(),NormalizationForm.getNormalizationForm(elem.getTerm()));		
			terms.put(elem.getTerm(), annot);
		}
		resourceIdsOptions.put(lookupTable.getId(),"all");
	}
	
	/**
	 * Atenção
	 * 
	 * @param rules
	 */
	private void addResources(IRule rules)
	{
		IResourceElementSet<IResourceElement> elems = rules.getResourceElements();
		for(IResourceElement elem : elems.getElements())
		{
			this.rules.add(elem);
		}
		resourceIdsOptions.put(rules.getId(),"all");
	}
	
	private void addResources(IDictionary dictionary,List<Integer> termClassesID)
	{
		String classes = new String();
		for(int classID:termClassesID)
		{
			IResourceElementSet<IResourceElement> elems = dictionary.getTermByClass(classID);
			for(IResourceElement elem : elems.getElements())
			{
				IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), elem.getTerm(),NormalizationForm.getNormalizationForm(elem.getTerm()));		
				terms.put(elem.getTerm(), annot);
			}
			classes = classes.concat(classID+",");
		}
		resourceIdsOptions.put(dictionary.getId(),classes);
	}
	
	private void addResources(ILookupTable lookupatabele,List<Integer> termClassesID)
	{
		String classes = new String();
		for(int classID:termClassesID)
		{
			IResourceElementSet<IResourceElement> elems = lookupatabele.getTermByClass(classID);
			for(IResourceElement elem : elems.getElements())
			{
				IEntityAnnotation annot = new EntityAnnotation(elem.getID(),0,0, elem.getTermClassID(),elem.getID(), elem.getTerm(),NormalizationForm.getNormalizationForm(elem.getTerm()));		
				terms.put(elem.getTerm(), annot);
			}
			classes = classes.concat(classID+",");
		}
		resourceIdsOptions.put(lookupatabele.getId(),classes);
	}
	
	private void addResources(IRule rules,List<Integer> termClassesID)
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
		resourceIdsOptions.put(rules.getId(),classes);
	}

	public OrderedMap<String, IEntityAnnotation> getTerms() {
		return terms;
	}

	public void setTerms(OrderedMap<String, IEntityAnnotation> terms) {
		this.terms = terms;
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

}
