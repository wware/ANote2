package pt.uminho.anote2.corpusloaders.gui;

import java.io.File;

import pt.uminho.anote2.corpusloaders.loaders.AIMEDProteinCorpusLoader;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;

public class SelectAIMEDCorpusGUI extends ASelectCorpusGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SelectAIMEDCorpusGUI()
	{
		super();
		this.setTitle("Select AIMED Corpus Directory");
	}

	@Override
	public boolean validateFile(File save_path) {
		if(!save_path.isDirectory())
		{
			Workbench.getInstance().warn("You must select a directory");
			return false;
		}
		else 
		{
			AIMEDProteinCorpusLoader loader = new AIMEDProteinCorpusLoader(save_path);
			if(loader.validateFile())
			{
				return true;
			}
			else
			{
				Workbench.getInstance().warn("One or more files in the directory are not compatible");
				return false;
			}
		}
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Create_Corpus_By_AIMED_Corpus";
	}

}
