package pt.uminho.anote2.datastructures.settings.database.help;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;

public class TreatDatabaseException {

	public static void treat(IDatabase databaseAdded,DatabaseLoadDriverException e) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JOptionPane.showMessageDialog(null, "No Database Driver Found", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
	}

	public static boolean treat(IDatabase databaseAdded, SQLException e) {
		if(e.getMessage().contains("Unknown database"))
		{
			try {
				databaseAdded.createDataBase();
			} catch (DatabaseLoadDriverException e1) {
			} catch (SQLException e1) {
				return treatProblemsInDatabseCreation(databaseAdded,e1);
			}
			return true;
		}
		else if(e.getMessage().contains("Access denied for user"))
		{
			final String showOption = e.getMessage()+" in database "+databaseAdded.getSchema();
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					JOptionPane.showMessageDialog(null, showOption, "Error", JOptionPane.ERROR_MESSAGE);
				}
			});
			return false;
		}
		else if(e.getMessage().contains("Communications link failure"))
		{
			final String showOption = "Database server "+databaseAdded.getDataBaseType().toString()+" is not running or not installed in machine "+databaseAdded.getHost();
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					JOptionPane.showMessageDialog(null, showOption, "Error", JOptionPane.ERROR_MESSAGE);
				}
			});
			return false;
		}
		final String showOption = e.getStackTrace().toString();
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JOptionPane.showMessageDialog(null, showOption, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		return false;
		
	}

	private static boolean treatProblemsInDatabseCreation(IDatabase databaseAdded, SQLException e1) {
		if(e1.getMessage().contains("Access denied for user"))
		{
			final String showOption = "Unknown database and impossible create databse with because:"+e1.getMessage()+" ton create database "+databaseAdded.getSchema();
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					JOptionPane.showMessageDialog(null, showOption, "Error", JOptionPane.ERROR_MESSAGE);
				}
			});
		}
		return false;
	}

}
