package pt.uminho.anote2.process.IE.re;

import pt.uminho.anote2.process.IE.IIECSVColumns;

public interface IRECSVColumns extends IIECSVColumns{
	public int getLeftEntitiesColumn();
	public int getRightEntitiesColumn();
	public int getSentenceColumn();
	public int getLeftEntitiesExternalIDs();
	public int getRightEntitiesExternalIDs();
}
