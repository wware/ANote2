package pt.uminho.anote2.process.IE.ner.export;

import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.anote2.process.IE.io.export.ICSVExportConfiguration;

public interface INERCSVConfiguration extends ICSVExportConfiguration{
	public INERCSVColumns getColumnConfiguration();
	public Delimiter getExternalIDDelimiter();
	public Delimiter getIntraExtenalIDdelimiter();
	public boolean exportPublicationOtherID();
	public boolean exportResourceInformation();
	public boolean exportResourceExternalID();

}
