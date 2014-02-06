package pt.uminho.anote2.datastructures.report.resources;

import java.io.File;

import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public class PortResourceUpdateReport {
	
	private IResourceUpdateReport report;

	
	public PortResourceUpdateReport(IResource<IResourceElement> resource,String font)
	{
		setReport(new ResourceUpdateReport(GlobalNames.updateDictionariesReportTitle, resource, new File(""), font));
	}


	public IResourceUpdateReport getReport() {
		return report;
	}


	public void setReport(IResourceUpdateReport report) {
		this.report = report;
	}
	
	protected void memoryAndProgress(int step, int total) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
	}

}
