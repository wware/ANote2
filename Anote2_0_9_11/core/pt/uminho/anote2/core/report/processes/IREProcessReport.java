package pt.uminho.anote2.core.report.processes;

import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.IREProcess;

public interface IREProcessReport extends INERProcessReport{
	public int getNumberOfRelations();
	public IIEProcess nerBasedProcesss();
	public void increaseRelations(int newRelations);
	public IREProcess getREProcess();
}
