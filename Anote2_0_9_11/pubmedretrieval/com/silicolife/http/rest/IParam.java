package com.silicolife.http.rest;

public interface IParam<T> {
	
	T getRaw();
	String buildString(String key);
	
}
