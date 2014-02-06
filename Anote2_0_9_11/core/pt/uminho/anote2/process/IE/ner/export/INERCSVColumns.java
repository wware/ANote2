package pt.uminho.anote2.process.IE.ner.export;

import pt.uminho.anote2.process.IE.IIECSVColumns;

public interface INERCSVColumns extends IIECSVColumns{
	public int getClassColumn();
	public int getResourceIDColumn();
	public int getResourceInformation();
	public int getResourceExternalIDs();
}
