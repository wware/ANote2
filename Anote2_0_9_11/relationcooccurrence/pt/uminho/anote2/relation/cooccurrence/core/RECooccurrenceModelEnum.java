package pt.uminho.anote2.relation.cooccurrence.core;

import java.sql.SQLException;
import java.util.Properties;

import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.relation.cooccurrence.core.models.RECooccurrenceSentenceContiguous;
import pt.uminho.anote2.relation.cooccurrence.core.models.RECooccurrenceSentencePortion;

public enum RECooccurrenceModelEnum {
	Sentence_Contigous{
		public ARECooccurrence getRelationCooccurrenceModel(ICorpus corpus,IEProcess nerProcess,Properties properties,TimeLeftProgress progress) throws SQLException, DatabaseLoadDriverException {
			properties.put(GlobalNames.recooccurrenceModel, this.getDescription());
			return new RECooccurrenceSentenceContiguous(corpus, nerProcess, properties,progress);
		}

		public String getDescription() {
			return "Entity Sentence Contigous";
		}
		
		public String getImagePath() {
			return "icons/recoocorrence_contigous.png";
		}

		public String toString(){
			return "Entity Sentence Contigous";
		}		
	},
	Sentence_Portion{
		public ARECooccurrence getRelationCooccurrenceModel(ICorpus corpus,IEProcess nerProcess,Properties properties,TimeLeftProgress progress) throws SQLException, DatabaseLoadDriverException {
			properties.put(GlobalNames.recooccurrenceModel, this.getDescription());
			return 	new RECooccurrenceSentencePortion(corpus, nerProcess, properties,progress);
		}

		public String getDescription(){
			return "Mix Entity Pairs Sentence";
		}
		
		public String getImagePath() {
			return "icons/recoocorrence_portion.png";
		}

		public String toString(){
			return "Mix Entity Pairs Sentence";
		}
	};

	public ARECooccurrence getRelationCooccurrenceModel(ICorpus corpus,IEProcess ieProcess,Properties properties, TimeLeftProgress progress) throws SQLException, DatabaseLoadDriverException{
		return this.getRelationCooccurrenceModel(corpus,ieProcess,properties,progress);
	}
	
	public String getDescription() {
		return this.getDescription();
	}
	
	public String getImagePath() {
		return this.getImagePath();
	}


}
