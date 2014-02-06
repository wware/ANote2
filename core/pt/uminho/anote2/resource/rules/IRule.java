package pt.uminho.anote2.resource.rules;

import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;


public interface IRule extends IResource<IResourceElement>{
	public boolean merge(IRule ruleSetDic);
}
