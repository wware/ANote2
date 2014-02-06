package pt.uminho.anote2.aibench.resources.datastructures.ontologies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.resource.ontologies.IOntology;

public class OBOOntologyExtensionv1p0 extends OBOOntologyExtensionv1p2{

	public static final String version = "1.0";
	
	
	public OBOOntologyExtensionv1p0(IOntology ontology,String font,TimeLeftProgress progress) {
		super(ontology, font,progress);
	}
	
	public boolean validateFile(File file) throws IOException {
		FileReader fr;
		BufferedReader br;
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

}
