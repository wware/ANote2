package pt.uminho.anote2.aibench.curator.datastructures;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

//@Datatype(structure = Structure.LIST, namingMethod="getName")
public abstract class ANoteDocumentBox extends Observable implements Serializable {
		
 	private static final long serialVersionUID = 7523043948863415631L;
 	
	protected List<AiANoteDocument> docs;
	//protected ANoteProject owner;
	protected String name;
	
	public void removeDoc(AiANoteDocument doc){
		this.docs.remove(doc);
		setChanged();
		notifyObservers();
	}
	
	public void refresh(){
		setChanged();
		notifyObservers();
	}
	
//	@ListElements
	public List<AiANoteDocument> getDocs() {
		return docs;
	}
	
	public void setDocs(List<AiANoteDocument> docs) {
		this.docs = docs;
		setChanged();
		notifyObservers();
	}
	
	public void addDoc(AiANoteDocument doc)
	{
		this.getDocs().add(doc);
		setChanged();
		notifyObservers();
	}


	public String getName() {
		return "Corpus";
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public void saveDocuments(){
//		for(AiANoteDocument document:this.docs)
//			try {
//				if(document.isOpened());
//					//document.getDocument().save(this.getOwner().getProjectPreferences().getTask().getProperties());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
	}


//	public ANoteProject getOwner() {
//		return owner;
//	}
//
//
//	public void setOwner(ANoteProject owner) {
//		this.owner = owner;
//	}

		
}
