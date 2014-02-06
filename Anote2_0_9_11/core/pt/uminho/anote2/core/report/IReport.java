package pt.uminho.anote2.core.report;

import java.util.Properties;

public interface IReport {
	public String getTitle();
	public Properties getProperties();
	public Long getTime();
	public void setTime(Long time);
	public boolean isFinishing();
	public void setcancel();
	public String getNotes();
	public void setNotes(String notes);
}
