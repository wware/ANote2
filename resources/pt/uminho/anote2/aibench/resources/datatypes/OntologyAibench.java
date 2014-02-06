package pt.uminho.anote2.aibench.resources.datatypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.aibench.resources.datastructures.tree.TreeNodeOntology;
import pt.uminho.anote2.aibench.resources.datastructures.tree.TreeOntology;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.ontology.Ontology;
import pt.uminho.anote2.datastructures.utils.tree.InterfaceTreeNode;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.SIMPLE,namingMethod="getNameLook")
public class OntologyAibench extends Ontology{
	
	private TreeOntology<IResourceElement> ontologyTree;

	public OntologyAibench(IDatabase db, int id, String name, String info) {
		super(db, id, name, info);
	}
	
	public String getNameLook()
	{
		return getName();
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}

	public TreeOntology<IResourceElement> getOntologyTree() throws SQLException {
		ontologyTree = new TreeOntology<IResourceElement>();
		ontologyTree.setRootNode(processOntologyElements());
		return ontologyTree;
	}
	
	public InterfaceTreeNode<IResourceElement> processOntologyElements() throws SQLException
	{
		Map<Integer,TreeNodeOntology<IResourceElement>> jtreeNodes = new HashMap<Integer, TreeNodeOntology<IResourceElement>>();
		createElements(jtreeNodes);
		Map<Integer,Integer> sunFather = new HashMap<Integer, Integer>();
		Map<Integer,List<Integer>> isArelations = getIsARelations(sunFather);
		int id = getRootID(sunFather);
		Set<Integer> vistors = new HashSet<Integer>();
		vistors.add(id);
		for(Integer sunID:isArelations.get(id))
		{
			TreeNodeOntology<IResourceElement> node = jtreeNodes.get(sunID);
			node.setParent(jtreeNodes.get(id));
			jtreeNodes.get(id).setChild(jtreeNodes.get(id).getNumberOfChildren(), node);
			buildtree(jtreeNodes,isArelations,sunID,vistors);
			
		}
		return jtreeNodes.get(id);
	}

	private int getRootID(Map<Integer, Integer> sunFather) {
		int id = sunFather.keySet().iterator().next();
		while(sunFather.containsKey(id))
		{
			id = sunFather.get(id);
		}
		return id;
	}

	private void buildtree(Map<Integer, TreeNodeOntology<IResourceElement>> jtreeNodes,
			Map<Integer, List<Integer>> isArelations,
			int id, Set<Integer> vistors) {
		
		TreeNodeOntology<IResourceElement> root = jtreeNodes.get(id);
		if(!isArelations.containsKey(id) || vistors.contains(id))
		{
			return;
		}
		else
		{
			vistors.add(id);
			for(Integer sunID:isArelations.get(id))
			{
				TreeNodeOntology<IResourceElement> node = jtreeNodes.get(sunID);
				node.setParent(root);
				root.setChild(root.getNumberOfChildren(), node);
				buildtree(jtreeNodes,isArelations,sunID,vistors);
			}
		}
	}

	private Map<Integer, List<Integer>> getIsARelations(Map<Integer, Integer> sunFather) throws SQLException {
		Map<Integer, List<Integer>> relations = new HashMap<Integer, List<Integer>>();
		PreparedStatement ps = getDb().getConnection().prepareStatement(QueriesResources.selectRelationsOntology);
		ps.setInt(1,getId());
		ResultSet rs = ps.executeQuery();
		int fatherID,sunID;
		List<Integer> list;
		while(rs.next())
		{
			fatherID = rs.getInt(1);
			sunID = rs.getInt(2);
			if(relations.containsKey(fatherID))
			{
				relations.get(fatherID).add(sunID);
				sunFather.put(sunID, fatherID);
			}
			else
			{
				list = new ArrayList<Integer>();
				list.add(sunID);
				relations.put(fatherID,list);
				sunFather.put(sunID, fatherID);
			}
		}
		return relations;
	}

	private Map<Integer, IResourceElement> createElements(Map<Integer, TreeNodeOntology<IResourceElement>> jtreeNodes) {
		IResourceElementSet<IResourceElement> elements = getResourceElements();
		Map<Integer,IResourceElement> result = new HashMap<Integer, IResourceElement>();
		for(IResourceElement re:elements.getElements())
		{
			result.put(re.getID(), re);
			TreeNodeOntology<IResourceElement> node = new TreeNodeOntology<IResourceElement>(re);
			jtreeNodes.put(re.getID(),node);
		}
		return result;
	}

}
