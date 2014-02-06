package pt.uminho.anote2.workflow.gui;

import java.sql.SQLException;

import javax.swing.JPanel;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.process.IE.ner.INERConfiguration;
import pt.uminho.anote2.workflow.datastructures.NERWorkflowProcessesAvailableEnum;

public abstract class ANERPanel extends JPanel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public abstract boolean validateOptions();
	public abstract INERConfiguration getConfiguration(ICorpus corpus) throws DatabaseLoadDriverException, SQLException;
	public abstract NERWorkflowProcessesAvailableEnum getNERProcess();
	
}
