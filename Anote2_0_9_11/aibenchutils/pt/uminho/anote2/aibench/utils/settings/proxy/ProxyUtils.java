package pt.uminho.anote2.aibench.utils.settings.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import es.uvigo.ei.aibench.workbench.Workbench;

public class ProxyUtils {
	
	public static boolean validProxy(String hostname, String port) {
		
		boolean valid = true;

		
		/** test if the proxy is reachable */
		InetSocketAddress socket = null;
		try {
			socket = new InetSocketAddress(hostname,new Integer(port));
		} catch (Exception e) {
//			Workbench.getInstance().error(e);
			return false;
		}
		
		if(socket.isUnresolved()){
			Workbench.getInstance().warn("The proxy ["+socket.getHostName()+":"+socket.getPort()+"] does not seem to be reachable... please confirm the host and port!");
			return false;
		}
		
//		try {
//			if(!socket.getAddress().isReachable(5000)){
//				Workbench.getInstance().warn("The proxy ["+socket.getHostName()+":"+socket.getPort()+"] does not seem to be reachable... please confirm the host, port and network accessibility (e.g. firewall)!");
//				return false;
//			}
//		} catch (IOException e) {			
//			Workbench.getInstance().error(e);
//			e.printStackTrace();
//			return false;
//		}
		
		socket = null;
		System.gc();
		
		return valid;
	}
	

	public static boolean validConnection() {
		
		boolean valid = true;
		
		HttpClientParams params = new HttpClientParams();
		params.setConnectionManagerTimeout(1000);
		params.setSoTimeout(1000);
		
		HttpClient client= new HttpClient(params);

		GetMethod get = new GetMethod("http://www.google.com");

		int statusCode=0;
		try {
			statusCode = client.executeMethod(get);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
			return false;
		}

		return valid;
	}

	
	

}
