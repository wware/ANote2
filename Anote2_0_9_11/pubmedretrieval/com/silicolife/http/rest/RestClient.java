package com.silicolife.http.rest;


import java.util.HashMap;
import java.util.Map;

import com.silicolife.http.HTTPClient;
import com.silicolife.http.ResponseHandler;
import com.silicolife.http.exceptions.ClientErrorException;
import com.silicolife.http.exceptions.ConnectionException;
import com.silicolife.http.exceptions.RedirectionException;
import com.silicolife.http.exceptions.ResponseHandlingException;
import com.silicolife.http.exceptions.ServerErrorException;


public class RestClient {
	
	HTTPClient client;
	String host;
	Integer port;
	
	public <T> T index(String controller, ResponseHandler<T> responseHandler) throws ConnectionException, RedirectionException, ClientErrorException, ServerErrorException, ResponseHandlingException {
		String url = getHostname() + "/" + controller;
		return client.get(url, new HashMap<String, String>(), responseHandler);
	}
	
	public <T> T index(String controller, RequestParameters params, ResponseHandler<T> responseHandler) throws ConnectionException, RedirectionException, ClientErrorException, ServerErrorException, ResponseHandlingException {
		String url = getHostname() + "/" + controller + "?" + params.join();
		return client.get(url, new HashMap<String, String>(), responseHandler);
	}
	

	public <T> T index(String controller, RequestParameters params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws ConnectionException, RedirectionException, ClientErrorException, ServerErrorException, ResponseHandlingException {
		String url = getHostname() + "/" + controller;
		return client.get(url, headers, responseHandler);
	}
	
	public <T> T find(String controller, String id, ResponseHandler<T> responseHandler) throws ConnectionException, RedirectionException, ClientErrorException, ServerErrorException, ResponseHandlingException {
		String url = getHostname() + "/" + controller + "/id";
		return client.get(url, new HashMap<String, String>(), responseHandler);
	}
	
	

	public String getHostname() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
