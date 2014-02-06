package pt.uminho.anote2.datastructures.annotation.log;

import java.util.Date;

import pt.uminho.anote2.datastructures.utils.Utils;

/**
 * Class for saving a annotation log when user add , remove or edit some NER/RE annotation
 * 
 * @author Hugo Costa
 *
 */
public class AnnotationLog {
	



	private int annotationLogID;
	private int originalAnnotationID;
	private int corpusID;
	private int processID;
	private int documentID;
	private AnnotationLogTypeEnum type;
	private String oldString;
	private String newString;
	private String notes;
	private Date date;
	
	
	public AnnotationLog(int annotationLogID,int originalAnnotationID, int corpusID, int processID,
			int documentID, AnnotationLogTypeEnum type, String oldString,
			String newString, String notes) {
		super();
		this.annotationLogID = annotationLogID;
		this.originalAnnotationID = originalAnnotationID;
		this.corpusID = corpusID;
		this.processID = processID;
		this.documentID = documentID;
		this.type = type;
		this.oldString = oldString;
		this.newString = newString;
		this.notes = notes;
		date = new Date();
	}
	
	public AnnotationLog(int annotationLogID,int originalAnnotationID, int corpusID, int processID,
			int documentID, AnnotationLogTypeEnum type, String oldString,
			String newString, String notes,Date date) {
		this(annotationLogID,originalAnnotationID,corpusID,processID,documentID,type,oldString,newString,notes);
		this.date = date;
	}


	public int getOriginalAnnotationID() {
		return originalAnnotationID;
	}


	public int getAnnotationLogID() {
		return annotationLogID;
	}


	public int getCorpusID() {
		return corpusID;
	}


	public int getProcessID() {
		return processID;
	}


	public int getDocumentID() {
		return documentID;
	}


	public AnnotationLogTypeEnum getType() {
		return type;
	}


	public String getOldString() {
		return oldString;
	}


	public String getNewString() {
		return newString;
	}


	public String getNotes() {
		return notes;
	}


	public Date getDate() {
		return date;
	}

	public void setAnnotationLogID(int annotationLogID) {
		this.annotationLogID = annotationLogID;
	}
	
	public String toString()
	{
		String base = Utils.SimpleDataFormat.format(date) + " " + type.toString() ;
		String changes = new String();
		if(!getNewString().isEmpty())
			changes = getNewString();
		if(!getOldString().isEmpty() && !getNewString().isEmpty())
			changes = changes + " <- " + getOldString();
		else if(!getOldString().isEmpty())
			changes = changes + getOldString();
		return base + " " + changes;
	}
	

}
