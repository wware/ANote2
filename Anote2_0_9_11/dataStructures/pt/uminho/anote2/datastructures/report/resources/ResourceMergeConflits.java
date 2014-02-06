package pt.uminho.anote2.datastructures.report.resources;

import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.resource.IResourceElement;

public class ResourceMergeConflits implements IInsertConflits{

	private ConflitsType conflitType;
	private boolean solve;
	private IResourceElement originalTerm;
	private IResourceElement conflitTerm;
	
	
	public ResourceMergeConflits(ConflitsType conflitType,IResourceElement originalTerm,IResourceElement conflitTerm)
	{
		this.conflitType=conflitType;
		this.originalTerm=originalTerm;
		this.conflitTerm=conflitTerm;
		this.solve = false;
	}
	
	public ConflitsType getConflitType() {
		return conflitType;
	}

	public boolean isSolve() {
		return solve;
	}

	public IResourceElement getOriginalTerm() {
		return originalTerm;
	}

	public IResourceElement getConflitTerm() {
		return conflitTerm;
	}

	public void changeSolveStatus(boolean newStatus) {
		this.solve=newStatus;		
	}

}
