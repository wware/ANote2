package pt.uminho.anote2.datastructures.process.ir.configuration;

import java.util.Properties;

import pt.uminho.anote2.process.IR.IIRSearchConfiguration;


public class IRSearchConfiguration implements IIRSearchConfiguration{

	
	public String keywords;
	public String organism;
	public Properties propeties;
	
	
	public IRSearchConfiguration(String keywords, String organism,
			Properties propeties) {
		super();
		this.keywords = keywords;
		this.organism = organism;
		this.propeties = propeties;
	}

	@Override
	public String getKeywords() {
		return keywords;
	}

	@Override
	public String getOrganism() {
		return organism;
	}

	@Override
	public Properties getProperties() {
		return propeties;
	}

}
