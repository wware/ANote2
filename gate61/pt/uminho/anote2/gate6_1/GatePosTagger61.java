package pt.uminho.anote2.gate6_1;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.creole.ExecutionException;
import gate.creole.POSTagger;
import gate.creole.ResourceInstantiationException;
import gate.creole.VPChunker;
import gate.creole.morph.Morph;
import gate.creole.splitter.RegexSentenceSplitter;
import gate.creole.splitter.SentenceSplitter;
import gate.creole.tokeniser.DefaultTokeniser;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import process.IGatePosTagger;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERProcess;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.process.IE.INERProcess;
import pt.uminho.anote2.process.IE.IREProcess;
import pt.uminho.anote2.process.IE.re.IDirectionality;
import pt.uminho.anote2.process.IE.re.IPolarity;
import pt.uminho.anote2.process.IE.re.IRelationModel;
import pt.uminho.anote2.process.IE.re.ISentenceSintaxRepresentation;
import pt.uminho.anote2.process.IE.re.IVerbInfo;
import pt.uminho.anote2.process.IE.re.SentenceSintaxRepresentation;
import pt.uminho.anote2.process.IE.re.VerbInfo;
import pt.uminho.anote2.process.IE.re.VerbTense;
import pt.uminho.anote2.process.IE.re.VerbVoice;
import pt.uminho.anote2.relation.core.RelationsExtraction;
import pt.uminho.anote2.relation.core.relationModels.RelationModelVerbLimitation;
import pt.uminho.anote2.relation.datastructures.Directionality;
import pt.uminho.anote2.relation.datastructures.Polarity;



/**
 * 
 * @author Hugo Costa
 *
 */
public class GatePosTagger61 implements IGatePosTagger{
	
	
	private Document gateDoc;
	private IPolarity polarityRules;
	private IDirectionality directionalyRules;
	
	public GatePosTagger61() throws GateException 
	{
		this.gateDoc = null;
		this.polarityRules=null;
		this.directionalyRules=null;
		GateInit();
	}
	
	public GatePosTagger61(IDirectionality rulesDir,IPolarity rulesPol) throws GateException 
	{
		this.gateDoc = null;
		this.directionalyRules= rulesDir;
		this.polarityRules = rulesPol;
		GateInit();
	}

	public void completePLSteps(String text) throws MalformedURLException, GateException
	{
		this.createDocumentForStringContent(text);
		this.tokeniser();
		this.sentenceSplitRegExp();
		this.posTagging();
		this.morphological();
		this.verbGroup();
	}
	
	public void completePLSteps(File file) throws MalformedURLException, GateException
	{
		this.gateDocument(file);
		this.tokeniser();
		this.sentenceSplitRegExp();
		this.posTagging();
		this.morphological();
		this.verbGroup();
	}
	
	public void sentenceSpliter(File file) throws MalformedURLException, GateException
	{
		this.gateDocument(file);
		this.tokeniser();
		this.sentenceSplitRegExp();
	}
	
	public void afterPosTagging() throws MalformedURLException, GateException
	{
		this.morphological();
		this.verbGroup();
	}
	
	public static void GateInit() throws GateException
	{
			Gate.init();
	}	
	
	public void createGateDocument(File file) throws ResourceInstantiationException
	{
		gateDocument(file);
	}
	
	/**
	 * Method that create a Document gate based in String sentences
	 * @throws ResourceInstantiationException 
	 */
	public void createDocumentForStringContent(String sentences) throws ResourceInstantiationException
	{	
		FeatureMap params = Factory.newFeatureMap();
		FeatureMap features = Factory.newFeatureMap();
		params.put("stringContent",sentences);
		params.put("markupAware", true);	
		this.gateDoc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, features, "GATE Homepage");
	}
	
	/**
	 * Method that creates a Gate Document 
	 *
	 * @param file file that origin the gate Document
	 * @throws ResourceInstantiationException 
	 */
	private void gateDocument(File file) throws ResourceInstantiationException
	{	
		if(this.gateDoc!=null)
		{
			this.gateDoc.cleanup();
			this.gateDoc = null;
		}
		FeatureMap params = Factory.newFeatureMap();
		params.put("sourceUrl",file.toURI().toString());			
		FeatureMap features = Factory.newFeatureMap();		
		this.gateDoc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, features, "GATE Homepage");
		this.gateDoc.setName(file.getName());
	}
	
	/**
	 * Method that create a tokeniser of gateDoc
	 * Need Create Document Gate first
	 * @throws GateException 
	 * @throws MalformedURLException 
	 * 
	 */
	private void tokeniser() throws MalformedURLException, GateException
	{
		Gate.getCreoleRegister().registerDirectories(new URL(Gate.getGateHome().toURI().toURL(),"plugins/ANNIE"));
		FeatureMap features = Factory.newFeatureMap();
		FeatureMap params = Factory.newFeatureMap();
		params.put("tokeniserRulesURL", "resources/tokeniser/DefaultTokeniser.rules");
		params.put("transducerGrammarURL", "resources/tokeniser/postprocess.jape");
		//params.put("encoding", "UTF-8");					
		features = Factory.newFeatureMap();	
		DefaultTokeniser tokeniser = null;
		tokeniser = (DefaultTokeniser) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser", params, features);
		tokeniser.setParameterValue("document", this.gateDoc);
		tokeniser.execute();
	}
	
	/**
	 * Method that create a sentence Spliter
	 * Need Tokeniser first
	 * @throws ResourceInstantiationException 
	 * @throws ExecutionException 
	 */
	protected void sentenceSplitter() throws ResourceInstantiationException, ExecutionException 
	{		
		FeatureMap features = Factory.newFeatureMap();
		FeatureMap params = Factory.newFeatureMap();	
		//params.put("encoding", "UTF-8");
		params.put("gazetteerListsURL", "resources/sentenceSplitter/gazetteer/lists.def");
		params.put("transducerURL", "resources/sentenceSplitter/grammar/main.jape");			
		features = Factory.newFeatureMap();	
		SentenceSplitter sentenceSplitter = (SentenceSplitter) Factory.createResource("gate.creole.splitter.SentenceSplitter", params, features);
		sentenceSplitter.setParameterValue("document", this.gateDoc);
		sentenceSplitter.execute();
	}
	
	/**
	 * Method that create a sentence Spliter based on RegExp
	 * Need Tokeniser first
	 * @throws ExecutionException 
	 * @throws ResourceInstantiationException 
	 */
	private void sentenceSplitRegExp() throws ExecutionException, ResourceInstantiationException
	{
		FeatureMap features = Factory.newFeatureMap();
		FeatureMap params = Factory.newFeatureMap();
		RegexSentenceSplitter regExpSentenceSplitter = (RegexSentenceSplitter) Factory.createResource("gate.creole.splitter.RegexSentenceSplitter", params, features);
		regExpSentenceSplitter.setParameterValue("document", this.gateDoc);
		regExpSentenceSplitter.execute();
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
		//params.put("encoding", "UTF-8");
		params.put("lexiconURL","resources/heptag/lexicon");
		params.put("rulesURL","resources/heptag/ruleset");
		params.put("outputAnnotationType","Token");
		POSTagger tagger = (POSTagger) Factory.createResource("gate.creole.POSTagger",params,features);		
		tagger.setParameterValue("document", this.gateDoc);
		tagger.setParameterValue("baseTokenAnnotationType", "Token");
		tagger.setParameterValue("baseSentenceAnnotationType", "Sentence");
		tagger.setParameterValue("outputAnnotationType", "Token");		
		tagger.execute();		
	}
	/**
	 * Method that create a morphological analysis of words
	 * Used for find lemma verbs
	 * 
	 * Need : Tokeniser,Sentence Spliter and Pos-Tagging first
	 * @throws GateException 
	 * @throws MalformedURLException 
	 */
	private void morphological() throws MalformedURLException, GateException
	{
		Gate.getCreoleRegister().registerDirectories(new URL(Gate.getGateHome().toURI().toURL(),"plugins/Tools"));
		FeatureMap features = Factory.newFeatureMap();
		FeatureMap params = Factory.newFeatureMap();	
		features = Factory.newFeatureMap();
		params = Factory.newFeatureMap();
		params.put("rulesFile","resources/morph/default.rul");
		params.put("caseSensitive","false");	
		Morph morph =  (Morph) Factory.createResource("gate.creole.morph.Morph",params,features);
		morph.setParameterValue("document", this.gateDoc);
		morph.execute();
	}

	/**
	 * Method that find in gate document the noun phases
	 * 
	 * Need : Tokeniser,Sentence Spliter and Pos-Tagging first
	 */
//	private void nounPhases()
//	{
//		Gate.getCreoleRegister().registerDirectories(new URL(Gate.getGateHome().toURI().toURL(),"plugins/NP_Chunking"));
//		FeatureMap features = Factory.newFeatureMap();
//		FeatureMap params = Factory.newFeatureMap();
//		features = Factory.newFeatureMap();	
//		params = Factory.newFeatureMap();
//		params.put("posTagURL","pos_tag_dict");
//		params.put("rulesURL","rules");	
//		GATEWrapper hunker = (GATEWrapper) Factory.createResource("mark.chunking.GATEWrapper",params,features);
//		chunker.setParameterValue("document", this.gateDoc);
//		chunker.execute();
//	}
	
	/**
	 * Method that find verbal expressions
	 * 
	 * Need : Tokeniser,Sentence Spliter and Pos Tagging first
	 * @throws ResourceInstantiationException 
	 * @throws ExecutionException 
	 */
	private void verbGroup() throws ResourceInstantiationException, ExecutionException
	{
		FeatureMap features = Factory.newFeatureMap();
		FeatureMap params = Factory.newFeatureMap();	
		features = Factory.newFeatureMap();	
		params = Factory.newFeatureMap();	
		VPChunker verbChunker = (VPChunker) Factory.createResource("gate.creole.VPChunker",params,features);	
		verbChunker.setParameterValue("document", this.gateDoc);
		verbChunker.execute();
	}
	
	/**
	 * Method that clear all annotations of the document gate
	 */
	public void deleteGateDoc()
	{
		this.gateDoc.cleanup();		
	}
	
	public Document getGateDoc() {
		return gateDoc;
	}



	public List<GenericPair<Long, Long>> getGateDocumentSentencelimits() {
		List<GenericPair<Long,Long>> sentenceLimits = new ArrayList<GenericPair<Long,Long>>();
		AnnotationSet annotSetSentences = gateDoc.getAnnotations().get("Sentence");
		for(Annotation annot:annotSetSentences)
		{
			sentenceLimits.add(new GenericPair<Long, Long>(annot.getStartNode().getOffset(),annot.getEndNode().getOffset()));
		}
		return sentenceLimits;
	}


	public GenericPair<List<IVerbInfo>, List<Long>> getSentenceSintaticLayer(Set<String> termionations, GenericPair<Long, Long> setenceLimits,Long offsetCorrection) {
		Long endSentence = setenceLimits.getY();
		Long startSentence = setenceLimits.getX();
		List<IVerbInfo>  verbsInfo = processSintaticSentence(startSentence,endSentence,offsetCorrection);
		List<Long> terminationsInfo = getTerminations(startSentence,endSentence,termionations,offsetCorrection);
		return new GenericPair<List<IVerbInfo>, List<Long>>(verbsInfo,terminationsInfo);
	}

	public List<IVerbInfo> processSintaticSentence(long startOffsetSentence,long endOffsetSentence,long offsetCorrection) 
	{
		List<IVerbInfo> verbsInfo = new ArrayList<IVerbInfo>();
		AnnotationSet allAnnottaions = getGateDoc().getAnnotations();
		allAnnottaions = allAnnottaions.getContained(startOffsetSentence, endOffsetSentence);
		ISentenceSintaxRepresentation sentenceSintax= getSentenceSintax(allAnnottaions.get("Token"),startOffsetSentence);
		AnnotationSet vpAnnotations = allAnnottaions.get("VG");
		Iterator<Annotation> itAnnot = vpAnnotations.iterator();
		IVerbInfo verbInfo;
		while(itAnnot.hasNext())
		{
			Annotation annot = itAnnot.next();
			boolean directional = true;
			int polarity = 1;
			long startAnnot = annot.getStartNode().getOffset();
			long endAnnot = annot.getEndNode().getOffset();
			String verb = getPartGateDocument(getGateDoc(), startAnnot, endAnnot);
			String lemma = getLemma(allAnnottaions,startAnnot,endAnnot);
			if(annot.getFeatures().get("neg")!=null)
				polarity=-1;
			String voice = (String) annot.getFeatures().get("voice");
			String tense = (String) annot.getFeatures().get("tense");
			VerbVoice verbVoice = getVerbVoice(voice);
			VerbTense verbTense = getVerbTense(tense);
			IVerbInfo verbInfoAux = new VerbInfo(startAnnot-offsetCorrection,endAnnot-offsetCorrection,verb,lemma,polarity);
			if(directionalyRules!=null)
				directional = directionalyRules.getDirectionality(verbInfoAux, sentenceSintax);
			if(polarityRules!=null)
				polarity = polarityRules.getPolarity(verbInfoAux);
			verbInfo = new VerbInfo(verb,lemma.toLowerCase(),polarity,verbVoice,verbTense,directional,startAnnot-offsetCorrection,endAnnot-offsetCorrection);	
			verbsInfo.add(verbInfo);
		}	
		return verbsInfo;
	}
	
	private ISentenceSintaxRepresentation getSentenceSintax(AnnotationSet annotationSet,long startOffsetSentence) 
	{
		ISentenceSintaxRepresentation sentenceSintax = new SentenceSintaxRepresentation();
		Iterator<Annotation> itAnnot = annotationSet.iterator();
		while(itAnnot.hasNext())
		{
			Annotation annot = itAnnot.next();
			String word = (String) annot.getFeatures().get("string");
			String cat = (String) annot.getFeatures().get("category");
			String lemma = (String) annot.getFeatures().get("root");		
			long startAnnotOffset = annot.getStartNode().getOffset();
			startAnnotOffset=startAnnotOffset-startOffsetSentence;		
			sentenceSintax.addElement(startAnnotOffset, word, cat, lemma);
		}
		return sentenceSintax;
	}
	
	
	
	/**
	 * Static Method
	 * Method that return a piece of Text.
	 * 
	 * 
	 * @param gateDoc Gate Document
	 * @param startPosition 
	 * @param endPosition
	 * @return
	 */
	public static String getPartGateDocument(Document gateDoc,Long startPosition,Long endPosition)
	{
		String allText = gateDoc.getContent().toString();
		return allText.substring(startPosition.intValue(),endPosition.intValue());
	}
	
	/**
	 * Method that return lemma of offset words under the star and end limits
	 * 
	 * @param allAnnottaions
	 * @param start
	 * @param end
	 * @return
	 */
	private static String getLemma(AnnotationSet allAnnottaions,long start,long end)
	{
		String lemma = new String();
		AnnotationSet annotSet = allAnnottaions.getContained(start, end);
		annotSet = annotSet.get("Token");
		TreeMap<Long,String> tokensOrder = new TreeMap<Long, String>();
		Iterator<Annotation> itAnnot = annotSet.iterator();
		while(itAnnot.hasNext())
		{
			Annotation annot = itAnnot.next();
			tokensOrder.put(annot.getStartNode().getOffset(),(String)annot.getFeatures().get("root"));
		}	
		Iterator<String> itTokens = tokensOrder.values().iterator();
		while(itTokens.hasNext())
			lemma=lemma.concat(itTokens.next()+" ");
		lemma = lemma.substring(0,lemma.length()-1);
		return lemma;
	}
	/**
	 * Method that return the tense of verb
	 * 
	 * @param tense
	 * @return
	 */
	private static VerbTense getVerbTense(String tense) 
	{
		if(tense.equals("SimPre"))
		{
			return VerbTense.SIMPLE_PRESENT;
		}
		else if(tense.equals("PreCon"))
		{
			return VerbTense.PRESENT_CONTINUOS;
		}
		else if(tense.equals("PrePer"))
		{
			return VerbTense.PRESENT_PERFECT;
		}
		else if(tense.equals("PrePerCon"))
		{
			return VerbTense.PRESENT_PERFECT_CONTINOUS;
		}
		else if(tense.equals("SinPas")||tense.equals("Pas"))
		{
			return VerbTense.SIMPLE_PAST;
		}
		else if(tense.equals("PasCon"))
		{
			return VerbTense.PAST_CONTINOUS;
		}
		else if(tense.equals("PasPer"))
		{
			return VerbTense.PAST_PERFECT;
		}
		else if(tense.equals("Inf"))
		{
			return VerbTense.INFINITIVE;
		}
		else if(tense.equals("Pre"))
		{
			return VerbTense.PRESENT_CONTINUOS;
		}
		else
		{
			return VerbTense.NONE;
		}
	}

	/**
	 * Method that return the voice verb
	 * 
	 * @param voice
	 * @return
	 */
	private static VerbVoice getVerbVoice(String voice) 
	{
		
		if(voice.equals("active"))
		{
			return VerbVoice.ACTIVE;
		}
		else if(voice.equals("passive"))
		{
			return VerbVoice.PASSIVE;
		}
		else
			return VerbVoice.NONE;
	}

	/**
	 * Method that find in sentence a possible relation terminatons
	 * 
	 * @param startOffsetSentence
	 * @param endOffsetSentence
	 * @param relationTerminations
	 * @param offsetCorrection 
	 * @param gateDocument
	 * @return
	 */
	public List<Long> getTerminations(long startOffsetSentence,long endOffsetSentence,Set<String> relationTerminations, long offsetCorrection)
	{
		List<Long> offsetTerminations = new ArrayList<Long>();
		
		AnnotationSet allAnnottaions = getGateDoc().getAnnotations();
		allAnnottaions = allAnnottaions.getContained(startOffsetSentence, endOffsetSentence);
		AnnotationSet tokenAnnotations = allAnnottaions.get("Token");
		Iterator<Annotation> itAnnot = tokenAnnotations.iterator();
		while(itAnnot.hasNext())
		{
			Annotation annot = itAnnot.next();
			String word = (String) annot.getFeatures().get("string");
			if(relationTerminations.contains(word))
			{
				long start = annot.getStartNode().getOffset();
				offsetTerminations.add(start-offsetCorrection);
			}
		}
		return offsetTerminations;
	}
	
	public Map<Long, IVerbInfo> getVerbsPosition(List<IVerbInfo> verbsInfo)
	{
		TreeMap<Long,IVerbInfo> verbsInfoReturn = new TreeMap<Long, IVerbInfo>();
		for(int i=0;i<verbsInfo.size();i++)
		{
			IVerbInfo verbInfo = verbsInfo.get(i);
			verbsInfoReturn.put(verbInfo.getStartOffset(),verbInfo);
		}	
		return verbsInfoReturn;
	}
	
	public static void main(String[] args) throws GateException, MalformedURLException 
	{
//		IDatabase db = new MySQLDatabase("localhost", "3306", "anote_db", "root", "admin");
//		IGatePosTagger postagger = new GatePosTagger61(new Directionality(),new Polarity());
//		Properties prop = new Properties();
//		prop.setProperty("textType", "abstract");
//		Corpora project = new Corpora(db);
//		ICorpus corpus = new Corpus(3, "", project, prop);		
//		INERProcess nerProcess = new NERProcess(8, "","RE",new Properties(), db);
//		IRelationModel rm = new  RelationModelVerbLimitation(corpus,nerProcess,postagger);
//		Properties prop2 = new Properties();
//		prop2.put("nerprocess",String.valueOf(nerProcess.getID()));
//		prop2.put("process","Rel@tion Process");
//		//prop.put("tagger",String.valueOf(tagger.name()));
//		//prop.put("relationModel",String.valueOf(rm));
//		IREProcess relExtraction = new RelationsExtraction(corpus,nerProcess,postagger,rm,prop2);
//		relExtraction.executeRE();
		
	}

	public void setGateDoc(Document gateDoc) {
		this.gateDoc = gateDoc;
	}


	





	
}
