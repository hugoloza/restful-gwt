## Project initalization ##
Add the .jar file to your classpath and add this line to your .gwt.xml
```
 <inherits name="com.googlecode.restfulgwt.RestfulGwt"/>
```

Your entry point should extend "TokenManagedEntryPoint" and should not override onModuleLoad().

In your entrypoint constructor, declare all root resources this way :
```
add((TokenResourceDeclaration)GWT.create(MyResource1.class));
add((TokenResourceDeclaration)GWT.create(MyResource2.class));
```

Add "**implements HasTokenHandling**" on your resources and you're good to go !

Feel free to browse or checkout the demo source code in the repository : [here](http://code.google.com/p/restful-gwt/source/browse/#svn/trunk/RestfulGwtDemo)

## About types ##

### Return type ###
If the return type is not void, the result will be appended to the root panel.
If it is a String type, you can specify a @Produce if it should be treated as HTML data or plain text, you can return a Widget too.

### Field initialization ###
  * String
  * Any class having a constructor with a String parameter.
  * **Special case** : For all subclass of "HasValue", the field have to be initialized in the constructor and RestfulGWT will call the method setValue automatically. This mean you can combine nicely the framework with UiBinder like this :
```
	@UiField
	@PathParam("name")
	TextBox tbHello;
```
and you TextBox will be filled with the value from the token.

Current Limitation :
  * QueryParam not supported for now
  * Sub Resource not tested
  * RegEx is the Javascript RegEx, not the java one.



You can now check [Examples](Examples.md)