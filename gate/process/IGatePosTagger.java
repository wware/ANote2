package process;

import gate.Document;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.process.IE.re.IVerbInfo;


public interface IGatePosTagger {
	
	public void completePLSteps(String text) throws MalformedURLException, GateException;
	public void completePLSteps(File file) throws MalformedURLException, GateException;
	public void sentenceSpliter(File file) throws ResourceInstantiationException, ExecutionException, MalformedURLException, GateException;
	public void afterPosTagging() throws MalformedURLException, GateException;
	public void createGateDocument(File file) throws ResourceInstantiationException;
	public List<GenericPair<Long,Long>> getGateDocumentSentencelimits();
	public GenericPair<List<IVerbInfo>, List<Long>> getSentenceSintaticLayer(Set<String> termionations, GenericPair<Long, Long> setenceLimits,Long offsetCorrection);
	public Document getGateDoc();
	public void setGateDoc(Document doc);
	public Map<Long, IVerbInfo> getVerbsPosition(List<IVerbInfo> verbsInfo);
	

	
}
