package pt.uminho.anote2.datastructures.process.io.export;

import pt.uminho.anote2.process.IE.io.export.DefaultDelimiterValue;
import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.anote2.process.IE.io.export.TextDelimiter;
import pt.uminho.anote2.process.IE.re.IRECSVColumns;
import pt.uminho.anote2.process.IE.re.IRECSVConfiguration;

public class RECSVConfigutarion extends CSVConfiguration implements IRECSVConfiguration{

	private IRECSVColumns columns;
	private Delimiter entitiesDelimiter;
	private Delimiter entityExternalIDMainDelimiter;
	private Delimiter entityExternalIDIntraDelimiter;
	private boolean exportPublicationOtherID;
	private boolean exportResourceExternalID;
	
	public RECSVConfigutarion(Delimiter mainDelimiter,TextDelimiter textDelimiter, DefaultDelimiterValue defaultDelimiter,Delimiter entitiesDilimiter,Delimiter entityExternalIDMainDelimiter,
			Delimiter entityExternalIDIntraDelimiter,boolean exportPublicationOtherID,boolean exportResourceExternalID,IRECSVColumns columns) {
		super(mainDelimiter, textDelimiter, defaultDelimiter);
		this.entitiesDelimiter = entitiesDilimiter;
		this.columns = columns;
		this.entityExternalIDMainDelimiter = entityExternalIDMainDelimiter;
		this.entityExternalIDIntraDelimiter = entityExternalIDIntraDelimiter;
		this.exportPublicationOtherID = exportPublicationOtherID;
		this.exportResourceExternalID = exportResourceExternalID;
	}

	@Override
	public IRECSVColumns getColumnConfiguration() {
		return columns;
	}

	@Override
	public Delimiter getEntityDelimiter() {
		return entitiesDelimiter;
	}

	public static IRECSVConfiguration getDefaultValues() {
		IRECSVColumns columnConf = new RECSVColumn(0, 1, 3, 5, 6, 2, 4, 7, 8, 9);
		return new RECSVConfigutarion(Delimiter.TAB, TextDelimiter.QUOTATION_MARK, DefaultDelimiterValue.HYPHEN,Delimiter.SEMICOLON, Delimiter.WHITE_SPACE,Delimiter.COLON,true,true,columnConf);
	}

	@Override
	public Delimiter getEntityExternalIDMainDelimiter() {
		return entityExternalIDMainDelimiter;
	}

	@Override
	public Delimiter getEntityExternalIDIntraDelimiter() {
		return entityExternalIDIntraDelimiter;
	}

	@Override
	public boolean exportPublicationOtherID() {
		return exportPublicationOtherID;
	}

	@Override
	public boolean exportResourceExternalID() {
		return exportResourceExternalID;
	}

}
