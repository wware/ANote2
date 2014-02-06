package pt.uminho.anote2.aibench.curator.operation;

import java.io.File;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.process.IEProcess;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;

@Operation()
public class OperationManualCuration {

	private Corpus corpus;
	
	@Port(name="Corpus",direction=Direction.INPUT,order=1)
	public void setCorpus(Corpus corpus)
	{
		this.corpus=corpus;
		Properties prop = new Properties();
		String textType = corpus.getProperties().getProperty("textType");
		prop.put("textType",textType);
		IEProcess ner = new IEProcess(corpus,"Manual Curation","Manual Curation",prop,corpus.getCorpora().getDb());
		corpus.registerProcess(ner);
		if(textType.equals("full text"))
		{
			findFullText();
		}
		new ShowMessagePopup("Manual Curation Process");
		corpus.notifyViewObserver();	
	}
	
	private void findFullText() {
		for(IDocument doc:corpus.getArticlesCorpus())
		{
			IPublication pub = (IPublication) doc;
			findFullTextArticle(pub);
		}
		
	}

	private void findFullTextArticle(IPublication pub) {
		String fullText=pub.getFullTextFromDatabase(corpus.getCorpora().getDb());
		if(fullText==null||fullText.equals(""))
		{
			String id_path = Corpora.saveDocs+"id"+pub.getID()+".pdf";
			String id_path_otherID = Corpora.saveDocs+"id"+pub.getID()+"-"+pub.getOtherID()+".pdf";
			String pdf_path = Corpora.saveDocs+pub.getOtherID()+".pdf";
			if(new File(id_path).exists())
			{
				fullText = pub.getFullTextFromURL(id_path);
			}
			else if(new File(id_path_otherID).exists())
			{
				fullText = pub.getFullTextFromURL(id_path_otherID);
			}
			else if(new File(pdf_path).exists())
			{
				fullText = pub.getFullTextFromURL(pdf_path);
			}
			if(fullText==null||fullText.equals(""))
			{
				Workbench.getInstance().warn("The article whit id: "+pub.getID()+" not contains full text");
			}
			else
			{
				pub.setFullTExtOnDatabase(corpus.getCorpora().getDb(), fullText);
			}
		}
	}
	
	

}
