package pt.uminho.anote2.aibench.curator.datastructures;


import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Observable;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.curator.exeption.ANoteFileNotExistsException;

public class AiANoteDocument extends Observable implements Serializable {

	private static final long serialVersionUID = 7228817031262156896L;
	private ANoteDocument document;
	private boolean opened;
	
	/**
	 * @throws ANoteFileNotExistsException
	 * @throws IOException
	 * @throws SQLException 
	 */
	public AiANoteDocument(NERDocumentAnnotation nerDoc) throws ANoteFileNotExistsException, IOException, SQLException{

		this.document = new ANoteDocument(nerDoc);
		opened=false;
	}
	public ANoteDocument getDocument() {return document;}
	public void setDocument(ANoteDocument document) {this.document = document;}
	public boolean isOpened() {return opened;}
	public void setOpened(boolean opened) {this.opened = opened;}
}
