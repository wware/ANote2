package pt.uminho.anote2.datastructures.report;

import java.util.Properties;

import pt.uminho.anote2.core.report.IReport;

public class Report implements IReport{
	
	private String title;
	private Properties properties;
	private long time;
	private boolean finishing = true;
	private String notes;
	
	
	public Report(String title)
	{
		this.title=title;
		this.properties = new Properties();
		this.time = 0;
	}
	
	public Report(String title, Properties properties)
	{
		this.title=title;
		this.properties = properties;
		this.time = 0;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isFinishing() {
		return finishing;
	}

	public void setFinishing(boolean finishing) {
		this.finishing = finishing;
	}

	public String getTitle() {
		return title;
	}

	public Properties getProperties() {
		return properties;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time=time;
	}

	public void setcancel() {
		this.finishing = false;
	}

}
