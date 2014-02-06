package pt.uminho.anote2.aibench.curator.datastructures;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.utils.AnnotationPosition;
import pt.uminho.anote2.aibench.corpus.utils.OrderedMap;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.datastructures.annotation.AnnotationColorStyleProperty;
import pt.uminho.anote2.datastructures.annotation.EntityAnnotation;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;

public class ANoteDocumentAnnotations implements Serializable {

	private static final long serialVersionUID = 9027206399419826768L;

	/** Key: Entity Class
	 * Values: Key: Term, Values: Term information */
	private Map<String,HashMap<String,ANoteAnnotation>> classTermAnnotations;
	
	/** Map ordered by the size of the term, this map associates the term with its class */
	private OrderedMap<String,String> sortTermClass;

	/** List with the relations in the text. The list contains the clear text corresponding to the relations */
	private List<String> relations;
	
	@SuppressWarnings("unchecked")
	public ANoteDocumentAnnotations(){
		this.classTermAnnotations = new HashMap<String, HashMap<String,ANoteAnnotation>>();
		@SuppressWarnings("rawtypes")
		TermComparator comparator = new TermComparator<Object>();
		this.sortTermClass = new OrderedMap<String, String>(comparator);
		this.relations = new ArrayList<String>();
	}
	
	public ANoteDocumentAnnotations(Map<String, HashMap<String,ANoteAnnotation>> annotations, OrderedMap<String,String> term_tag, List<String> relations){
		this.classTermAnnotations = annotations;
		this.sortTermClass = term_tag;
		this.relations = relations;
	}
	
	@SuppressWarnings("unchecked")
	public ANoteDocumentAnnotations(NERDocumentAnnotation nerDocAnnot){
		this.classTermAnnotations = new HashMap<String, HashMap<String,ANoteAnnotation>>();
		@SuppressWarnings("rawtypes")
		TermComparator comparator = new TermComparator<Object>();
		this.sortTermClass = new OrderedMap<String, String>(comparator);
		this.relations = new ArrayList<String>();
		fillTermStructures(nerDocAnnot);
	}

	
	private void fillTermStructures(NERDocumentAnnotation nerDocAnnot) {
		
		Map<Integer, String> classIDclass = null;
		try {
			classIDclass = ResourcesHelp.getClassIDClassOnDatabase(((Corpus)nerDocAnnot.getCorpus()).getCorpora().getDb());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(AnnotationPosition pos:nerDocAnnot.getAnnotationPositions().getAnnotations().keySet())
		{
			IEntityAnnotation entAnnot = (IEntityAnnotation) nerDocAnnot.getAnnotationPositions().getAnnotations().get(pos);
			String entityName = entAnnot.getAnnotationValue();
			String classe =  classIDclass.get(entAnnot.getClassAnnotationID());		
			ANoteAnnotation annotAnnot = new ANoteAnnotation(1,entityName,entityName,"", "",entityName);
			if(classTermAnnotations.containsKey(classe))
			{
				if(classTermAnnotations.get(classe).containsKey(entityName))
				{
					classTermAnnotations.get(classe).get(entityName).incCount();
				}
				else
				{
					classTermAnnotations.get(classe).put(entityName, annotAnnot);
				}	
			}
			else
			{
				HashMap<String,ANoteAnnotation> hash = new HashMap<String, ANoteAnnotation>();
				if(hash.containsKey(entityName))
				{
					hash.get(entityName).incCount();
				}
				else
				{
					hash.put(entityName,annotAnnot);
					classTermAnnotations.put(classe,hash);
				}
			}
			sortTermClass.put(entityName, classe);
		}
	}

//	/** Get the terms annotated in the document */
//	@SuppressWarnings("unchecked")
//	public OrderedMap<String,String> textAnnotatedTerms(String text){
//		String span = "<span class='(.+?)'.*?>(.+?)</span>";
//		Pattern pattern = Pattern.compile(span);
//		Matcher m = pattern.matcher(text);
//		
//		TermComparator comparator = new TermComparator<Object>();
//		OrderedMap<String,String> annotatedTerms = new OrderedMap<String, String>(comparator);
//		
//		while(m.find())
//		{
//			String termClass = m.group(1);
//			String term = m.group(2);
//			if(!annotatedTerms.containsKey(term))
//				annotatedTerms.put(term, termClass);
//		}
//		return annotatedTerms;
//	}
	
//	/** Get the annotations in the stat files and add them to the map received */
//	public void statAnnotatedTerms(File documentFile, OrderedMap<String,String> statAnnotatedTerms) throws IOException{
//		String path = documentFile.getPath();
//		File statsFile;
//		String statsText = null;
//		if((statsFile = new File(path.replace(".xml", ".stat2"))).exists())
//			statsText = FileHandling.textFromFile(statsFile);
//		else if ((statsFile = new File(path.replace(".xml", ".stat"))).exists())
//			statsText = FileHandling.textFromFile(statsFile);
//	
//		if(statsText != null)
//		{
//			String regexp = "<statistic>\n<term>~(.+?)</term>\n<class>(.+?)</class>\n<occurs>.+?</occurs>\n</statistic>";
//			Pattern p1 = Pattern.compile(regexp);
//			Matcher m1 = p1.matcher(statsText);
//			
//			while(m1.find())
//				statAnnotatedTerms.put(m1.group(1), m1.group(2));
//		}
//	}
	
//	/** Parse the relations in the text, and converts them to Html  */
//	public String parseRelations(String xmlText){
//		String htmlText = xmlText;
//		Pattern p = Pattern.compile("<relation>(.+?)</relation>");
//		Matcher m = p.matcher(xmlText);
//		
//		while(m.find())
//		{
//			htmlText = htmlText.replaceAll("<relation>[ ]{0,1}" + ParsingUtils.textToRegExp(m.group(1)) + "[ ]{0,1}</relation>", "<FONT style=\"BACKGROUND-COLOR: yellow\"> " + m.group(1) + " </FONT>");
//			addRelation(m.group(1), false);
//		}
//				
//		return htmlText;
//	}
	
//	public String getDetails(String s, String term){
//		String exp = "class='(.+?)' id='(.*?)'";
//		Pattern pattern = Pattern.compile(exp);
//		Matcher m = pattern.matcher(s);
//		ANoteAnnotation annotation=null;
//		String termClass=null;
//		
//		if(m.find())
//		{
//			String id = null;
//			if(!m.group(2).equals(""))
//				id = m.group(2);
//			annotation = new ANoteAnnotation(1, term, term, id, null, null);
//			termClass = m.group(1);
//		}
//		else
//		{
//			exp = "class='(.+?)' title='(.+?)'";
//			pattern = Pattern.compile(exp);
//			m = pattern.matcher(s);
//			if(m.find())
//			{
//				annotation = new ANoteAnnotation(1, term, term, null, m.group(2), null);
//				termClass = m.group(1);
//			}
//			else
//			{
//				exp = "class='(.+?)' lemma='(.+?)'";
//				pattern = Pattern.compile(exp);
//				m = pattern.matcher(s);
//				if(m.find())
//				{
//					annotation = new ANoteAnnotation(1, term, term, null, null, m.group(2));
//					termClass = m.group(1);
//				}
//				else
//				{
//					exp = "class='(.+?)'";
//					pattern = Pattern.compile(exp);
//					m = pattern.matcher(s);
//					if(m.find())
//					{
//						annotation = new ANoteAnnotation(1, term, term, null, null, null);
//						termClass = m.group(1);
//					}
//				}
//			}
//		}
//		addAnnotation(annotation, termClass);
//		return termClass;
//	}
	
	public void addAnnotation(IEntityAnnotation elem, String termClass){
		String entity = elem.getAnnotationValue();
		HashMap<String,ANoteAnnotation> map = this.classTermAnnotations.get(termClass);
		ANoteAnnotation annot = new ANoteAnnotation(1,entity,entity,String.valueOf(elem.getID()),entity,entity);
		if(map==null)
		{
			map = new HashMap<String, ANoteAnnotation>();
			this.classTermAnnotations.put(termClass, map);
		}
		else
		{
			if(map.containsKey(entity))
			{
				map.get(entity).incCount();
			}
			else
			{
				map.put(entity, annot);	
			}
		}
		this.sortTermClass.put(entity, termClass);
	}
	
	public void addMoreAnnotion(IEntityAnnotation elem, String termClass,int numberTimes){
		String entity = elem.getAnnotationValue();
		HashMap<String,ANoteAnnotation> map = this.classTermAnnotations.get(termClass);
		ANoteAnnotation annot = new ANoteAnnotation(1,entity,entity,String.valueOf(elem.getID()),entity,entity);
		if(map==null)
		{
			map = new HashMap<String, ANoteAnnotation>();
			this.classTermAnnotations.put(termClass, map);
		}
		if(map.containsKey(entity))
		{
			map.get(entity).incCount(numberTimes);
		}
		else
		{
			map.put(entity, annot);
			map.get(entity).incCount(numberTimes-1);		
		}
		this.sortTermClass.put(entity, termClass);
	}
	
	/** Removes an annotation if it exists. If so, it will also return the previous class of the removed term */
	public void removeOneAnnotation(String term){
		
		String termClass = null;
		if(this.sortTermClass.containsKey(term))
		{
			termClass = this.sortTermClass.get(term);
			HashMap<String,ANoteAnnotation> terms = this.classTermAnnotations.get(termClass);
			if(terms.get(term).getCount()==1)
			{
				sortTermClass.remove(term);
				terms.remove(term);
				if(terms.size()==0)
					this.classTermAnnotations.remove(termClass);
			}
			else
			{
				terms.get(term).decCount();
			}
		}	
	}
	
	public void removeAllAnnotation(String term){
		
		String termClass = null;
		if(this.sortTermClass.containsKey(term))
		{
			termClass = this.sortTermClass.get(term);
			this.sortTermClass.remove(term);
			HashMap<String,ANoteAnnotation> terms = this.classTermAnnotations.get(termClass);
			terms.remove(term);
			if(terms.size()==0)
				this.classTermAnnotations.remove(termClass);
		}	
	}
	
	public void correctAnnotation(String term, String termClass,int resourceTErmID,int numberTimes,ANoteDocument doc){
		removeAllAnnotation(term);
		addMoreAnnotion(new EntityAnnotation(0,0,0,doc.getProperties().getClassClassID().get(termClass),resourceTErmID,term,NormalizationForm.getNormalizationForm(term)),termClass,numberTimes);
	}

	
	public void updateCount(String term, String termClass){
		Map<String,ANoteAnnotation> map = this.classTermAnnotations.get(termClass);
		ANoteAnnotation annotation = map.get(term);
		annotation.incCount();
	}
	
	public String filterTags(String htmlText, List<String> tags, Map<String,AnnotationColorStyleProperty> tasks){
		String resultext = htmlText, token, newTag;
				
		for(String tag: classTermAnnotations.keySet())
		{
			if(!tags.contains(tag))
			{	
				token = "<FONT COLOR=" + tasks.get(tag).getColor() + "><b>";
				newTag = "<!--<FONT COLOR=" + tasks.get(tag).getColor() + "><b>-->";
				resultext = resultext.replaceAll(token, newTag);
			}
			else
			{
				newTag = "<FONT COLOR=" + tasks.get(tag).getColor() + "><b>";
				token = "<!--<FONT COLOR=" + tasks.get(tag).getColor() + "><b>-->";
				resultext = resultext.replaceAll(token, newTag);
			}
		}
		return resultext;
	}
	
//	public String changeHtmlColors(String htmlText, Map<String,ANoteProperty> oldTasks , Map<String,ANoteProperty> newTasks){
//		
//		String resultext = htmlText, token, newTag;
//		
//		for(String tag: classTermAnnotations.keySet())
//		{
//			token = "<FONT COLOR=" + oldTasks.get(tag).getColor() + "><b>";
//			newTag = "<FONT COLOR=" + newTasks.get(tag).getColor() + "><b>";
//			resultext = resultext.replaceAll(token, newTag);
//		}
//				
//		return resultext;
//	}
	
	
	public ANoteDocumentAnnotations clone(){
		Map<String,HashMap<String,ANoteAnnotation>> cloneClassTermAnnotations = new HashMap<String, HashMap<String,ANoteAnnotation>>();
		OrderedMap<String,String> cloneOrderedTermClass = new OrderedMap<String, String>(this.sortTermClass.getComparator());
		List<String> cloneRelations = new ArrayList<String>();
		
		for(String cls: this.classTermAnnotations.keySet())
		{
			HashMap<String,ANoteAnnotation> classAnnotations = new HashMap<String, ANoteAnnotation>();
			for(String term: this.classTermAnnotations.get(cls).keySet())
			{
				ANoteAnnotation annotation = this.classTermAnnotations.get(cls).get(term).clone();
				classAnnotations.put(term, annotation);
			}
			cloneClassTermAnnotations.put(cls,classAnnotations);
		}
		
		for(String term : this.sortTermClass.keySet())
			cloneOrderedTermClass.put(term, this.sortTermClass.get(term));
		
		for(String relation : this.relations)
			cloneRelations.add(relation);
		
		return new ANoteDocumentAnnotations(cloneClassTermAnnotations, cloneOrderedTermClass, cloneRelations);
	}
	
	public void addRelation(String text, boolean raw){
		String relation = text;
		if(!raw)
			relation = relation.replaceAll("<[^,.: -]*?>", "");
		this.relations.add(relation);
	}
	

	
	public Map<String, HashMap<String,ANoteAnnotation>> getClassTermAnnotations() {return classTermAnnotations;}
	public void setClassTermAnnotations(Map<String, HashMap<String,ANoteAnnotation>> annotations) {this.classTermAnnotations = annotations;}
	public OrderedMap<String, String> getTermClass() {return sortTermClass;}
	public void setTermClass(OrderedMap<String, String> term_tag) {this.sortTermClass = term_tag;}
	public List<String> getRelations() {return relations;}
	public void setRelations(List<String> relations) {this.relations = relations;}
}
