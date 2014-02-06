package pt.uminho.anote2.datastructures.report.processes;

import java.util.Properties;

import pt.uminho.anote2.core.report.processes.INERProcessReport;
import pt.uminho.anote2.datastructures.report.Report;
import pt.uminho.anote2.process.ProcessTypeEnum;
import pt.uminho.anote2.process.IE.INERProcess;

public class NERProcessReport extends Report implements INERProcessReport{

	private int numberOfDocuments;
	private int entitiesExtracted;
	private INERProcess nerProcess;
	
	public NERProcessReport(String title,INERProcess process) {
		super(title);
		this.numberOfDocuments = 0;
		this.entitiesExtracted = 0;
		this.nerProcess = process;
	}
	
	public NERProcessReport(String title,Properties prop,INERProcess process) {
		super(title,prop);
		this.numberOfDocuments = 0;
		this.entitiesExtracted = 0;
		this.nerProcess = process;
	}

	public int getNumberOfDocuments() {
		return numberOfDocuments;
	}

	public ProcessTypeEnum getProcessType() {
		return ProcessTypeEnum.NER;
	}

	public int getNumberOFEntities() {
		return entitiesExtracted;
	}

	public void incrementDocument() {
		numberOfDocuments++;
	}

	public void incrementEntitiesAnnotated(int newEntities) {
		this.entitiesExtracted += newEntities;
	}

	@Override
	public INERProcess getNERProcess() {
		return nerProcess;
	}
	
	

}
