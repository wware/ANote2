package pt.uminho.gate.process;


import gate.Document;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.process.IE.IPOSTagger;


public interface IGatePosTagger extends IPOSTagger{
	
//	public void completePLSteps(String text) throws MalformedURLException, GateException;
	public void completePLSteps(File file) throws MalformedURLException, GateException;
	public void sentenceSpliter(File file) throws ResourceInstantiationException, ExecutionException, MalformedURLException, GateException;
	public void afterPosTagging() throws MalformedURLException, GateException;
	public void createGateDocument(File file) throws ResourceInstantiationException;
	public Document getGateDoc();
	public void setGateDoc(Document doc);
	public void cleanALL();
	public List<ISentence> getSentences();
		
}
