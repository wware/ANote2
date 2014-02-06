package pt.uminho.anote2.relation.datastructures;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IRelationModel;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.core.relationModels.RelationModelBinaryBiomedicalVerbs;
import pt.uminho.anote2.relation.core.relationModels.RelationModelBinaryVerbLimitation;
import pt.uminho.anote2.relation.core.relationModels.RelationModelSimple;
import pt.uminho.anote2.relation.core.relationModels.RelationModelVerbLimitation;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.gate.process.IGatePosTagger;

public enum RelationsModelEnem {
	Binary_Biomedical_Verbs{
		public IRelationModel getRelationModel(ICorpus corpus,IIEProcess nerProcess,IGatePosTagger postagger,ILexicalWords biomedicalverbs,IRERelationAdvancedConfiguration advancedConfiguration) {
			return 	new RelationModelBinaryBiomedicalVerbs(corpus,nerProcess,postagger,biomedicalverbs,advancedConfiguration);
		}

		public String getDescription(){
			return "Binary Selected Verbs Only";
		}
		
		public String getImagePath() {
			return "icons/relation_model_binary_verb_select_user.png";
		}

		public String toString(){
			return "Binary Selected Verbs Only (1 x 1)*";
		}
	},
	Binary_Verb_limitation{
		public IRelationModel getRelationModel(ICorpus corpus,IIEProcess nerProcess,IGatePosTagger postagger,ILexicalWords biomedicalverbs,IRERelationAdvancedConfiguration advancedConfiguration) {
			return 	new RelationModelBinaryVerbLimitation(corpus,nerProcess,postagger,advancedConfiguration);
		}

		public String getDescription(){
			return "Binary Verb Limitation";
		}
		
		public String getImagePath() {
			return "icons/relation_model_binary_verb_limitation.png";
		}

		public String toString(){
			return "Binary Verb Limitation (1 x 1)";
		}
	}
	,
	Verb_Limitation{
		public IRelationModel getRelationModel(ICorpus corpus,IIEProcess nerProcess,IGatePosTagger postagger,ILexicalWords biomedicalverbs,IRERelationAdvancedConfiguration advancedConfiguration) {
			return 	new RelationModelVerbLimitation(corpus,nerProcess,postagger,advancedConfiguration);
		}

		public String getDescription(){
			return "Verb Limitation";
		}
		
		public String getImagePath() {
			return "icons/relation_model_verb_limitation.png";
		}

		public String toString(){
			return "Verb Limitation (M x M)";
		}
	},
	
	Simple_Model{
		public IRelationModel getRelationModel(ICorpus corpus,IIEProcess nerProcess,IGatePosTagger postagger,ILexicalWords biomedicalverbs,IRERelationAdvancedConfiguration advancedConfiguration) {
			return new RelationModelSimple(corpus,nerProcess,postagger,advancedConfiguration);
		}

		public String getDescription() {
			return "Simple Model";
		}
		
		public String getImagePath() {
			return "icons/relation_model_simple.png";
		}

		public String toString(){
			return "Simple Model (M x M )";
		}		
	};



	public IRelationModel getRelationModel(ICorpus corpus,IIEProcess ieProcess,IGatePosTagger postagger,ILexicalWords biomedicalverbs,IRERelationAdvancedConfiguration advancedConfiguration){
		return this.getRelationModel(corpus,ieProcess,postagger,biomedicalverbs,advancedConfiguration);
	}
	
	public String getDescription() {
		return this.getDescription();
	}
	
	public String getImagePath() {
		return this.getImagePath();
	}

	public static RelationsModelEnem convertStringToRelationsModelEnem(String str)
	{	
		if(str.equals("Binary Selected Verbs Only (1 x 1)*"))
		{
			return RelationsModelEnem.Binary_Biomedical_Verbs;
		}
		else if(str.equals("Binary Verb Limitation (1 x 1)"))
		{
			return RelationsModelEnem.Binary_Verb_limitation;
		}
		else if(str.equals("Verb Limitation (M x M)"))
		{
			return RelationsModelEnem.Verb_Limitation;

		}
		else if(str.equals("Simple Model (M x M )"))
		{
			return RelationsModelEnem.Simple_Model;

		}
		return null;
	}
	
}
