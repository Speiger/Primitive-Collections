package speiger.src.testers.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("javadoc")
public class TestUtils
{
	public static void getSurpession(Collection<Method> methods, Class<?> clz, String...args) {
		Set<String> set = new HashSet<>(Arrays.asList(args));
		try {
			for(Method method : clz.getMethods()) {
				if(set.contains(method.getName())) methods.add(method);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getSurpession(Collection<Method> methods, Class<?> clz) {
		try {
			for(Method method : clz.getMethods()) {
				methods.add(method);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Method[] getSurpession(Class<?> clz) {
		try {
			return clz.getMethods();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Method[0];
	}
	
	public static Method[] getSurpession(Class<?> clz, String...args) {
		List<Method> methods = new ArrayList<>();
		Set<String> set = new HashSet<>(Arrays.asList(args));
		try {
			for(Method method : clz.getMethods()) {
				if(set.contains(method.getName())) methods.add(method);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return methods.toArray(new Method[methods.size()]);
	}
}
