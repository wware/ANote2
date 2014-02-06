package pt.uminho.anote2.aibench.corpus.structures;

import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColorStyleProperty;
import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColors;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;




public class CorporaProperties extends AnnotationColors {
	

	protected CorporaProperties() throws SQLException,DatabaseLoadDriverException {
		super();
	}

	public static AnnotationColorStyleProperty getCorporaClassColor(int classID) throws SQLException, DatabaseLoadDriverException
	{
		if(getInstance().getTasks().containsKey(classID))
		{
			return getTasks().get(classID);
		}
		else
		{
			String color = getDefaultColor();
			color = insertDatabseColor(classID);
			String className = ClassProperties.getClassIDClass().get(classID);
			AnnotationColorStyleProperty col = new AnnotationColorStyleProperty(className,color, defaultStyle);
			getInstance().getTasks().put(classID,col);
			updateAllAnnotatedDocumentOpen();
			return col;
		}
	}	

	public static void updateCorporaClassColor(int classID,String newColor) throws SQLException, DatabaseLoadDriverException {
		updateColor(classID, newColor);
		updateAllAnnotatedDocumentOpen();
	}

	
	public static void updateAllAnnotatedDocumentOpen() {
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.changeclasscolorop")){
				Workbench.getInstance().executeOperation(def);
				return;
			}
		}
		
	}
	
	public static void updateVerbColors() {
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.changeclasscolorverbop")){
				Workbench.getInstance().executeOperation(def);
				return;
			}
		}
		
	}
}
