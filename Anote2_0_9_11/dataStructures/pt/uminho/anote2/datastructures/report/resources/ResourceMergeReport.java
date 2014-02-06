package pt.uminho.anote2.datastructures.report.resources;

import java.util.Properties;

import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public class ResourceMergeReport extends ResourceReport implements IResourceMergeReport{

	private IResource<IResourceElement> source;
	private IResource<IResourceElement> source2;
	private boolean isCreateNewResource;

	
	public ResourceMergeReport(String title,IResource<IResourceElement> source,IResource<IResourceElement> destiny) {
		super(title,destiny);
		this.source = source;
		this.isCreateNewResource = false;
	}
	
	public ResourceMergeReport(String title,Properties properties,IResource<IResourceElement> source,IResource<IResourceElement> destiny) {
		super(title,properties);
		this.source = source;
		this.isCreateNewResource = false;
	}

	public IResource<IResourceElement> getResourceSource() {
		return source;
	}

	public IResource<IResourceElement> getResourceSource2() {
		return source2;
	}

	public void setResourceSource2(IResource<IResourceElement> resource) {
		this.isCreateNewResource = true;
		this.source2 = resource;
	}

	public boolean isCreatedNewResource() {
		return isCreateNewResource;
	}

}
