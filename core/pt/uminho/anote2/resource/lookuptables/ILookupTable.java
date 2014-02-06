package pt.uminho.anote2.resource.lookuptables;

import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public interface ILookupTable extends IResource<IResourceElement>{

	public boolean importCVSFile(String csvFile);
	public boolean exportCSVFile(String csvFile);
	public boolean merge(ILookupTable idSncDic);
	
}
