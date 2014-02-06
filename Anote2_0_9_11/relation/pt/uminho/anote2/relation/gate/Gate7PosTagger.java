package pt.uminho.anote2.relation.gate;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.ExecutionException;
import gate.creole.POSTagger;
import gate.creole.ResourceInstantiationException;
import gate.creole.VPChunker;
import gate.creole.morph.Morph;
import gate.creole.splitter.RegexSentenceSplitter;
import gate.creole.splitter.SentenceSplitter;
import gate.creole.tokeniser.SimpleTokeniser;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPOSToken;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.core.utils.IGenericPair;
import pt.uminho.anote2.datastructures.documents.POSToken;
import pt.uminho.anote2.datastructures.documents.Sentence;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.GenericTriple;
import pt.uminho.anote2.process.IE.re.IDirectionality;
import pt.uminho.anote2.process.IE.re.IPolarity;
import pt.uminho.anote2.process.IE.re.ISentenceSintaxRepresentation;
import pt.uminho.anote2.process.IE.re.SentenceSintaxRepresentation;
import pt.uminho.anote2.process.IE.re.VerbInfo;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;
import pt.uminho.anote2.process.IE.re.clue.VerbTenseEnum;
import pt.uminho.anote2.process.IE.re.clue.VerbVoiceEnum;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.gate.GateInit;
import pt.uminho.gate.process.IGatePosTagger;



/**
 * 
 * @author Hugo Costa
 *
 */
public class Gate7PosTagger implements IGatePosTagger{
	
	
	private Document gateDoc;
	private IPolarity polarityRules;
	private IDirectionality directionalyRules;
	private ILexicalWords verbFilter;
	private ILexicalWords verbAddition;
	private SimpleTokeniser tokeniser;
	private SentenceSplitter sentenceSplitter;
	private RegexSentenceSplitter regExpSentenceSplitter;
	private POSTagger tagger;
	private Morph morph;
	private VPChunker verbChunker;
	
	public Gate7PosTagger()
	{
		this.gateDoc = null;
		this.polarityRules=null;
		this.directionalyRules=null;
		try {
			GateInit();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (GateException e) {
			e.printStackTrace();
		}
	}
	
	public Gate7PosTagger(IDirectionality rulesDir,IPolarity rulesPol,ILexicalWords verbFilter,ILexicalWords verbAddition) throws GateException, MalformedURLException 
	{
		this.gateDoc = null;
		this.directionalyRules= rulesDir;
		this.polarityRules = rulesPol;
		this.verbFilter=verbFilter;
		this.verbAddition=verbAddition;
		GateInit();
	}

	public void posTaggingSteps(String text)
	{
		try {
			this.createDocumentForStringContent(text);
			this.tokeniser();
			this.sentenceSplitRegExp();
			this.posTagging();
		} catch (ResourceInstantiationException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
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
	
	public void sentenceSpliter(File file) throws ResourceInstantiationException, ExecutionException
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
	
	public static void GateInit() throws GateException, MalformedURLException
	{
			GateInit.getInstance().init();
			GateInit.getInstance().creoleRegister("plugins/ANNIE");
			GateInit.getInstance().creoleRegister("plugins/Tools");
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
		//params.put("markupAware", true);	
		this.gateDoc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, features, "GATE Homepage");
	}
	
	/**
	 * Method that creates a Gate Document 
	 *
	 * @param file file that origin the gate Document
	 * @throws ResourceInstantiationException 
	 */
	protected void gateDocument(File file) throws ResourceInstantiationException
	{	
		if(this.gateDoc!=null)
			this.gateDoc.cleanup();
		FeatureMap params = Factory.newFeatureMap();
		params.put("sourceUrl",file.toURI().toString());	
		//params.put("encoding", "UTF-8");
		FeatureMap features = Factory.newFeatureMap();		
		this.gateDoc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, features, "GATE Homepage");
		this.gateDoc.setName(file.getName());
	}
	
	/**
	 * Method that create a tokeniser of gateDoc
	 * Need Create Document Gate first
	 * @throws ResourceInstantiationException 
	 * @throws ExecutionException 
	 * 
	 */
	protected void tokeniser() throws ResourceInstantiationException, ExecutionException
	{
		if(tokeniser == null)
		{
			FeatureMap features = Factory.newFeatureMap();
			FeatureMap params = Factory.newFeatureMap();				
			features = Factory.newFeatureMap();	
			tokeniser = (SimpleTokeniser) Factory.createResource("gate.creole.tokeniser.SimpleTokeniser", params, features);
		}
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
		if(sentenceSplitter==null)
		{
			FeatureMap features = Factory.newFeatureMap();
			FeatureMap params = Factory.newFeatureMap();	
			//params.put("encoding", "UTF-8");
			params.put("gazetteerListsURL", "resources/sentenceSplitter/gazetteer/lists.def");
			params.put("transducerURL", "resources/sentenceSplitter/grammar/main.jape");			
			features = Factory.newFeatureMap();	
			sentenceSplitter = (SentenceSplitter) Factory.createResource("gate.creole.splitter.SentenceSplitter", params, features);
		}
		sentenceSplitter.setParameterValue("document", this.gateDoc);
		sentenceSplitter.execute();
	}

	/**
	 * Method that create a sentence Spliter based on RegExp
	 * Need Tokeniser first
	 * @throws ExecutionException 
	 * @throws ResourceInstantiationException 
	 */
	protected void sentenceSplitRegExp() throws ExecutionException, ResourceInstantiationException
	{
		if(regExpSentenceSplitter==null)
		{
			FeatureMap features = Factory.newFeatureMap();
			FeatureMap params = Factory.newFeatureMap();
			regExpSentenceSplitter = (RegexSentenceSplitter) Factory.createResource("gate.creole.splitter.RegexSentenceSplitter", params, features);
		}
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
		if(tagger == null)
		{
			FeatureMap features = Factory.newFeatureMap();
			FeatureMap params = Factory.newFeatureMap();
			params = Factory.newFeatureMap();
			//params.put("encoding", "UTF-8");
			params.put("lexiconURL","resources/heptag/lexicon");
			params.put("rulesURL","resources/heptag/ruleset");
			params.put("outputAnnotationType","Token");
			tagger = (POSTagger) Factory.createResource("gate.creole.POSTagger",params,features);		
			tagger.setParameterValue("baseTokenAnnotationType", "Token");
			tagger.setParameterValue("baseSentenceAnnotationType", "Sentence");
			tagger.setParameterValue("outputAnnotationType", "Token");	
		}
		tagger.setParameterValue("document", this.gateDoc);
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
	protected void morphological() throws MalformedURLException, GateException
	{
		if(morph == null)
		{
			FeatureMap features = Factory.newFeatureMap();
			FeatureMap params = Factory.newFeatureMap();	
			features = Factory.newFeatureMap();
			params = Factory.newFeatureMap();
			params.put("rulesFile","resources/morph/default.rul");
			params.put("caseSensitive","false");	
			morph =  (Morph) Factory.createResource("gate.creole.morph.Morph",params,features);
		}
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
	protected void verbGroup() throws ResourceInstantiationException, ExecutionException
	{
		if(verbChunker==null)
		{
			FeatureMap features = Factory.newFeatureMap();
			FeatureMap params = Factory.newFeatureMap();	
			features = Factory.newFeatureMap();	
			params = Factory.newFeatureMap();	
			verbChunker = (VPChunker) Factory.createResource("gate.creole.VPChunker",params,features);	
		}
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

	public List<IGenericPair<Long, Long>> getDocumentSentencelimits() {
		List<IGenericPair<Long,Long>> sentenceLimits = new ArrayList<IGenericPair<Long,Long>>();
		AnnotationSet annotSetSentences = gateDoc.getAnnotations().get("Sentence");
		for(Annotation annot:annotSetSentences)
		{
			sentenceLimits.add(new GenericPair<Long, Long>(annot.getStartNode().getOffset(),annot.getEndNode().getOffset()));
		}
		return sentenceLimits;
	}


	public GenericPair<List<IVerbInfo>, List<Long>> getSentenceSintaticLayer( Set<String> termionations, Set<String> filterVerbs, Set<String> additionVerbs, GenericPair<Long, Long> setenceLimits,long documentStartOffset) throws SQLException, DatabaseLoadDriverException  {
		long endSentence = setenceLimits.getY();
		long startSentence = setenceLimits.getX();
		List<IVerbInfo>  verbsInfo = processSintaticSentence(startSentence,endSentence,documentStartOffset);
		if(verbAddition !=null && verbAddition.getLexicalWords().size()>0)
		{
			verbAddition(startSentence,endSentence,verbAddition.getLexicalWords(),verbsInfo,documentStartOffset);
		}
		List<Long> terminationsInfo = getTerminations(startSentence,endSentence,termionations);
		return new GenericPair<List<IVerbInfo>, List<Long>>(verbsInfo,terminationsInfo);
	}

	
	private boolean alreadyAnnotated(long startAnnot, long endAnnot, List<IVerbInfo> verbsInfo) {
		for(IVerbInfo verb:verbsInfo)
		{
			Long start = verb.getStartOffset();
			Long end = verb.getEndOffset();
			if(startAnnot > start && endAnnot > end)
			{
				return true;
			}
		}
		return false;
	}

	private void findVerbs(List<IVerbInfo> verbsInfo,
			AnnotationSet allAnnottaions,
			ISentenceSintaxRepresentation sentenceSintax, Annotation annot,
			DirectionallyEnum directional, PolarityEnum polarity, long startAnnot, long endAnnot,
			String verb,long offsetCorrection) {
		IVerbInfo verbInfo;
		String lemma = getLemma(allAnnottaions,startAnnot,endAnnot);
		if(annot.getFeatures().get("neg")!=null)
			polarity=PolarityEnum.Negative;
		String voice = (String) annot.getFeatures().get("voice");
		String tense = (String) annot.getFeatures().get("tense");
		VerbVoiceEnum verbVoice = getVerbVoice(voice);
		VerbTenseEnum verbTense = getVerbTense(tense);
		IVerbInfo verbInfoAux = new VerbInfo(startAnnot-offsetCorrection,endAnnot-offsetCorrection,verb,lemma,polarity);
		if(directionalyRules!=null)
			directional = directionalyRules.getDirectionality(verbInfoAux, sentenceSintax);
		if(polarityRules!=null)
			polarity = polarityRules.getPolarity(verbInfoAux);
		verbInfo = new VerbInfo(verb,lemma.toLowerCase(),polarity,verbVoice,verbTense,directional,startAnnot-offsetCorrection,endAnnot-offsetCorrection);	
		verbsInfo.add(verbInfo);
	}
	
	private ISentenceSintaxRepresentation getSentenceSintax(AnnotationSet annotationSet,long startOffsetSentence,long documentStartOffset) 
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
			long start = startAnnotOffset-documentStartOffset;		
			sentenceSintax.addElement(start, word, cat, lemma);
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
	private static VerbTenseEnum getVerbTense(String tense) 
	{
		if(tense.equals("SimPre"))
		{
			return VerbTenseEnum.SIMPLE_PRESENT;
		}
		else if(tense.equals("PreCon"))
		{
			return VerbTenseEnum.PRESENT_CONTINUOS;
		}
		else if(tense.equals("PrePer"))
		{
			return VerbTenseEnum.PRESENT_PERFECT;
		}
		else if(tense.equals("PrePerCon"))
		{
			return VerbTenseEnum.PRESENT_PERFECT_CONTINOUS;
		}
		else if(tense.equals("SinPas")||tense.equals("Pas"))
		{
			return VerbTenseEnum.SIMPLE_PAST;
		}
		else if(tense.equals("PasCon"))
		{
			return VerbTenseEnum.PAST_CONTINOUS;
		}
		else if(tense.equals("PasPer"))
		{
			return VerbTenseEnum.PAST_PERFECT;
		}
		else if(tense.equals("Inf"))
		{
			return VerbTenseEnum.INFINITIVE;
		}
		else if(tense.equals("Pre"))
		{
			return VerbTenseEnum.PRESENT_CONTINUOS;
		}
		else
		{
			return VerbTenseEnum.NONE;
		}
	}

	/**
	 * Method that return the voice verb
	 * 
	 * @param voice
	 * @return
	 */
	private static VerbVoiceEnum getVerbVoice(String voice) 
	{
		
		if(voice.equals("active"))
		{
			return VerbVoiceEnum.ACTIVE;
		}
		else if(voice.equals("passive"))
		{
			return VerbVoiceEnum.PASSIVE;
		}
		else
			return VerbVoiceEnum.NONE;
	}

	/**
	 * Method that find in sentence a possible relation terminatons
	 * 
	 * @param startOffsetSentence
	 * @param endOffsetSentence
	 * @param relationTerminations
	 * @param gateDocument
	 * @return
	 */
	public List<Long> getTerminations(long startOffsetSentence,long endOffsetSentence,Set<String> relationTerminations)
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
				offsetTerminations.add(start);
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

	public IGenericPair<List<IVerbInfo>, List<Long>> getSentenceSintaticLayer(
			Set<String> termionations, IGenericPair<Long, Long> setenceLimits,
			Long offsetCorrection) throws SQLException, DatabaseLoadDriverException {
		Long endSentence = setenceLimits.getY();
		Long startSentence = setenceLimits.getX();
		List<IVerbInfo>  verbsInfo = processSintaticSentence(startSentence,endSentence,offsetCorrection);
		if(verbAddition !=null && verbAddition.getLexicalWords().size()>0)
		{
			verbAddition(startSentence,endSentence,verbAddition.getLexicalWords(),verbsInfo,offsetCorrection);
		}
		List<Long> terminationsInfo = getTerminations(startSentence,endSentence,termionations,offsetCorrection);
		return new GenericPair<List<IVerbInfo>, List<Long>>(verbsInfo,terminationsInfo);
	}

	private void verbAddition(Long startOffsetSentence, Long endOffsetSentence,Set<String> lexicalWords, List<IVerbInfo> verbsInfo, long documetStartOffset) throws SQLException, DatabaseLoadDriverException {
		AnnotationSet allAnnottaions = getGateDoc().getAnnotations();
		allAnnottaions = allAnnottaions.getContained(startOffsetSentence, endOffsetSentence);
		ISentenceSintaxRepresentation sentenceSintax= getSentenceSintax(allAnnottaions.get("Token"),startOffsetSentence,documetStartOffset);
		Iterator<Annotation> itAnnot = allAnnottaions.iterator();
		IVerbInfo verbInfo;
		while(itAnnot.hasNext())
		{
			Annotation annot = itAnnot.next();
			DirectionallyEnum directional = DirectionallyEnum.LeftToRight;
			PolarityEnum polarity = PolarityEnum.Positive;
			long startAnnot = annot.getStartNode().getOffset();
			long endAnnot = annot.getEndNode().getOffset();
			String token = getPartGateDocument(getGateDoc(), startAnnot, endAnnot);
			if(verbAddition.getLexicalWords().contains(token))
			{
				if(!alreadyAnnotated(startAnnot-documetStartOffset,endAnnot-documetStartOffset,verbsInfo))
				{
					String lemma = getLemma(allAnnottaions,startAnnot,endAnnot);
					if(annot.getFeatures().get("neg")!=null)
						polarity=PolarityEnum.Negative;
					IVerbInfo verbInfoAux = new VerbInfo(startAnnot-documetStartOffset,endAnnot-documetStartOffset,token,lemma,polarity);
					if(directionalyRules!=null)
						directional = directionalyRules.getDirectionality(verbInfoAux, sentenceSintax);
					if(polarityRules!=null)
						polarity = polarityRules.getPolarity(verbInfoAux);
					verbInfo = new VerbInfo(token,lemma.toLowerCase(),polarity,VerbVoiceEnum.NONE,VerbTenseEnum.NONE,directional,startAnnot-documetStartOffset,endAnnot-documetStartOffset);	
					verbsInfo.add(verbInfo);
				}
			}
		}
	}	

	
	private List<IVerbInfo> processSintaticSentence(long startOffsetSentence,long endOffsetSentence,long documetStartOffset) throws SQLException, DatabaseLoadDriverException 
	{
		List<IVerbInfo> verbsInfo = new ArrayList<IVerbInfo>();
		AnnotationSet allAnnottaions = getGateDoc().getAnnotations();
		allAnnottaions = allAnnottaions.getContained(startOffsetSentence, endOffsetSentence);
		ISentenceSintaxRepresentation sentenceSintax= getSentenceSintax(allAnnottaions.get("Token"),startOffsetSentence,documetStartOffset);
		AnnotationSet vpAnnotations = allAnnottaions.get("VG");
		Iterator<Annotation> itAnnot = vpAnnotations.iterator();
		while(itAnnot.hasNext())
		{
			Annotation annot = itAnnot.next();
			DirectionallyEnum directional = DirectionallyEnum.LeftToRight;
			PolarityEnum polarity = PolarityEnum.Positive;
			long startAnnot = annot.getStartNode().getOffset();
			long endAnnot = annot.getEndNode().getOffset();
			String verb = getPartGateDocument(getGateDoc(), startAnnot, endAnnot);
			if(verbFilter == null)
			{
				findVerbs(verbsInfo, allAnnottaions, sentenceSintax, annot,
						directional, polarity, startAnnot, endAnnot, verb,documetStartOffset);
			}
			else if(!verbFilter.getLexicalWords().contains(verb))
			{
				findVerbs(verbsInfo, allAnnottaions, sentenceSintax, annot,
						directional, polarity, startAnnot, endAnnot, verb,documetStartOffset);
			}
		}	
		return verbsInfo;
	}
	
	private List<Long> getTerminations(long startOffsetSentence,long endOffsetSentence,Set<String> relationTerminations, long offsetCorrection)
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

	public void setGateDoc(Document doc) {
		this.gateDoc=doc;
		
	}

	
	public void cleanALL(){
		if(this.gateDoc!=null){ 
			Factory.deleteResource(gateDoc);
//			gateDoc.cleanup();
//			gateDoc.getAnnotations().clear();
//			try {
//				if(gateDoc.getDataStore()!= null)
//					gateDoc.getDataStore().close();
//			} catch (PersistenceException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(gateDoc.getFeatures()!=null)
//				gateDoc.getFeatures().clear();
//			gateDoc.setContent(null);
//			gateDoc=null;
		}
		if(this.tagger!=null){
			Factory.deleteResource(tagger);
//			tagger.cleanup();
//			try {
//				tagger.execute();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			tagger=null;
		}
		if(this.tokeniser!=null){
			Factory.deleteResource(tokeniser);
//			tokeniser.cleanup();
//			try {
//				tokeniser.execute();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			tokeniser =null;
		}
		if(this.sentenceSplitter != null){
			Factory.deleteResource(sentenceSplitter);

//			sentenceSplitter.cleanup();
//			try {
//				sentenceSplitter.execute();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			sentenceSplitter = null;
		}
		if(this.morph != null){
			Factory.deleteResource(morph);
//			morph.cleanup();
//			try {
//				morph.execute();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			morph = null;
		}
		if(this.regExpSentenceSplitter!=null){
//			regExpSentenceSplitter.cleanup();
			Factory.deleteResource(regExpSentenceSplitter);

//			try {
////				regExpSentenceSplitter.execute();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			regExpSentenceSplitter = null;
		}
		if(this.verbChunker!=null){
			Factory.deleteResource(verbChunker);
//			verbChunker.cleanup();
//			try {
//				verbChunker.execute();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			verbChunker = null;
		}
		
	}

	@Override
	public List<ISentence> getSentences() {
		List<ISentence> sentenceresult = new ArrayList<ISentence>();
		List<IGenericPair<Long, Long>> sentenceDelimiter = getDocumentSentencelimits();
		AnnotationSet allAnnottaions = getGateDoc().getAnnotations();
		for(IGenericPair<Long, Long> sentencesDelimite : sentenceDelimiter)
		{		
			allAnnottaions = allAnnottaions.getContained(sentencesDelimite.getX(), sentencesDelimite.getY());
			ISentenceSintaxRepresentation sentenceSintax= getSentenceSintax(allAnnottaions.get("Token"),sentencesDelimite.getX(),sentencesDelimite.getY());	
			List<IPOSToken> orderPosTokens = new ArrayList<IPOSToken>();
			Set<Long> keys = sentenceSintax.getSentenceSintax().keySet();
			for(Long keyOffset : keys)
			{
				GenericTriple<String, String, String> res = sentenceSintax.getSentenceSintax().get(keyOffset);
				orderPosTokens.add(new POSToken(keyOffset, keyOffset+res.getX().length(), res.getX(), res.getY()));
			}
			String sentenceText = new String();
			try {
				sentenceText = getGateDoc().getContent().getContent(sentencesDelimite.getX(), sentencesDelimite.getY()).toString();
			} catch (InvalidOffsetException e) {
				e.printStackTrace();
			}
			ISentence sentence = new Sentence(sentencesDelimite.getX(), sentencesDelimite.getY(), sentenceText, orderPosTokens );
			sentenceresult.add(sentence);
		}
		return sentenceresult;
	}	
	
	
}
