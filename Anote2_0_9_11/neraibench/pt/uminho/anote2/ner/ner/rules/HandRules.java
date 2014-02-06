package pt.uminho.anote2.ner.ner.rules;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.aibench.ner.datastructures.ElementToNer;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ITextSegment;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.annotation.ner.EntityAnnotation;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.resource.IResourceElement;


public class HandRules {
	private ElementToNer elementsToNER;
	
	public HandRules(ElementToNer elementsToNER){
		this.elementsToNER = elementsToNER;
	}
	
	/** annotations is the list of the already existing annotations 
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException */
	public void applyRules(String text, AnnotationPositions annotations, String separator) throws SQLException, DatabaseLoadDriverException{
		if(elementsToNER.isUsingOtherResourceInfoToImproveRuleAnnotstions() && elementsToNER.getResourcesForRulesInfo().size() > 0)
		{
			HandRulesExtraRulesInformation extraInfo = new HandRulesExtraRulesInformation(elementsToNER.getResourcesForRulesInfo(),elementsToNER.getResourceToNER().isCaseSensitive());
			for(IResourceElement rule : elementsToNER.getRules())
			{
				applyRule(extraInfo,rule, annotations, text, separator);
			}
		}
		else
		{
			for(IResourceElement rule : elementsToNER.getRules())
			{
				applyRule(rule, annotations, text, separator);
			}
		}
	}
	


	/** annotations is the list of the already existing annotations 
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException */
	public void applyRules(ITextSegment text, AnnotationPositions annotations, String separator) throws SQLException, DatabaseLoadDriverException{
		if(elementsToNER.isUsingOtherResourceInfoToImproveRuleAnnotstions() && elementsToNER.getResourcesForRulesInfo().size() > 0)
		{
			HandRulesExtraRulesInformation extraInfo = new HandRulesExtraRulesInformation(elementsToNER.getResourcesForRulesInfo(),elementsToNER.getResourceToNER().isCaseSensitive());
			for(IResourceElement rule : elementsToNER.getRules())
			{
				applyRule(extraInfo,rule, annotations, text, separator);
			}
		}
		else
		{
			for(IResourceElement rule : elementsToNER.getRules())
			{
				applyRule(rule, annotations, text, separator);
			}
		}
	}

	public void applyRule(IResourceElement handRule, AnnotationPositions annotations, ITextSegment segment, String separator){
		
		String rule = handRule.getTerm();
		String text = segment.getText();
		Pattern p = Pattern.compile("("+rule+")");
		Matcher m = p.matcher(text);
		
		while(m.find())
		{
			updateRule(handRule, annotations, segment, m);
		}
	}
	
	private void applyRule(HandRulesExtraRulesInformation extraInfo,IResourceElement handRule, AnnotationPositions annotations,ITextSegment segment, String separator) throws SQLException, DatabaseLoadDriverException {
		String rule = handRule.getTerm();
		String text = segment.getText();
		Pattern p = Pattern.compile("("+rule+")");
		Matcher m = p.matcher(text);	
		while(m.find())
		{
			if(m.groupCount() > 1)
			{
				for(int i=2;i<=m.groupCount();i++)
				{
					if(m.start(i) > 0 && m.end(i) > 0)
					{
						AnnotationPosition pos = new AnnotationPosition(m.start(i)+(int)segment.getStartOffset(), m.end(i)+ (int)segment.getStartOffset());
						String term = segment.getText().substring(m.start(i),m.end(i));			
						IEntityAnnotation annotation = getEntityAnnotationTextSegment(extraInfo,handRule, segment, pos, term);
						annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMoreRule(pos, annotation);
					}
				}
			}
			else
			{
				AnnotationPosition pos = new AnnotationPosition(m.start(1)+(int)segment.getStartOffset(), m.end(1)+ (int)segment.getStartOffset());
				String term = segment.getText().substring(m.start(1),m.end(1));			
				IEntityAnnotation annotation = getEntityAnnotationTextSegment(extraInfo,handRule, segment, pos, term);
				annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMoreRule(pos, annotation);
			}
		}
		
	}

	private void updateRule(IResourceElement handRule,AnnotationPositions annotations, ITextSegment segment, Matcher m) {
		if(m.groupCount() > 1)
		{
			for(int i=2;i<=m.groupCount();i++)
			{
				if(m.start(i) > 0 && m.end(i) > 0)
				{
					AnnotationPosition pos = new AnnotationPosition(m.start(i)+(int)segment.getStartOffset(), m.end(i)+ (int)segment.getStartOffset());
					String term = segment.getText().substring(m.start(i),m.end(i));			
					IEntityAnnotation annotation = new EntityAnnotation(handRule.getID(), pos.getStart()+segment.getStartOffset(), pos.getEnd()+segment.getStartOffset(),
							handRule.getTermClassID(),handRule.getID(), term,NormalizationForm.getNormalizationForm(term));
					annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMoreRule(pos, annotation);
				}
			}
		}
		else
		{
			AnnotationPosition pos = new AnnotationPosition(m.start(1)+(int)segment.getStartOffset(), m.end(1)+ (int)segment.getStartOffset());
			String term = segment.getText().substring(m.start(1),m.end(1));			
			IEntityAnnotation annotation = new EntityAnnotation(handRule.getID(), pos.getStart()+segment.getStartOffset(), pos.getEnd()+segment.getStartOffset(),
					handRule.getTermClassID(),handRule.getID(), term,NormalizationForm.getNormalizationForm(term));
			annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMoreRule(pos, annotation);
		}
	}
	
	public void applyRule(IResourceElement handRule, AnnotationPositions annotations, String text, String separator){
		
		String rule = handRule.getTerm();
		
		Pattern p = Pattern.compile("("+rule+")");
		Matcher m = p.matcher(text);

		while(m.find())
		{
			if(m.groupCount() > 1)
			{

				for(int i=2;i<=m.groupCount();i++)
				{
					if(m.start(i) > 0 && m.end(i) > 0)
					{
						AnnotationPosition pos = new AnnotationPosition(m.start(i), m.end(i));
						String term = text.substring(m.start(i),m.end(i));			
						IEntityAnnotation annotation = new EntityAnnotation(handRule.getID(), pos.getStart(), pos.getEnd(),handRule.getTermClassID(),handRule.getID(), term,NormalizationForm.getNormalizationForm(term));
						annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMoreRule(pos, annotation);
					}
				}
			}
			else
			{
				AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1));
				String term = text.substring(m.start(1),m.end(1));			
				IEntityAnnotation annotation = new EntityAnnotation(handRule.getID(), pos.getStart(), pos.getEnd(),handRule.getTermClassID(),handRule.getID(), term,NormalizationForm.getNormalizationForm(term));
				annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMoreRule(pos, annotation);
			}
		}
	}
	
	/** For getting resource aditional infomation 
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException */
	private void applyRule(HandRulesExtraRulesInformation extraInfo,IResourceElement handRule, AnnotationPositions annotations,String text, String separator) throws SQLException, DatabaseLoadDriverException {
		// TODO Auto-generated method stub
		String rule = handRule.getTerm();
		
		Pattern p = Pattern.compile("("+rule+")");
		Matcher m = p.matcher(text);

		while(m.find())
		{
			if(m.groupCount() > 1)
			{

				for(int i=2;i<=m.groupCount();i++)
				{
					if(m.start(i) > 0 && m.end(i) > 0)
					{
						AnnotationPosition pos = new AnnotationPosition(m.start(i), m.end(i));
						String term = text.substring(m.start(i),m.end(i));
						IEntityAnnotation annotation = getEntityAnnotation(extraInfo,handRule, pos, term);
						annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMoreRule(pos, annotation);
					}
				}
			}
			else
			{
				AnnotationPosition pos = new AnnotationPosition(m.start(1), m.end(1));
				String term = text.substring(m.start(1),m.end(1));			
				IEntityAnnotation annotation = getEntityAnnotation(extraInfo,handRule,pos, term);
				annotations.addAnnotationWhitConflitsAndReplaceIfRangeIsMoreRule(pos, annotation);
			}
		}
	}

	private IEntityAnnotation getEntityAnnotation(HandRulesExtraRulesInformation extraInfo, IResourceElement handRule,AnnotationPosition pos, String term) throws SQLException, DatabaseLoadDriverException {
		int resourceInfoNumber = extraInfo.getResourceExtraInfoID(handRule,term);
		if(resourceInfoNumber == -1)
		{
			return new EntityAnnotation(handRule.getID(), pos.getStart(), pos.getEnd(),handRule.getTermClassID(),handRule.getID(), term,NormalizationForm.getNormalizationForm(term));
		}
		else
		{
			return new EntityAnnotation(resourceInfoNumber, pos.getStart(), pos.getEnd(),handRule.getTermClassID(),resourceInfoNumber, term,NormalizationForm.getNormalizationForm(term));
		}
	}
	
	private IEntityAnnotation getEntityAnnotationTextSegment(HandRulesExtraRulesInformation extraInfo, IResourceElement handRule, ITextSegment segment,AnnotationPosition pos, String term) throws SQLException, DatabaseLoadDriverException {
		int resourceInfoNumber = extraInfo.getResourceExtraInfoID(handRule,term);
		if(resourceInfoNumber == -1)
		{
			return new EntityAnnotation(handRule.getID(), pos.getStart()+segment.getStartOffset(), pos.getEnd()+segment.getStartOffset(),
					handRule.getTermClassID(),handRule.getID(), term,NormalizationForm.getNormalizationForm(term));
		}
		else
		{
			return new EntityAnnotation(resourceInfoNumber, pos.getStart()+segment.getStartOffset(), pos.getEnd()+segment.getStartOffset(),
					handRule.getTermClassID(),resourceInfoNumber, term,NormalizationForm.getNormalizationForm(term));
		}
	}
}
