package pt.uminho.anote2.datastructures.resources.ontology.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.report.resources.PortResourceUpdateReport;
import pt.uminho.anote2.datastructures.report.resources.ResourceMergeConflits;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourcesManagement;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.ontologies.IOntology;
import pt.uminho.anote2.resource.ontologies.IOntologyLoader;


public class LoadOBOOntologies extends PortResourceUpdateReport implements IOntologyLoader{
	
	private Map<String,OntologicalClass> ontologicatermIDDetails;
	private String version;
	private IOntology ontology;
	private BufferedReader br;
	
	public LoadOBOOntologies(IOntology ontology,String font,String oboVersion)
	{
		super(ontology, font);
		this.ontology=ontology;
		this.version= oboVersion;
		this.ontologicatermIDDetails = new HashMap<String, OntologicalClass>();
	}
	
	protected void loadFile(File file) throws IOException
	{
		FileReader fr;
		BufferedReader br;
		fr = new FileReader(file);
		br = new BufferedReader(fr);		
		String line,id = new String(),name = new String(),def = new String();
		List<String> isA = new ArrayList<String>();
		List<String> syns = new ArrayList<String>();
		OntologicalClass classe;
		while((line = br.readLine())!=null)
		{
			if(line.contains("[Term]"))
			{		
				if(!id.equals(""))
				{
					classe = new OntologicalClass(name, def, isA, syns);
					ontologicatermIDDetails.put(id, classe);
				}
				id = new String();
				name = new String();
				def = new String();
				isA = new ArrayList<String>();
				syns = new ArrayList<String>();
			}
			else if(line.startsWith("id:"))
			{
				id = line.substring(4);
			}
			else if(line.startsWith("name:"))
			{
				name = line.substring(6);
			}
			else if(line.startsWith("def:"))
			{
				def = line.substring(5);
			}
			else if(line.startsWith("is_a:"))
			{
				if(line.contains("!"))
					isA.add(line.substring(6, line.indexOf("!")-1));
				else
					isA.add(line.substring(6));
			}
			else if(line.startsWith("synonym:"))
			{
				syns.add(line.substring(line.indexOf('\"')+1,line.lastIndexOf('\"')));
			}
			else if(line.startsWith("is_obsolete"))
			{
				id = "";
			}
			else if(line.startsWith("relationship: part_of"))
			{
				if(line.contains("!"))
					isA.add(line.substring(22, line.indexOf("!")-1));
				else
					isA.add(line.substring(22));
			}
		}			
	}
	
	public Map<String, OntologicalClass> getOntologicatermIDDetails() {
		return ontologicatermIDDetails;
	}

	public boolean validateFile(File file) throws IOException {
		FileReader fr;
			fr = new FileReader(file);
			br = new BufferedReader(fr);		
			String line;
			if((line = br.readLine())!=null)
			{
				if(line.contains("format-version") && line.contains(version))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		return false;
	}
	
	public IResourceUpdateReport processOntologyFile(File file) throws IOException, DatabaseLoadDriverException, SQLException
	{
		loadFile(file);
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		getReport().updateFile(file);
		Set<String> syns = new HashSet<String>();
		Set<String> extIDsSet = new HashSet<String>();
		Iterator<String> itOnto = getOntologicatermIDDetails().keySet().iterator();
		Map<String,Integer> ontologyIDDatabaseIndex = new HashMap<String, Integer>();
		int resourceIDNext = HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements);
		int sourceID = ResourcesManagement.addSource(ontology.getName());
		int total = 2*getOntologicatermIDDetails().size();
		int point = 0;
		String onto = "ontology";
		if(ontology.getName()!=null && !ontology.getName().equals(""))
		{
			onto = ontology.getName();
		}
		int classID = ontology.addElementClass(onto);
		int rootID = resourceIDNext;	
		ontology.addTermNoRestritions(new ResourceElement("root",classID));
		resourceIDNext++;
		ontology.addResourceContent(onto);
		getReport().addClassesAdding(1);
		while(itOnto.hasNext())
		{
			resourceIDNext = updateTerm(syns, extIDsSet, itOnto,ontologyIDDatabaseIndex, resourceIDNext, sourceID, classID);
			if(point % 100 == 0 )
			{
				memoryAndProgress(point,total);
			}
			syns = new HashSet<String>();
			point ++;
		}
		int relationTypeID = ResourcesManagement.addRelationType("is_a");
		PreparedStatement psIsA;
		psIsA = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.insertResourceElementRelation);
		psIsA.setInt(3,relationTypeID);
		itOnto = getOntologicatermIDDetails().keySet().iterator();
		while(itOnto.hasNext())
		{
			porcessRelations(itOnto, ontologyIDDatabaseIndex, rootID, psIsA);
			if(point % 100 == 0 )
			{
				memoryAndProgress(point,total);
			}
			point ++;
		}
		psIsA.close();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		getReport().setTime(endTime-startTime);
		return getReport();

	}

	private void porcessRelations(Iterator<String> itOnto,
			Map<String, Integer> ontologyIDDatabaseIndex, int rootID,
			PreparedStatement psIsA) throws SQLException {
		String cl = itOnto.next();
		OntologicalClass clO = getOntologicatermIDDetails().get(cl);
		psIsA.setInt(2,ontologyIDDatabaseIndex.get(cl));
		if(clO.getIs_a().size()==0)
		{
			psIsA.setInt(1,rootID);
			psIsA.execute();
		}
		else
		{
			processIsA(ontologyIDDatabaseIndex, psIsA, clO);
		}
	}

	private void processIsA(Map<String, Integer> ontologyIDDatabaseIndex,
			PreparedStatement psIsA, OntologicalClass clO) throws SQLException {
		for(String isA:clO.getIs_a())
		{
			if(ontologyIDDatabaseIndex.get(isA) == null)
			{
				System.err.println(isA);
			}
			else
			{
				psIsA.setInt(1,ontologyIDDatabaseIndex.get(isA));
			}
			psIsA.execute();
		}
	}

	private int updateTerm(Set<String> syns, Set<String> extIDsSet,
			Iterator<String> itOnto,
			Map<String, Integer> ontologyIDDatabaseIndex, int resourceIDNext,
			int sourceID, int classID) throws DatabaseLoadDriverException, SQLException {
		String cl = itOnto.next();
		OntologicalClass clO = getOntologicatermIDDetails().get(cl);
		
		if(!ontologyIDDatabaseIndex.containsKey(cl) && ontology.addTermNoRestritions(new ResourceElement(clO.getName(),classID)))
		{
			updateSynonyms(syns, resourceIDNext, clO);
			updateExternalID(extIDsSet, resourceIDNext, sourceID, cl);
			ontologyIDDatabaseIndex.put(cl, resourceIDNext);
			getReport().addTermAdding(1);
			resourceIDNext++;
		}
		else
		{
			IResourceElement elem = new ResourceElement(-1,cl,-1,"");
			IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elem, elem);
			getReport().addConflit(conflit);
		}
		return resourceIDNext;
	}

	private void updateSynonyms(Set<String> syns, int resourceIDNext,
			OntologicalClass clO) {
		for(String syn:clO.getSynonyms())
		{
			if(!syns.contains(syn.toUpperCase()))
			{
				if(ontology.addSynonymsNoRestritions(new ResourceElement(resourceIDNext, syn)))
				{
					syns.add(syn.toUpperCase());
					getReport().addSynonymsAdding(1);
				}
				else
				{
					IResourceElement elem = new ResourceElement(-1,syn,-1,"");
					IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveSynonyms, elem, elem);
					getReport().addConflit(conflit);
				}
			}
		}
	}

	private void updateExternalID(Set<String> extIDsSet, int resourceIDNext,int sourceID, String cl) throws DatabaseLoadDriverException, SQLException {
		if(ontology.addExternalID(resourceIDNext, cl,sourceID))
		{
			getReport().addExternalIDs(1);
			extIDsSet.add(cl.toUpperCase());
		}
		else
		{
			IResourceElement newElem = new ResourceElement(-1, "", 1, "", new ExternalID(cl, ontology.getName(), sourceID));
			IInsertConflits conflit = new ResourceMergeConflits(ConflitsType.AlteradyHaveExternalID, newElem,newElem);
			getReport().addConflit(conflit);
		}
	}

	public IOntology getOntology() {
		return ontology;
	}
}
