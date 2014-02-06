package pt.uminho.anote2.aibench.corpus.gui.help;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.corpora.QueriesExternalLinks;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;

public class LinkOutsPropertiesGUI {
	
	public static List<String> getUrlsForSource(int sourceID) throws SQLException, DatabaseLoadDriverException
	{
		List<String> links = new ArrayList<String>();
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesExternalLinks.getLinkoutsForSource);
		ps.setInt(1, sourceID);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			links.add(rs.getString(1));
		}
		rs.close();
		ps.close();
		return links;
	}
}
