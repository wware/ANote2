package pt.uminho.anote2.datastructures.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.QueriesGeneral;
import pt.uminho.anote2.datastructures.database.queries.corpora.QueriesExternalLinks;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.database.startdatabase.StartDatabase;
import pt.uminho.anote2.datastructures.utils.MenuItem;
import pt.uminho.anote2.datastructures.utils.Source;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;

/**
 * Class that contains some Help method usefull 
 * 
 * @author Hugo Costa
 *
 */
public class HelpDatabase {
	
	/**
	 * Return nextID in databse for @param tableName
	 * 
	 * @param db
	 * @param tableName
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public static int getNextInsertTableID(String tableName) throws DatabaseLoadDriverException, SQLException
	{
			PreparedStatement showTableInfoPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesGeneral.getTableInformation);
			showTableInfoPS.setString(1,tableName);
			ResultSet rs = showTableInfoPS.executeQuery();		
			if(rs.next())
			{
				int id = rs.getInt(11);
				rs.close();
				showTableInfoPS.close();
				return id;
			}
			else
			{
				rs.close();
				showTableInfoPS.close();
				return -2;
			}
	}
	
	/**
	 * Return a Article Type Databse I. If Article Type not exists in database will be inserted
	 * 
	 * @param type
	 * @return
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	public static  int initArticleTypeID(String type) throws SQLException, DatabaseLoadDriverException {
		int result = -1;

		java.sql.PreparedStatement getPmidTypeIDPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.selectPublicationIndetifierTypeID);
		getPmidTypeIDPS.setNString(1, type);
		ResultSet res = getPmidTypeIDPS.executeQuery();
		if(res.next())
		{
			result = res.getInt(1);
		}
		else
		{
			java.sql.PreparedStatement insertPmidTypePS = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.insertIndetifierType);
			insertPmidTypePS.setNString(1,type);
			int pmidIDtype = HelpDatabase.getNextInsertTableID(GlobalTablesName.publicationsIdYype);
			insertPmidTypePS.execute();
			result = pmidIDtype;
		}
		res.close();
		getPmidTypeIDPS.close();
		return result;
	}	
	
	/**
	 * Return all available Sources
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 */
	public static List<Source> getAllAvailableSources() throws SQLException, DatabaseLoadDriverException
	{
		List<Source> sources = new ArrayList<Source>();
		PreparedStatement getAllSources = Configuration.getDatabase().getConnection().prepareStatement(QueriesGeneral.getAllSources);
		ResultSet rs = getAllSources.executeQuery();
		while(rs.next())
		{
			sources.add(new Source(rs.getInt(1), rs.getString(2)));
		}
		rs.close();
		getAllSources.close();
		return sources;
	}

	/**
	 * Update Menu Item 
	 * 
	 * @param item
	 * @throws SQLException
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws DatabaseLoadDriverException
	 */
	public static void updateMenuItem(MenuItem item) throws SQLException, InterruptedException, IOException, DatabaseLoadDriverException {
		PreparedStatement updateItem = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.updateMenuItem);	
		PreparedStatement updateItemWithouIcon = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.updateMenuItemWithouIcon);	
		ImageIcon imageIcon = item.getIcon();
		if(imageIcon!=null && !(new File(imageIcon.toString()).exists()))
		{
			updateItemWithouIcon.setString(1,item.getName());
			updateItemWithouIcon.setString(2,item.getUrl());
			updateItemWithouIcon.setInt(3,item.getId());
			updateItemWithouIcon.executeUpdate();	
			updateItemWithouIcon.close();
		}
		else
		{
			updateItem.setString(1,item.getName());
			updateItem.setString(2,item.getUrl());
			if(imageIcon!=null)
			{
				File file = new File(imageIcon.toString());
				FileInputStream  fis = new FileInputStream (file);
				updateItem.setBinaryStream(3, fis, (int) file.length());
			}
			else
			{
				updateItem.setNull(3, 1);
			}
			updateItem.setInt(4,item.getId());
			updateItem.executeUpdate();	
			updateItem.close();
		}
		
	}

	public static void removeMenuItemSourceLinkage(MenuItem item, List<Source> sources) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement removeItemSourceLinkage = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.removeMenuItemSourceLinkage);
		removeItemSourceLinkage.setInt(1, item.getId());
		for(Source source:sources)
		{
			removeItemSourceLinkage.setInt(2, source.getSourceID());
			removeItemSourceLinkage.execute();
		}
		removeItemSourceLinkage.close();
	}

	public static void addMenuItemSourceLinkage(MenuItem item, List<Source> sources) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement addItemSourceLinkage = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.updateMenuItemSourceLinkage);
		addItemSourceLinkage.setInt(1, item.getId());
		for(Source source:sources)
		{
			addItemSourceLinkage.setInt(2, source.getSourceID());
			addItemSourceLinkage.execute();
		}
		addItemSourceLinkage.close();
	}

	/**
	 *  Missing treat icon;
	 * @throws DatabaseLoadDriverException 
	 */
	public static void addMenuLink( MenuItem newItem) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement addMenuItem = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.addMenuLink);
		addMenuItem.setString(1, newItem.getName());
		addMenuItem.setString(2, newItem.getUrl());
		addMenuItem.setNull(3, 0);
		addMenuItem.setInt(4,newItem.getLevel());
		addMenuItem.execute();
		addMenuItem.close();
	}

	public static void addMenuSubMenuLinkage(MenuItem menuItem, MenuItem newItem) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement addItemSourceLinkage = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.addSubMenuItemToMenu);
		addItemSourceLinkage.setInt(1, menuItem.getId());
		addItemSourceLinkage.setInt(2, newItem.getId());
		addItemSourceLinkage.execute();	
		addItemSourceLinkage.close();
	}

	public static void removeMenuLink(MenuItem menuItem) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement removeAllSourceLinkage = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.removeAllSourceLinkages);
		removeAllSourceLinkage.setInt(1, menuItem.getId());
		removeAllSourceLinkage.execute();
		removeAllSourceLinkage.close();
		PreparedStatement removeAllMenuLinkages = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.removeAllMenuLinkages);
		removeAllMenuLinkages.setInt(1, menuItem.getId());
		removeAllMenuLinkages.setInt(2, menuItem.getId());
		removeAllMenuLinkages.execute();
		removeAllMenuLinkages.close();
		PreparedStatement removeMenu = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.removeMenuLink);
		removeMenu.setInt(1, menuItem.getId());
		removeMenu.execute();
		removeMenu.close();
	}
	
	public static int getMaxDatabaseVersion() throws SQLException, DatabaseLoadDriverException
	{
			int result = -1;
			if(Configuration.getDatabase() instanceof StartDatabase)
				return -1;
			if(Configuration.getDatabase()==null)
				return -1;
			PreparedStatement versionPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesGeneral.getDatabaseVersion);
			ResultSet rs = versionPS.executeQuery();
			if(rs.next())
			{
				result = rs.getInt(1);
			}
			else
			{
				result = -1;
			}
			rs.close();
			versionPS.close();
			return result;
		}

	public static void insertNewVersionRegestry(int i,String comments) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement newVersion = Configuration.getDatabase().getConnection().prepareStatement(QueriesGeneral.insertNewVersion);
		newVersion.setInt(1,i);
		newVersion.setNString(2,Utils.currentTime());
		newVersion.setNString(3, comments);
		newVersion.execute();
		newVersion.close();
	}
	
	
}
