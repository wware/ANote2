package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.Biowarehouse;


/**
 * @author Analia
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class Loader{
	

	private IDictionary dictionary;
	private IDatabase source;

	public Loader(IDictionary dictionary,IDatabase source)
	{
		this.dictionary=dictionary;
		this.source=source;
	}

	public void setDictionary(IDictionary dictionary) {
		this.dictionary = dictionary;
	}



	public void setSource(IDatabase source) {
		this.source = source;
	}

	public IDatabase getSource() {
		return source;
	}

   public IDictionary getDictionary() {
	   return dictionary;
   }

}
