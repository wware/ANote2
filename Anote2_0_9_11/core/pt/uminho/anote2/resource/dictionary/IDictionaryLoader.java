package pt.uminho.anote2.resource.dictionary;

import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;

public interface IDictionaryLoader {
	
	public void setcancel(boolean cancel);
	public IResourceUpdateReport getReport();
	public IDictionary getDictionary();

}
