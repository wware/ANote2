package pt.uminho.anote2.resource.dictionary;

import java.io.File;
import java.util.Properties;

public interface IDicionaryFlatFilesLoader extends IDictionaryLoader{
	
	public boolean checkFile(File file);
	public void loadTermsFromFile(File file,Properties properties);

}
