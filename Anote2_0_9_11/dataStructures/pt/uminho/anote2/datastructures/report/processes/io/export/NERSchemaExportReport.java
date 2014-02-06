package pt.uminho.anote2.datastructures.report.processes.io.export;

import pt.uminho.anote2.core.report.processes.INERSchemaExportReport;
import pt.uminho.anote2.datastructures.report.Report;

public class NERSchemaExportReport extends Report implements INERSchemaExportReport{

	public int entitiesExported;
	
	public NERSchemaExportReport() {
		super("Export NER Schema");
		entitiesExported = 0;
	}

	
	@Override
	public int entitiesExported() {
		return entitiesExported;
	}


	@Override
	public void incremetExportedEntity(int incrementNumber) {
		this.entitiesExported += incrementNumber;
	}


}
