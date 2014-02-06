package pt.uminho.anote2.workflow.gui;

import javax.swing.JPanel;

import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.re.IREConfiguration;
import pt.uminho.anote2.workflow.datastructures.REWorkflowProcessesAvailableEnum;

public abstract class AREPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public abstract boolean validateOptions();
	public abstract IREConfiguration getConfiguration(ICorpus corpus,IIEProcess process);
	public abstract REWorkflowProcessesAvailableEnum getREProcess();

}
