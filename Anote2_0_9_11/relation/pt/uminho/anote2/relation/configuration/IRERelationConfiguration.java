package pt.uminho.anote2.relation.configuration;

import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;

public interface IRERelationConfiguration extends IREConfiguration{
	
	public PosTaggerEnem getPOSTagger();
	public RelationsModelEnem getRelationModel();
	public ILexicalWords getVerbsFilter();
	public ILexicalWords getVerbsAddition();
	public IRERelationAdvancedConfiguration getAdvancedConfiguration();

}
