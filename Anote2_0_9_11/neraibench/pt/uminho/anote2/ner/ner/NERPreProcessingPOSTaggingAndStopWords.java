package pt.uminho.anote2.ner.ner;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ITextSegment;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.ner.ner.rules.HandRules;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;

public class NERPreProcessingPOSTaggingAndStopWords extends NERPreProcessingPOSTagging{
	
	private ILexicalWords stopWords;
	private boolean stop = false;
	
	public NERPreProcessingPOSTaggingAndStopWords(List<IEntityAnnotation> termAnnotations,Set<String> positiveFilterTags,ILexicalWords stopWords) {
		super(termAnnotations, positiveFilterTags);
		this.stopWords = stopWords;
	}
	
	
	public NERPreProcessingPOSTaggingAndStopWords(List<IEntityAnnotation> termAnnotations, HandRules rules,Set<String> positiveFilterTags,ILexicalWords stopWords) {
		super(termAnnotations, rules, positiveFilterTags);
		this.stopWords = stopWords;
	}

	public AnnotationPositions executeNer(String text,List<Integer> listClassIDCaseSensative,boolean caseSensitive) throws SQLException, DatabaseLoadDriverException{
		
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
		AnnotationPosition auxpos;
		if(stop)
			return new AnnotationPositions();
		List<ITextSegment> textSegments = getOpenNLPTools().geTextSegmentsFilterByPOSTags(text,getPositiveFilterTags());
		for(IEntityAnnotation termAnnot : getElementsToNER())
		{
			if(stop)
			{
				break;
			}
			if(listClassIDCaseSensative.contains(termAnnot.getClassAnnotationID()))
			{
				caseSensitiveOption = true;
			}
			else
			{
				caseSensitiveOption = caseSensitive;
			}
			String term = termAnnot.getAnnotationValue();
			if(!caseSensitive && stopWordsList.contains(term.toLowerCase()))
			{
			}
			else if(stopWordsList.contains(term))
			{
			}
			else
			{
				List<AnnotationPosition> positions = searchTermInText(term, text+" ", caseSensitiveOption);
				for(AnnotationPosition pos : positions)
				{
					if(stop)
						return new AnnotationPositions();
					if(insideTExtSegment(textSegments,pos))
					{
						auxpos = new AnnotationPosition(pos.getStart(),pos.getEnd());
						annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMore(auxpos,termAnnot);
					}
				}
			}

		}
		if(getRules() != null)
		{
			for(ITextSegment segment:textSegments)
			{
				if(stop)
					return new AnnotationPositions();
				getRules().applyRules(segment, annotations, SEPARATOR);
			}
		}
		return annotations;
	}
	
	public Properties getProperties(ResourcesToNerAnote resources,boolean normalization) {
		Properties prop = super.getProperties(resources, normalization);
		Properties other = stopWordsProperties();
		prop.putAll(other);
		return prop;
	}
	
	private Properties stopWordsProperties() {
		Properties prop = new Properties();
		if(getStopWords()!=null)
		{
			prop.put(GlobalNames.stopWordsResourceID,String.valueOf(stopWords.getID()));
		}
		return prop;
	}
	
	public void stop() {
		stop = true;		
	}

	public ILexicalWords getStopWords() {
		return stopWords;
	}

	public void preprocessing(Properties prop) {
		prop.put(GlobalNames.nerpreProcessing,GlobalNames.nerpreProcessingPosTaggingAndStopWords);
	}

}
