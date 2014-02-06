package pt.uminho.anote2.relation.cooccurrence.core.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.datastructures.annotation.re.EventAnnotation;
import pt.uminho.anote2.datastructures.annotation.re.EventProperties;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.relation.cooccurrence.core.ARECooccurrence;

public class RECooccurrenceSentencePortion extends ARECooccurrence{

	public RECooccurrenceSentencePortion(ICorpus corpus, IEProcess nerProcess,
			Properties properties, TimeLeftProgress progress) throws SQLException, DatabaseLoadDriverException {
		super(corpus, nerProcess, properties,progress);
	}

	@Override
	public List<IEventAnnotation> processDocumetAnnotations(List<IEntityAnnotation> listEntitiesSortedByOffset,IDocument doc) throws SQLException, DatabaseLoadDriverException,Exception {
		List<ISentence> list = getSentencesLimits(doc);
		getRelations(listEntitiesSortedByOffset, list);
		return getRelations(listEntitiesSortedByOffset, list);
	}
	
	private List<IEventAnnotation> getRelations(List<IEntityAnnotation> listEntitiesSortedByOffset, List<ISentence> list) {
		List<IEventAnnotation> events = new ArrayList<IEventAnnotation>();
		for(ISentence sentence:list)
		{
			List<IEntityAnnotation> listEntitiesSentenceOrderOffset = getSentenceEntties(listEntitiesSortedByOffset,sentence);
			for(int i=0;i<listEntitiesSentenceOrderOffset.size();i++)
			{
				if(i+1>=listEntitiesSentenceOrderOffset.size())
				{

				}
				else
				{
					IEntityAnnotation entLeft1 = listEntitiesSentenceOrderOffset.get(i);
					for(int j=i+1;j<listEntitiesSentenceOrderOffset.size();j++)
					{
						IEntityAnnotation entrifgt1 = listEntitiesSentenceOrderOffset.get(j);
						if(!entLeft1.equals(entrifgt1))
						{
							IEventAnnotation ev = new EventAnnotation(-1, sentence.getStartOffset(), sentence.getStartOffset(),"", entLeft1, entrifgt1, "", -1, "", new EventProperties());
							events.add(ev);
						}
					}
				}
			}
		}
		return events;
	}

}
