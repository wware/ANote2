package pt.uminho.anote2.aibench.curator.datastructures;

import java.io.Serializable;

public class ANoteDocumentText implements Serializable {

	private static final long serialVersionUID = 1908339149347314619L;

	public static final int SLIDING_WINDOW_SIZE = 7;
	
	private String clearText;
	private String htmlText;
	
	public ANoteDocumentText(String cleaText,String html)
	{
		this.clearText=cleaText;
		this.htmlText=html;
	}
		
//	/** Constructor that receives a xml text, a Map with the terms and its class and the 
//	 * properties of the classes. Converts it to a clean text and html text */
//	public ANoteDocumentText(String xmlText, Map<String,ANoteProperty> tasks) throws IOException{
//		this.htmlText = xmlToHtml(xmlText, tasks);
//	}

	
//	public String rawToHtml(String text, OrderedMap<String,String> termTag, Map<String,ANoteProperty> tasks){
//		String htmlText = text, htmlTerm;
//		
//		for(String term: termTag.keySet())
//		{
//			htmlTerm = "<FONT COLOR=" + tasks.get(termTag.get(term)).getColor() + "><b>" + term + "</b></FONT>";
//			htmlText = htmlText.replaceAll(term, htmlTerm);
//		}		
//		return htmlText;
//	}
	
//	public String xmlToRaw(String xmlText){
//		String rawText = xmlText.replaceAll("<[^,.:-]+?>", "");
//		return rawText;
//	}
	
//	public String xmlToHtml(String xmlText, Map<String,ANoteProperty> tasks){
//		String htmlText = xmlText;
//		htmlText = htmlText.replace("<PARAGRAPH>", "<br/>");
//		htmlText = htmlText.replace("</PARAGRAPH>", "<br/>");
//			
//		htmlText = htmlText.replace("</span>", "</b></FONT>");
//			
//		Pattern pattern = Pattern.compile("<span class='(.+?)'" + "[^>]*>");
//		Matcher m = pattern.matcher(htmlText);
//
//		int start=0, end=0;
//		StringBuffer str = new StringBuffer();
//		String xmlTag, htmlTag;
//			
//		while(m.find())
//		{
//			xmlTag = htmlText.substring(m.start(), m.end());
//			htmlTag = "<FONT COLOR=" + tasks.get(m.group(1)).getColor() + "><b>";
//			start = end;
//			end = m.end();
//			str.append(htmlText.substring(start,end).replace(xmlTag,htmlTag));
//		}
//		if(end<htmlText.length())
//			str.append(htmlText.substring(end, htmlText.length()));
//		
//		return str.toString();
//	}
//	
//	/** Counts the occurrence of the term in the text. This method search the term in the clear text */
//	public int countOccurrence(String term){
//		return countOccurrence(term, clearText);		
//	}
//	
//	/** Counts the occurrence of the term in the text. This method search the term in the given text */
//	public int countOccurrence(String term, String text){
//		
//		if(term.equals(text))
//			return 1;
//		
//		String termExp = ParsingUtils.textToRegExp(term);
//		int count = 0;
//		
//		Pattern p = Pattern.compile("[ -']+" + termExp + "[ -']+");
//		Matcher m = p.matcher(text);
//		
//		while(m.find())
//			count++;
//		
//		p = Pattern.compile("^" + termExp + "[ -']+");
//		m = p.matcher(text);
//		
//		while(m.find())
//			count++;
//		
//		p = Pattern.compile("[ -']+" + termExp + "$");
//		m = p.matcher(text);
//		
//		while(m.find())
//			count++;
//		
//		return count;
//	}
//	
////	/** Return the given text with the annotations tagged
////	 * If tasks is null, the returned text will be a XML text, else it will be a HTML*/
//	public String captureAnnotations(String text, ANoteDocumentAnnotations annotations, Map<String,ANoteProperty> tasks){
//		if(text.length() == 0)
//			return null;
//		
//		StringBuffer html_buffer = new StringBuffer();
//		String subString, htmlText;
//		int initPointer, endPointer, aux; // these pointers defines the current Slide Window size
//		
//		/* Initiate Pointers */
//		initPointer = 0;
//		endPointer = indexEndSpace(text,0);
//		
//		while(initPointer!=-1 && initPointer<text.length())
//		{
//			if( (aux=initPointer)!=0)
//				aux++;
//			subString = text.substring(aux, endPointer);
//					
//			if(annotations.getTermClass().containsKey(subString)) // If the text contains the sub_string, init_pointer go to the end of sub_string and the end_pointer is refreshed 
//			{
//				initPointer += subString.length() + 1; // +1 to sum one space that is between substring and the rest of the text
//				endPointer = indexEndSpace(text, initPointer);
//				
//				String tag = annotations.getTermClass().get(subString);
//				if(tasks==null)
//					html_buffer.append(putXtmlTag(subString, tag, annotations));
//				else
//					html_buffer.append(putHtmlTag(subString, tag, tasks));
//				
//				if(aux==0) // If the term was found in the beginning of the text
//					initPointer--;
//			}
//			else
//			{
//				if( (endPointer=backOneWord(text, initPointer, endPointer))==initPointer ) // else just the end_pointer is changed, backs one position
//				{
//					initPointer = text.indexOf(" ", initPointer+1); /* go to the next space */
//					endPointer = indexEndSpace(text, initPointer); 
//					
//					html_buffer.append(subString + " ");
//				}
//			}
//		}
//		htmlText =  html_buffer.toString();
//		
//		if(htmlText.length()==0) // No annotations in the text
//			return text;
//		
//		if(htmlText.charAt(htmlText.length()-1)==' ')
//			htmlText = htmlText.substring(0, htmlText.length()-1);
//		return htmlText;
//	}
//	
//	/** Return the index of the WINTHOW_SIZEth space in the text, if the text haven´t WINDOW_SIZE spaces, return the text length */
//	public int indexEndSpace(String text, int init_index){
//		int last_space=init_index, count=0, result=0;
//		
//		while( ((last_space = text.indexOf(" ", last_space))!=-1) && count++!=SLIDING_WINDOW_SIZE )
//			result = last_space++;
//		if(count<7) // text have less than 7 words
//			return text.length(); 
//		return result;
//	}
//	
//	/** Return the index of the space before the given end space index */
//	public int backOneWord(String text, int start, int end){
//		int index = start, previous=0;
//		while( (index = text.indexOf(" ",index))!=end && index!=-1)
//			previous = index++;
//		return previous;
//	}
//	
//	public String putHtmlTag(String term, String termClass, Map<String,ANoteProperty> tasks){
//		return "<FONT COLOR=" + tasks.get(termClass).getColor() + "><b>" + term + "</b></FONT> ";
//	}
//	
//	public String putXtmlTag(String term, String termClass, ANoteDocumentAnnotations annotations){
//		ANoteAnnotation annotation = annotations.getClassTermAnnotations().get(termClass).get(term);
//		String openTag = "<span class='" + termClass;
//		
//		if(annotation.getId()!=null)
//			openTag += "' id='" + annotation.getId();
//		else if(annotation.getTitle()!=null)
//			openTag += "' title='" + annotation.getTitle();
//		else if(annotation.getLemma()!=null)
//			openTag += "' lemma='" + annotation.getLemma();
//		
//		openTag += "'>";
//		
//		return openTag + term + "</span> ";
//	}
//	
	public ANoteDocumentText clone(){
		ANoteDocumentText clone = new ANoteDocumentText(clearText,htmlText);
		return clone;
	}
//		
//	public void addAnnotation(ANoteDocument doc, Map<String, ANoteProperty> tasks, String term, String tag, ANoteProperty prop){
//		
//		final ANoteProperty property = prop;
//		final String cls=property.getName();
//		String id = null;
//		
//		int nOccurrence = doc.getDocumentText().countOccurrence(term);
//		nOccurrence -= addTermToText(doc, property, id, term, tasks);
//		
//		if(nOccurrence>0)
//		{
//			ANoteAnnotation annotation = new ANoteAnnotation(nOccurrence, term, term, id, null, null);
//			doc.getAnnotations().addAnnotation(annotation, cls);
//		}
//	}
//	
//	public int addTermToText(ANoteDocument doc, ANoteProperty property, String id_term, String term, Map<String,ANoteProperty> tasks)
//	{
//		String htmlTag = "<FONT COLOR=" + property.getColor() + "><b>" + term + "</b></FONT>";
//		String htmlText = doc.getDocumentText().getHtmlText(); 
//		
//		htmlText = ParsingUtils.replace(htmlText, term, "<FONT COLOR=" + property.getColor() + "><b>", "</b></FONT>");
//				
//		String tag, token, token2, text_to_exp;
//		int to_decrement=0;
//		
//		text_to_exp = ParsingUtils.textToRegExp(term);
//		
//		Pattern p1 = Pattern.compile("[ -']+" + text_to_exp + "[ -']");
//		Pattern p2 = Pattern.compile("^" + text_to_exp + "[ -']");
//		Pattern p3 = Pattern.compile("[ -']+" + text_to_exp + "$");	
//		Matcher m1, m2, m3;
//		
//		for(String t: doc.getAnnotations().getTermClass().keySet())
//		{
//			m1 = p1.matcher(t);
//			m2 = p2.matcher(t);
//			m3 = p3.matcher(t);
//			
//			if(m1.find() || m2.find() || m3.find())
//			{
//				tag = doc.getAnnotations().getTermClass().get(t);
//				ANoteProperty prop = tasks.get(tag);
//				
//				token = "<FONT COLOR=" + prop.getColor() + "><b>"+t+"</b></FONT>";
//				token2 = token.replace(" " + term + " ", " " + htmlTag + " ");
//				htmlText = htmlText.replace(token2,token);
//				
//				to_decrement += doc.getAnnotations().getClassTermAnnotations().get(tag).get(t).getCount();				
//			}
//		}
//		setHtmlText(htmlText);
//
//		return to_decrement;
//	}
//	
//	public void correctAnnotation(ANoteDocument doc, Map<String,ANoteProperty> tasks, String term, String newClass){
//		String selectedHtml = doc.getDocumentText().captureAnnotations(term, doc.getAnnotations(), tasks);
//			
//		String htmlTag = "<FONT COLOR=" + tasks.get(newClass).getColor() + "><b>"+term+"</b></FONT>";
//			
//		String resultText = doc.getDocumentText().getHtmlText().replace(" " + selectedHtml + " ", " " + htmlTag + " ");
//		setHtmlText(resultText);
//			
//		doc.getAnnotations().correctAnnotation(term, newClass, doc);
//	}
	
	public String getClearText() {return clearText;}
	public void setClearText(String clearText) {this.clearText = clearText;}
	public String getHtmlText() {return htmlText;}
	public void setHtmlText(String htmlText) {this.htmlText = htmlText;}
}
