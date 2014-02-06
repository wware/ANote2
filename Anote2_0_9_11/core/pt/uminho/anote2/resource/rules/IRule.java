package pt.uminho.anote2.resource.rules;

import java.sql.SQLException;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;


public interface IRule extends IResource<IResourceElement>{
	public IResourceMergeReport merge(IRule ruleSetDic) throws DatabaseLoadDriverException, SQLException;
	public IResourceMergeReport merge(IRule ruleSetDic,Set<Integer> cloassIDs) throws DatabaseLoadDriverException, SQLException;
}
