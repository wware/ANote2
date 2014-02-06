package pt.uminho.anote2.workflow.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.settings.corpora.CorporaDefaultSettings;
import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PubmedCrawlExtention;
import pt.uminho.anote2.aibench.utils.timeleft.ITimeLeftProgressSteps;
import pt.uminho.anote2.core.corpora.ICorpusCreateConfiguration;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.report.corpora.ICorpusCreateReport;
import pt.uminho.anote2.core.report.processes.ir.IIRCrawlingProcessReport;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.documents.query.Query;
import pt.uminho.anote2.datastructures.report.corpora.CorpusCreateReport;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;

public class WorkflowCreateCorpus {

	public static Query getLastQuery() throws DatabaseLoadDriverException, SQLException {
		int lastQuery = HelpDatabase.getNextInsertTableID(GlobalTablesName.query) - 1;
		return getQuery(lastQuery);
	}
	
	private static Query getQuery(int idint) throws SQLException, DatabaseLoadDriverException{
		Query query = null;
		Properties prop = null;
		PreparedStatement queryInfo = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.selectQueriesInformationByID);
		PreparedStatement queryProperties = Configuration.getDatabase().getConnection().prepareStatement(QueriesIRProcess.selectQueryProperties);
		queryInfo.setInt(1,idint);
		ResultSet rs = queryInfo.executeQuery();
		prop = new Properties();
		queryProperties.setInt(1,idint);
		ResultSet rs2 = queryProperties.executeQuery();	
		while(rs2.next())
		{
			prop.put(rs2.getString(1),rs2.getString(2));
		}
		if(rs.next())
		{
			query = new Query(
					rs.getInt(1),
					rs.getString(2),
					rs.getDate(3),
					rs.getString(4),
					rs.getString(5),
					rs.getInt(6),
					rs.getInt(7),
					0,
					rs.getBoolean(7),
					prop);
		}
		rs.close();
		rs2.close();
		queryInfo.close();
		queryProperties.close();
		return query;
	}
	
	public static IIRCrawlingProcessReport processIRCrawl(PubmedCrawlExtention crawl,ICorpusCreateConfiguration conf,Query query,ITimeLeftProgressSteps progress) throws SQLException, DatabaseLoadDriverException {
		if(conf.getCorpusTextType() == CorpusTextType.FullText && conf.processJournalRetrievalBefore())
		{
			crawl = new PubmedCrawlExtention(OtherConfigurations.getFreeFullTextOnly(),progress);
			IIRCrawlingProcessReport reportCrawl = crawl.getFullText(query.getPublications());	
			return reportCrawl;
		}
		return null;
	}
	
	public static IIRCrawlingProcessReport processIRCrawl(PubmedCrawlExtention crawl,ICorpusCreateConfiguration conf,List<IPublication> pubs,ITimeLeftProgressSteps progress) {
		if(conf.getCorpusTextType() == CorpusTextType.FullText && conf.processJournalRetrievalBefore())
		{
			crawl = new PubmedCrawlExtention(OtherConfigurations.getFreeFullTextOnly(),progress);
			IIRCrawlingProcessReport reportCrawl = crawl.getFullText(pubs);	
			return reportCrawl;
		}
		return null;
	}
	
	public static ICorpusCreateReport processCreateCorpus(Corpora corpora, ICorpusCreateConfiguration conf, Query query, ITimeLeftProgressSteps progress) throws DatabaseLoadDriverException, SQLException
	{
		long startTime = new GregorianCalendar().getTimeInMillis();
		Properties prop = new Properties();
		if(conf.getCorpusTextType() == CorpusTextType.FullText)
		{
			prop.put(GlobalNames.textType, GlobalNames.fullText);
		}
		else
		{
			prop.put(GlobalNames.textType, GlobalNames.abstracts);

		}

		Corpus corpus = corpora.createCorpus(conf.getCorpusName(),prop);
		List<IPublication> pubsTOCorpus = getPublicationToCorpus(conf.getCorpusTextType(),query.getPublications());
		int step = 0;
		int size = pubsTOCorpus.size();
		int maxCorpusValue = Integer.valueOf(PropertiesManager.getPManager().getProperty(CorporaDefaultSettings.CORPUS_SIZE_LIMIT).toString());
		if( maxCorpusValue != -1)
		{
			if(size > maxCorpusValue)
			{
				size = maxCorpusValue;
			}
		}
		for(IPublication pub:pubsTOCorpus)
		{
			if(step >= size)
			{
				break;
			}
			IAnnotatedDocument annotDocument = new AnnotatedDocument(pub.getID());
			corpus.addDocument(annotDocument);
			progress.setProgress((float) step / (float) size );
			long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
			progress.setTime(nowTime-startTime, step, size);
			step++;
		}
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		ICorpusCreateReport reportCreateCorpus = new CorpusCreateReport(conf.getCorpusTextType(),conf.getCorpusName());
		reportCreateCorpus.setCorpus(corpus);
		reportCreateCorpus.setTime(endTime-startTime);
		return reportCreateCorpus;
	}
	
	public static ICorpusCreateReport processCreateCorpus(Corpora corpora, ICorpusCreateConfiguration conf, List<IPublication> pubs, ITimeLeftProgressSteps progress) throws DatabaseLoadDriverException, SQLException
	{
		long startTime = new GregorianCalendar().getTimeInMillis();
		Properties prop = new Properties();
		if(conf.getCorpusTextType() == CorpusTextType.FullText)
		{
			prop.put(GlobalNames.textType, GlobalNames.fullText);
		}
		else
		{
			prop.put(GlobalNames.textType, GlobalNames.abstracts);

		}

		Corpus corpus = corpora.createCorpus(conf.getCorpusName(),prop);
		int step = 0;
		List<IPublication> pubsTOCorpus = getPublicationToCorpus(conf.getCorpusTextType(),pubs);
		int size = pubsTOCorpus.size();
		int maxCorpusValue = Integer.valueOf(PropertiesManager.getPManager().getProperty(CorporaDefaultSettings.CORPUS_SIZE_LIMIT).toString());
		if( maxCorpusValue != -1)
		{
			if(size > maxCorpusValue)
			{
				size = maxCorpusValue;
			}
		}
		for(IPublication pub:pubsTOCorpus)
		{
			if(step >= size)
			{
				break;
			}
			IAnnotatedDocument annotDocument = new AnnotatedDocument(pub.getID());
			corpus.addDocument(annotDocument);
			progress.setProgress((float) step / (float) size );
			long nowTime = GregorianCalendar.getInstance().getTimeInMillis();
			progress.setTime(nowTime-startTime, step, size);
			step++;
		}
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		ICorpusCreateReport reportCreateCorpus = new CorpusCreateReport(conf.getCorpusTextType(),conf.getCorpusName());
		reportCreateCorpus.setCorpus(corpus);
		reportCreateCorpus.setTime(endTime-startTime);
		return reportCreateCorpus;
	}
	
	private static List<IPublication> getPublicationToCorpus(CorpusTextType corpusTextType, List<IPublication> publications) {
		List<IPublication> result = new ArrayList<IPublication>();
		if(corpusTextType == CorpusTextType.Abstract)
		{
			for(IPublication pub:publications)
			{
				if(pub.getAbstractSection()!=null && pub.getAbstractSection().length() > 0)
				{
					result.add(pub);
				}
			}
			return result;
		}
		else if(corpusTextType == CorpusTextType.FullText)
		{
			for(IPublication pub:publications)
			{
				if(pub.isPDFAvailable())
				{
					result.add(pub);
				}
			}
			return result;
		}
		return null;
	}

	
}
