package com.silicolife.http.rest.params;

import com.silicolife.http.rest.IParam;

public class StringParam implements IParam<String> {
	
	protected String value;
	
	public StringParam(String string) {
		this.value = string;
	}
	
	@Override
	public String buildString(String key) {
		return key+"="+value;
	}
	
	public String getRaw() { return value; }
}
