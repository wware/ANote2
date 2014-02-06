package pt.uminho.anote2.ner.ner;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.datastructures.textprocessing.ParsingUtils;
import pt.uminho.anote2.datastructures.textprocessing.Tokenizer;
import pt.uminho.anote2.datastructures.utils.OrderedMap;
import pt.uminho.anote2.ner.annotations.AnnotationPosition;
import pt.uminho.anote2.ner.corpus.GetGlobalAnnotations;
import pt.uminho.anote2.ner.ner.rules.HandRules;

public class NER {

	/** Decreasing ordered map with the terms of the dictionary */
	private OrderedMap<String, IEntityAnnotation> dictionary;
	
	private HandRules rules;
	
	public static final String SEPARATOR = "[ '<>/(),\n\\.]+";
		
	public NER(OrderedMap<String, IEntityAnnotation> termAnnotations){
		this.dictionary = termAnnotations;
		this.rules = null;
	}
	
	public NER(OrderedMap<String, IEntityAnnotation> termAnnotations, HandRules rules){
		this.dictionary = termAnnotations;
		this.rules = rules;
	}
	
	public AnnotationPositions executeNer(String text) throws Exception{
	
		AnnotationPositions annotations = new AnnotationPositions();
		
		for(String term : dictionary.keySet())
		{
			boolean caseSensitive = GetGlobalAnnotations.isSensitive(dictionary.get(term).getAnnotationValue(), term);
			
			String tokenizedTerm = Tokenizer.tokenizer(term);

			List<AnnotationPosition> positions = searchTermInText(tokenizedTerm, text, caseSensitive);
			
			for(AnnotationPosition pos : positions)
				annotations.addAnnotation(pos, dictionary.get(term));
		}
	
		if(rules != null)
			rules.applyRules(text, annotations, SEPARATOR);
		
		return annotations;
	}

	
	public List<AnnotationPosition> searchTermInText(String term, String text, boolean caseSensitive){
		List<AnnotationPosition> positions = new ArrayList<AnnotationPosition>();
		String termExp = ParsingUtils.textToRegExp(term);
		
		Pattern p = caseSensitive ? Pattern.compile(SEPARATOR + "("+termExp+")" + SEPARATOR) : Pattern.compile(SEPARATOR + "("+termExp+")" + SEPARATOR, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1));
			positions.add(pos);
		}
		
		p = caseSensitive ? Pattern.compile("^" + "("+termExp+")" + SEPARATOR) : Pattern.compile("^" + "("+termExp+")" + SEPARATOR, Pattern.CASE_INSENSITIVE);
		m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1));
			positions.add(pos);
		}
		
		p = caseSensitive ? Pattern.compile(SEPARATOR + "("+termExp+")" + "$") : Pattern.compile(SEPARATOR + "("+termExp+")" + "$", Pattern.CASE_INSENSITIVE);
		m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1));
			positions.add(pos);
		}
		return positions;
	}
}
