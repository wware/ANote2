package pt.uminho.anote2.ner.ner;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ITextSegment;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.nlptools.OpenNLP;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.ner.ner.rules.HandRules;

public class NERPreProcessingPOSTagging extends NER{

	private Set<String> positiveFilterTags;
	private OpenNLP openNLPTools;
	private boolean stop = false;

	
	public NERPreProcessingPOSTagging(List<IEntityAnnotation> termAnnotations,Set<String> positiveFilterTags) {
		super(termAnnotations);
		this.positiveFilterTags = positiveFilterTags;
		this.openNLPTools = new OpenNLP();
	}
	
	public NERPreProcessingPOSTagging(List<IEntityAnnotation> termAnnotations, HandRules rules,Set<String> positiveFilterTags)
	{
		super(termAnnotations,rules);
		this.positiveFilterTags = positiveFilterTags;
		this.openNLPTools = new OpenNLP();
	}
	
	public AnnotationPositions executeNer(String text,List<Integer> listClassIDCaseSensative,boolean caseSensitive) throws SQLException, DatabaseLoadDriverException{
		AnnotationPositions annotations = new AnnotationPositions();
		boolean caseSensitiveOption;
		AnnotationPosition auxpos;
		int start = 0;
		int total = getElementsToNER().size();
		if(stop)
			return new AnnotationPositions();
		List<ITextSegment> textSegments = openNLPTools.geTextSegmentsFilterByPOSTags(text,positiveFilterTags);
		for(IEntityAnnotation termAnnot : getElementsToNER())
		{
			if(stop)
			{
				break;
			}
			String term = termAnnot.getAnnotationValue();
			if(listClassIDCaseSensative.contains(termAnnot.getClassAnnotationID()))
			{
				caseSensitiveOption = true;
			}
			else
			{
				caseSensitiveOption = caseSensitive;
			}
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
	
	protected boolean insideTExtSegment(List<ITextSegment> textSegments,AnnotationPosition pos) {
		
		for(ITextSegment textSegment:textSegments)
		{
			if(textSegment.getStartOffset() > pos.getEnd())
			{
				return false;
			}
			if(textSegment.getStartOffset() <= pos.getStart() && textSegment.getEndOffset() >= pos.getEnd())
			{
				return true;
			}
		}
		return false;
	}

	public Properties getProperties(ResourcesToNerAnote resources,boolean normalization) {
		Properties prop = super.getProperties(resources, normalization);
		Properties other = tagProperties();
		prop.putAll(other);
		return prop;
	}
	
	public void preprocessing(Properties prop) {
		prop.put(GlobalNames.nerpreProcessing,GlobalNames.nerpreProcessingPosTagging);
	}
	
	private Properties tagProperties()
	{
		Properties prop = new Properties();
		String posTags = new String();
		for(String label:positiveFilterTags)
		{
			posTags = posTags + label + "|";
		}
		prop.put(GlobalNames.nerpreProcessingTags,posTags);
		return prop;
	}
	
	public void stop() {
		stop = true;		
	}
	
	public Set<String> getPositiveFilterTags() {
		return positiveFilterTags;
	}

	public OpenNLP getOpenNLPTools() {
		return openNLPTools;
	}


}
