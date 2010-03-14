package com.googlecode.restfulgwt.demo.client;

import com.google.gwt.core.client.GWT;
import com.googlecode.restfulgwt.client.TokenManagedEntryPoint;
import com.googlecode.restfulgwt.client.TokenResourceDeclaration;

public class DemoEntryPoint extends TokenManagedEntryPoint {

	private static final long serialVersionUID = 1L;

	public DemoEntryPoint() {
		add((TokenResourceDeclaration)GWT.create(Demo1.class));
		add((TokenResourceDeclaration)GWT.create(Demo2.class));
	}

}

