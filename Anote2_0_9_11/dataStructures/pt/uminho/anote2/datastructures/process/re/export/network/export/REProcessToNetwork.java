package pt.uminho.anote2.datastructures.process.re.export.network.export;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IDocumentSet;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.datastructures.annotation.ner.SimpleEntity;
import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColorStyleProperty;
import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColors;
import pt.uminho.anote2.datastructures.annotation.re.SimpleEvent;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.network.Atributes;
import pt.uminho.anote2.datastructures.process.network.Edge;
import pt.uminho.anote2.datastructures.process.network.GraphicsAtribute;
import pt.uminho.anote2.datastructures.process.network.NetWorkMetaData;
import pt.uminho.anote2.datastructures.process.network.Network;
import pt.uminho.anote2.datastructures.process.network.Node;
import pt.uminho.anote2.datastructures.process.re.RelationType;
import pt.uminho.anote2.datastructures.process.re.export.configuration.Cardinality;
import pt.uminho.anote2.datastructures.process.re.export.configuration.DocumentInformation;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import pt.uminho.anote2.process.IE.IRESchema;
import pt.uminho.anote2.process.IE.network.IAtributes;
import pt.uminho.anote2.process.IE.network.IEdge;
import pt.uminho.anote2.process.IE.network.IGraphicsAtribute;
import pt.uminho.anote2.process.IE.network.INetwork;
import pt.uminho.anote2.process.IE.network.INetworkMetaData;
import pt.uminho.anote2.process.IE.network.INode;
import pt.uminho.anote2.process.IE.network.XGMMLPolygnos;
import pt.uminho.anote2.process.IE.re.IRESchemaToNetwork;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.process.IE.re.export.configuration.ICardinality;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfiguration;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class REProcessToNetwork implements IRESchemaToNetwork{

	private boolean stop;
	private int nodeIDIncrement = 1;
	private int edgeIDIncrement = 1;
	private boolean directed;

		
	public REProcessToNetwork()
	{
		stop = false;
	}
	
	public INetwork<INode, IEdge> getNetwork(IRESchema rePorcess,IREToNetworkConfiguration configurations) throws SQLException, DatabaseLoadDriverException 
	{
		nodeIDIncrement = 1;
		edgeIDIncrement = 1;
		directed = false;
		if(configurations!=null && configurations.getAdvanceConfigurations() != null && configurations.getAdvanceConfigurations().isDirectedNetwork())
			directed = true;
		TreeMap<SimpleEntity,INode> mappingEntityNode = new TreeMap<SimpleEntity, INode>();
		TreeMap<SimpleEvent,IEdge> mappingEdgeEvent = new TreeMap<SimpleEvent, IEdge>();
		TreeMap<IEdge,DocumentInformation> simpeEventDocumentsinformation = new TreeMap<IEdge, DocumentInformation>();
		Map<Integer,String> docIDPmid = new HashMap<Integer, String>();
		Map<Integer,IResourceElement> termIDTerm = new HashMap<Integer, IResourceElement>();
		Map<Integer,IResourceElementSet<IResourceElement>> termIDSynonyms = new HashMap<Integer, IResourceElementSet<IResourceElement>>();
		Map<Integer,List<IExternalID>> externalIDs = new HashMap<Integer, List<IExternalID>>();
		IDictionary dic = new Dictionary(1, "", "");
		ICorpus corpus = rePorcess.getCorpus();
		IDocumentSet docs = corpus.getArticlesCorpus();
		Iterator<IDocument> itDocs =docs.iterator();
		int total = docs.size();
		int step = 0;
		long starttime = GregorianCalendar.getInstance().getTimeInMillis();
		while(itDocs.hasNext())
		{
			if(stop)
			{
				break;
			}
			IDocument doc = itDocs.next();
			IAnnotatedDocument annotDoc = processNodesDocument(mappingEntityNode,rePorcess, corpus,doc,configurations,dic, termIDTerm, termIDSynonyms, externalIDs);
			docIDPmid.put(annotDoc.getID(), annotDoc.getOtherID());
			processEdgesDocument(mappingEntityNode,mappingEdgeEvent,simpeEventDocumentsinformation,rePorcess, corpus,doc,configurations);
			memoryAndProgressAndTime(step, total, starttime);
			step++;
		}
		if(configurations!=null && configurations.getAdvanceConfigurations()!=null && !configurations.getAdvanceConfigurations().allowLonelyNodes())
		{
			removeAloneNodes(mappingEntityNode);
		}
		if(configurations!=null && configurations.getAdvanceConfigurations()!=null && configurations.getAdvanceConfigurations().exportRelationsDetails())
		{
			IAtributes atribute;
			IAtributes st;
			for(IEdge edge : simpeEventDocumentsinformation.keySet())
			{
				DocumentInformation documentInfo = simpeEventDocumentsinformation.get(edge);
				List<IAtributes> sentences = new ArrayList<IAtributes>();
				for(int docID : documentInfo.getDocumentIDRelationSetences().keySet())
				{
					String pmid = "";
					if(docIDPmid.containsKey(docID))
					{
						pmid = "PMID: "+docIDPmid.get(docID) +" " ;
					}
					else
					{
						pmid = "ID: "+docID +" ";
					}

					for(String sentence : documentInfo.getDocumentIDRelationSetences().get(docID))
					{
						st = new Atributes("", pmid + sentence, pmid + sentence, "string", false, null);
						sentences.add(st);
					}
				}
				atribute = new Atributes(null, "Event Details (PMID - Sentences)", "Event Details (PMID - Sentences)", "list", true, sentences);
				edge.addEdgeAAtributes(atribute);
			}
		}
		Collection<INode> nodes = mappingEntityNode.values();
		Collection<IEdge> edges = mappingEdgeEvent.values();
		if(stop)
		{
			return null;
		}		
		return builtNetwork(directed,rePorcess, new ArrayList<INode>(nodes), new ArrayList<IEdge>(edges));
	}



	private void removeAloneNodes(TreeMap<SimpleEntity, INode> mappingEntityNode) {
		List<SimpleEntity> toRemove = new ArrayList<SimpleEntity>();
		for(SimpleEntity simpleEnt:mappingEntityNode.keySet())
		{
			INode node = mappingEntityNode.get(simpleEnt);
			if(node.isNodeAlone())
			{
				toRemove.add(simpleEnt);
			}
		}
		for(SimpleEntity entremove:toRemove)
			mappingEntityNode.remove(entremove);
	}

	private void processEdgesDocument(TreeMap<SimpleEntity, INode> mappingEntityNode,TreeMap<SimpleEvent, IEdge> mappingEdgeEvent,
			TreeMap<IEdge, DocumentInformation> simpeEventDocumentsinformation, IRESchema rePorcess, ICorpus corpus, IDocument doc,
			IREToNetworkConfiguration configurations) throws SQLException, DatabaseLoadDriverException {
		IAnnotatedDocument annotDOc = new AnnotatedDocument(rePorcess, corpus, doc);
		List<IEventAnnotation> list = annotDOc.getEventAnnotations();
		List<IEntityAnnotation> entitiesAtLeft;
		List<IEntityAnnotation>entitiesAtRight;
		SimpleEntity entSimpleLeft;
		SimpleEntity entSimpleRight;
		SimpleEvent simpleEvent;
		IEdge edge;
		boolean writeWithAtribute = false;
		boolean isDirected = false;
		if(configurations!= null && configurations.getAdvanceConfigurations() != null)
		{
			writeWithAtribute = configurations.getAdvanceConfigurations().useGraphicWeights();
			isDirected = configurations.getAdvanceConfigurations().isDirectedNetwork();
		}
		for(IEventAnnotation event:list)
		{
			if(eventValidadion(event,mappingEntityNode,configurations))
			{
				entitiesAtLeft = event.getEntitiesAtLeft();
				entitiesAtRight = event.getEntitiesAtRight();
				for(IEntityAnnotation entLeft:entitiesAtLeft)
				{
					entSimpleLeft = new SimpleEntity(entLeft.getAnnotationValue(), entLeft.getClassAnnotationID(), entLeft.getResourceElementID());
					if(mappingEntityNode.containsKey(entSimpleLeft))
					{
						for(IEntityAnnotation entRight:entitiesAtRight)
						{
							entSimpleRight = new SimpleEntity(entRight.getAnnotationValue(), entRight.getClassAnnotationID(), entRight.getResourceElementID());
							
							if(mappingEntityNode.containsKey(entSimpleRight))
							{
								String clue = event.getEventProperties().getLemma();
								if(configurations!=null && configurations.getAdvanceConfigurations() != null && configurations.getAdvanceConfigurations().ignoreClues())
									clue = new String();
								if(event.getEventProperties().getDirectionally() == DirectionallyEnum.RightToLeft)
								{
									simpleEvent = new SimpleEvent(clue,entSimpleRight,entSimpleLeft,directed);
								}
								else 
								{
									simpleEvent = new SimpleEvent(clue,entSimpleLeft,entSimpleRight,directed);
								}
								if(validateSimpleEvent(simpleEvent,configurations))
								{
									String sentence = getSentence(annotDOc,entLeft.getStartOffset(),entRight.getEndOffset());
									if(mappingEdgeEvent.containsKey(simpleEvent))
									{
										mappingEdgeEvent.get(simpleEvent).increaseWeight();
										IEdge edgeAux = mappingEdgeEvent.get(simpleEvent);
										simpeEventDocumentsinformation.get(edgeAux).update(annotDOc.getID(), sentence);
									}
									else
									{
										IGraphicsAtribute graphicsAtribute = new GraphicsAtribute(1, OtherConfigurations.getVerbColor(), OtherConfigurations.getVerbColorBackGround(),null,writeWithAtribute, null, null);
										edge = new Edge(String.valueOf(edgeIDIncrement), simpleEvent.getClue(),mappingEntityNode.get(simpleEvent.getSourceEntity()) , mappingEntityNode.get(simpleEvent.getTargetEntity()),isDirected, new ArrayList<IAtributes>(), graphicsAtribute);
										mappingEdgeEvent.put(simpleEvent, edge);
										simpeEventDocumentsinformation.put(edge, new DocumentInformation());
										simpeEventDocumentsinformation.get(edge).update(annotDOc.getID(), sentence);
										if(simpleEvent.getSourceEntity().compareTo(simpleEvent.getTargetEntity()) != 0)
										{
											mappingEntityNode.get(simpleEvent.getSourceEntity()).setNodeAlone(false);
											mappingEntityNode.get(simpleEvent.getTargetEntity()).setNodeAlone(false);
										}
										edgeIDIncrement++;
									}
								}
							}
						}
					}
				}
			}
		}
		
	}

	private String getSentence(IAnnotatedDocument annotDOc, long startOffset,long endOffset) throws SQLException, DatabaseLoadDriverException {
		List<ISentence> sentences = annotDOc.getSentencesText();
		ISentence sentenceInit = findSentence(sentences,(int)startOffset);	
		ISentence sentenceEnd = findSentence(sentences,(int)endOffset);
		return annotDOc.getDocumetAnnotationText().substring((int)sentenceInit.getStartOffset(),(int)sentenceEnd.getEndOffset());
	}
	
	private ISentence findSentence(List<ISentence> sentences, int offset) {
		for(ISentence set:sentences)
		{
			if(set.getStartOffset() <= offset && offset <= set.getEndOffset())
			{
				return set;
			}
		}
		return null;
	}

	private boolean validateSimpleEvent(SimpleEvent simpleEvent,IREToNetworkConfiguration configurations) {
		IRelationsType type = new RelationType(simpleEvent.getSourceEntity().getClassID(), simpleEvent.getTargetEntity().getClassID());
		if(configurations!=null && configurations.getAdvanceConfigurations() !=null &&
				configurations.getAdvanceConfigurations().getRelationsType()!=null && configurations.getAdvanceConfigurations().getRelationsType().size()>0
				&& !configurations.getAdvanceConfigurations().getRelationsType().contains(type))
		{
			return false;
		}
		return true;
	}

	private boolean eventValidadion(IEventAnnotation event, TreeMap<SimpleEntity, INode> mappingEntityNode, IREToNetworkConfiguration configurations) {

		if(event.getEntitiesAtLeft().size() == 0 || event.getEntitiesAtRight().size() == 0)
		{
			return false;
		}
		String lemma = event.getEventProperties().getLemma();
		if(lemma==null)
			lemma = new String();
		if(configurations != null && configurations.getAdvanceConfigurations()!= null && configurations.getAdvanceConfigurations().getLemmaVerbsAllow()!=null &&
				configurations.getAdvanceConfigurations().getLemmaVerbsAllow().size()>0 && !configurations.getAdvanceConfigurations().getLemmaVerbsAllow().contains(lemma))
		{
			return false;
		}
		int leftEntities = getCardinallity(event.getEntitiesAtLeft());
		int rigtEntities = getCardinallity(event.getEntitiesAtRight());
		ICardinality cardidality = new Cardinality(leftEntities, rigtEntities);
		configurations.getRelationConfigurationOptions().getCardinalitiesAllowed().contains(cardidality);

		if(configurations!= null && configurations.getRelationConfigurationOptions()!=null &&
				configurations.getRelationConfigurationOptions().getCardinalitiesAllowed() !=null && 
				configurations.getRelationConfigurationOptions().getCardinalitiesAllowed().size() > 0 &&
				!configurations.getRelationConfigurationOptions().getCardinalitiesAllowed().contains(cardidality))
		{
			return false;
		}
		PolarityEnum polarity = event.getEventProperties().getPolarity();
		if(configurations!= null && configurations.getRelationConfigurationOptions()!=null 
				&& configurations.getRelationConfigurationOptions().getPolaritiesAllowed()!=null
				&& configurations.getRelationConfigurationOptions().getPolaritiesAllowed().size() > 0
				&& !configurations.getRelationConfigurationOptions().getPolaritiesAllowed().contains(polarity))
		{
			return false;
		}
		DirectionallyEnum directionally = event.getEventProperties().getDirectionally();
		if(configurations!= null && configurations.getRelationConfigurationOptions()!=null 
				&& configurations.getRelationConfigurationOptions().getDirectionallyAllowed()!=null
				&& configurations.getRelationConfigurationOptions().getDirectionallyAllowed().size() > 0
				&& !configurations.getRelationConfigurationOptions().getDirectionallyAllowed().contains(directionally))
		{
			return false;
		}
		return true;
	}

	private int getCardinallity(List<IEntityAnnotation> entities) {
		if(entities.size()==1)
		{
			return 1;
		}
		else if(entities.size()>1)
		{
			return 2;
		}
		return -1;
	}

	private IAnnotatedDocument processNodesDocument(TreeMap<SimpleEntity, INode> mappingEntityNode, IRESchema rePorcess,ICorpus corpus, IDocument doc, IREToNetworkConfiguration configurations,IDictionary dic,
			Map<Integer,IResourceElement> termIDTerm,Map<Integer,IResourceElementSet<IResourceElement>> termIDSynonyms,Map<Integer,List<IExternalID>> externalIDs) throws SQLException, DatabaseLoadDriverException {
		IAnnotatedDocument annotDOc = new AnnotatedDocument(rePorcess, corpus, doc);
		List<IEntityAnnotation> entities = annotDOc.getEntitiesAnnotations();
		boolean writeWithAtribute = false;
		if(configurations!= null && configurations.getAdvanceConfigurations() != null)
			writeWithAtribute = configurations.getAdvanceConfigurations().useGraphicWeights();
		for(IEntityAnnotation entity:entities)
		{
			SimpleEntity simplEnt = new SimpleEntity(entity.getAnnotationValue(),entity.getClassAnnotationID(),entity.getResourceElementID());
			if(configurations!=null && configurations.getEntityConfigutrationOptions()!=null && !configurations.getEntityConfigutrationOptions().getClassIdsAllowed().contains(simplEnt.getClassID()))
			{
				/**
				 * Entity Filter (Not added Node)
				 */

			}
			else if(mappingEntityNode.containsKey(simplEnt))
			{
				mappingEntityNode.get(simplEnt).increaseWeight();
			}
			else
			{
				AnnotationColorStyleProperty color = AnnotationColors.getInstanceWithoutAIBench().getClassColor2(simplEnt.getClassID());
				XGMMLPolygnos nodeType = XGMMLPolygnos.geDefault();
				if(configurations!=null && configurations.getAdvanceConfigurations() !=null && configurations.getAdvanceConfigurations().getXGMMLPolygnos()!=null &&
						configurations.getAdvanceConfigurations().getXGMMLPolygnos().containsKey(simplEnt.getClassID()))
				{
					nodeType = configurations.getAdvanceConfigurations().getXGMMLPolygnos().get(simplEnt.getClassID());
				}
				IGraphicsAtribute graphicsAtribute = new GraphicsAtribute(1,color.getColor(), "#000000",nodeType,writeWithAtribute,null, null);
				List<IAtributes> atributesList = new ArrayList<IAtributes>();
				String classe = ClassProperties.getClassIDClass().get(simplEnt.getClassID());
				atributesList.add(new Atributes(null, "class", classe, "string", false, null));
				if(entity.getResourceElementID()>0)
				{
					entityResourceInfo(configurations, dic, entity, simplEnt,atributesList,termIDTerm,termIDSynonyms,externalIDs);
				}
				INode node = new Node(nodeIDIncrement, simplEnt.getName(), simplEnt.getName(), atributesList , graphicsAtribute);
				mappingEntityNode.put(simplEnt,node);
				nodeIDIncrement ++;
			}
		}
		return annotDOc;
	}

	private void entityResourceInfo(IREToNetworkConfiguration configurations,
			IDictionary dic, IEntityAnnotation entity, SimpleEntity simplEnt,
			List<IAtributes> atributesList,Map<Integer,IResourceElement> termIDTerm,
			Map<Integer,IResourceElementSet<IResourceElement>> termIDSynonyms,
			Map<Integer,List<IExternalID>> externalIDs) throws DatabaseLoadDriverException, SQLException {
		if(!termIDSynonyms.containsKey(simplEnt.getResourceElementID()))
		{
			termIDSynonyms.put(simplEnt.getResourceElementID(), dic.getTermSynomns(simplEnt.getResourceElementID()));
		}
		if(!termIDTerm.containsKey(simplEnt.getResourceElementID()))
		{
			termIDTerm.put(simplEnt.getResourceElementID(), dic.getTerm(simplEnt.getResourceElementID()));
		}
		if(!externalIDs.containsKey(simplEnt.getResourceElementID()))
		{
			externalIDs.put(simplEnt.getResourceElementID(), dic.getexternalIDandSorceIDandSource(Integer.valueOf(simplEnt.getResourceElementID())));
		}
		IResourceElement elemTerm = termIDTerm.get(simplEnt.getResourceElementID());
		if(configurations!=null && configurations.getAdvanceConfigurations() != null && configurations.getAdvanceConfigurations().isEntitiesRepresentedByPrimaryName())
		{
			elemTerm = new ResourceElement(elemTerm.getID(),simplEnt.getName(),elemTerm.getTermClassID(),elemTerm.getTermClass(),elemTerm.isActive());
			simplEnt.setName(elemTerm.getTerm());
		}	
		putSynonyms(entity, simplEnt, atributesList, termIDSynonyms.get(simplEnt.getResourceElementID()), elemTerm);
		putExternalIDs(atributesList, externalIDs.get(simplEnt.getResourceElementID()));
	}

	private void putSynonyms(IEntityAnnotation entity, SimpleEntity simplEnt,
			List<IAtributes> atributesList,
			IResourceElementSet<IResourceElement> list,
			IResourceElement elemTerm) {
		if(list.size()>0)
		{
			List<IAtributes> synonyms = new ArrayList<IAtributes>();
			for(IResourceElement elem :list.getElements())
			{
				if(!elem.getTerm().equals(entity.getAnnotationValue()))
					synonyms.add(new Atributes("", elem.getTerm(), elem.getTerm(), "string", false, null));
			}
			if(elemTerm!=null && !elemTerm.getTerm().equals(simplEnt.getName()))
				synonyms.add(new Atributes("", elemTerm.getTerm(), elemTerm.getTerm(), "string", false, null));
			atributesList.add(new Atributes(null, "synonyms", "synonyms", "list", true, synonyms));
		}
	}

	private void putExternalIDs(List<IAtributes> atributesList,List<IExternalID> externalIDSource) {
		if(externalIDSource.size()>0)
		{
			List<IAtributes> extIdsAtrinutes = new ArrayList<IAtributes>();
			for(IExternalID extID :externalIDSource )
			{
				extIdsAtrinutes.add(new Atributes(null,extID.getSource(), extID.getExternalID() + " ( "+extID.getSource()+" )", "string", false, null));
			}
			atributesList.add(new Atributes(null, "External (Database) Ids", "External (Database) Ids", "list", true, extIdsAtrinutes ));
		}
	}


	
	private static INetwork<INode, IEdge> builtNetwork(boolean directed, IRESchema rePorcess,List<INode> nodes, List<IEdge> edges) {	
		String label = "ID : "+rePorcess.getID() + " ( "+rePorcess.getType() + " ) ";
		INetworkMetaData metaData = new NetWorkMetaData("@Note2 - RE Schema", "",GregorianCalendar.getInstance().getTime(), "@Note2 - RE Schema "+label,"@Note2 - RE Schema", ".xgmml");
		String id = label;
		String version = "1.0";
		List<IAtributes> atributesList = new ArrayList<IAtributes>();
		return new Network(id, nodes, edges, label, version, directed, metaData, atributesList);
	}
	
	protected void memoryAndProgressAndTime(int step, int total,long startTime) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
	}

	public void setcancel() {
		stop = true;
	}

}
