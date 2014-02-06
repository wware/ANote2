package pt.uminho.anote2.core.report.processes;

import pt.uminho.anote2.process.IE.INERProcess;

public interface INERProcessReport extends IProcessReport{
	public int getNumberOFEntities();
	public void incrementDocument();
	public void incrementEntitiesAnnotated(int nesAnnotations);
	public INERProcess getNERProcess();

}
