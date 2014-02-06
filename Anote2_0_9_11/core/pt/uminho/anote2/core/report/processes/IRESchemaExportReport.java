package pt.uminho.anote2.core.report.processes;

import pt.uminho.anote2.core.report.IReport;

public interface IRESchemaExportReport extends IReport{
	public int relationsExported();
	public void incrementeRelationsExported(int increment);
}
