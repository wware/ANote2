package pt.uminho.anote2.aibench.corpus.management.aibench;

import java.util.List;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;

public class CorporaAIBenchManagement {
	
	
	public static void removeCorpus(Corpus corpus)
	{
		List<ClipboardItem> itemCorpora = Core.getInstance().getClipboard().getItemsByClass(Corpora.class);
		if(itemCorpora.size() == 0)
		{
			Corpora corpora = (Corpora) itemCorpora.get(0).getUserData();
			corpora.removeCorpus(corpus);
		}
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Corpus.class);
		ClipboardItem torem = null;
		for(ClipboardItem item : items){
			if(item.getUserData().equals(corpus)){
				torem = item;
				break;
			}
		}
//		Corpus corpusToRemove = (Corpus) torem.getUserData();
//		List<IIEProcess> list = corpusToRemove.getProcesses();
//		for(IIEProcess process : list)
//		{
//			removeProcesse(corpus,process);
//		}
		corpus.freeMemory();
		Core.getInstance().getClipboard().removeClipboardItem(torem);
		System.gc();
	}
	
	public static void removeNERProcess(NERSchema nerSchema)
	{
		Corpus corpus = (Corpus) nerSchema.getCorpus();
		List<ClipboardItem> itemCorpora = Core.getInstance().getClipboard().getItemsByClass(Corpus.class);
		for(ClipboardItem itemCorpus :itemCorpora)
		{
			if(itemCorpus.getUserData().equals(nerSchema)){
				corpus.getIEProcesses().remove(nerSchema);
				break;
			}
		}
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(NERSchema.class);
		ClipboardItem torem = null;
		for(ClipboardItem item : items){
			if(item.getUserData().equals(nerSchema)){
				torem = item;
				break;
			}
		}
		nerSchema.freeMemory();
		Core.getInstance().getClipboard().removeClipboardItem(torem);
		System.gc();
	}

//	private static void removeProcesse(Corpus corpus, IIEProcess process) {
//		if(process instanceof REDocumentAnnotation)
//		{
//			List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(REDocumentAnnotation.class);
//			ClipboardItem torem = null;
//			for(ClipboardItem item : items){
//				if(item.getUserData().equals(corpus)){
//					torem = item;
//					break;
//				}
//			}
//			REDocumentAnnotation reDocument = (REDocumentAnnotation) torem.getUserData();		
//			reDocument.freeMemory();
//			Core.getInstance().getClipboard().removeClipboardItem(torem);
//
//		}
//		else
//		{
//			List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(NERDocumentAnnotation.class);
//			ClipboardItem torem = null;
//			for(ClipboardItem item : items){
//				if(item.getUserData().equals(corpus)){
//					torem = item;
//					break;
//				}
//			}
//			NERDocumentAnnotation nerDocument = (NERDocumentAnnotation) torem.getUserData();		
//			nerDocument.freeMemory();
//			Core.getInstance().getClipboard().removeClipboardItem(torem);
//		}
//	}
	

}
