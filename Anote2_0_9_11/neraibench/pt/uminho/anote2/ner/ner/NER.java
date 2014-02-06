package pt.uminho.anote2.ner.ner;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.textprocessing.ParsingUtils;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.ner.ner.rules.HandRules;

public class NER {

	private List<IEntityAnnotation> elements;	
	private HandRules rules;
	protected static final String SEPARATOR = "[ '<>/(),\n\\.]+";
	private boolean stop = false;
		
	public NER(List<IEntityAnnotation> termAnnotations){
		this.elements = termAnnotations;
		this.rules = null;
	}
	
	public NER(List<IEntityAnnotation> termAnnotations, HandRules rules){
		this.elements = termAnnotations;
		this.rules = rules;
	}
	
	
	public AnnotationPositions executeNer(String text,List<Integer> listClassIDCaseSensative,boolean caseSensitive) throws SQLException, DatabaseLoadDriverException{
		AnnotationPositions annotations = new AnnotationPositions();
		boolean caseSensitiveOption;
		if(stop)
		{
			return new AnnotationPositions();
		}
		for(IEntityAnnotation termAnnot : elements)
		{
			if(stop)
			{
				return new AnnotationPositions();
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
				annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMore(pos,termAnnot);
			}
		}
		if(rules != null && !stop)
			rules.applyRules(text, annotations, SEPARATOR);	
		return annotations;
	}

	
	public List<IEntityAnnotation> getElementsToNER() {
		return elements;
	}

	public HandRules getRules() {
		return rules;
	}

	protected List<AnnotationPosition> searchTermInText(String term, String text, boolean caseSensitive){
		List<AnnotationPosition> positions = new ArrayList<AnnotationPosition>();
		String termExp = ParsingUtils.textToRegExp(term);
		
		Pattern p = caseSensitive ? Pattern.compile(SEPARATOR + "("+termExp+")" + SEPARATOR) : Pattern.compile(SEPARATOR + "("+termExp+")" + SEPARATOR, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1),text.substring(m.start(1), m.end(1)));
			positions.add(pos);
		}
		
		p = caseSensitive ? Pattern.compile("^" + "("+termExp+")" + SEPARATOR) : Pattern.compile("^" + "("+termExp+")" + SEPARATOR, Pattern.CASE_INSENSITIVE);
		m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1),text.substring(m.start(1), m.end(1)));
			positions.add(pos);
		}
	
		return positions;
	}
	
	public void setDictionary(List<IEntityAnnotation> dictionary) {
		this.elements = dictionary;
	}

	public void setRules(HandRules rules) {
		this.rules = rules;
	}

	public void stop() {
		stop = true;		
	}

	public Properties getProperties(ResourcesToNerAnote resources,boolean normalization) {
		return configureProperties(resources,normalization) ;
	}
	
	
	private Properties configureProperties(ResourcesToNerAnote resources,boolean normalization) {
		Properties prop = transformResourcesToOrderMapInProperties(resources);
		if(normalization)
		{
			prop.put(GlobalNames.normalization,String.valueOf(normalization));
		}
		if(resources.isCaseSensitive())
		{
			prop.put(GlobalNames.casesensitive,"true");
		}
		else
		{
			prop.put(GlobalNames.casesensitive,"false");
		}
		if(resources.isUseOtherResourceInformationInRules())
		{
			prop.put(GlobalNames.useOtherResourceInformationInRules,"true");
		}
		preprocessing(prop);
		return prop;
	}
	
	public void preprocessing(Properties prop) {
		prop.put(GlobalNames.nerpreProcessing,GlobalNames.nerpreProcessingNo);
	}

	private static Properties transformResourcesToOrderMapInProperties(ResourcesToNerAnote resources) {
		Properties prop = new Properties();
		for(int i=0;i<resources.getList().size();i++)
		{
			Set<Integer> selected = resources.getList().get(i).getZ();
			int id = resources.getList().get(i).getX().getID();
			{
				prop.put(String.valueOf(id),ResourcesHelp.convertClassesToResourceProperties(selected));
			}
		}
		return prop;
	}
	

}
