package pt.uminho.anote2.workflow.gui;

import javax.swing.JPanel;

import pt.uminho.anote2.core.corpora.ICorpusCreateConfiguration;

public abstract class ACorpusPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public abstract boolean validateOptions();
	public abstract ICorpusCreateConfiguration getConfiguration();
	public abstract void updataCorpusSettings(ICorpusCreateConfiguration conf);
}
