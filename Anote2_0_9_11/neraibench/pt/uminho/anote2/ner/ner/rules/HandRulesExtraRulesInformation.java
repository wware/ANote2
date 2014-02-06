package pt.uminho.anote2.ner.ner.rules;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.ner.lexicalresources.QueriesNERHandRules;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;

public class HandRulesExtraRulesInformation {
	
	private List<IResource<IResourceElement>> resources;
	private boolean casesensitive;

	public HandRulesExtraRulesInformation(List<IResource<IResourceElement>> resources,boolean caseSensitive)
	{
		this.resources = resources;
		this.casesensitive = caseSensitive;
	}
	
	public int getResourceExtraInfoID(IResourceElement handRule, String text) throws SQLException, DatabaseLoadDriverException
	{
		for(IResource<IResourceElement> resource:resources)
		{
			int result = -1;
			result = findTextInResource(handRule,resource,text);
			if( result != -1)
			{
				return result;
			}
		}
		return -1;
	}

	private int findTextInResource(IResourceElement handRule, IResource<IResourceElement> resource,String text) throws SQLException, DatabaseLoadDriverException {
		PreparedStatement getRulesAditionalInfoOnDictionariesTerm;
		PreparedStatement getRulesAditionalInfoOnDictionariesTermSynonyms;
		if(this.casesensitive)
		{
			getRulesAditionalInfoOnDictionariesTerm = GlobalOptions.database.getConnection().prepareStatement(QueriesNERHandRules.findDictionaryTermCaseSensitive);
			getRulesAditionalInfoOnDictionariesTermSynonyms = GlobalOptions.database.getConnection().prepareStatement(QueriesNERHandRules.findDictionaryTermSynonymCaseSensitive);
		}
		else
		{
			getRulesAditionalInfoOnDictionariesTerm = GlobalOptions.database.getConnection().prepareStatement(QueriesNERHandRules.findDictionaryTerm);
			getRulesAditionalInfoOnDictionariesTermSynonyms = GlobalOptions.database.getConnection().prepareStatement(QueriesNERHandRules.findDictionaryTermSynonym);
		}
		getRulesAditionalInfoOnDictionariesTerm.setInt(1, resource.getID());
		getRulesAditionalInfoOnDictionariesTerm.setString(2, text);
		getRulesAditionalInfoOnDictionariesTerm.setInt(3, handRule.getTermClassID());
		ResultSet rs = getRulesAditionalInfoOnDictionariesTerm.executeQuery();
		if(rs.next())
		{
			int result = rs.getInt(1);
			getRulesAditionalInfoOnDictionariesTerm.close();
			getRulesAditionalInfoOnDictionariesTermSynonyms.close();
			rs.close();
			return result;
		}
		rs.close();
		getRulesAditionalInfoOnDictionariesTerm.close();
		getRulesAditionalInfoOnDictionariesTermSynonyms.setInt(1, resource.getID());
		getRulesAditionalInfoOnDictionariesTermSynonyms.setString(2, text);
		getRulesAditionalInfoOnDictionariesTermSynonyms.setInt(3, handRule.getTermClassID());
		ResultSet rs2 = getRulesAditionalInfoOnDictionariesTermSynonyms.executeQuery();
		if(rs2.next())
		{
			int result = rs2.getInt(1);
			rs2.close();
			getRulesAditionalInfoOnDictionariesTermSynonyms.close();
			return result;
		}
		rs2.close();
		getRulesAditionalInfoOnDictionariesTermSynonyms.close();
		return -1;
	}




}
