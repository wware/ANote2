package pt.uminho.anote2.process.IE.io.export;


public interface ICSVExportConfiguration {
	public Delimiter getMainDelimiter();
	public TextDelimiter getTextDelimiter();
	public DefaultDelimiterValue getDefaultDelimiter();
}
