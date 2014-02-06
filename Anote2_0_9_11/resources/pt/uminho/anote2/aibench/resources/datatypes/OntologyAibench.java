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
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.exceptions.resources.ontologies.OntologyMissingRootExeption;
import pt.uminho.anote2.datastructures.resources.ontology.Ontology;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.tree.InterfaceTreeNode;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;

@Datatype(structure = Structure.SIMPLE,namingMethod="getNameLook",setNameMethod="setName",removable=true,renamed=true,autoOpen=true)
public class OntologyAibench extends Ontology{
	
	private TreeOntology<IResourceElement> ontologyTree;

	public OntologyAibench(int id, String name, String info) {
		super(id, name, info);
	}
	
	public String getNameLook()
	{
		return getName();
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}
	
	
	
	public void setName(String name)
	{
		if(name.equals(getName()))
		{
			
		}
		else if(name==null  || name.length() == 0)
		{
			this.setChanged();
			this.notifyObservers();
			Workbench.getInstance().warn("Ontology Name can not be empty");	
		}
		else
		{
			try {
				super.setName(name);
				this.notifyViewObservers();
				List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Ontologies.class);
				Ontologies dics = (Ontologies) items.get(0).getUserData();
				dics.notifyViewObservers();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
		}
	}

	public TreeOntology<IResourceElement> getOntologyTree() throws SQLException, OntologyMissingRootExeption, DatabaseLoadDriverException {
		ontologyTree = new TreeOntology<IResourceElement>();
		InterfaceTreeNode<IResourceElement> processOntologyElems = processOntologyElements();
		ontologyTree.setRootNode(processOntologyElems);
		return ontologyTree;
	}
	
	public InterfaceTreeNode<IResourceElement> processOntologyElements() throws SQLException,OntologyMissingRootExeption, DatabaseLoadDriverException
	{
		Map<Integer,TreeNodeOntology<IResourceElement>> jtreeNodes = new HashMap<Integer, TreeNodeOntology<IResourceElement>>();
		createElements(jtreeNodes);
		Map<Integer,Integer> sunFather = new HashMap<Integer, Integer>();
		Map<Integer,List<Integer>> isArelations = getIsARelations(sunFather);
		int id = getRootID(sunFather);
		if(id==-1)
		{
			throw new OntologyMissingRootExeption();
		}
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
		if(sunFather.keySet().size()==0)
		{
			return -1;
		}
		else
		{
			int id = sunFather.keySet().iterator().next();
			while(sunFather.containsKey(id))
			{
				id = sunFather.get(id);
			}
			return id;
		}
	}

	private void buildtree(Map<Integer, TreeNodeOntology<IResourceElement>> jtreeNodes,
			Map<Integer, List<Integer>> isArelations,
			int id, Set<Integer> vistors) {
		if(vistors.contains(id) || !isArelations.containsKey(id)  )
		{
			return;
		}
		else
		{
			vistors.add(id);
			TreeNodeOntology<IResourceElement> root = jtreeNodes.get(id);
			for(Integer sunID:isArelations.get(id))
			{
				TreeNodeOntology<IResourceElement> node = jtreeNodes.get(sunID);
				node.setParent(root);
				root.setChild(root.getNumberOfChildren(), node);
				buildtree(jtreeNodes,isArelations,sunID,vistors);
			}
		}
	}

	private Map<Integer, List<Integer>> getIsARelations(Map<Integer, Integer> sunFather) throws SQLException, DatabaseLoadDriverException {
		Map<Integer, List<Integer>> relations = new HashMap<Integer, List<Integer>>();
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectRelationsOntology);
		ps.setInt(1,getID());
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
		rs.close();
		ps.close();
		return relations;
	}

	private Map<Integer, IResourceElement> createElements(Map<Integer, TreeNodeOntology<IResourceElement>> jtreeNodes) throws SQLException, DatabaseLoadDriverException {
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
