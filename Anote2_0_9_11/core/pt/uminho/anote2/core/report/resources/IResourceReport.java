package pt.uminho.anote2.core.report.resources;

import java.util.List;

import pt.uminho.anote2.core.report.IReport;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public interface IResourceReport extends IReport{
	public boolean isActive();
	public IResource<IResourceElement> getResourceDestination();
	public int getNumberConflits();
	public List<IInsertConflits> getConflicts();
	public void addConflit(IInsertConflits conflit);
	public void addAllConflit(List<IInsertConflits> conflits);
	public boolean isPerformed();
	public void changePerformedStatus(boolean newStatus);
	public void changeActiveStatus(boolean newStatus);
	public void addTermAdding(int newTerms);
	public void addSynonymsAdding(int newSynonyms);
	public void addClassesAdding(int newClasses);	
	public void addExternalIDs(int newExternalIDs);	
	public int getTermsAdding();
	public int getSynonymsAdding();
	public int getClassesAdding();
	public int getExternalIDs();
	
}
