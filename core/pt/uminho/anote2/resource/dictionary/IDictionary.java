package pt.uminho.anote2.resource.dictionary;

import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;

/**
 * Interface 
 * 
 * @author Hugo Costa
 *
 */
public interface IDictionary extends IResource<IResourceElement>{
	public boolean merge(IDictionary idSncDic);
	public IResourceElementSet<IResourceElement> getTermSynomns(int id);
	public boolean addSynomns(IResourceElement elem);
	public boolean addExternalID(int termID,String externalID,int sourceID);
	public int addSource(String source);
	public IResourceElement getTerm(int termID);
	public IResourceElementSet<IResourceElement> getAllTermsAndSynonyms();
	public IResourceElementSet<IResourceElement> getAllTermByClass(String termClass);
	public IResourceElementSet<IResourceElement> getAllTermByClass(int termClassID);
	public void loadAllTerms();
	public void deleteTerms();
	public void initExistElem();
	public void deleteInitElem();
}
