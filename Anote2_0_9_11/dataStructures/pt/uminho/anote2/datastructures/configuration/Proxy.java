package pt.uminho.anote2.datastructures.configuration;

import pt.uminho.anote2.core.configuration.IProxy;


public class Proxy implements IProxy{

	
	
	private boolean enable;
	private String proxyHost;
	private String proxyPort;
	
	public Proxy()
	{
		this.enable=false;
		this.proxyHost=new String();
		this.proxyPort=new String();
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

	@Override
	public void setProxyToSystem() {
		if(enable && getProxyHost()!=null && getProxyPort() !=null)
		{
			System.setProperty("http.proxyHost", getProxyHost());
			System.setProperty("http.proxyPort", getProxyHost());			
		}
		else
		{
			System.setProperty("http.proxyHost", "");
			System.setProperty("http.proxyPort", "");	
		}
	}
	
	public String toString()
	{
		if(!enable)
		{
			return IProxy.withoutProxy;
		}
		else
		{
			return getProxyHost() + "," + getProxyPort();
		}
	}

	public static IProxy getProxyByString(String propValue) {
		if(propValue.equalsIgnoreCase(IProxy.withoutProxy))
		{
			return new Proxy();
		}
		else
		{
			String[] props = propValue.split(",");
			if(props.length == 2)
			{
				return new Proxy(props[0], props[1]);
			}
			else
			{
				return new Proxy();
			}
		}
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof IProxy)
		{
			IProxy proxyObj = (IProxy) obj;
			if( !this.isEnable() && this.isEnable() == proxyObj.isEnable())
			{
				return true;
			}
			else
			{
				return this.getProxyHost().equals(proxyObj.getProxyHost()) && this.getProxyPort().equals(proxyObj.getProxyPort());
			}
		}
		return false;
	}

	@Override
	public boolean isEnable() {
		return enable;
	}

}
