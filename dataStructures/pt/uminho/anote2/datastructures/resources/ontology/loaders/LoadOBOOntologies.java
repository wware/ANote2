package pt.uminho.anote2.datastructures.resources.ontology.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.resources.ResourcesManagement;
import pt.uminho.anote2.resource.ontologies.IOntology;
import pt.uminho.anote2.resource.ontologies.IOntologyLoader;


public class LoadOBOOntologies implements IOntologyLoader{
	
	private File file;
	private Map<String,OntologicalClass> ontologicatermIDDetails;
	private int numberSynonyms;
	private int numberTerms;
	private IDatabase db;
	private String name;
	private String info;
	
	public LoadOBOOntologies(File file,String name,String info,IDatabase db)
	{
		this.file=file;
		this.name=name;
		this.info=info;
		this.ontologicatermIDDetails = new HashMap<String, OntologicalClass>();
		this.numberSynonyms = 0;
		this.numberTerms = 0;
		this.db=db;
	}
	
	public String getName() {
		return name;
	}

	protected void loadFile()
	{
		FileReader fr;
		BufferedReader br;
		try {
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
						numberTerms ++;
						numberSynonyms = numberSynonyms + syns.size();
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
					syns.add(line.substring(9));
				}
				else if(line.startsWith("is_obsolete"))
				{
					id = "";
				}
			}		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void processOntology(IOntology ontology) throws SQLException
	{
		loadFile();
		int resourceID = ontology.getId();
		Iterator<String> itOnto = getOntologicatermIDDetails().keySet().iterator();
		Map<String,Integer> ontologyIDDatabaseIndex = new HashMap<String, Integer>();
		ResourcesHelp rh = new ResourcesHelp(db, resourceID, name, info);
		int resourceIDNext = HelpDatabase.getNextInsertTableID(db, "resource_elements");
		int sourceID = ResourcesManagement.addSource(db,name);
		while(itOnto.hasNext())
		{
			String cl = itOnto.next();
			OntologicalClass clO = getOntologicatermIDDetails().get(cl);
			rh.addTermNoRestritions(new ResourceElement(clO.getName()));
			for(String syn:clO.getSynonyms())
				rh.addSynonymsNoRestritions(new ResourceElement(resourceIDNext, syn));
			rh.addExternalID(resourceIDNext, cl, sourceID);
			ontologyIDDatabaseIndex.put(cl, resourceIDNext);
			resourceIDNext++;
		}
		int relationTypeID = ResourcesManagement.addRelationType(db,"is_a");
		PreparedStatement psIsA = db.getConnection().prepareStatement(QueriesResources.insertResourceElementRelation);
		psIsA.setInt(3,relationTypeID);
		itOnto = getOntologicatermIDDetails().keySet().iterator();
		while(itOnto.hasNext())
		{
			String cl = itOnto.next();
			OntologicalClass clO = getOntologicatermIDDetails().get(cl);
			psIsA.setInt(2,ontologyIDDatabaseIndex.get(cl));
			for(String isA:clO.getIs_a())
			{
				psIsA.setInt(1,ontologyIDDatabaseIndex.get(isA));
				psIsA.execute();
			}
		}
	}

	
	public Map<String, OntologicalClass> getOntologicatermIDDetails() {
		return ontologicatermIDDetails;
	}
	
	public int getNumberTerms() {
		return numberTerms;
	}


	public int getNumberSyn() {
		return numberSynonyms;
	}

	public void getOntology(IOntology ontology) {
		try {
			processOntology(ontology);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public IDatabase getDB() {
		return db;
	}


	public boolean validateFile(File file) {
//		FileReader fr;
//		BufferedReader br;
//
//		try {
//			fr = new FileReader(file);
//			br = new BufferedReader(fr);		
//			String line;
//			while((line = br.readLine())!=null)
//			{
//				if(line.contains("ontology"))
//				{		
//					if(line.contains("go"))
//					{
//						return true;
//					}
//					else
//					{
//						return false;
//					}
//				}
//				else if(line.startsWith("[Term]"))
//				{
//					return false;
//				}
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		System.exit(1);
		return true;
	}




}
