package com.googlecode.restfulgwt.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

public class TokenManagedEntryPoint extends ArrayList<TokenResourceDeclaration> implements EntryPoint {

	boolean silent = false;
	
	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	@Override
	public void onModuleLoad() {
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				exec(History.getToken());
			}
		});
		exec(History.getToken());
	}
	
	protected void exec(String token) {
		try {
			if(!tryExec(token)) {
				String message = "Page '"+token+"' not found";
				if(!silent)
					Window.alert(message);
				else
					throw new RuntimeException(message);
			}
		} catch(Exception ex) {
			if(!silent) {
				ex.printStackTrace();
				Window.alert(ex.getMessage());
			} else
				throw new RuntimeException(ex);
		}
	}
	
	@Override
	public boolean add(TokenResourceDeclaration e) {
		return super.add(e);
	}

	protected boolean tryExec(String token) throws Exception {
		for(TokenResourceDeclaration decl : this) {
			if(decl.exec(token))
				return true;
		}
		return false;
	}

}
