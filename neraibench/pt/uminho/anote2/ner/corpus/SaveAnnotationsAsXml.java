package pt.uminho.anote2.ner.corpus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SaveAnnotationsAsXml {
	
	public void save(File file, GetGlobalAnnotations globalAnnotations, Map<String, HashMap<String, TermCount>> docsAnnotations) throws IOException{
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		
		StringBuffer annotations = new StringBuffer();
		annotations.append("<annotations>\n");
				
		for(String pmid : docsAnnotations.keySet())
		{
			annotations.append("\t<document>\n");
			annotations.append("\t\t<pmid>" + pmid + "</pmid>\n");
	
			for(String cls : globalAnnotations.getAnnotations().keySet())
			{
				for(String cname : globalAnnotations.getAnnotations().get(cls).keySet())
				{
					NormTerm norm = globalAnnotations.getAnnotations().get(cls).get(cname);
					StringBuffer forms = new StringBuffer();
					int normCount = 0;
					String id=norm.getId();

//if(globalAnnotations.getAnnotations().get(cls).get(cname).getTerms().contains("ppgpp"))
//	System.out.println(pmid + " cname: " + cname + " cls " + termClass);	
//if(globalAnnotations.getAnnotations().get(termClass).get(cname).getTerms().contains("ppgpp"))
//	System.out.println(pmid + " cname: " + cname + " termClass " + termClass);	

//if(globalAnnotations.getAnnotations().get(cls).get(cname).getTerms().contains("ppgpp"))
//	System.out.println(pmid + " cname: " + cname + " cls " + termClass);					
					
//if(globalAnnotations.getAnnotations().get(termClass).containsKey(termClass))
//	System.out.println("termClass: " + termClass);
//if(globalAnnotations.getAnnotations().get(cls).containsKey(termClass))
//	System.out.println("cls: " + cls);

					for(String term : norm.getTerms())
					{
						if(docsAnnotations.get(pmid).containsKey(term))
						{
							int tCount = docsAnnotations.get(pmid).get(term).getRealCount();
							if(tCount>0)
							{		
								forms.append("\t\t\t<form>\n");
								forms.append("\t\t\t\t<term>"+term+"</term>\n");
								forms.append("\t\t\t\t<termcount>"+tCount+"</termcount>\n");
								forms.append("\t\t\t</form>\n");
								normCount += tCount;
							}
						}
					}
					if(normCount>0)
					{
						annotations.append("\t\t<annotation>\n");
						annotations.append("\t\t\t<class>"+cls+"</class>\n");
						annotations.append("\t\t\t<id>"+id+"</id>\n");
						annotations.append("\t\t\t<cname>"+cname+"</cname>\n");
						annotations.append("\t\t\t<count>"+normCount+"</count>\n");
						annotations.append(forms);
						annotations.append("\t\t</annotation>\n");
					}
				}
			}
			
			annotations.append("\t</document>\n");
		}

		annotations.append("</annotations>");
		
		bw.write(annotations.toString());
		bw.flush();
		bw.close();
		fw.close();
	}

}
