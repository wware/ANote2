package pt.uminho.anote2.core.report.processes;

import pt.uminho.anote2.core.report.IReport;

public interface INERSchemaExportReport extends IReport{
	public int entitiesExported();
	public void incremetExportedEntity(int incrementNumber);
}
