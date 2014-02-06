package pt.uminho.anote2.ner.corpus;

@SuppressWarnings("rawtypes")
public 	class Abreviation implements Comparable{
	private String longform;
	private String shortForm;
	private String idLong;
	private String idShort;
	
	public Abreviation(String longform, String shortForm, String idLong, String idShort) {
		this.longform = longform;
		this.shortForm = shortForm;
		this.idLong = idLong;
		this.idShort = idShort;
	}
	
	public String getLongform() {return longform;}
	public void setLongform(String longform) {this.longform = longform;}
	public String getShortForm() {return shortForm;}
	public void setShortForm(String shortForm) {this.shortForm = shortForm;}
	public String getIdLong() {return idLong;}
	public void setIdLong(String idLong) {this.idLong = idLong;}
	public String getIdShort() {return idShort;}
	public void setIdShort(String idShort) {this.idShort = idShort;}
	
	@Override
	public int compareTo(Object arg0) {
		Abreviation o2 = (Abreviation) arg0;
		if(o2.longform.equals(this.longform) && o2.shortForm.equals(this.shortForm) && o2.idLong.equals(this.idLong) && o2.idShort.equals(this.idShort))
			return 0;
		else 
			return 1;
	}
}

