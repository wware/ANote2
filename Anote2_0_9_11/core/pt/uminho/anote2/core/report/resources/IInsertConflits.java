package pt.uminho.anote2.core.report.resources;

import pt.uminho.anote2.resource.IResourceElement;

public interface IInsertConflits {
	public ConflitsType getConflitType();
	public boolean isSolve();
	public void changeSolveStatus(boolean newStatus);
	public IResourceElement getOriginalTerm();
	public IResourceElement getConflitTerm();
}
