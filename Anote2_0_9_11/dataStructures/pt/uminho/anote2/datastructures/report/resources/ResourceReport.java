package pt.uminho.anote2.datastructures.report.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceReport;
import pt.uminho.anote2.datastructures.report.Report;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public class ResourceReport extends Report implements IResourceReport{

	private boolean active;
	private int termsAdding;
	private int synonymsAdding;
	private int classesAdding;
	private int externalIDs;
	private IResource<IResourceElement> destiny;
	private List<IInsertConflits> conflits;
	private int getConflitsNumber;
	private boolean performed;

	
	public ResourceReport(String title,IResource<IResourceElement> destiny) {
		super(title);
		this.active = true;
		this.getConflitsNumber = 0;
		this.destiny = destiny;
		this.performed = true;
		this.conflits = new ArrayList<IInsertConflits>();

	}
	
	public ResourceReport(String title,Properties properties)
	{
		super(title,properties);
		this.active=false;
	}

	public boolean isActive() {
		return active;
	}

	public void changeActiveStatus(boolean newStatus) {
		this.active=newStatus;
	}

	public int getTermsAdding() {
		return termsAdding;
	}

	public int getSynonymsAdding() {
		return synonymsAdding;
	}

	public int getClassesAdding() {
		return classesAdding;
	}

	public void addTermAdding(int newTerms) {
		this.termsAdding+=newTerms;
	}

	public void addSynonymsAdding(int newSynonyms) {
		this.synonymsAdding+=newSynonyms;
	}

	public void addClassesAdding(int newClasses) {
		this.classesAdding+=newClasses;		
	}

	public void addExternalIDs(int newExternalIDs) {
		this.externalIDs+=newExternalIDs;
	}

	public int getExternalIDs() {
		return externalIDs;
	}
	
	public IResource<IResourceElement> getResourceDestination() {
		return destiny;
	}

	public List<IInsertConflits> getConflicts() {
		return conflits;
	}
	
	public void addConflit(IInsertConflits conflit) {
		conflits.add(conflit);
		getConflitsNumber ++;
	}
	
	public void addAllConflit(List<IInsertConflits> conflits) {
		this.conflits.addAll(conflits);
		getConflitsNumber += conflits.size();
	}

	public boolean isPerformed() {
		return performed;
	}

	public void changePerformedStatus(boolean newStatus) {
		this.performed = newStatus;
	}

	public int getNumberConflits() {
		return getConflitsNumber;
	}


}
