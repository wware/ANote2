package pt.uminho.anote2.relation.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.lingpipe.POSTaggerPR;
import gate.lingpipe.SentenceSplitterPR;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.process.IE.re.IDirectionality;
import pt.uminho.anote2.process.IE.re.IPolarity;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.gate.GateInit;

public class LingPipePosTagger extends Gate7PosTagger{
	
	private POSTaggerPR tagger;
	private SentenceSplitterPR regExpSentenceSplitter;

	public LingPipePosTagger() {
		super();
		try {
			GateInit.getInstance().creoleRegister("plugins/LingPipe");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (GateException e) {
			e.printStackTrace();
		}
	}
	
	public LingPipePosTagger(IDirectionality rulesDir,IPolarity rulesPol,ILexicalWords verbFilter,ILexicalWords verbAddition) throws GateException, MalformedURLException 
	{
		super(rulesDir,rulesPol,verbFilter,verbAddition);
		GateInit.getInstance().creoleRegister("plugins/LingPipe");
	}
	
	public void completePLSteps(File file) throws MalformedURLException, GateException
	{
		super.gateDocument(file);
		super.tokeniser();
		this.sentenceSpliter();
		this.posTagging();
		super.morphological();
		super.verbGroup();
	}
	
	public void posTaggingSteps(String text)
	{
		try {
			super.createDocumentForStringContent(text);
			super.tokeniser();
			this.sentenceSpliter();
			this.posTagging();
		} catch (ResourceInstantiationException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that create a Pos-Tagging
	 * Need Tokeniser and Sentence Spliter first
	 * @throws ResourceInstantiationException 
	 * @throws ExecutionException 
	 */
	private void posTagging() throws ResourceInstantiationException, ExecutionException
	{
		if(tagger == null)
		{
			FeatureMap features = Factory.newFeatureMap();
			FeatureMap params = Factory.newFeatureMap();
			params = Factory.newFeatureMap();
			String url = "resources/models/posenbiogeniaHiddenMarkovModel";
			params.put("modelFileUrl", url);
			tagger = (POSTaggerPR) Factory.createResource("gate.lingpipe.POSTaggerPR",params,features);	
		}
		tagger.setParameterValue("document", getGateDoc());	
		tagger.execute();		
	}

	
	/**
	 * Method that create a sentence Spliter based on RegExp
	 * Need Tokeniser first
	 * @throws ExecutionException 
	 * @throws ResourceInstantiationException 
	 */
	protected void sentenceSpliter() throws ExecutionException, ResourceInstantiationException
	{
		if(regExpSentenceSplitter == null)
		{
			FeatureMap features = Factory.newFeatureMap();
			FeatureMap params = Factory.newFeatureMap();
			regExpSentenceSplitter = (SentenceSplitterPR) Factory.createResource("gate.lingpipe.SentenceSplitterPR", params, features);
		}
		regExpSentenceSplitter.setParameterValue("document", this.getGateDoc());
		regExpSentenceSplitter.execute();
	}
	
	
	public void cleanALL(){
		super.cleanALL();
		if(this.tagger!=null){
			Factory.deleteResource(tagger);
//			tagger.cleanup();
//			tagger=null;
		}
		if(this.regExpSentenceSplitter != null){
			Factory.deleteResource(regExpSentenceSplitter);
//			regExpSentenceSplitter.cleanup();
//			regExpSentenceSplitter = null;
			
		}
	}
	

}
