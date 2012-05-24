package com.googlecode.restfulgwt.generator;

import java.io.PrintWriter;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.HasAnnotations;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class RestGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		TypeOracle oracle = context.getTypeOracle();
		try {
			JClassType type = oracle.getType(typeName);
			String packageName = type.getPackage().getName();
			String simpleName = type.getSimpleSourceName()+"_Generated";
			ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
			composer.setSuperclass("com.googlecode.restfulgwt.client.TokenResourceDeclaration");
			PrintWriter printWriter = context.tryCreate(logger, composer.getCreatedPackage(), composer.getCreatedClassShortName());
			if(printWriter == null)
				return composer.getCreatedClassName();
			SourceWriter writer = composer.createSourceWriter(context,printWriter);

			// la classe generee implements ValueChangeListener<String> ?
			// @Param sur HasValue<String> pour auto remplissage de TextBox
			// defaut regex "[^/]+?",
			// {username: [a-zA-Z][a-zA-Z_0-9]}

			writer.indent();
			writer.println("protected boolean exec(final String token) throws Exception {");
			// check if class Path match the url
			writer.indent();
			writer.println("String path = token;");
			writer.println("String query = \"\";");
			writer.println("int qmarkPos = path.indexOf('?');");
			writer.println("if(qmarkPos >= 0) {");
			writer.println("        query = path.substring(qmarkPos+1);");
			writer.println("        path = path.substring(0,qmarkPos);");
			writer.println("}");
			writer.println("final com.googlecode.restfulgwt.client.SimpleMultivaluedMap<String,String> queryMap = new com.googlecode.restfulgwt.client.SimpleMultivaluedMap<String,String>();");
			writer.println("for(String queryItem : query.split(\"&\")) {");
			writer.println("	String[] kv = queryItem.split(\"=\");");
			writer.println("	String key = \"\";");
			writer.println("	String value = \"\";");
			writer.println("	if(kv.length > 0)");
			writer.println("		key = kv[0];");
			writer.println("	if(kv.length > 1)");
			writer.println("		value = kv[1];");
			writer.println("	queryMap.add(key,value);");
			writer.println("}");
			writer.println("String currentPath = path;");
			Path classDefinedPath = type.findAnnotationInTypeHierarchy(Path.class);
			if(classDefinedPath != null)
				processPath(writer,"currentPath",classDefinedPath,"return false;");
			// search a matching method
			for(JMethod m : type.getMethods()) {
				boolean managed = m.isAnnotationPresent(GET.class) ||
						m.isAnnotationPresent(Path.class);

				if(!managed)
					continue;
				writer.println("while(true){");
				writer.indent();
				writer.println("String methodCurrentPath = currentPath;");
				Path pathAnno = m.getAnnotation(Path.class);
				if(pathAnno != null)
					processPath(writer, "methodCurrentPath", pathAnno, "break;");
				writer.println("if(methodCurrentPath.length() != 0)");
				writer.println("     break;");
				int paramNo = 0;

				// TODO catch the "throws" exception of the method
				writer.println("com.google.gwt.core.client.GWT.runAsync(new com.google.gwt.core.client.RunAsyncCallback(){ ");
				writer.indent();
				writer.println("public void onFailure(Throwable caught){ ");
				writer.println("	throw new RuntimeException(caught);");
				writer.println("}");
				writer.println("public void onSuccess(){ ");
				writer.indent();

				for(JParameter parameter : m.getParameters()) {
					paramNo++;
					writer.println(parameter.getType().getQualifiedSourceName()+" callParam_"+paramNo+" = null;");
					assignFromAnnotations(writer,parameter,"callParam_"+paramNo,parameter.getType(),oracle);
				}

				// invoke method with param et obtain result;
				String callParamString = "";
				for(int i=1; i<=m.getParameters().length; i++) {
					if(callParamString.length() != 0)
						callParamString += ",";
					callParamString += "callParam_"+i;
				}
				if(!m.getReturnType().toString().equals("void"))
					writer.print(m.getReturnType().getQualifiedSourceName()+" o = ");

				// new MyResource
				writer.println("final "+type.getName()+" res = new "+type.getName()+"();");
				// init fields with path params
				for(JField f : type.getFields()) {
					assignFromAnnotations(writer,f,"res."+f.getName(),f.getType(),oracle);
				}
				writer.println("res."+m.getName()+"("+callParamString+");");
				writer.outdent();
				writer.println("}");
				writer.outdent();
				writer.println("});");

				// Recursing if no method and has a subresource
				if(m.getReturnType() != null && !m.isAnnotationPresent(GET.class)) {
					writer.println("TokenResourceDeclaration resDec = GWT.create("+m.getReturnType().getQualifiedSourceName()+".class)");
					//writer.println("boolean handled = resDec.fillObj(methodCurrentPath,o);");
					writer.println("boolean handled = resDec.exec(methodCurrentPath);");
					writer.println("if(handled) return true;");
					writer.println("break;");
				} else {
					if(!m.getReturnType().toString().equals("void"))
						handleReturnValue(writer, m, oracle);
					writer.println("return true;");
				}
				writer.outdent();
				writer.println("}"); // close while(true) loop
			}
			writer.println("return false;");
			writer.outdent();
			writer.println("}"); // close exec
			writer.commit(logger);
			return composer.getCreatedClassName();
		} catch(Exception ex) {
			logger.log(TreeLogger.ERROR, "Unable to generate code for "+typeName, ex);
			throw new UnableToCompleteException();
		}
	}

	private void handleReturnValue(SourceWriter writer, JMethod m, TypeOracle oracle) throws NotFoundException {
		String expression = null;

		JClassType widgetType = oracle.getType("com.google.gwt.user.client.ui.Widget").isClassOrInterface();
		if(m.getReturnType().isClass().isAssignableTo(widgetType))
			expression = "o";
		else {
			Produces produce = m.getAnnotation(Produces.class);
			if(produce == null || produce.value().equals("text/plain")) {
				expression = "new com.google.gwt.user.client.ui.Label(\"\"+o)";
			} else {
				expression = "new com.google.gwt.user.client.ui.HTML(\"\"+o)";
			}
		}
		writer.println("com.google.gwt.user.client.ui.RootPanel.get().clear();");
		writer.println("com.google.gwt.user.client.ui.RootPanel.get().add("+expression+");");
	}

	private void processPath(SourceWriter writer, String pathVar, Path path, String abortStatement) throws UnableToCompleteException {
		String currentToken = path.value();
		while(currentToken.length() != 0) {
			if(currentToken.startsWith("{")) {
				int endPos = currentToken.indexOf('}');
				if(endPos < 0)
					throw new UnableToCompleteException();
				String pathParamName = currentToken.substring(1,endPos);
				currentToken = currentToken.substring(endPos+1);

				int doubleDotPos = pathParamName.indexOf(":");
				String regEx = "[^/]+";
				if(doubleDotPos > 0) {
					regEx = pathParamName.substring(doubleDotPos+1).trim();
					pathParamName = pathParamName.substring(0,doubleDotPos).trim();
				}
				writer.println("final String pathParam_"+pathParamName+" = firstMatch("+pathVar+",\"^"+regEx+"\");");
				writer.println("if(pathParam_"+pathParamName+" == null)");
				writer.println("    "+abortStatement);
				writer.println(pathVar+" = "+pathVar+".substring(pathParam_"+pathParamName+".length());");
			} else {
				int endPos = currentToken.indexOf('{');
				if(endPos == -1)
					endPos = currentToken.length();

				String staticPathElement = currentToken.substring(0,endPos);
				writer.println("if(!"+pathVar+".startsWith(\""+staticPathElement+"\"))");
				writer.println("    "+abortStatement);
				writer.println(pathVar +" = " + pathVar+".substring("+staticPathElement.length()+");");
				currentToken = currentToken.substring(endPos);
			}
		}
	}

	/*
	 * Print value assignation from
	 * QueryParam + DefaultValue or PathParam
	 */
	private void assignFromAnnotations(SourceWriter writer, HasAnnotations f, String var, JType type, TypeOracle oracle) throws NotFoundException {
		PathParam pathParamAnno = f.getAnnotation(PathParam.class);
		String valueExpression = null;
		if(pathParamAnno != null) {
			valueExpression = "pathParam_" + pathParamAnno.value();
		}
		QueryParam queryParamAnno = f.getAnnotation(QueryParam.class);
		if(queryParamAnno != null) {
			valueExpression = "queryMap.getFirst(\""+queryParamAnno.value()+"\")";
			DefaultValue defaultValueAnno = f.getAnnotation(DefaultValue.class);
			if(defaultValueAnno != null) {
				String defaultValue = defaultValueAnno.value();
				valueExpression = "("+valueExpression + " == null ? \"" +defaultValue + "\" : " + valueExpression+")";
			}
		}
		JClassType multiMapType = oracle.findType("com.googlecode.restfulgwt.client.MultivaluedMap");
		if(type.isClassOrInterface().isAssignableTo(multiMapType)) {
			writer.println(var+" = queryMap;");
		}

		if(valueExpression == null)
			return; // no annotation : not a field managed by the framwork
		JClassType hasValueType = oracle.findType("com.google.gwt.user.client.ui.HasValue");
		if(type.isClassOrInterface().isAssignableTo(hasValueType)) {
			JParameterizedType genType = type.isClassOrInterface().asParameterizationOf(hasValueType.isGenericType());
			JType paramType = genType.getTypeArgs()[0];
			writer.println( var +".setValue("+adaptValue(valueExpression,paramType,oracle)+");");
			return;
		}

		writer.println(var+" = "+adaptValue(valueExpression,type,oracle)+";");

	}

	private String adaptValue(String valueExpression, JType type, TypeOracle oracle) throws NotFoundException {
		JType stringType = oracle.getType("java.lang.String");
		if(type == null || type.equals(stringType))
			return valueExpression;
		try {

			if(type.isClass() != null) {
				// check if constructor exist
				type.isClass().getConstructor(new JType[]{stringType});
				valueExpression = valueExpression +" == null ? null : new "+type.getQualifiedSourceName()+"("+valueExpression+")";
				return valueExpression;
			} else {
				throw new RuntimeException("Unsupported type "+type);
			}
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
