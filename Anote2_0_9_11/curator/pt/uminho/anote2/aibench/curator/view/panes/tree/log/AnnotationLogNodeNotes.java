package pt.uminho.anote2.aibench.curator.view.panes.tree.log;

public class AnnotationLogNodeNotes {
	
	private String info;

	public AnnotationLogNodeNotes(String info)
	{
		this.setInfo(info);
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String toString()
	{
		return "Notes : "+info;
	}
}
