package pt.uminho.anote2.datastructures.configuration;

import pt.uminho.anote2.core.configuration.IProxy;


public class Proxy implements IProxy{

	private boolean enable;
	private String proxyHost;
	private String proxyPort;
	
	public Proxy()
	{
		this.enable=false;
		this.proxyHost=null;
		this.proxyPort=null;
	}
	
	public Proxy(String proxyHost,String proxyPort)
	{
		this.enable=true;
		this.proxyHost=proxyHost;
		this.proxyPort=proxyPort;
	}
	

	public String getProxyHost() {
		return this.proxyHost;
	}


	public String getProxyPort() {
		return this.proxyPort;
	}

	
	public boolean isEnable(){
		return this.enable;
	}

	
	public void disableProxy() {
		this.enable=false;
		
	}

	
	public boolean enableProxy(String proxyHost, String proxyPort) {
		if(this.proxyHost==null&&proxyHost==null)
		{
			return false;
		}
		else if(this.proxyPort==null&&proxyPort==null)
		{
			return false;
		}
		if(proxyHost!=null)
		{
			this.proxyHost=proxyHost;
		}
		if(proxyPort!=null)
		{
			this.proxyPort=proxyPort;
		}
		return true;
	}

}
