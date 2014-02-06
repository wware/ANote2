package pt.uminho.anote2.datastructures.annotation.properties;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.QueriesGeneral;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
/**
 * A singleton class that manager class colors 
 * 
 * @author Hugo Costa
 *
 */
public class AnnotationColors {
	
	private static Map<Integer,AnnotationColorStyleProperty> tasks;
	private static String defaultColor = "#0000FF";
	public static String defaultStyle = "";
	
	private static AnnotationColors _instance= null;
	
	
	protected AnnotationColors() throws SQLException, DatabaseLoadDriverException
	{
		synchronized (AnnotationColors.class){
			if (_instance == null) {
					AnnotationColors.tasks = getClassColors();
				_instance = this;
			} else {
				throw new RuntimeException("This is a singleton.");
			}
		}
	}
	
	
	/**
	 * Creates the singleton instance.
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException 
	 */
	private static synchronized void createInstance() throws SQLException, DatabaseLoadDriverException {

		if (_instance == null) {
			_instance = new AnnotationColors();
		}	
	}

	/**
	 * Gives access to the Workbench instance
	 * @return
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException 
	 */
	protected static AnnotationColors getInstance() throws SQLException, DatabaseLoadDriverException {
		if (_instance == null) {
			AnnotationColors.createInstance();
		}
		return _instance;
	}
	
	public static AnnotationColors getInstanceWithoutAIBench() throws SQLException, DatabaseLoadDriverException {
		if (_instance == null) {
			AnnotationColors.createInstance();
		}
		return _instance;
	}
	
	private static Map<Integer, AnnotationColorStyleProperty> getClassColors() throws SQLException, DatabaseLoadDriverException
	{
		Map<Integer, AnnotationColorStyleProperty> classProperties = new HashMap<Integer, AnnotationColorStyleProperty>();
		String color,classe;
		for(Integer classID:ClassProperties.getClassIDClass().keySet())
		{
			color = getColorForClass(classID);
			classe = ClassProperties.getClassIDClass().get(classID);
			classProperties.put(classID,new AnnotationColorStyleProperty(classe,color,""));
		}
		return classProperties;
	}
	
	private static String getColorForClass(int classID) throws SQLException, DatabaseLoadDriverException
	{
		String color = getDefaultColor();
		{
			PreparedStatement getcolorPS = GlobalOptions.database.getConnection().prepareStatement(QueriesGeneral.selectClassColor);
			getcolorPS.setInt(1, classID);
			ResultSet rs = getcolorPS.executeQuery();
			if(rs.next())
			{
				color =  rs.getString(1);
			}
			else
			{
				color = insertDatabseColor(classID);	
			}
			rs.close();
			getcolorPS.close();
		}
		return color;
	}
	
	protected static String insertDatabseColor(int classID)throws SQLException, DatabaseLoadDriverException {
		String color = Utils.randomAlphaNumColor(6);
		PreparedStatement putcolor = GlobalOptions.database.getConnection().prepareStatement(QueriesGeneral.insertClassColor);
		putcolor.setInt(1, classID);
		putcolor.setNString(2, color);
		putcolor.execute();
		putcolor.close();
		return color;
	}
	
	protected static AnnotationColorStyleProperty getClassColor(int classID) throws SQLException, DatabaseLoadDriverException
	{
		if(getTasks().containsKey(classID))
		{
			return getTasks().get(classID);
		}
		else
		{
			String color = getDefaultColor();
			try {
				color = insertDatabseColor(classID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String className = ClassProperties.getClassIDClass().get(classID);
			AnnotationColorStyleProperty col = new AnnotationColorStyleProperty(className,color, defaultStyle);
			getTasks().put(classID,col);
			return col;
		}
	}	

	public static AnnotationColorStyleProperty getClassColor2(int classID) throws SQLException, DatabaseLoadDriverException
	{
		return getClassColor(classID);
	} 

	protected static void updateColor(int classID,String newColor) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement updatecolorPS = GlobalOptions.database.getConnection().prepareStatement(QueriesGeneral.updateColor);
		updatecolorPS.setNString(1, newColor);
		updatecolorPS.setInt(2, classID);
		updatecolorPS.execute();
		updatecolorPS.close();
		getTasks().get(classID).setColor(newColor);
	}

	public static String getDefaultColor() {
		return defaultColor;
	}

	protected static Map<Integer,AnnotationColorStyleProperty> getTasks() {
		return tasks;
	}
	


}
