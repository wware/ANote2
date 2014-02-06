package pt.uminho.anote2.aibench.utils.exceptions.treat;

import java.io.IOException;
import java.sql.SQLException;

import pt.uminho.anote2.aibench.utils.settings.database.DatabaseManagementGUI;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.exceptions.ColorRestrictionException;
import pt.uminho.anote2.datastructures.exceptions.InvalidDirectoryException;
import pt.uminho.anote2.datastructures.exceptions.LoaderFileException;
import pt.uminho.anote2.datastructures.exceptions.PubmedException;
import pt.uminho.anote2.datastructures.exceptions.UnknownOperationSystemException;
import pt.uminho.anote2.datastructures.exceptions.process.manualcuration.ManualCurationException;
import pt.uminho.anote2.datastructures.exceptions.process.reprocess.RelationDelimiterExeption;
import pt.uminho.anote2.datastructures.exceptions.resources.ontologies.OntologyMissingRootExeption;
import pt.uminho.anote2.process.IR.exception.InternetConnectionProblemException;
import es.uvigo.ei.aibench.workbench.Workbench;

public class TreatExceptionForAIbench {

	public static void treatExcepion(DatabaseLoadDriverException e) {
		new DatabaseManagementGUI(e);
	}

	public static void treatExcepion(SQLException e) {
		if(e.getMessage().contains("Unknown database"))
		{
			new DatabaseManagementGUI(e);
		}
		else if(e.getMessage().contains("Access denied for user"))
		{
			new DatabaseManagementGUI(e);
		}
		else if(e.getMessage().contains("Communications link failure"))
		{
			new DatabaseManagementGUI(e);
		}
		else
		{
			ExceptionMessageDialog.showMessageDialog(Workbench.getInstance().getMainFrame(), e);
		}
	}

	public static void treatExcepion(IOException e) {
		ExceptionMessageDialog.showMessageDialog(Workbench.getInstance().getMainFrame(), e);
	}

	public static void treatExcepion(PubmedException e) {
		ExceptionMessageDialog.showMessageDialog(Workbench.getInstance().getMainFrame(), e);
	}

	public static void treatExcepion(InternetConnectionProblemException e) {
		ExceptionMessageDialog.showMessageDialog(Workbench.getInstance().getMainFrame(), e);		
	}

	public static void treatExcepion(RelationDelimiterExeption e) {
		ExceptionMessageDialog.showMessageDialog(Workbench.getInstance().getMainFrame(), e);		
	}

	public static void treatExcepion(IllegalArgumentException e) {
		ExceptionMessageDialog.showMessageDialog(Workbench.getInstance().getMainFrame(), e);		
	}

	public static void treatExcepion(OntologyMissingRootExeption e) {
		ExceptionMessageDialog.showMessageDialog(Workbench.getInstance().getMainFrame(), e);		
	}
	
	public static void treatExcepion(ColorRestrictionException e) {
		Workbench.getInstance().warn(e.getMessage());
	}
	
	public static void treatExcepion(InvalidDirectoryException e) {
		Workbench.getInstance().warn(e.getMessage());
	}
	
	public static void treatExcepion(UnknownOperationSystemException e) {
		Workbench.getInstance().warn(e.getMessage());
	}
	
	public static void treatExcepion(ManualCurationException e) {
		Workbench.getInstance().warn(e.getMessage());
	}
	
	public static void treatExcepion(LoaderFileException e) {
		Workbench.getInstance().warn(e.getMessage());
	}
	
	public static void treatExcepion(Exception e) {
		ExceptionMessageDialog.showMessageDialog(Workbench.getInstance().getMainFrame(), e);		
	}
	


	
}
