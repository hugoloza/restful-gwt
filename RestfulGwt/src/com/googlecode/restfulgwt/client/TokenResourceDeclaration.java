package com.googlecode.restfulgwt.client;


public abstract class TokenResourceDeclaration {
	
	protected abstract boolean exec(String test) throws Exception;
		
	public static native String firstMatch(String text, String regEx)/*-{
	  var re = new RegExp(regEx);
	  var m = re.exec(text);
	  if (m == null) {
	    return null;
	  } else {
	  	return m[0];
	  }
	}-*/;
}
