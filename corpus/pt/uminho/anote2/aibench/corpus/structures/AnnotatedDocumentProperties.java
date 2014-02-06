package pt.uminho.anote2.aibench.corpus.structures;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.datastructures.annotation.AnnotationColorStyleProperty;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.utils.Utils;

public class AnnotatedDocumentProperties {
	
	private Map<String,AnnotationColorStyleProperty> tasks;
	private Map<Integer,String> classIDclass;
	private Map<String,Integer> classClassID;
	private IDatabase db;

	@SuppressWarnings("unchecked")
	public AnnotatedDocumentProperties(IAnnotatedDocument nerDoc,IDatabase db)
	{
		this.db=db;
		try {
			classIDclass = ResourcesHelp.getClassIDClassOnDatabase(db);
			classClassID = (Map<String, Integer>) Utils.swapHashElements(classIDclass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tasks = getClassColors(nerDoc);
	}
	
	private Map<String, AnnotationColorStyleProperty> getClassColors(IAnnotatedDocument nerDoc)
	{
		Map<String, AnnotationColorStyleProperty> classProperties = new HashMap<String, AnnotationColorStyleProperty>();
		String color,className;
		try {
			for(Integer classID:classIDclass.keySet())
			{
				className = classIDclass.get(classID);
				color = Corpora.getColorForClass(classID, db);
				classProperties.put(className,new AnnotationColorStyleProperty(className,color,""));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return classProperties;
	}
	
	public Map<String, AnnotationColorStyleProperty> getTasks() {
		return tasks;
	}

	public Map<Integer, String> getClassIDclass() {
		return classIDclass;
	}
	
	public Map<String, Integer> getClassClassID() {
		return classClassID;
	}


}
