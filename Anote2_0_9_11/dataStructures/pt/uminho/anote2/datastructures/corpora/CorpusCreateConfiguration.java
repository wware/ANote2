package pt.uminho.anote2.datastructures.corpora;

import java.util.Set;

import pt.uminho.anote2.core.corpora.ICorpusCreateConfiguration;
import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.core.document.IPublication;

public class CorpusCreateConfiguration implements ICorpusCreateConfiguration{

	private String name;
	private Set<IPublication> docs;
	private CorpusTextType textType;
	private boolean journalRetrievalBefore;
	
	public CorpusCreateConfiguration(String name, Set<IPublication> docIds,CorpusTextType textType, boolean journalRetrievalBefore) {
		super();
		this.name = name;
		this.docs = docIds;
		this.textType = textType;
		this.journalRetrievalBefore = journalRetrievalBefore;
	}

	@Override
	public String getCorpusName() {
		return name;
	}

	@Override
	public Set<IPublication> getDocuments() {
		return docs;
	}

	@Override
	public CorpusTextType getCorpusTextType() {
		return textType;
	}

	@Override
	public boolean processJournalRetrievalBefore() {
		return journalRetrievalBefore;
	}

	@Override
	public void setDocuments(Set<IPublication> docs) {
		this.docs = docs;
	}

}
