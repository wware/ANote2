package pt.uminho.anote2.aibench.publicationmanager.gui.help;

import java.util.Set;

import javax.swing.JPanel;

import pt.uminho.anote2.core.document.IPublication;

public abstract class ASelectQueryDocumentsPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public abstract boolean validateOptions();
	public abstract Set<IPublication> getPublications();
	public abstract void updateSelectedPublications(Set<IPublication> publications);
}
