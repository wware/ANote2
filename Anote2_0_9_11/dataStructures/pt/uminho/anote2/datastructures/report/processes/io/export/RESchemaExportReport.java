package pt.uminho.anote2.datastructures.report.processes.io.export;

import pt.uminho.anote2.core.report.processes.IRESchemaExportReport;
import pt.uminho.anote2.datastructures.report.Report;

public class RESchemaExportReport extends Report implements IRESchemaExportReport{

	private int relationsExported;
	
	
	public RESchemaExportReport() {
		super("Export NER Schema");
		this.relationsExported = 0;
	}

	@Override
	public int relationsExported() {
		return relationsExported;
	}

	@Override
	public void incrementeRelationsExported(int increment) {
		relationsExported += increment;	
	}

}
