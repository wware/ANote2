package pt.uminho.anote2.process.IE;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.utils.IGenericPair;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;

public interface IPOSTagger {
	public List<IGenericPair<Long,Long>> getDocumentSentencelimits();
	public IGenericPair<List<IVerbInfo>, List<Long>> getSentenceSintaticLayer(Set<String> termionations, IGenericPair<Long, Long> setenceLimits,Long documentStartOffset) throws SQLException, DatabaseLoadDriverException;
	public Map<Long, IVerbInfo> getVerbsPosition(List<IVerbInfo> verbsInfo);


}
