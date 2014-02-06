package pt.uminho.anote2.datastructures.process.re.export.configuration;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import pt.uminho.anote2.process.IE.network.XGMMLPolygnos;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationAdvanceOptions;

public class REToNetworkConfigurationAdvanceOptions implements IREToNetworkConfigurationAdvanceOptions{

	private Set<String> lemmaAllowed;
	private boolean allowLonelyNodes;
	private boolean directed;
	private boolean entitiesRepresentedByprimaryNAme;
	private boolean ignoreClues;
	private boolean exportRelationsDetails;
	private boolean useGraphicWeights;

	private SortedSet<IRelationsType> relationTypesAllowed;	
	private Map<Integer, XGMMLPolygnos> classXGMMLPolygnos;
	
	public REToNetworkConfigurationAdvanceOptions(Set<String> lemmaAllowed,boolean allowLonelyNodes,boolean directed,boolean entitiesRepresentedByprimaryNAme, boolean ignoreClues,
			SortedSet<IRelationsType> relationTypesAllowed,boolean exportRelationDetails,boolean useGraphicWeights,Map<Integer, XGMMLPolygnos> classXGMMLPolygnos) {
		super();
		this.lemmaAllowed = lemmaAllowed;
		this.allowLonelyNodes = allowLonelyNodes;
		this.directed = directed;
		this.entitiesRepresentedByprimaryNAme = entitiesRepresentedByprimaryNAme;
		this.ignoreClues = ignoreClues;
		this.relationTypesAllowed = relationTypesAllowed;
		this.exportRelationsDetails = exportRelationDetails;
		this.useGraphicWeights = useGraphicWeights;
		this.classXGMMLPolygnos = classXGMMLPolygnos;
	}
	
	public Set<String> getLemmaVerbsAllow() {
		return lemmaAllowed;
	}

	public boolean allowLonelyNodes() {
		return allowLonelyNodes;
	}

	public SortedSet<IRelationsType> getRelationsType() {
		return relationTypesAllowed;
	}
	
	public boolean isDirectedNetwork() {
		return directed;
	}

	public boolean isEntitiesRepresentedByPrimaryName() {
		return entitiesRepresentedByprimaryNAme;
	}

	public boolean ignoreClues() {
		return ignoreClues;
	}
	

	public boolean exportRelationsDetails() {
		return exportRelationsDetails;
	}

	public Map<Integer, XGMMLPolygnos> getXGMMLPolygnos() {
		return classXGMMLPolygnos;
	}

	public boolean useGraphicWeights() {
		return useGraphicWeights;
	}

}
