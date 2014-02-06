package pt.uminho.anote2.ner.ner.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.datastructures.annotation.EntityAnnotation;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.ner.annotations.AnnotationPosition;
import pt.uminho.anote2.ner.ner.AnnotationPositions;
import pt.uminho.anote2.resource.IResourceElement;


public class HandRules {
	private List<IResourceElement> handRules;
	
	public HandRules(List<IResourceElement> rules){
		this.handRules = rules;
	}
	
	public HandRules(){
		this.handRules = new ArrayList<IResourceElement>();
	}
	
	/** annotations is the list of the already existing annotations */
	public void applyRules(String text, AnnotationPositions annotations, String separator){
		
		for(IResourceElement rule : handRules)
		{
			applyRule(rule, annotations, text, separator);
		}
	}
	
	public void applyRule(IResourceElement handRule, AnnotationPositions annotations, String text, String separator){
		
		String rule = handRule.getTerm();
		
		Pattern p = Pattern.compile(separator + "("+rule+")" + separator);
		Matcher m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1));
			String term = text.substring(m.start(1),m.end(1));			
			IEntityAnnotation annotation = new EntityAnnotation(handRule.getID(), pos.getStart(), pos.getEnd(),handRule.getTermClassID(),handRule.getID(), term,NormalizationForm.getNormalizationForm(term));
			annotations.addAnnotation(pos, annotation);
		}
		
		p = Pattern.compile("^" + "("+rule+")" + separator);
		m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1));
			String term = text.substring(m.start(1),m.end(1));
			IEntityAnnotation annotation = new EntityAnnotation(handRule.getID(), pos.getStart(), pos.getEnd(),handRule.getTermClassID(),handRule.getID(), term,NormalizationForm.getNormalizationForm(term));
			annotations.addAnnotation(pos, annotation);
		}
		
		p = Pattern.compile(separator + "("+rule+")" + "$");
		m = p.matcher(text);
		
		while(m.find())
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1));
			String term = text.substring(m.start(1),m.end(1));
			IEntityAnnotation annotation = new EntityAnnotation(handRule.getID(), pos.getStart(), pos.getEnd(),handRule.getTermClassID(),handRule.getID(), term,NormalizationForm.getNormalizationForm(term));
			annotations.addAnnotation(pos, annotation);
		}
	}
}
