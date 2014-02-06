package pt.uminho.anote2.core.report.resources;

import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public interface IResourceMergeReport extends IResourceReport{
	
	public IResource<IResourceElement> getResourceSource2();
	public void setResourceSource2(IResource<IResourceElement> resource);
	public IResource<IResourceElement> getResourceSource();
	public boolean isCreatedNewResource();

}
