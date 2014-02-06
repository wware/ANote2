package pt.uminho.anote2.datastructures.configuration;

import pt.uminho.anote2.core.configuration.IProxy;
import pt.uminho.anote2.core.database.IDatabase;
/**
 * This class saves a database and proxy settings
 * 
 * @author Hugo Costa
 * 
 * @version 1.0 (16 Junho 2009)
 *
 */
public class ProxyAndDatabase {
	
	private IProxy proxy;
	private IDatabase db;
	
	public ProxyAndDatabase(IProxy proxy,IDatabase db)
	{
		this.proxy=proxy;
		this.db=db;
	}

	public IProxy getProxy() {
		return proxy;
	}

	public IDatabase getDatabase() {
		return db;
	}
	
	public void setProxy() {
		if(proxy!=null&&proxy.isEnable())
		{
			System.setProperty("http.proxyHost",proxy.getProxyHost());
			System.setProperty("http.proxyPort",proxy.getProxyPort());
		}
		else
		{	
			System.getProperties().remove("http.proxyHost");
			System.getProperties().remove("http.proxyPort");
		}
	}
	
}
