package pt.uminho.anote2.aibench.resources.datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;


@Datatype(structure = Structure.LIST,namingMethod="getNameLexWords",removable=false,renamed=false)
public class LexicalWordsSet extends Observable implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Resources resources;
	private List<LexicalWordsAibench> lexicalWords;
	
	public LexicalWordsSet(Resources resources)
	{
		this.resources = resources;
		this.lexicalWords=new ArrayList<LexicalWordsAibench>();
	}

	public List<LexicalWordsAibench> getLexicalWordsSetClipboard() {
		return lexicalWords;
	}
	
	public void notifyViewObservers(){
		this.setChanged();
		this.notifyObservers();
	}
	
	public String getNameLexWords()
	{
		return "Lexical Words";
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	public Resources getResources() {
		return resources;
	}
	
	public void addLexicalWords(LexicalWordsAibench lexicalWords)
	{	
		if(alreadyExist(lexicalWords))
		{
			new ShowMessagePopup("Lexical Words Already In Clipboard.");
		}
		else
		{
			this.lexicalWords.add(lexicalWords);
			new ShowMessagePopup("Lexical Words Added To Clipboard.");
			notifyViewObservers();
		}

	}
	
	public boolean alreadyExist(LexicalWordsAibench lexicalWords)
	{
		for(LexicalWordsAibench lexicalWordsValue:this.lexicalWords)
		{
			int compare = lexicalWordsValue.compareTo(lexicalWords);
			if(compare==0)
			{
				return true;
			}
		}
		return false;
	}
	
	@ListElements(modifiable=true)
	public List<LexicalWordsAibench> getLexicalWordsAIbenchClipboard()
	{
		return this.lexicalWords;
	}

}
