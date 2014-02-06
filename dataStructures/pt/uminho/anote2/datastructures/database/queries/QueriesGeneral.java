package pt.uminho.anote2.datastructures.database.queries;

public class QueriesGeneral {
	
	public static final String getTableInformation = "SHOW TABLE STATUS LIKE ? ";
	
	public static final String selectClassColor = "SELECT color FROM classes_colors "+
												  "WHERE classes_idclasses=? ";

	public static final String insertClassColor = "INSERT INTO classes_colors VALUES (?,?) ";
	
	public static final String updateColor = "UPDATE classes_colors SET color=? "+
			 								 "WHERE classes_idclasses=? ";


}
