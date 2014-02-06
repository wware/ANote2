package pt.uminho.anote2.aibench.resources.datastructures.ontologies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.resources.ResourcesManagement;
import pt.uminho.anote2.datastructures.resources.ontology.loaders.LoadOBOOntologies;
import pt.uminho.anote2.datastructures.resources.ontology.loaders.OntologicalClass;
import pt.uminho.anote2.resource.ontologies.IOntology;

public class OBOOntologyExtensionv1p0 extends LoadOBOOntologies{

	private TimeLeftProgress progress;
	private boolean cancel = false;
	public static final String version = "1.0";
	
	
	public OBOOntologyExtensionv1p0(File file, String name, String info,IDatabase db,TimeLeftProgress progress) {
		super(file, name, info, db);
		this.progress=progress;
	}
	
	public boolean validateFile(File file) {
		FileReader fr;
		BufferedReader br;

		try {
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void processOntology(IOntology ontology) throws SQLException
	{
		progress.setTimeString("Calculating...loading GO ONtology File...");
		progress.setProgress(0);
		loadFile();
		if(cancel) { return;}
		int resourceID = ontology.getId();
		Iterator<String> itOnto = getOntologicatermIDDetails().keySet().iterator();
		Map<String,Integer> ontologyIDDatabaseIndex = new HashMap<String, Integer>();
		ResourcesHelp rh = new ResourcesHelp(getDB(), resourceID,"","");
		int resourceIDNext = HelpDatabase.getNextInsertTableID(getDB(), "resource_elements");
		int sourceID = ResourcesManagement.addSource(getDB(),getName());
		long timeStart = GregorianCalendar.getInstance().getTimeInMillis();
		int total = 2*getOntologicatermIDDetails().size();
		int point = 0;
		Set<String> syns = new HashSet<String>();
		while(itOnto.hasNext())
		{
			String cl = itOnto.next();
			OntologicalClass clO = getOntologicatermIDDetails().get(cl);
			rh.addTermNoRestritions(new ResourceElement(clO.getName()));
			for(String syn:clO.getSynonyms())
			{
				if(!syns.contains(syn.toUpperCase()))
				{
					rh.addSynonymsNoRestritions(new ResourceElement(resourceIDNext, syn));
					syns.add(syn.toUpperCase());
				}
			}
			rh.addExternalID(resourceIDNext, cl, sourceID);
			ontologyIDDatabaseIndex.put(cl, resourceIDNext);
			resourceIDNext++;
			point ++;
			if(point % 10 == 0 && !cancel)
			{
				long runTime = GregorianCalendar.getInstance().getTimeInMillis();
				progress.setTime(runTime-timeStart,point, total);	
			}
			syns = new HashSet<String>();
		}
		int relationTypeID = ResourcesManagement.addRelationType(getDB(),"is_a");
		PreparedStatement psIsA = getDB().getConnection().prepareStatement(QueriesResources.insertResourceElementRelation);
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
			
			if(point % 10 == 0 && !cancel)
			{
				long runTime = GregorianCalendar.getInstance().getTimeInMillis();
				progress.setTime(runTime-timeStart,point, total);
			}
			point ++;
		}
	}
	
	public void cancel()
	{
		this.cancel = true;
	}
	
	public void getOntology(IOntology ontology) {
		try {
			processOntology(ontology);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	

}
