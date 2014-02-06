package pt.uminho.anote2.datastructures.process.io.export;

import pt.uminho.anote2.process.IE.io.export.DefaultDelimiterValue;
import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.anote2.process.IE.io.export.TextDelimiter;
import pt.uminho.anote2.process.IE.ner.export.INERCSVColumns;
import pt.uminho.anote2.process.IE.ner.export.INERCSVConfiguration;

public class NERCVSConfiguration extends CSVConfiguration implements INERCSVConfiguration{


	private INERCSVColumns columns;
	private boolean exportPublicationID;
	private boolean exportResourceInformation;
	private boolean exportResourceExternalIDs;
	private Delimiter externalIDDelimiter;
	private Delimiter intraExtenalIDdelimiter;

	public NERCVSConfiguration(Delimiter mainDelemeter,TextDelimiter textDelimiter,DefaultDelimiterValue defaultDelimiter, INERCSVColumns columns) {
		super(mainDelemeter,textDelimiter,defaultDelimiter);
		this.columns = columns;
		this.exportPublicationID = false;
		this.exportResourceInformation = false;
		this.exportResourceExternalIDs = false;
		this.externalIDDelimiter = Delimiter.VERTICAL_BAR;
		this.intraExtenalIDdelimiter = Delimiter.COLON;
		
	}
	
	public NERCVSConfiguration(Delimiter mainDelemeter,TextDelimiter textDelimiter,DefaultDelimiterValue defaultDelimiter, INERCSVColumns columns,boolean exportPublicationID,boolean exportResourceInformation,boolean exportResourceExternalIDs,
			Delimiter externalIDDelimiter, Delimiter intraExtenalIDdelimiter) {
		super(mainDelemeter,textDelimiter,defaultDelimiter);
		this.columns = columns;
		this.exportPublicationID = exportPublicationID;
		this.exportResourceInformation =  exportResourceInformation;
		this.exportResourceExternalIDs = exportResourceExternalIDs;
		this.externalIDDelimiter = externalIDDelimiter;
		this.intraExtenalIDdelimiter = intraExtenalIDdelimiter;
	}

	@Override
	public INERCSVColumns getColumnConfiguration() {
		return columns;
	}

	public static NERCVSConfiguration getDefaultSettings()
	{
		INERCSVColumns columnsDefenition = new NERCSVColumn(0, 1, 2, 3, 6,4,5,7,8);
		return new NERCVSConfiguration(Delimiter.TAB,TextDelimiter.QUOTATION_MARK,DefaultDelimiterValue.HYPHEN,columnsDefenition,true,true,true,Delimiter.SEMICOLON,Delimiter.COLON);
	}

	public boolean exportPublicationOtherID() {
		return exportPublicationID;
	}

	@Override
	public boolean exportResourceInformation() {
		return exportResourceInformation;
	}

	@Override
	public boolean exportResourceExternalID() {
		return exportResourceExternalIDs;
	}
	
	public Delimiter getExternalIDDelimiter() {
		return externalIDDelimiter;
	}
	
	public Delimiter getIntraExtenalIDdelimiter() {
		return intraExtenalIDdelimiter;
	}


}
