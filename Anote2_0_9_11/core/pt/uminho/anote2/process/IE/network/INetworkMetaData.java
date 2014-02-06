package pt.uminho.anote2.process.IE.network;

import java.util.Date;

public interface INetworkMetaData {
	public String getType();
	public String getDescription();
	public Date getDate();
	public String getTitle();
	public String getSource();
	public String getFormat();
}
