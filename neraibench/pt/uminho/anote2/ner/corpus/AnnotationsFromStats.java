package pt.uminho.anote2.ner.corpus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AnnotationsFromStats {
	
	private Map<String, Map<String, Map<String,Integer>>> annotations;// Map<Pmid, Map<Class, Map<Term,Count>>>
	
	public AnnotationsFromStats(){
		annotations = new HashMap<String, Map<String,Map<String,Integer>>>();
	}
	
	public void parseStatAnnotations(File dir) throws Exception{
		
		
		for(File file : dir.listFiles())
		{
			if(file.getName().endsWith(".stat2"))
				documentStatAnnotations(file);
			
		}
		
	}
	
	
	public void documentStatAnnotations(File file) throws Exception{
		String pmid = file.getName().replace(".stat2", "");
		annotations.put(pmid, new HashMap<String, Map<String,Integer>>());
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		
		NodeList annotations = doc.getElementsByTagName("statistic");
		
		for (int i=0; i<annotations.getLength(); i++)
		{
			Element annotElmnt = (Element) annotations.item(i);
			
			NodeList termLst = annotElmnt.getElementsByTagName("term");
			Element termElmnt = (Element) termLst.item(0);
			if(termElmnt!=null)
			{	
				String term = ((Node) termElmnt.getChildNodes().item(0)).getNodeValue();
				
				NodeList clsLst = annotElmnt.getElementsByTagName("class");
				Element clsElmnt = (Element) clsLst.item(0);
				
				if(clsElmnt!=null)
				{
					String cls = ((Node) clsElmnt.getChildNodes().item(0)).getNodeValue();
					
					NodeList countLst = annotElmnt.getElementsByTagName("occurs");
					Element countElmnt = (Element) countLst.item(0);
					
					if(countElmnt!=null)
					{
						String count = ((Node) countElmnt.getChildNodes().item(0)).getNodeValue();
						addAnnotation(cls, term, Integer.decode(count));
					}
				}
			}
		}
		
		
	}
	
	
	public void addAnnotation(String cls, String term, int count){
	}

}
