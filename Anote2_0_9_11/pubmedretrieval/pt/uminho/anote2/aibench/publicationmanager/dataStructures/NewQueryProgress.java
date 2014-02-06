package pt.uminho.anote2.aibench.publicationmanager.dataStructures;

import pt.uminho.anote2.aibench.utils.timeleft.SimpleTimeLeft;

public class NewQueryProgress extends SimpleTimeLeft{
	
	private float yearProgress;
	private String year;

	public void setYearProgress(float yearProgress) {
		this.yearProgress = yearProgress;
	}

	public float getYearProgress() {
		return yearProgress;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}
