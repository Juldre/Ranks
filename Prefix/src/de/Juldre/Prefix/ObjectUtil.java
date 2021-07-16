package de.Juldre.Prefix;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectUtil {

	public static Object deserialize(Map<String, Object> args, Class<?> myClass) {
		try {
			Object newClass = myClass.getConstructor().newInstance();
			for (Field f : myClass.getFields()) {
				myClass.getField(f.getName()).set(newClass,
						(args.containsKey(f.getName()) ? args.get(f.getName()) : null));
			}
			return newClass;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static LinkedHashMap<String, Object> serialize(Object myClass) {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		Field[] fields = myClass.getClass().getFields();
		for (Field f : fields) {
			try {
				Object value = f.get(myClass);
				if (value != null) {
					result.put(f.getName(), value);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
