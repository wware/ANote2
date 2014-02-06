package pt.uminho.anote2.aibench.publicationmanager.gui.help;

import javax.swing.JPanel;

import pt.uminho.anote2.process.IR.IIRSearchConfiguration;

public abstract class APubmedSeach extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public abstract boolean validateOptions();
	public abstract IIRSearchConfiguration getConfiguration();
	public abstract void updateInfo(IIRSearchConfiguration pubmedSearchConfigutration);

}
