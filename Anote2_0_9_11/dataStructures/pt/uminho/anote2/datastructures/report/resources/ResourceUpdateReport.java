package pt.uminho.anote2.datastructures.report.resources;

import java.io.File;

import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public class ResourceUpdateReport extends ResourceReport implements IResourceUpdateReport{

	
	private String file;
	private String font;
	
	public ResourceUpdateReport(String title,IResource<IResourceElement> destiny,File file,String font) {
		super(title, destiny);
	}

	public String getFile() {
		return file;
	}

	public String getDataFont() {
		return font;
	}

	public void updateFile(File file) {
		this.file = file.getAbsolutePath();
	}
	
	public IResourceUpdateReport clone()
	{
		IResourceUpdateReport newReport = new ResourceUpdateReport(getTitle(),getResourceDestination(), new File(getFile()), getDataFont());
		newReport.addClassesAdding(getClassesAdding());
		newReport.addAllConflit(getConflicts());
		newReport.addExternalIDs(getExternalIDs());
		newReport.addSynonymsAdding(getSynonymsAdding());
		newReport.addTermAdding(getTermsAdding());
		newReport.setTime(getTime());
		return newReport;
		
	}

}
