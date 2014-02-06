package pt.uminho.anote2.workflow.processes;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Properties;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PubMedSearchExtension;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import pt.uminho.anote2.process.IR.IIRSearchConfiguration;
import pt.uminho.anote2.process.IR.exception.InternetConnectionProblemException;

public class WorkflowPubmedSearch {
	
	public static IIRSearchProcessReport processPubmedSearch(PubMedSearchExtension pubmed, IIRSearchConfiguration configuration) throws InternetConnectionProblemException, DatabaseLoadDriverException, SQLException
	{
		
		Properties properties = configuration.getProperties();
		if(!OtherConfigurations.getFreeFullTextOnly())
		{	
			return fullversion(pubmed,configuration);
		}
		else
		{
			if(!properties.containsKey("articleDetails"))
			{
				return restrictedversion(pubmed,configuration);
			}
			else 
			{
				String prop = properties.getProperty("articleDetails");
				if(prop.equals("freefulltext"))
				{
					pubmed.setIsfreefulltextquery(true);
					return fullversion(pubmed,configuration);
				}
				else
				{
					return restrictedversion(pubmed,configuration);
				}
			}
		}
	}
	
	private static IIRSearchProcessReport fullversion(PubMedSearchExtension pubmed, IIRSearchConfiguration configuration) throws InternetConnectionProblemException, DatabaseLoadDriverException, SQLException {
		String organism = new String();
		String keywords = new String();
		if(configuration.getKeywords()!=null)
			keywords = configuration.getKeywords();
		if(configuration.getOrganism()!=null)
			organism = configuration.getOrganism();
		Properties prop = configuration.getProperties();
		return pubmed.search(keywords, organism,prop);
	} 
	
	private static IIRSearchProcessReport restrictedversion(PubMedSearchExtension pubmed, IIRSearchConfiguration configuration) throws InternetConnectionProblemException, DatabaseLoadDriverException, SQLException {
		String organism = new String();
		String keywords = new String();
		if(configuration.getKeywords()!=null)
			keywords = configuration.getKeywords();
		if(configuration.getOrganism()!=null)
			organism = configuration.getOrganism();
		Properties prop = configuration.getProperties();
		IIRSearchProcessReport report = pubmed.search(keywords, organism,prop);
		if(report.isFinishing())
		{
			long time = report.getTime();
			long start = GregorianCalendar.getInstance().getTimeInMillis();
			pubmed.searchAddProperty(keywords, organism,prop);
			long end = GregorianCalendar.getInstance().getTimeInMillis();
			report.setTime(time+end-start);
		}
		else
		{
			report.setcancel();
		}
		return report;
	}

}
