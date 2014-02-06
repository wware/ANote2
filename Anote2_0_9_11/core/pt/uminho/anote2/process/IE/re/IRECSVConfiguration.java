package pt.uminho.anote2.process.IE.re;

import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.anote2.process.IE.io.export.ICSVExportConfiguration;

public interface IRECSVConfiguration extends ICSVExportConfiguration{
	public IRECSVColumns getColumnConfiguration();
	public Delimiter getEntityDelimiter();
	public Delimiter getEntityExternalIDMainDelimiter();
	public Delimiter getEntityExternalIDIntraDelimiter();
	public boolean exportPublicationOtherID();
	public boolean exportResourceExternalID();
}
