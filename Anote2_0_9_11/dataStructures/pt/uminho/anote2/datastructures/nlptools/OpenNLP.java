package pt.uminho.anote2.datastructures.nlptools;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.AbstractBottomUpParser;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;
import pt.uminho.anote2.core.document.IChunkerToken;
import pt.uminho.anote2.core.document.IPOSToken;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.core.document.ITextSegment;
import pt.uminho.anote2.datastructures.documents.ChunkerToken;
import pt.uminho.anote2.datastructures.documents.POSToken;
import pt.uminho.anote2.datastructures.documents.Sentence;
import pt.uminho.anote2.datastructures.textprocessing.TermSeparator;

/**
 * Class that represents a OpenNLP Tagging 
 * 
 * @author Hugo Costa
 *
 */
public class OpenNLP {
	
	private final static String sentenceModelFile = "nlpmodels/en-sent.bin";
	private final static String tokeniserModelFile = "nlpmodels/en-token.bin";
	private final static String postaggingModelFile = "nlpmodels/en-pos-maxent.bin";
	private final static String parsingModelFile = "nlpmodels/en-parser-chunking.bin";
	private final static String chunkerModelFile = "nlpmodels/en-chunker.bin";


	private SentenceModel sentenceModel;
	private POSModel postaggerModel;
	private TokenizerModel tokeniserModel; 
	private ChunkerModel chunkerModel;
	private ParserModel parserModel;
	private Parser parser;

	
	public OpenNLP()
	{
		
	}
	
	private void initChunkerModelModel(){
		try {
			InputStream modelIn = new FileInputStream(chunkerModelFile);
			chunkerModel = new ChunkerModel(modelIn);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initParserModelModel(){
		try {
			InputStream parsingModelIn = new FileInputStream(parsingModelFile);
			parserModel = new ParserModel(parsingModelIn);
			parser = ParserFactory.create(parserModel);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initSentenceModel() 
	{
		InputStream modelIn;
		try {
			modelIn = new FileInputStream(sentenceModelFile);
			sentenceModel = new SentenceModel(modelIn);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initTokenizerModel()
	{
		InputStream tokenizerFileInput;
		try {
			tokenizerFileInput = new FileInputStream(tokeniserModelFile);
			tokeniserModel = new TokenizerModel(tokenizerFileInput);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initPosTagModel()
	{
		InputStream postaggerFileInput;
		try {
			postaggerFileInput = new FileInputStream(postaggingModelFile);
			postaggerModel = new POSModel(postaggerFileInput);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sentence Splitter. Return a List of {@link ISentence}
	 * 
	 * @param text
	 * @return
	 */
	public List<ISentence> getSentencesText(String text)
	{
		if(sentenceModel==null)
			initSentenceModel();
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
		Span sentences[] = sentenceDetector.sentPosDetect(text);
		List<ISentence> sents = new ArrayList<ISentence>();
		for(Span sent:sentences)
		{
			Sentence sen = new Sentence(sent.getStart(), sent.getEnd(), text.substring((int) sent.getStart(), (int) sent.getEnd()));
			sents.add(sen);
		}	
		return sents;
	}
	
	/**
	 * Return a {@link ISentence} for text with POS Tagging
	 * 
	 * @param text
	 * @param startOffset
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ISentence posTaggingSentence(String text, long startOffset) throws InvalidFormatException, IOException
	{
		List<IPOSToken> orderPosTokens = new ArrayList<IPOSToken>();
		if(tokeniserModel==null)
			initTokenizerModel();
		Tokenizer tokenizer = new TokenizerME(tokeniserModel);
		Span tokenSpans[] = tokenizer.tokenizePos(text);
		String[] tokens = new String[tokenSpans.length];
		for(int i=0;i<tokenSpans.length;i++)
		{
			tokens[i] = tokenSpans[i].getCoveredText(text).toString();
		}
		if(postaggerModel==null)
			initPosTagModel();
		POSTaggerME postagger = new POSTaggerME(postaggerModel);
		String[] categories = postagger.tag(tokens);
		for(int j=0;j<categories.length;j++)
		{
			IPOSToken e = new POSToken(tokenSpans[j].getStart()+startOffset, tokenSpans[j].getEnd()+startOffset, tokens[j], categories[j]);
			orderPosTokens.add(e);
		}
		ISentence sentence = new Sentence(startOffset, startOffset + text.length(), text, orderPosTokens);
		return sentence;
	}
	
	/**
	 * POS Tagging. Return a List of {@link ISentence} with {@link IPOSToken} tokens
	 * 
	 * @param text
	 * @return
	 */
	public List<ISentence> getSetencesWhitPOSTagging(String text) 
	{
		List<ISentence> sentences = getSentencesText(text);
		List<ISentence> sentencesResult = new ArrayList<ISentence>();
		for(ISentence setence : sentences)
		{
			long start = setence.getStartOffset();
			ISentence newSentencePostagas;
			try {
				newSentencePostagas = posTaggingSentence(setence.getText(),start);
				sentencesResult.add(new Sentence(setence.getStartOffset(), setence.getStartOffset(), text.substring((int)setence.getStartOffset(),(int)setence.getEndOffset()),newSentencePostagas.getOrderPOSTokens()));
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sentencesResult;
	}
	
	
	/**
	 * Shallow Parsing. Return a List of {@link ISentence} with {@link IChunkerToken}
	 * 
	 * @param text
	 * @return
	 */
	public List<ISentence> getSentenceOrderChunkers(List<ISentence> sentences)
	{
		if(chunkerModel==null)
			initChunkerModelModel();

		List<ISentence> sentencesWithChunker = new ArrayList<ISentence>();
		for(ISentence sentence : sentences)
		{
			if(sentence.getOrderPOSTokens()!=null && sentence.getOrderPOSTokens().size() > 0)
			{
				ChunkerME chunker = new ChunkerME(chunkerModel);
				List<IPOSToken> posTAgs = sentence.getOrderPOSTokens();
				String[] tags = new String[posTAgs.size()];
				String[] toks = new String[posTAgs.size()];
				for(int i=0;i<posTAgs.size();i++)
				{
					tags[i] = posTAgs.get(i).getPOSCategory();
					toks[i] = posTAgs.get(i).getText();
				}
				Span[] chunkers = chunker.chunkAsSpans(toks, tags);
				List<IChunkerToken> chunkerTokensSentence = new ArrayList<IChunkerToken>();
				for(int j=0;j<chunkers.length;j++)
				{
					Span span = chunkers[j];
					int posTagStart = span.getStart();
					int posTagEnd = span.getEnd();
					String chunkCatagory = span.getType();
					long endOffset = posTAgs.get(posTagEnd-1).getEndOffset() - sentence.getStartOffset();
					long startOffset = posTAgs.get(posTagStart).getStartOffset() - sentence.getStartOffset();
					String chunkertext = sentence.getText().substring((int)startOffset,(int)endOffset);
					IChunkerToken chunk = new ChunkerToken(startOffset, endOffset, chunkCatagory, chunkertext);
					chunkerTokensSentence.add(chunk);		
				}
				ISentence newsentence = new Sentence(sentence.getStartOffset(), sentence.getEndOffset(), sentence.getText(),posTAgs,chunkerTokensSentence);
				sentencesWithChunker.add(newsentence );
			}
			else
			{
				sentencesWithChunker.add(sentence);
			}

		}
		return sentencesWithChunker;
	}
	
	public List<ISentence> getSentenceParserResults(List<ISentence> sentences)
	{
		if(parserModel==null)
			initParserModelModel();
		List<ISentence> sentencesWithChunker = new ArrayList<ISentence>();
		for(ISentence sentence : sentences)
		{
			
			Parse topParses[] = ParserTool.parseLine(sentence.getText(), parser, 1);
			for(int i=0;i<topParses.length;i++)
			{
				 Parse parserRes = topParses[i];
				 System.out.println(show(parserRes));
			}
		}
		return sentencesWithChunker;
	}
	
	
	  private String show(Parse parserRes) {
		    StringBuffer sb = new StringBuffer(parserRes.getText().length()*4);
		    show(parserRes,sb);
		    return sb.toString();
	  }
	  
	  private void show(Parse parserRes,StringBuffer sb) {
		  int start;
		  start = parserRes.getSpan().getStart();
		  if (!parserRes.getType().equals(AbstractBottomUpParser.TOK_NODE)) {
			  sb.append("(");
			  sb.append(parserRes.getType()).append(" ");
		  }
		  for (Parse c : parserRes.getChildren()) {
			  Span s = c.getSpan();
			  if (start < s.getStart()) {
				  sb.append(encodeToken(parserRes.getText().substring(start, s.getStart())));
			  }
			  c.show(sb);
			  start = s.getEnd();
		  }
		  if (start < parserRes.getSpan().getEnd()) {
			  sb.append(encodeToken(parserRes.getText().substring(start, parserRes.getSpan().getEnd())));
		  }
		  if (!parserRes.getType().equals(AbstractBottomUpParser.TOK_NODE)) {
			  sb.append(")");
		  }
	  }

	  
	  
	  private static String encodeToken(String token) {
		  if (Parse.BRACKET_LRB.equals(token)) {
			  return "-LRB-";
		  }
		  else if (Parse.BRACKET_RRB.equals(token)) {
			  return "-RRB-";
		  }
		  else if (Parse.BRACKET_LCB.equals(token)) {
			  return "-LCB-";
		  }
		  else if (Parse.BRACKET_RCB.equals(token)) {
			  return "-RCB-";
		  }

		  return token;
	  }

	public List<ITextSegment> geTextSegmentsFilterByPOSTags(String text,Set<String> posTags) {
		List<ITextSegment> segment = new ArrayList<ITextSegment>();
		List<ISentence> sentences = getSentencesText(text);
		for(ISentence sentence:sentences)
		{
			long start = sentence.getStartOffset();
			ISentence newSentencePostagas;
			try {
				newSentencePostagas = posTaggingSentence(sentence.getText(),start);
				List<ITextSegment> sentencesAux = searchContinuosSegments(newSentencePostagas,posTags,text);
				for(ITextSegment sentAux:sentencesAux)
				{
					segment.add(sentAux);
				}
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return segment;
	}
	
	private List<ITextSegment> searchContinuosSegments(ISentence sentence,Set<String> posTags,String originalText) {
		List<IPOSToken> tokens = sentence.getOrderPOSTokens();
		List<ITextSegment> list = new ArrayList<ITextSegment>();
		long start = sentence.getStartOffset();
		long end = sentence.getStartOffset();
		for(int i=0;i<tokens.size();i++)
		{
			if(posTags.contains(tokens.get(i).getPOSCategory()))
			{
				end = tokens.get(i).getEndOffset();
				if(i+1>=tokens.size() && end-start>1)
				{
					ITextSegment e = new TextSegments(start, end, originalText.substring((int)start, (int) end));
					list.add(e);
				}
			}
			else
			{
				if(i+1<tokens.size() && start!=end && end-start>1)
				{

					ITextSegment e = new TextSegments(start, end, originalText.substring((int)start, (int) end));
					list.add(e);
					start = tokens.get(i+1).getStartOffset();
					end = start;
				}
				else if(i+1>=tokens.size())
				{
					
				}
				else
				{
					start = tokens.get(i+1).getStartOffset();
					end = start;
				}
			}
		}
		
		return list;
	}

	public static void main(String[] args) throws InvalidFormatException, IOException {
		OpenNLP nlp = new OpenNLP();
		String text = "Escherichia coli is an enteric bacterium that is capable of growing over a wide range of pH values (pH 5-9) and, incredibly, is able to survive extreme acid stresses including passage through the mammalian stomach where the pH can fall to as low as pH 1-2. To enable such a broad range of acidic pH survival, E. coli possesses four different inducible amino acid decarboxylases that decarboxylate their substrate amino acids in a proton-dependent manner thus raising the internal pH. The decarboxylases include the glutamic acid decarboxylases GadA and GadB, the arginine decarboxylase AdiA, the lysine decarboxylase LdcI and the ornithine decarboxylase SpeF. All of these enzymes utilize pyridoxal-5'-phosphate as a co-factor and function together with inner-membrane substrate-product antiporters that remove decarboxylation products to the external medium in exchange for fresh substrate. In the case of LdcI, the lysine-cadaverine antiporter is called CadB. Recently, we determined the X-ray crystal structure of LdcI to 2.0 Å, and we discovered a novel small-molecule bound to LdcI the stringent response regulator guanosine 5'-diphosphate,3'-diphosphate (ppGpp). The stringent response occurs when exponentially growing cells experience nutrient deprivation or one of a number of other stresses. As a result, cells produce ppGpp which leads to a signaling cascade culminating in the shift from exponential growth to stationary phase growth. We have demonstrated that ppGpp is a specific inhibitor of LdcI. Here we describe the lysine decarboxylase assay, modified from the assay developed by Phan et al., that we have used to determine the activity of LdcI and the effect of pppGpp/ppGpp on that activity. The LdcI decarboxylation reaction removes the α-carboxy group of L-lysine and produces carbon dioxide and the polyamine cadaverine (1,5-diaminopentane). L-lysine and cadaverine can be reacted with 2,4,6-trinitrobenzensulfonic acid (TNBS) at high pH to generate N,N'-bistrinitrophenylcadaverine (TNP-cadaverine) and N,N'-bistrinitrophenyllysine (TNP-lysine), respectively. The TNP-cadaverine can be separated from the TNP-lysine as the former is soluble in organic solvents such as toluene while the latter is not. The linear range of the assay was determined empirically using purified cadaverine.";
		Set<String> posTags = new HashSet<String>();
		for(PartOfSpeechLabels label:PartOfSpeechLabels.values())
		{
			if(label.getEnableDefaultValue())
			{
				posTags.add(label.value());
			}
		}
		nlp.geTextSegmentsFilterByPOSTags(TermSeparator.termSeparator(text),posTags);
	}

	

}
