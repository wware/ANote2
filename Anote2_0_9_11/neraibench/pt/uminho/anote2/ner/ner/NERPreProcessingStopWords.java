package pt.uminho.anote2.ner.ner;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.ner.ner.rules.HandRules;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;

public class NERPreProcessingStopWords extends NER {
	
	private ILexicalWords stopWords;
	private boolean stop = false;
		
	public NERPreProcessingStopWords(List<IEntityAnnotation> termAnnotations,ILexicalWords stopWords){
		super(termAnnotations);
		this.stopWords = stopWords;
	}
	
	public NERPreProcessingStopWords(List<IEntityAnnotation> termAnnotations, HandRules rules,ILexicalWords stopWords){
		super(termAnnotations,rules);
		this.stopWords = stopWords;
	}
	
	
	public AnnotationPositions executeNer(String text,List<Integer> listClassIDCaseSensative,boolean caseSensitive) throws SQLException, DatabaseLoadDriverException {
		Set<String> stopWordsList = new HashSet<String>();
		if(stopWords!=null && stopWords.getLexicalWords()!=null)
		{
			if(caseSensitive)
			{
				stopWordsList = stopWords.getLexicalWords();
			}
			else
			{
				for(String sp : stopWords.getLexicalWords())
				{
					stopWordsList.add(sp.toLowerCase());
				}
			}
		}
		AnnotationPositions annotations = new AnnotationPositions();
		boolean caseSensitiveOption;
		if(stop)
		{
			return new AnnotationPositions();
		}
		int total = getElementsToNER().size();
		int start = 0;
		for(IEntityAnnotation termAnnot : getElementsToNER())
		{
			String term = termAnnot.getAnnotationValue();
			if(stop)
			{
				return new AnnotationPositions();
			}
			if(!caseSensitive && stopWordsList.contains(term.toLowerCase()))
			{
			}
			else if(stopWordsList.contains(term))
			{
			}
			else
			{
				if(listClassIDCaseSensative.contains(termAnnot.getClassAnnotationID()))
				{
					caseSensitiveOption = true;
				}
				else
				{
					caseSensitiveOption = caseSensitive;
				}
				/**
				 * For working we have to put a space character - regular expression problem
				 */
				List<AnnotationPosition> positions = searchTermInText(term, text+" ", caseSensitiveOption);		
				for(AnnotationPosition pos : positions)
				{
					annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMore(pos,termAnnot);
				}
			}
			start++;
		}
		if(getRules() != null && !stop)
			getRules().applyRules(text, annotations, SEPARATOR);	
		return annotations;
	}


	public void stop() {
		stop = true;		
	}

	public Properties getProperties(ResourcesToNerAnote resources,boolean normalization) {
		Properties prop = super.getProperties(resources, normalization);
		Properties other = stopWordsProperties();
		prop.putAll(other);
		return prop;
	}
	
	private Properties stopWordsProperties() {
		Properties prop = new Properties();
		if(stopWords!=null)
		{
			prop.put(GlobalNames.stopWordsResourceID,String.valueOf(stopWords.getID()));
		}
		return prop;
	}
	
	public void preprocessing(Properties prop) {
		prop.put(GlobalNames.nerpreProcessing,GlobalNames.stopWords);
	}


}
