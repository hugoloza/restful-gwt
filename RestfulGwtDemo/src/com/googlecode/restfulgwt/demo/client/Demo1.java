package com.googlecode.restfulgwt.demo.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.restfulgwt.client.HasTokenHandling;

public class Demo1 implements HasTokenHandling {

	@GET
	@Produces("text/html")
	public String textHtmlDemo() {
		return "welcome <b>home<b> !";
	}

	@GET
	@Path("textplaindemo")
	public String textPlainDemo() {
		return "welcome <b>home<b> !";
	}

	@GET
	@Path("widgetdemo")
	public Widget widgetDemo() {
		return new Button("My <b>demo</b> button");
	}
	
	@GET
	@Path("voiddemo")
	public void voidDemo() {
		Window.alert("Hello !");
	}
	
	@GET
	@Path("hello/{name}")
	public void helloWithIntegerParam(@PathParam("name") Integer myName) {
		Window.alert("Hello "+(myName+1));
	}


	

}
