package cetus.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cetus.util.StringUtil;

public class Reflector {

    public static Field getField(Object o, String fieldName) {
        try {
            return o.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException e) {
            return null;
        }
    }

    public static Method getGetter(Object o, String fieldName) {
        try {
            String getterName = getterMethodName(fieldName);
            return o.getClass().getMethod(getterName, new Class<?>[]{ });
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getSetter(Object o, String fieldName, Class<?> type) {
        try {
            String setterName = setterMethodName(fieldName);
            return o.getClass().getMethod(setterName, new Class<?>[]{ type });
        } catch (NoSuchMethodException e) {
            return null;
        }

    }

    public static <T> T getValue(Object o, String fieldName) {
        Method getter = getGetter(o, fieldName);
        return getValue(o, getter);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Object o, Method m) {
        Object res = null;
        try {
            m.setAccessible(true);
            res = m.invoke(o,  new Object[] {});
        } catch (Exception e) {
            return null;
        }
        
        return (T)res;
    }

    public static Object getValue(Object o, Field f) {
        Object res = null;
        try {
            f.setAccessible(true);
            res = f.get(o);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return null;
        }
        return res;
    }

    public static Annotation[] getAnnotations(Field f) {
        return getAnnotations(f, null);
    }

    public static Annotation[] getAnnotations(Field f, Class<? extends Annotation> clazz) {
        if(clazz == null) {
            return f.getDeclaredAnnotations();
        }
        return f.getDeclaredAnnotationsByType(clazz);
    }

    public static <T> T getTypedBean(Object[] args, Class<T> clazz) {

        for(Object obj : args) {
            if(clazz.isInstance(obj)) {
                return clazz.cast(obj);
            }
        }
        return null;
    }

    public static <T extends Annotation> T getTypedAnnotation(Field f, Class<T> clazz) {
        T annotation = f.getDeclaredAnnotation(clazz);
        if(annotation != null) {
            return clazz.cast(annotation);
        }
        return null;
    }

    public static <T extends Annotation> T getTypedAnnotation(Method m, Class<T> clazz) {
        T annotation = m.getDeclaredAnnotation(clazz);
        if(annotation != null) {
            return clazz.cast(annotation);
        }
        return null;
    }

    public static Map<String, Object> getFieldMap(Object o) {
        Map<String, Object> map = new HashMap<>();
        return getFieldMap(o, o.getClass(), map);
    }

    private static Map<String, Object> getFieldMap(Object o, Class<?> clazz, Map<String, Object> map) {
        Field[] fields = o.getClass().getFields();
        for(Field f : fields) {
            map.put(f.getName(), getValue(o, f));
        }
        Class<?> s = clazz.getSuperclass();
        if(s == null || s.equals(Object.class)) {
            return map;
        }

        return getFieldMap(o, s, map);
    }

    public static List<Field> getFieldList(Class<?> clazz) {
        return getFieldList(clazz, new ArrayList<>());
    }

    public static List<Field> getFieldList(Class<?> clazz, List<Field> list) {

        Field[] fields = clazz.getDeclaredFields();
        for(Field f : fields) {
            list.add(f);
        }
        Class<?> s = clazz.getSuperclass();
        if(s == null || s.equals(Object.class)) {
            return list;
        }

        return getFieldList(s, list);
    }

    public static Map<String, Object> getGetterMap(Object o) {
        Map<String, Object> map = new HashMap<>();
        return getGetterMap(o, o.getClass(), map);
    }

    private static Map<String, Object> getGetterMap(Object o, Class<?> clazz, Map<String, Object> map) {
        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods) {
            if(m.getName().startsWith("get") && !m.getName().equals("getClass")) {
                String key = fieldName(m.getName());
                map.put(key, getValue(o, m));
            }
        }
        Class<?> s = clazz.getSuperclass();
        if(s == null || s.equals(Object.class)) {
            return map;
        }

        return getGetterMap(o, s, map);
    }

    public static List<Method> getGetterList(Class<?> clazz) {
        return getGetterList(clazz, new ArrayList<>());
    }

    public static List<Method> getGetterList(Class<?> clazz, List<Method> list) {

        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods) {
            if(m.getName().startsWith("get") && !m.getName().equals("getClass")) {
                list.add(m);
            }
        }
        Class<?> s = clazz.getSuperclass();
        if(s == null || s.equals(Object.class)) {
            return list;
        }

        return getGetterList(s, list);
    }

    // public static List<String> getGetterList(Class<?> clazz, List<String> list) {

    //     if(list == null) {
    //         list = new ArrayList<>();            
    //     }
    //     Method[] methods = clazz.getDeclaredMethods();
    //     for(Method m : methods) {
    //         if(m.getName().startsWith("get") && !m.getName().equals("getClass")) {
    //             list.add(m);
    //         }
    //     }
        
    //     Class<?> s = clazz.getSuperclass();
    //     if(s == null || s.equals(Object.class)) {
    //         return list;
    //     }

    //     return getFieldList(s, list);
    // }

    private static String fieldName(String getterMethodName) {
        return StringUtil.uncapitalize(getterMethodName.replace("get", ""));
    }

    private static String getterMethodName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String setterMethodName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static void setValue(Object o, String fieldName, Object value) {
        Method setter = getSetter(o, fieldName, value.getClass());
        setValue(o, setter, value);
    }

    public static void setValue(Object o, Method m, Object value) {
        try {
            m.setAccessible(true);
            m.invoke(o,  new Object[] { value });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <A extends Annotation> List<Field> getAnnotationFields(Object o, Class<A> annotationType) {
        List<Field> fieldList = getFieldList(o.getClass());
        List<Field> annotationFields = fieldList.stream().filter(f -> 
            f.getAnnotation(annotationType) != null).collect(Collectors.toList()
        );

        return annotationFields;
    }

}
