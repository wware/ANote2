package pt.uminho.anote2.datastructures.annotation.log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesAnnotatedDocument;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;

public class AnnotationLogManagement {
	
	private static final int  enumTypeToDatabase = 1;
	
	public static List<AnnotationLog> getDocumentLogsInAnProcessAndCorpus(int corpusID,int processID, int documentID) throws SQLException, DatabaseLoadDriverException
	{
		List<AnnotationLog> annotationLogs = new ArrayList<AnnotationLog>();
		AnnotationLog log;
		PreparedStatement getAllDocumentLogs = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.selectLogAnnotations);
		getAllDocumentLogs.setInt(1, corpusID);
		getAllDocumentLogs.setInt(2, processID);
		getAllDocumentLogs.setInt(3, documentID);
		ResultSet rs = getAllDocumentLogs.executeQuery();
		while(rs.next())
		{
			Date date = rs.getTimestamp(10);
			AnnotationLogTypeEnum type = AnnotationLogTypeEnum.valueOf(rs.getString(6));
			log = new AnnotationLog(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),type ,
					rs.getString(7), rs.getString(8), rs.getString(9),date);
			annotationLogs.add(log);
		}
		rs.close();
		getAllDocumentLogs.close();
		return annotationLogs;
	}
	
	public static void addDocumentLog(int corpusID,int originalAnnotationID, int processID,int documentID, AnnotationLogTypeEnum type, String oldString,
			String newString, String notes, Date date) throws SQLException, DatabaseLoadDriverException
	{
		PreparedStatement addNewDocumentLog = Configuration.getDatabase().getConnection().prepareStatement(QueriesAnnotatedDocument.addAnnotationLog);
		if(originalAnnotationID<=0)
		{
			addNewDocumentLog.setNull(1, originalAnnotationID);

		}
		else
		{
			addNewDocumentLog.setInt(1, originalAnnotationID);
		}
		addNewDocumentLog.setInt(2, corpusID);
		addNewDocumentLog.setInt(3, processID);
		addNewDocumentLog.setInt(4, documentID);
		// Enum starts with 0 and databse enum stars with 1
		addNewDocumentLog.setInt(5, type.ordinal()+enumTypeToDatabase);
		addNewDocumentLog.setNString(6, oldString);
		addNewDocumentLog.setNString(7, newString);
		addNewDocumentLog.setNString(8, notes);
		// Change Date Java to SQL date
		addNewDocumentLog.setNString(9,Utils.SimpleDataFormat.format(date));
		addNewDocumentLog.execute();
		addNewDocumentLog.close();
	}
	
	public static void addDocumentLog(AnnotationLog annotationLog) throws SQLException, DatabaseLoadDriverException
	{
		addDocumentLog(annotationLog.getCorpusID(),annotationLog.getOriginalAnnotationID(), annotationLog.getProcessID(), annotationLog.getDocumentID(), annotationLog.getType(), annotationLog.getOldString(),
				annotationLog.getNewString(), annotationLog.getNotes(), annotationLog.getDate());
	}

}
