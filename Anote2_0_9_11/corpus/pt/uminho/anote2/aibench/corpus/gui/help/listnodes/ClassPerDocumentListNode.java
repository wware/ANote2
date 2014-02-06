package pt.uminho.anote2.aibench.corpus.gui.help.listnodes;

import java.text.DecimalFormat;

import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

public class ClassPerDocumentListNode {
	private int classeID;
	private int classOccurrences;
	private int documents;
	DecimalFormat format = GlobalOptions.decimalformat;
	
	public ClassPerDocumentListNode(int classeID, int classOccurrences,
			int documents) {
		super();
		this.classeID = classeID;
		this.classOccurrences = classOccurrences;
		this.documents = documents;
	}
	
	public int getClasseID() {
		return classeID;
	}
	public int getClassOccurrences() {
		return classOccurrences;
	}
	public int getDocuments() {
		return documents;
	}
	
	public String toString()
	{
		String classe = ClassProperties.getClassIDClass().get(getClasseID());
		return classe + " ("+GlobalOptions.decimalformat.format((float) classOccurrences/(float) documents)+")";
	}
	
}
