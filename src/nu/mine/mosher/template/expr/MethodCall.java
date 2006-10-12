/*
 * Created on Oct 26, 2005
 */
package nu.mine.mosher.template.expr;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nu.mine.mosher.template.exception.TemplateParsingException;



class MethodCall extends Selector
{
	private final String sNameMethod;
	private final ArrayList<Object> rArg;

	MethodCall(final String sNameMethod, final ArrayList<Object> rArg)
	{
		this.sNameMethod = sNameMethod;
		this.rArg = rArg;
	}

	private String getMethodName()
	{
		return this.sNameMethod;
	}

	private ArrayList<Object> getArgs()
	{
		return this.rArg;
	}

	@Override
	Object apply(final Object var) throws TemplateParsingException
	{
		try
		{
			return tryApply(var);
		}
		catch (final Throwable e)
		{
			throw new TemplateParsingException("error while trying to invoke method: "+this.sNameMethod,e);
		}
	}

	private Object tryApply(final Object var) throws TemplateParsingException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		final Class<?> clas = var instanceof Class ? (Class)var : var.getClass();

		// Array.length is not reflected, so we check for it ourselves here
		if (clas.isArray() && this.sNameMethod.equals("length"))
		{
			if (this.rArg.size() > 0)
			{
				throw new TemplateParsingException("cannot find method with matching parameter types");
			}
			return Array.getLength(var);
		}

		final Map<String,Set<Method>> mapMethods = new HashMap<String,Set<Method>>();
		getMethods(clas,mapMethods);
		final Method methodToInvoke = findMethod(mapMethods);

		return methodToInvoke.invoke(var,this.rArg.toArray());
	}

	private Method findMethod(final Map<String,Set<Method>> mapMethods) throws TemplateParsingException
	{
		if (!mapMethods.containsKey(this.sNameMethod))
		{
			throw new TemplateParsingException("cannot find method with that name");
		}

		for (final Method method : mapMethods.get(this.sNameMethod))
		{
			if (paramsMatch(method.getParameterTypes(),this.rArg))
			{
				return method;
			}
		}

		throw new TemplateParsingException("cannot find method with matching parameter types");
	}

	private static void getMethods(final Class<?> clas, Map<String,Set<Method>> mapMethod)
	{
		final Method[] rMethod = clas.getMethods();
		for (final Method method: Arrays.asList(rMethod))
		{
			final String nameMethod = method.getName();
			if (!mapMethod.containsKey(nameMethod))
			{
				mapMethod.put(nameMethod,new HashSet<Method>());
			}
			mapMethod.get(nameMethod).add(method);
		}
	}

	private static boolean paramsMatch(final Class<?>[] paramType, final ArrayList<Object> rArg)
	{
		if (paramType.length != rArg.size())
		{
			return false;
		}

		int iParam = 0;
		for (final Object arg: rArg)
		{
			if (arg == null)
			{
				// null is OK for any parameter type except primitives
				if (paramType[iParam].isPrimitive())
				{
					return false;
				}
			}
			else if (!argumentMatchesParameter(arg.getClass(),paramType[iParam]))
			{
				return false;
			}
			++iParam;
		}
		return true;
	}

	private static Map<Class,Class> mapPrimitiveToWrapper = new HashMap<Class,Class>();
	static
	{
		MethodCall.mapPrimitiveToWrapper.put(Integer.TYPE,Integer.class);
		MethodCall.mapPrimitiveToWrapper.put(Long.TYPE,Long.class);
		MethodCall.mapPrimitiveToWrapper.put(Short.TYPE,Short.class);
		MethodCall.mapPrimitiveToWrapper.put(Character.TYPE,Character.class);
		MethodCall.mapPrimitiveToWrapper.put(Boolean.TYPE,Boolean.class);
		MethodCall.mapPrimitiveToWrapper.put(Float.TYPE,Float.class);
		MethodCall.mapPrimitiveToWrapper.put(Double.TYPE,Double.class);
		MethodCall.mapPrimitiveToWrapper.put(Byte.TYPE,Byte.class);
	}

	private static boolean argumentMatchesParameter(final Class<?> argument, final Class<?> parameter)
	{
		if (parameter.isAssignableFrom(argument))
		{
			return true;
		}

		assert !argument.isPrimitive();
		if (parameter.isPrimitive())
		{
			final Class<?> wrapper = MethodCall.mapPrimitiveToWrapper.get(parameter);
			if (wrapper.isAssignableFrom(argument))
			{
				return true;
			}
		}

		return false;
	}
}
