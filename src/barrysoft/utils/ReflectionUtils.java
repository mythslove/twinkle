package barrysoft.utils;

import java.lang.reflect.Constructor;

public class ReflectionUtils {
	
	@SuppressWarnings("unchecked")
	public static <Type> Constructor<Type> getConstructorFor(Class<Type> clazz, Class<?> target) 
		throws ClassNotFoundException
	{
		Constructor<Type>[] cons = (Constructor<Type>[])clazz.getConstructors();
		
		for (Constructor<Type> con : cons) {
			
			Class<?>[] params = con.getParameterTypes();
			
			if (params.length != 1)
				continue;
			
			if (params[0].equals(target))
				return con;
			
		}
		
		throw new ClassNotFoundException("Can't find constructor for class "+
				target.getCanonicalName()+" in class "+clazz.getCanonicalName());
		
	}

}
