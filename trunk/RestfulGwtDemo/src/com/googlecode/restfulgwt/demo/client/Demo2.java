package com.googlecode.restfulgwt.demo.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.restfulgwt.client.HasTokenHandling;

@Path("demo2/{name}")
public class Demo2 extends Composite implements HasTokenHandling {

	private static Demo2UiBinder uiBinder = GWT
			.create(Demo2UiBinder.class);

	interface Demo2UiBinder extends UiBinder<Widget, Demo2> {
	}

	@UiField
	Button button;

	@UiField
	@PathParam("name")
	TextBox tbHello;
	
	public Demo2() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@GET
	public Widget get() {
		return this;
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		Window.alert("Hello "+tbHello.getValue()+" !");
	}

}
