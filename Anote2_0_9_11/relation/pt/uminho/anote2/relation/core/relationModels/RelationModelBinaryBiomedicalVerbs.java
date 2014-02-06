package pt.uminho.anote2.relation.core.relationModels;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.utils.IGenericPair;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.clue.IVerbInfo;
import pt.uminho.anote2.relation.configuration.IRERelationAdvancedConfiguration;
import pt.uminho.anote2.relation.core.relationModels.specialproperties.VerbClassificationInSentenceEnum;
import pt.uminho.anote2.resource.lexicalwords.ILexicalWords;
import pt.uminho.gate.process.IGatePosTagger;

public class RelationModelBinaryBiomedicalVerbs extends RelationModelBinaryVerbLimitation {

	private ILexicalWords biomedicalVerbs;
	private Set<String> rejectedVerbs;
	private Set<String> aceptedVerbs;
	
	public RelationModelBinaryBiomedicalVerbs(ICorpus corpus,IIEProcess nerProcess, IGatePosTagger postagger,ILexicalWords biomedicalVerbs,IRERelationAdvancedConfiguration advancedConfiguration) {
		super(corpus, nerProcess, postagger,advancedConfiguration);
		this.biomedicalVerbs = biomedicalVerbs;
		this.rejectedVerbs = new HashSet<String>();
		this.aceptedVerbs = new HashSet<String>();
	}
	
	public List<IEventAnnotation> extractSentenceRelation(IAnnotatedDocument document,List<IEntityAnnotation> semanticLayer, IGenericPair<List<IVerbInfo>, List<Long>> sintatic) throws SQLException, DatabaseLoadDriverException{
		Map<Long, IEntityAnnotation> treeEntitiesPositions = RelationModelutils.getEntitiesPosition(semanticLayer); // offset->entityID
		List<IVerbInfo> verbsSubSetInfo = filterVerbs(sintatic.getX());
		Map<Long, IVerbInfo> treeverbSubsetPositions = getPostagger().getVerbsPosition(verbsSubSetInfo); 
		List<IVerbInfo> allverbsList = sintatic.getX();
		Map<Long, IVerbInfo> treeAllverbPositions = getPostagger().getVerbsPosition(allverbsList); 
		Set<Long> setAllVerbsPositions = treeAllverbPositions.keySet();
		List<Long> listAllverbsPosition = new ArrayList<Long>(setAllVerbsPositions);
		Set<Long> entitiesSet = treeEntitiesPositions.keySet();
		List<Long> entitiesList = new ArrayList<Long>(entitiesSet);
		List<IEventAnnotation> relations = new ArrayList<IEventAnnotation>();	
		for(int i=0;i<listAllverbsPosition.size();i++)
		{
			if(treeverbSubsetPositions.keySet().contains(listAllverbsPosition.get(i)))
			{
				if(entitiesList.size()>1)
				{
					if(i==0)
					{
						// Just one verb
						if(i==listAllverbsPosition.size()-1)
						{
							extractRelation(document,relations,listAllverbsPosition.get(i),entitiesList.get(0),entitiesList.get(entitiesList.size()-1),
									treeverbSubsetPositions,entitiesList,treeEntitiesPositions,VerbClassificationInSentenceEnum.UNIQUE);
						}
						// The first One
						else
						{

							extractRelation(document,relations,listAllverbsPosition.get(i),entitiesList.get(0),listAllverbsPosition.get(i+1),treeverbSubsetPositions,entitiesList,
									treeEntitiesPositions,VerbClassificationInSentenceEnum.FIRST);
						}			
					}
					// last verb
					else if(i==listAllverbsPosition.size()-1)
					{
						extractRelation(document,relations,listAllverbsPosition.get(i),listAllverbsPosition.get(i-1),entitiesList.get(entitiesList.size()-1),
								treeverbSubsetPositions,entitiesList,treeEntitiesPositions,VerbClassificationInSentenceEnum.LAST);

					}
					// verb in the middle of text
					else
					{
						extractRelation(document,relations,listAllverbsPosition.get(i),listAllverbsPosition.get(i-1),listAllverbsPosition.get(i+1),treeverbSubsetPositions,entitiesList
								,treeEntitiesPositions,VerbClassificationInSentenceEnum.MIDDLE);

					}
				}	
			}
		}
		return relations;
	}

	protected List<IVerbInfo> filterVerbs(List<IVerbInfo> verbsToFilter) throws SQLException, DatabaseLoadDriverException {
		List<IVerbInfo> result = new ArrayList<IVerbInfo>();
		for(IVerbInfo verbInfo:verbsToFilter)
		{
			String verb = verbInfo.getVerb();
			if(!rejectedVerbs.contains(verb))
			{
				if(aceptedVerbs.contains(verb ) || biomedicalVerbs.getLexicalWords().contains(verb) || getPartialMatchingVerbs(verb))
				{
					aceptedVerbs.add(verb);
					result.add(verbInfo);
				}
				else
				{
					rejectedVerbs.add(verb);
				}
			}
		}
		return result;
	}

	private boolean getPartialMatchingVerbs(String verbCandidate) throws SQLException, DatabaseLoadDriverException {
		for(String verb:biomedicalVerbs.getLexicalWords())
		{
			if(verbCandidate.contains(" "+verb) || verbCandidate.contains(verb+" "))
			{
				return true;
			}
		}
		return false;
	}
	
	public ILexicalWords getBiomedicalVerbs() {
		return biomedicalVerbs;
	}

	public Set<String> getRejectedVerbs() {
		return rejectedVerbs;
	}

	public Set<String> getAceptedVerbs() {
		return aceptedVerbs;
	}

}
