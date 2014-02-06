package pt.uminho.anote2.relation.configuration;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.process.re.export.REConfiguration;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;

public class RERelationConfiguration extends REConfiguration implements IRERelationConfiguration{

	private PosTaggerEnem posTagger;
	private RelationsModelEnem relationModel;
	private ILexicalWords verbFilter;
	private ILexicalWords verbAdittion;
	private IRERelationAdvancedConfiguration advancedConfiguration;
	
	public RERelationConfiguration(ICorpus corpus, IIEProcess entityProcess,PosTaggerEnem posTagger,RelationsModelEnem relationModel,
			ILexicalWords verbFilter,ILexicalWords verbAdittion,IRERelationAdvancedConfiguration advancedConfiguration) {
		super(corpus, entityProcess);
		this.posTagger = posTagger;
		this.relationModel = relationModel;
		this.verbFilter = verbFilter;
		this.verbAdittion = verbAdittion;
		this.advancedConfiguration = advancedConfiguration;
	}

	@Override
	public PosTaggerEnem getPOSTagger() {
		return posTagger;
	}

	@Override
	public RelationsModelEnem getRelationModel() {
		return relationModel;
	}

	@Override
	public ILexicalWords getVerbsFilter() {
		return verbFilter;
	}

	@Override
	public ILexicalWords getVerbsAddition() {
		return verbAdittion;
	}

	@Override
	public IRERelationAdvancedConfiguration getAdvancedConfiguration() {
		return advancedConfiguration;
	}

}
