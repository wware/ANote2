package pt.uminho.anote2.datastructures.process.io.export;

import pt.uminho.anote2.process.IE.io.export.DefaultDelimiterValue;
import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.anote2.process.IE.io.export.TextDelimiter;

public class CSVConfiguration {
	
	private Delimiter mainDelimiter;
	private TextDelimiter textDelimiter;
	private DefaultDelimiterValue defaultDelimiter;


	public CSVConfiguration(Delimiter mainDelimiter,
			TextDelimiter textDelimiter, DefaultDelimiterValue defaultDelimiter) {
		super();
		this.mainDelimiter = mainDelimiter;
		this.textDelimiter = textDelimiter;
		this.defaultDelimiter = defaultDelimiter;
	}
	
	
	public Delimiter getMainDelimiter() {
		return mainDelimiter;
	}


	public TextDelimiter getTextDelimiter() {
		return textDelimiter;
	}


	public DefaultDelimiterValue getDefaultDelimiter() {
		return defaultDelimiter;
	}
	
}
