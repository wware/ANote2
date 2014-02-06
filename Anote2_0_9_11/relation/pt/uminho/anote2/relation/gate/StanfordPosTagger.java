package pt.uminho.anote2.relation.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;

import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.process.IE.re.IDirectionality;
import pt.uminho.anote2.process.IE.re.IPolarity;
import pt.uminho.gate.GateInit;

public class StanfordPosTagger extends Gate7PosTagger{

	public StanfordPosTagger() throws GateException, MalformedURLException {
		super();
		GateInit.getInstance().creoleRegister("plugins/Parser_Stanford");
	}
	
	public StanfordPosTagger(IDirectionality rulesDir,IPolarity rulesPol,LexicalWords verbFilter,LexicalWords verbAddition) throws GateException, MalformedURLException 
	{
		super(rulesDir,rulesPol,verbFilter,verbAddition);
		GateInit.getInstance().creoleRegister("plugins/Parser_Stanford");
	}
	
	public void completePLSteps(File file) throws MalformedURLException, GateException
	{
		super.gateDocument(file);
		super.tokeniser();
		super.sentenceSplitRegExp();
		this.posTagging();
		super.morphological();
		super.verbGroup();
	}
	
	/**
	 * Method that create a Pos-Tagging
	 * Need Tokeniser and Sentence Spliter first
	 * @throws ResourceInstantiationException 
	 * @throws ExecutionException 
	 */
	private void posTagging() throws ResourceInstantiationException, ExecutionException
	{		
		FeatureMap features = Factory.newFeatureMap();
		FeatureMap params = Factory.newFeatureMap();
		params = Factory.newFeatureMap();
		params.put("document", this.getGateDoc());
		params.put("addDependencyFeatures",false);
		params.put("addDependencyAnnotations",false);
		params.put("addPosTags",true);
//		Parser tagger = (Parser) Factory.createResource("gate.stanford.parser",params,features);	
//		tagger.setParameterValue("document", getGateDoc());	
//		tagger.execute();		
	}
	
	public static void main(String[] args) throws GateException, MalformedURLException {
		StanfordPosTagger gate = new StanfordPosTagger();	
		gate.completePLSteps(new File("gate7/8531889.xml"));
	}

}
