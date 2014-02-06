package pt.uminho.anote2.corpusloaders.gui;

import java.io.File;

import pt.uminho.anote2.corpusloaders.loaders.ANoteCorpusLoader;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;

public class SelectAnoteV1CorpusGUI extends ASelectCorpusGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectAnoteV1CorpusGUI()
	{
		super();
		this.setTitle("Select @Note v1 Directory Files");
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Create_Corpus_By_AnoteV1_Corpus";
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
			ANoteCorpusLoader loader = new ANoteCorpusLoader(save_path);
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

}
