In the GWT framework, you have to handle a simple String value for describing your current page. If you are trying to handle the history the correct way, you have to do some lame string parsing in order to extract some ids from the token. A very similar problem has been solved in a very elegant way on the server side for processing URLs with JAX-RS.

So, why not using the same API on client side ? This is the purpose of this framework.
It's very similar to JAX-RS developpement, it use the same annotations.

You don't know JAX-RS ? Check this tutorial for a quick view of JAX-RS on server side : [Overview of JAX-RS @sun](http://wikis.sun.com/display/Jersey/Overview+of+JAX-RS+1.0+Features)

Benefits :
  * Easy history handling
  * No manual string spliting and parsing
  * Easy to use (using beans)
  * Cleaner code
  * Modularity


You can in some extend expose the same class on both client and server side this will produce :

http://.../MyFile.html#people/view/1211 => client side invocation

http://.../people/view/1211 ==> server side invocation

The usage is really easy :
```
@GET
@Produces("text/html")
public String textHtmlDemo() {
	return "welcome <b>home<b> !";
}
```
A new gwt Label with : "welcome **home** !" will be appended to the RootPanel.

  * [More advanced examples](Examples.md)
  * [GettingStarted](GettingStarted.md)
  * [Online Demo](http://restful-gwt.googlecode.com/svn/trunk/RestfulGwtDemo/demo/RestfulGwtDemo.html)

Give us your feedback in the [Discussion Group](http://groups.google.com/group/restful-gwt-discuss) !