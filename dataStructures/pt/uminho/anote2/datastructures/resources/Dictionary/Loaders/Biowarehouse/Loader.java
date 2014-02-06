package pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.Biowarehouse;


/**
 * @author Analia
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class Loader{
	
	private Map<String, Integer> term_idclasses=new HashMap<String, Integer>();

	private Map<Integer,String> classID_Class=new HashMap<Integer,String>();

	private IDictionary dictionary;
	private IDatabase source;

	public Loader(IDictionary dictionary,IDatabase source)
	{
		this.dictionary=dictionary;
		this.source=source;
	}
	
	

   public Map<String, Integer> getTerm_idclasses() {
		return term_idclasses;
	}



	public void setTerm_idclasses(Map<String, Integer> term_idclasses) {
		this.term_idclasses = term_idclasses;
	}



	public void setClassID_Class(Map<Integer, String> classID_Class) {
		this.classID_Class = classID_Class;
	}



	public void setDictionary(IDictionary dictionary) {
		this.dictionary = dictionary;
	}



	public void setSource(IDatabase source) {
		this.source = source;
	}



public Map<Integer, String> getClassID_Class() {
	   return classID_Class;
   }

	public IDatabase getSource() {
		return source;
	}

   public IDictionary getDictionary() {
	   return dictionary;
   }

}
