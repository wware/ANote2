package pt.uminho.anote2.datastructures.report.processes;

import java.util.Properties;

import pt.uminho.anote2.core.report.processes.IREProcessReport;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.INERProcess;
import pt.uminho.anote2.process.IE.IREProcess;

public class REProcessReport extends NERProcessReport implements IREProcessReport{

	private int numberOfRElations;
	private IIEProcess nerProcess;
	private IREProcess reProcess;
	
	public REProcessReport(String title,IIEProcess ieProcess,IREProcess reProcess) {
		super(title,null);
		this.nerProcess = ieProcess;
		this.numberOfRElations = 0;
		this.reProcess = reProcess;
	}
	
	public REProcessReport(String title,Properties prop,INERProcess process)
	{
		super(title,prop,null);
		this.nerProcess = process;
		this.numberOfRElations = 0;
	}

	public int getNumberOfRelations() {
		return numberOfRElations;
	}

	public IIEProcess nerBasedProcesss() {
		return nerProcess;
	}

	public void increaseRelations(int newRelations) {
		this.numberOfRElations +=newRelations;
	}

	@Override
	public IREProcess getREProcess() {
		return reProcess;
	}

}
