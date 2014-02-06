package pt.uminho.anote2.core.configuration;

import pt.uminho.anote2.core.configuration.IConfiguration;



/**
 * This interface represent a HttpProxy for correct acess to internet services 
 * 
 * @author Hugo Costa
 * 
 * @version 1.0 (16 Junho 2009)
 *
 */
public interface IProxy extends IConfiguration{
	/**
	 * Method that return the ProxyHost
	 * 
	 * @return ProxyHost
	 * -- Null if not exist
	 */
	public String getProxyHost();
	/**
	 * Method that return the ProxyPort
	 * 		-- Null if not exist
	 * 
	 * @return ProxyPort
	 * -- Null if not exist
	 */
	public String getProxyPort();
	/**
	 * Method that return the proxy status
	 * 
	 * @return True -> Proxy Enable
	 * 	     -- False -> Proxy Disable
	 */
	public boolean isEnable();
	/**
	 * Method that enable Proxy
	 * 
	 * @param proxyHost
	 * @param proxyPort
	 * @return true if is enable
	 * 		 -- false
	 */
	public boolean enableProxy(String proxyHost,String proxyPort);
	/**
	 * Method that disable Proxy
	 */
	public void disableProxy();
}
