package pt.uminho.anote2.aibench.curator.datastructures;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.structures.AnnotatedDocumentProperties;
import pt.uminho.anote2.aibench.corpus.utils.AnnotationPosition;
import pt.uminho.anote2.aibench.corpus.utils.AnnotationPositions;
import pt.uminho.anote2.aibench.curator.utils.ParsingUtils;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.datastructures.annotation.EntityAnnotation;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public class ANoteDocument implements Serializable {
	
	public static final String SEPARATOR = "[ '<>/(),\n\\.]+";

	
	/**
	 * Save clean text and html text of annotated text
	 */
	private ANoteDocumentText documentText;
	
	private ANoteDocumentAnnotations annotations;
	
	/**
	 * Document Structuring 
	 */
	private ANoteDocumentStructure documentStructure;
	
	/**
	 * Save last structure
	 */
	private ANoteDocumentPreviousData previousData;
	
	/**
	 * Document Properties
	 */
	private AnnotatedDocumentProperties properties;
	
	/**
	 * 	Entitty annotations
	 */
	private AnnotationPositions annotNERAnnotations;
	
	/**
	 * Database Prepare Statment
	 * 
	 * @param ner
	 */
	private AnoteDataBAseAnnotationsManagement annotManagenment;
	
	/**
	 * 
	 * @param ner
	 * @throws SQLException
	 */
	private NERDocumentAnnotation ner;
	
	private static final long serialVersionUID = -4753573606938684706L;
	
	public ANoteDocument(NERDocumentAnnotation ner) throws SQLException{
		this.ner=ner;
		annotNERAnnotations = ner.getAnnotationPositions();
		documentStructure = new ANoteDocumentStructure();
		properties = new AnnotatedDocumentProperties(ner,((Corpus) ner.getCorpus()).getCorpora().getDb());
		annotations = new ANoteDocumentAnnotations(ner);
		documentText = new ANoteDocumentText(ner.getTextWhitoutAnnotations(),gerateHtmlForAnnotaions(ner));
		annotManagenment = new AnoteDataBAseAnnotationsManagement(ner);
	}
	
	public boolean addOneAnnotation(AnnotationPosition posC,IEntityAnnotation ent,IResource<IResourceElement> lookuptable)
	{
		IResourceElement elem;
		if(!annotNERAnnotations.getAnnotations().containsKey(posC))
		{
			if(annotNERAnnotations.addAnnotationWhitConflicts(posC, ent))
			{
				if(lookuptable==null)
				{

				}
				else
				{
					elem = new ResourceElement(-1,ent.getAnnotationValue(),ent.getClassAnnotationID(),"");
					int id = getResourceElementWhitNaneANdCLass(elem,lookuptable);
					ent.setResourceElementID(id);
				}
				try {
					annotManagenment.addAnnotation(posC, ent);
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				String termClass = getProperties().getClassIDclass().get(ent.getClassAnnotationID());
				getAnnotations().addAnnotation(ent,termClass);
				ner.setAnnotPositions(annotNERAnnotations);
				ner.notifyViewObservers();
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	public boolean addAllAnnotation(IEntityAnnotation entAnnot,IResource<IResourceElement> lookuptable)
	{
		ResourceElement elem = new ResourceElement(-1,entAnnot.getAnnotationValue(),entAnnot.getClassAnnotationID(),"");

		String cleanTExt = ner.getTextWhitoutAnnotations();
		if(lookuptable==null)
		{
			
		}
		else
		{
			int id = getResourceElementWhitNaneANdCLass(elem,lookuptable);
			entAnnot.setResourceElementID(id);
		}
		List<AnnotationPosition> annot = searchTermInText(entAnnot.getAnnotationValue(),cleanTExt,true);
		String termClass = getProperties().getClassIDclass().get(entAnnot.getClassAnnotationID());
		int nunber=0;
		for(AnnotationPosition pos:annot)
		{
			if(!annotNERAnnotations.getAnnotations().containsKey(pos))
			{

				if(annotNERAnnotations.addAnnotationWhitConflicts(pos, entAnnot))
				{
					try {
						annotManagenment.addAnnotation(pos, entAnnot);
					} catch (SQLException e) {
						e.printStackTrace();
						return false;
					}
					nunber++;
				}
			}
		}
		if(annot.size()!=0)
		{
			getAnnotations().addMoreAnnotion(entAnnot,termClass,nunber);
			ner.setAnnotPositions(annotNERAnnotations);
			ner.notifyViewObservers();
			return true;
		}
		else
		{
			return true;
		}
	}
	
	private List<AnnotationPosition> searchTermInText(String term, String text, boolean caseSensitive){
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
	
	public boolean removeByTerm(String term)
	{
		List<AnnotationPosition> listToRemove = new ArrayList<AnnotationPosition>();
		for(AnnotationPosition pos:annotNERAnnotations.getAnnotations().keySet())
		{	
			IEntityAnnotation elem = (IEntityAnnotation) annotNERAnnotations.getAnnotations().get(pos);
			if(elem.getAnnotationValue().equalsIgnoreCase(term))
			{
				try {
					annotManagenment.removeAnnotation(pos);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				listToRemove.add(pos);
			}
		}
		if(listToRemove.size()==0)
		{
			return false;
		}
		else
		{
			for(AnnotationPosition pos:listToRemove)
			{
				annotNERAnnotations.getAnnotations().remove(pos);
			}
			ner.setAnnotPositions(annotNERAnnotations);
			ner.notifyViewObservers();
			return true;
		}
	}
	
	public int updateAllTerm(String term, int classID) {
		IEntityAnnotation elem=null;
		List<AnnotationPosition> list = new ArrayList<AnnotationPosition>();
		for(AnnotationPosition pos:annotNERAnnotations.getAnnotations().keySet())
		{	
			elem = (IEntityAnnotation) annotNERAnnotations.get(pos);
			if(elem.getAnnotationValue().equalsIgnoreCase(term))
			{
				IEntityAnnotation newElem = new EntityAnnotation(0,pos.getStart(),pos.getEnd(),classID,elem.getResourceElementID(),elem.getAnnotationValue(),elem.getAnnotationValueNormalization());
				annotNERAnnotations.getAnnotations().put(pos, newElem);
				list.add(pos);
			}
		}
		if(list.size()>0)
		{
			try {
				annotManagenment.updateAnnotations(list,classID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		ner.setAnnotPositions(annotNERAnnotations);
		ner.notifyViewObservers();
		return list.size();
	}
	
	public int updateOneTerm(AnnotationPosition posC, int classID) {	
		try {
			List<AnnotationPosition> list = new ArrayList<AnnotationPosition>();
			list.add(posC);
			annotManagenment.updateAnnotations(list,classID);
		} catch (SQLException e) {
			e.printStackTrace();	
		}
		ner.setAnnotPositions(annotNERAnnotations);
		ner.notifyViewObservers();
		return 1;
	}
	
	public boolean removeByPosition(AnnotationPosition posC)
	{
		for(AnnotationPosition pos:annotNERAnnotations.getAnnotations().keySet())
		{
			if(pos.equals(posC))
			{
				annotNERAnnotations.getAnnotations().remove(pos);
				try {
					annotManagenment.removeAnnotation(pos);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				ner.setAnnotPositions(annotNERAnnotations);
				ner.notifyViewObservers();
				return true;
			}
		}
		return false;
	}
	
	public void rebuildHtml(int zoom)
	{
		String newHtml = textAsHtml(ner.getTextWhitoutAnnotations(), annotNERAnnotations);
		newHtml = documentStructure.complexTagToHtml(newHtml);
		newHtml = newHtml.replaceAll("\\r",".");
		newHtml = newHtml.replaceAll("\\n",".");
		newHtml = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:"+zoom+"pt\" align=\"justify\">" + newHtml + "</DIV>";
		documentText.setHtmlText(newHtml);
	}
	
	public String textAsHtml(String text, AnnotationPositions annotationsPositions){
		StringBuffer htmlText = new StringBuffer();
		int pointer=0;
		
		for(AnnotationPosition pos : annotationsPositions.getAnnotations().keySet())
		{
			IEntityAnnotation annot = (IEntityAnnotation) annotationsPositions.getAnnotations().get(pos);
			if(pointer<pos.getStart())
				htmlText.append(text.substring(pointer, pos.getStart()));			
			String entity = "<FONT COLOR=" + properties.getTasks().get(properties.getClassIDclass().get(annot.getClassAnnotationID())).getColor() + "><b>";
//			if(annotationsPositions.getAnnotations().get(pos).getID()!=0)
//				span += " id='" + annotationsPositions.getAnnotations().get(pos).getID() + "'";
//			else if(annotationsPositions.getAnnotations().get(pos).getTerm()!=null)
//				span += " cname='" + annotationsPositions.getAnnotations().get(pos).getTerm() + "'";
//			span += ">";
			htmlText.append(entity);
			htmlText.append(text.substring(pos.getStart(), pos.getEnd()));
			htmlText.append("</b></FONT>");
			pointer = pos.getEnd();
		}
		if(pointer<text.length())
			htmlText.append(text.substring(pointer, text.length()));
		return htmlText.toString();
	}
	
	private String gerateHtmlForAnnotaions(NERDocumentAnnotation nerDoc) {		
		String htmlText = textAsHtml(nerDoc.getTextWhitoutAnnotations(),annotNERAnnotations);
		htmlText = documentStructure.complexTagToHtml(htmlText);
		htmlText = htmlText.replaceAll("\\r",".");
		htmlText = htmlText.replaceAll("\\n",".");
		//htmlText = htmlText.replaceAll("\\s","\b");
		htmlText = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:12pt\" align=\"justify\">" + htmlText + "</DIV>";
		return htmlText;
	}

	
//	public String saveRelation(String text){
//		String result = text;
//		String xmlRelation;
//		for(String relation : annotations.getRelations())
//		{
//			xmlRelation = documentText.captureAnnotations(relation, annotations, null);
//			result = result.replace(xmlRelation, "<relation>" + xmlRelation + "</relation>");
//		}
//		return result;
//	}
		
//	public File getFile() {return file;}
//	public void setFile(File file) {this.file = file;}
//	public String getName() {return name;}
//	public void setName(String name) {this.name = name;}
	public AnnotatedDocumentProperties getProperties() {
		return properties;
	}

	public AnnotationPositions getAnnotNERAnnotations() {
		return annotNERAnnotations;
	}
	
	public int getResourceElementWhitNaneANdCLass(IResourceElement elem,IResource<IResourceElement> dic)
	{	
		int nextResourceID = HelpDatabase.getNextInsertTableID(dic.getDb(), "resource_elements");
		if(dic.addElement(elem))
		{
			if(!dic.getClassContent().contains(elem.getTermClassID()))
			{
				dic.addResourceContent(elem.getTermClassID());
			}
			return nextResourceID;
		}
		else{
			return dic.getFirstTermByName(elem.getTerm()).getID();
		}
	}
	
	
	public ANoteDocumentText getDocumentText() {return documentText;}
	public void setDocumentText(ANoteDocumentText documentText) {this.documentText = documentText;}
	public ANoteDocumentAnnotations getAnnotations() {return annotations;}
	public void setAnnotations(ANoteDocumentAnnotations annotations) {this.annotations = annotations;}
	public ANoteDocumentStructure getDocumentStructure() {return documentStructure;}
	public void setDocumentStructure(ANoteDocumentStructure documentStructure) {this.documentStructure = documentStructure;}
	public ANoteDocumentPreviousData getPreviousData() {return previousData;}
	public void setPreviousData(ANoteDocumentPreviousData previousData) {this.previousData = previousData;}




}
