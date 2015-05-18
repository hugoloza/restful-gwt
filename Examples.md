## Examples ##

### Simple Example ###
```
@GET
@Produces("text/html")
public String textHtmlDemo() {
	return "welcome <b>home<b> !";
}
```
A new gwt Label with : "welcome **home** !" will be appended to the RootPanel.

### Example 2 ###
```
@GET
@Path("hello/{name}")
public void helloWithIntegerParam(@PathParam("name") Integer myName) {
   Window.alert("Hello "+(myName+1));
}
```
If you try to access http://.../MyPage.html#hello/1 the helloWithIntegerParam will be invoked **on client side** with the parameter "1".
Since the return type is void, nothing will be added automatically to the Rootpanel. You will just see a popup displaying "Hello 2".

### Example 3 ###
```
@GET
public Widget demo3() {
	return new Button("Click me");
}
```
Yes, returning widgets works too !

### Example 4 ###
```
@Path("search/{name}")
public class Test {

@PathParam("name")
String name;

@GET
public void search() {
   myTextBox.setValue(name);
}
}
```
Field initialisation is working the same way.

### Example 5 ###
```
@Path("search/{name}")
public class Test {

@UiField
@PathParam("name")
TextBox tbName;

@GET
public void search() {
   // nothing here
}
}
```
In this special case, when a field extends "HasValue" class, the field have to be initialized by yourself, the framework will call "setValue" with the value extracted from the token. Here, it's UiBinder that initialize the field, and RestfulGWT will fill it's value. The result will be the same as example 4.