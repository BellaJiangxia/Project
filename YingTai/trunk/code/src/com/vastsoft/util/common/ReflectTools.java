package com.vastsoft.util.common;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vastsoft.util.base.BaseEnum;
import com.vastsoft.util.collection.UniqueList;
import com.vastsoft.util.common.inf.IterateReturnable;
import com.vastsoft.util.common.inf.ReflectToMapFilter;
import com.vastsoft.util.exception.BaseException;
import com.vastsoft.util.exception.BreakIterateException;
import com.vastsoft.util.exception.ReflectException;
import com.vastsoft.util.exception.ReturnIterateException;

public class ReflectTools {
    /**
     * 将对象的属性按照指定的属性名的顺序返回属性值的数组
     */
    public static Object[] takeObject(Object obj, String[] field_names) {
        if (ArrayTools.isEmpty(field_names))
            return field_names;
        Object[] result = new Object[field_names.length];
        for (int i = 0; i < field_names.length; i++) {
            String field_name = field_names[i];
            Object ooo = readProperty(obj, field_name);
            result[i] = ooo;
        }
        return result;
    }

    /**
     * 调用公共的clone方法
     */
    public static Object clone(Object obj) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> cla = obj.getClass();
        Method cloneMethod = cla.getMethod("clone");
        return cloneMethod.invoke(obj);
    }

    @SuppressWarnings("unchecked")
    public static <T> T buildOf(Object obj, Class<T> cla) throws ReflectException {
        if (obj == null)
            return null;
        Class<?> parentClass = obj.getClass();
        if (parentClass == cla)
            return (T) obj;
        if (isChildClassOf(cla, parentClass))
            return (T) obj;
        if (!isChildClassOf(parentClass, cla))
            throw new ClassCastException();
        T result;
        try {
            result = cla.newInstance();
            copyObject(obj, result, null, true);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | BaseException e) {
            e.printStackTrace();
            throw new ReflectException(e);
        }
        return result;
    }

    /**
     * 获取调用者的类名和方法名
     */
    public static String takeCallerName() {
        try {
            StackTraceElement[] arrayAtack = new Throwable().getStackTrace();
            if (arrayAtack.length <= 2)
                return "";
            StackTraceElement atack = arrayAtack[2];
            return atack.getClassName() + '.' + atack.getMethodName();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Object newInstanse(String classPath) throws ReflectException {
        Class<?> cla;
        try {
            cla = Class.forName(classPath);
            return cla.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ReflectException(e);
        }
    }

    /**
     * 迭代指定类的所有方法,不包含其父类方法
     *
     * @param clazz      类
     * @param modifierLevel
     * @param onlySelf   是否只迭代成员方法，及不包含静态方法
     * @param iterateReturnable
     * @throws ReturnIterateException
     * @throws ReflectException
     */
    public static void foreachAllMothodByClass(Class<?> clazz, AccessableLevel modifierLevel, boolean onlySelf,
                                               IterateReturnable<Method> iterateReturnable) throws ReturnIterateException, ReflectException {
        Method[] methods = clazz.getDeclaredMethods();
        if (ArrayTools.isEmpty(methods))
            return;
        for (Method method : methods) {
            if (onlySelf && isStatic(method))
                continue;
            if (involveModifierLevel(getAccessableLevel(method.getModifiers()), modifierLevel))
                continue;
            try {
                iterateReturnable.run(method);
            } catch (BreakIterateException e) {
                break;
            } catch (ReturnIterateException e) {
                throw e;
            } catch (BaseException e) {
                e.printStackTrace();
                throw new ReflectException(e);
            }
        }
    }

    /**
     * 获取指定类的所有方法,不包含其父类方法
     *
     * @param clazz      类
     * @param modifierLevel
     * @param onlySelf   是否只返回成员方法，及不包含静态方法
     * @return
     * @throws ReflectException
     */
    public static List<Method> getAllMothodByClass(Class<?> clazz, AccessableLevel modifierLevel, boolean onlySelf)
            throws ReflectException {
        final List<Method> result = new ArrayList<Method>();
        try {
            foreachAllMothodByClass(clazz, modifierLevel, onlySelf, new IterateReturnable<Method>() {
                @Override
                public void run(Method t) {
                    result.add(t);
                }
            });
        } catch (ReturnIterateException e) {
        }
        return result;
    }

    /**
     * 迭代指定类的所有属性,不包含其父类属性
     *
     * @param clazz      类
     * @param modifierLevel
     * @param onlySelf   是否只迭代成员属性，及不包含静态属性
     * @param iterateReturnable
     * @throws ReturnIterateException
     * @throws ReflectException
     */
    public static void foreachAllFieldByClass(Class<?> clazz, AccessableLevel modifierLevel, boolean onlySelf,
                                              IterateReturnable<Field> iterateReturnable) throws ReturnIterateException, ReflectException {
        Field[] fields = clazz.getDeclaredFields();
        if (ArrayTools.isEmpty(fields))
            return;
        for (Field field : fields) {
            if (onlySelf && isStatic(field))
                continue;
            if (involveModifierLevel(getAccessableLevel(field.getModifiers()), modifierLevel))
                continue;
            try {
                iterateReturnable.run(field);
            } catch (BreakIterateException e) {
                break;
            } catch (ReturnIterateException e) {
                throw e;
            } catch (BaseException e) {
                e.printStackTrace();
                throw new ReflectException(e);
            }
        }
    }

    /**
     * 获取指定类的所有属性,不包含其父类属性
     *
     * @param clazz      类
     * @param modifierLevel
     * @param onlySelf   是否只返回成员属性，及不包含静态属性
     * @return
     * @throws ReflectException
     */
    public static List<Field> getAllFieldByClass(Class<?> clazz, AccessableLevel modifierLevel, boolean onlySelf)
            throws ReflectException {
        final List<Field> result = new ArrayList<Field>();
        try {
            foreachAllFieldByClass(clazz, modifierLevel, onlySelf, new IterateReturnable<Field>() {
                @Override
                public void run(Field t) throws ReturnIterateException {
                    result.add(t);
                }
            });
        } catch (ReturnIterateException e) {
        }
        return result;
    }

    /**
     * 判断属性是否是public
     */
    public static boolean isPublic(Field field) {
        return Modifier.isPublic(field.getModifiers());
    }

    /**
     * 判断属性是否是static
     */
    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * 将对象数组转换成Map的列表，注意，当两种过滤器都存在将优先使用名称过滤器，而不在使用类型过滤器
     *
     * @param <T>
     * @param coll            要转换的集合，当为null或者size<=0时返回null
     * @param accessableLevel 要转换的属性的可访问级别，低于此级别的属性或者方法（get,is）将不会转换
     * @param onlyCurrClass   是否只转换当前类，默认为false
     * @param mapNameFilter   属性名称过滤器MAP，如果通过属性名可以在此MAP中找到ReflectToMapFilter对象，将调用此对象的filter方法
     * @param mapTypeFilter   属性类型过滤器
     * @return 返回被转换后的结果
     * @throws ReflectException
     */
    public static <T> List<Map<String, Object>> arrayToMap(T[] coll, AccessableLevel accessableLevel,
                                                           boolean onlyCurrClass, Map<String, ReflectToMapFilter> mapNameFilter,
                                                           Map<Class<?>, ReflectToMapFilter> mapTypeFilter) throws ReflectException {
        return collectionToMap(CollectionTools.arrayToList(coll), accessableLevel, onlyCurrClass, mapNameFilter,
                mapTypeFilter);
    }

    /**
     * 将对象集合转换成Map的列表，注意，当两种过滤器都存在将优先使用名称过滤器，而不在使用类型过滤器
     *
     * @param coll            要转换的集合，当为null或者size<=0时返回null
     * @param accessableLevel 要转换的属性的可访问级别，低于此级别的属性或者方法（get,is）将不会转换
     * @param onlyCurrClass   是否只转换当前类，默认为false
     * @param mapNameFilter   属性名称过滤器MAP，如果通过属性名可以在此MAP中找到ReflectToMapFilter对象，将调用此对象的filter方法
     * @param mapTypeFilter   属性类型过滤器
     * @return 返回被转换后的结果
     * @throws ReflectException
     */
    public static <E> List<Map<String, Object>> collectionToMap(Collection<E> coll, AccessableLevel accessableLevel,
                                                                boolean onlyCurrClass, Map<String, ReflectToMapFilter> mapNameFilter,
                                                                Map<Class<?>, ReflectToMapFilter> mapTypeFilter) throws ReflectException {
        if (CollectionTools.isEmpty(coll))
            return null;
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (E e : coll) {
            Map<String, Object> mapTmp = toMap(e, accessableLevel, onlyCurrClass, mapNameFilter, mapTypeFilter);
            result.add(mapTmp);
        }
        return result;
    }

    /**
     * 将对象转换成MAP ，注意，当两种过滤器都存在将优先使用名称过滤器，而不在使用类型过滤器
     *
     * @param obj
     * @param accessableLevel
     * @param onlyCurrClass
     * @param mapNameFilter 字段name过滤器
     * @return
     * @throws ReflectException
     */
    public static Map<String, Object> toMap(Object obj, AccessableLevel accessableLevel, boolean onlyCurrClass,
                                            Map<String, ReflectToMapFilter> mapNameFilter) throws ReflectException {
        return toMap(obj, obj.getClass(), accessableLevel, onlyCurrClass, mapNameFilter, null);
    }

    /**
     * 将对象转换成MAP ，注意，当两种过滤器都存在将优先使用名称过滤器，而不在使用类型过滤器
     *
     * @param obj
     * @param accessableLevel
     * @param onlyCurrClass
     * @param mapFilter
     * @param mapTypeFilter 字段类型过滤器
     * @return
     * @throws ReflectException
     */
    public static Map<String, Object> toMap(Object obj, AccessableLevel accessableLevel, boolean onlyCurrClass,
                                            Map<String, ReflectToMapFilter> mapFilter, Map<Class<?>, ReflectToMapFilter> mapTypeFilter)
            throws ReflectException {
        return toMap(obj, obj.getClass(), accessableLevel, onlyCurrClass, mapFilter, mapTypeFilter);
    }

    /**
     * 将对象转换成MAP ，注意，当两种过滤器都存在将优先使用名称过滤器，而不在使用类型过滤器
     *
     * @param obj
     * @param clazz
     * @param modifierLevel
     * @param onlyCurrClass
     * @param mapNameFilter 字段name过滤器
     * @param mapTypeFilter 字段类型过滤器
     * @return
     * @throws ReflectException
     */
    public static Map<String, Object> toMap(Object obj, Class<?> clazz, AccessableLevel modifierLevel,
                                            boolean onlyCurrClass, Map<String, ReflectToMapFilter> mapNameFilter,
                                            Map<Class<?>, ReflectToMapFilter> mapTypeFilter) throws ReflectException {
        Map<String, Object> resultTmp = new HashMap<String, Object>();
        List<Field> fields;
        try {
            fields = getAllFieldByClass(clazz, modifierLevel, true);
        } catch (BaseException e) {
            e.printStackTrace();
            throw new ReflectException(e);
        }
        if (!CollectionTools.isEmpty(fields)) {
            for (Field field : fields) {
                if (isStatic(field))
                    continue;
                if (involveModifierLevel(getAccessableLevel(field.getModifiers()), modifierLevel))
                    continue;
                String key = field.getName();
                Object value = resultTmp.get(key);
                if (value != null)
                    continue;
                value = readProperty(obj, key);
                if (CommonTools.isEmpty(mapNameFilter)) {
                    resultTmp.put(key, value);
                    continue;
                }
                ReflectToMapFilter ytpf = mapNameFilter.get(key);
                if (ytpf == null && !CommonTools.isEmpty(mapTypeFilter))
                    ytpf = mapTypeFilter.get(field.getType());
                if (ytpf != null) {
                    resultTmp.put(key, ytpf.filter(value));
                    continue;
                }
                resultTmp.put(key, value);
            }
        }
        List<Method> methods;
        try {
            methods = takeGetMethods(clazz, modifierLevel, true);
        } catch (BaseException e) {
            e.printStackTrace();
            throw new ReflectException(e);
        }
        if (!CollectionTools.isEmpty(methods)) {
            for (Method method : methods) {
                if (isStatic(method))
                    continue;
                if (involveModifierLevel(getAccessableLevel(method.getModifiers()), modifierLevel))
                    continue;
                String methodName = method.getName();
                String key;
                if (StringTools.beginWith(methodName, "get", true)) {
                    key = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                } else if (StringTools.beginWith(methodName, "is", true)) {
                    key = methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
                } else {
                    continue;
                }
                Object value = resultTmp.get(key);
                if (value != null)
                    continue;
                value = readProperty(obj, key);
                if (CommonTools.isEmpty(mapNameFilter)) {
                    resultTmp.put(key, value);
                    continue;
                }
                ReflectToMapFilter ytpf = mapNameFilter.get(key);
                if (ytpf == null && !CommonTools.isEmpty(mapTypeFilter))
                    ytpf = mapTypeFilter.get(method.getReturnType());
                if (ytpf != null) {
                    resultTmp.put(key, ytpf.filter(value));
                    continue;
                }
                resultTmp.put(key, value);
            }
        }
        if (onlyCurrClass)
            return resultTmp;
        Class<?> parentClass = clazz.getSuperclass();
        if (parentClass != null && parentClass != Object.class) {
            Map<String, Object> mapttt = toMap(obj, parentClass, modifierLevel, onlyCurrClass, mapNameFilter,
                    mapTypeFilter);
            mapttt.putAll(resultTmp);
            return mapttt;
        }
        return resultTmp;

    }

    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 将数组转换为list,不是数组返回null
     */
    public static List<?> arrayToList(Object obj) {
        if (!isArray(obj))
            return null;
        List<Object> list = new ArrayList<>();
        int length = Array.getLength(obj);
        if (length <= 0)
            return list;
        for (int i = 0; i < length; i++) {
            Object ob = Array.get(obj, i);
            list.add(ob);
        }
        return list;
    }

    /**
     * 判断对象是否是数组
     */
    public static boolean isArray(Object obj) {
        if (obj == null)
            return false;
        Class<?> clazz = obj.getClass();
        return clazz.isArray();
    }

    // /**
    // * 判断一个数组内是否含有某一个对象<br>
    // * 使用equals进行测试
    // */
    // @Deprecated
    // public static boolean exist(Object array, Object obj) {
    // if (array == null || obj == null)
    // return false;
    // Class<?> clazz = array.getClass();
    // if (!clazz.isArray())
    // return false;
    // int length = Array.getLength(array);
    // if (length <= 0)
    // return false;
    // for (int i = 0; i < length; i++) {
    // Object ob = Array.get(array, i);
    // if (ob == null)
    // continue;
    // if (ob.equals(obj)) {
    // return true;
    // }
    // }
    // return false;
    // }

    /**
     * 读取某对象的属性，包括私有属性,如果不存在返回null<br>
     * 此方法将首先调用get方法获取，如果失败才调用获取属性
     */
    public static Object readProperty(Object obj, String property_name) {
        return readProperty(obj, obj.getClass(), property_name, false, false);
    }

    /**
     * 读取某对象的属性，包括私有属性,如果不存在返回null<br>
     * 此方法将首先调用get方法获取，如果失败才调用获取属性
     */
    public static Object readProperty(Object obj, String property_name, boolean onlyPulic, boolean onlyCurrClass) {
        return readProperty(obj, obj.getClass(), property_name, onlyPulic, onlyCurrClass);
    }

    /**
     * 读取某对象的属性，包括私有属性,如果不存在返回null<br>
     * 此方法将首先调用get方法获取，如果失败才调用获取属性
     */
    public static Object readProperty(Object obj, Class<?> currClass, String property_name, boolean onlyPulic,
                                      boolean onlyCurrClass) {
        Object result = null;
        try {
            try {
                String getMethodName = "get" + property_name.substring(0, 1).toUpperCase() + property_name.substring(1);
                Method method = currClass.getDeclaredMethod(getMethodName);
                if (method != null) {
                    if (!onlyPulic || (onlyPulic && isPublic(method))) {
                        method.setAccessible(true);
                        result = method.invoke(obj);
                        if (result != null)
                            return result;
                    }
                }
            } catch (Exception e) {
            }
            try {
                String getMethodName = "is" + property_name.substring(0, 1).toUpperCase() + property_name.substring(1);
                Method method = currClass.getDeclaredMethod(getMethodName);
                if (method != null) {
                    if (!onlyPulic || (onlyPulic && isPublic(method))) {
                        method.setAccessible(true);
                        result = method.invoke(obj);
                        if (result != null)
                            return result;
                    }
                }
            } catch (Exception e) {
            }
            Field field = currClass.getDeclaredField(property_name);
            if (field != null) {
                if (!onlyPulic || (onlyPulic && isPublic(field))) {
                    field.setAccessible(true);
                    return field.get(obj);
                }
            }
        } catch (Exception e) {
        }
        if (onlyCurrClass)
            return result;
        Class<?> parentClass = currClass.getSuperclass();
        if (parentClass == null)
            return null;
        if (parentClass.equals(Object.class))
            return null;
        return readProperty(obj, parentClass, property_name, onlyPulic, onlyCurrClass);
    }

    /**
     * 写入某对象在指定类中的的属性，包括私有属性,包含其父类的对象，返回是否成功<br>
     * 此方法将首先调用set方法写入，如果失败才调用写入属性
     */
    public static boolean writePropertyByClass(Object obj, Class<?> superClass, String property_name,
                                               Object property_value, boolean onlyPulic, boolean onlyCurrClass) {
        Class<? extends Object> property_value_cla = property_value.getClass();
        try {
            try {
                String getMethodName = "set" + property_name.substring(0, 1).toUpperCase() + property_name.substring(1);
                Method method = superClass.getDeclaredMethod(getMethodName, property_value_cla);
                if (method != null) {
                    if (!onlyPulic || (onlyPulic && isPublic(method))) {
                        method.setAccessible(true);
                        method.invoke(obj, property_value);
                        return true;
                    }
                }
            } catch (Exception e) {
            }
            Field field = superClass.getDeclaredField(property_name);
            if (field != null) {
                if (!onlyPulic || (onlyPulic && isPublic(field))) {
                    field.setAccessible(true);
                    field.set(obj, property_value);
                    return true;
                }
            }
        } catch (Exception e) {
        }
        if (onlyCurrClass)
            return false;
        Class<?> parentClass = superClass.getSuperclass();
        if (parentClass == null)
            return false;
        if (parentClass.equals(Object.class))
            return false;
        return writePropertyByClass(obj, parentClass, property_name, property_value, onlyPulic, onlyCurrClass);
    }

    /**
     * 写入某对象的属性，包括私有属性,包含其父类的对象,返回是否成功<br>
     * 此方法将首先调用set方法写入，如果失败才调用写入属性
     */
    public static boolean writeProperty(Object obj, String property_name, Object property_value) {
        return writePropertyByClass(obj, obj.getClass(), property_name, property_value, false, false);
    }

    /**
     * 写入某对象的属性，包括私有属性,包含其父类的对象,返回是否成功<br>
     * 此方法将首先调用set方法写入，如果失败才调用写入属性
     */
    public static boolean writeProperty(Object obj, String property_name, Object property_value, boolean onlyPulic,
                                        boolean onlyCurrClass) {
        return writePropertyByClass(obj, obj.getClass(), property_name, property_value, onlyPulic, onlyCurrClass);
    }

    /**
     * 通过方法名和参数类型数组查询方法列表中的方法！如果参数列表为null标识不参与筛选
     */
    public static Method findMethodByName(List<Method> listMethod, String name, Class<?>[] paramterTypes) {
        if (CollectionTools.isEmpty(listMethod))
            return null;
        if (StringTools.isEmpty(name))
            return null;
        name = name.trim();
        for (Method method : listMethod) {
            if (name.equals(method.getName()))
                if (paramterTypes == null)
                    return method;
                else if (ArrayTools.equals(paramterTypes, method.getParameterTypes()))
                    return method;
        }
        return null;
    }

    /**
     * 获取一个类的所有的标准“get”方法
     *
     * @throws BaseException
     */
    public static List<Method> takeGetMethods(Class<?> clazz, AccessableLevel modifierLevel, boolean onlySelf)
            throws ReflectException {
        List<Method> result = new UniqueList<Method>();
        List<Method> arrMothod = getAllMothodByClass(clazz, modifierLevel, onlySelf);
        if (CollectionTools.isEmpty(arrMothod))
            return result;
        for (Method method : arrMothod) {
            if (method.getName().startsWith("get") || method.getName().startsWith("is"))
                if (ArrayTools.isEmpty(method.getParameterTypes()))
                    result.add(method);
        }
        return result;
    }

    /**
     * 检查方法是否是public
     */
    public static boolean isPublic(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    /**
     * 获取一个类的所有的标准“set”方法
     *
     * @throws BaseException
     */
    public static List<Method> takeSetMethods(Class<?> clazz, AccessableLevel accessableLevel, boolean onlySelf)
            throws ReflectException {
        List<Method> result = new UniqueList<Method>();
        List<Method> arrMothod = getAllMothodByClass(clazz, accessableLevel, onlySelf);
        if (CollectionTools.isEmpty(arrMothod))
            return result;
        for (Method method : arrMothod) {
            if (method.getName().startsWith("set")) {
                Class<?>[] clas = method.getParameterTypes();
                if (!ArrayTools.isEmpty(clas) && clas.length == 1)
                    result.add(method);
            }
        }
        return result;
    }

    /**
     * 将一个对象拷贝到另外一个对象,通过public的get和set方法完成
     *
     * @param srcObj             源对象
     * @param srcObjCla          指定要复制的属性所在类
     * @param destObj            目标对象
     * @param destObjCla         目标对象属性所在类
     * @param mapPropertyMapping 属性名映射，将首先使用此映射，如果映射中不存在将拷贝同名属性
     * @param copyParentClass    是否拷贝父类中的属性
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws BaseException
     */
    public static void copyObject(Object srcObj, Class<?> srcObjCla, Object destObj, Class<?> destObjCla,
                                  Map<String, String> mapPropertyMapping, boolean copyParentClass) throws ReflectException {
        List<Method> listGetMethod = takeGetMethods(srcObjCla, AccessableLevel.PUBLIC, true);
        List<Method> listSetMethod = takeSetMethods(destObjCla, AccessableLevel.PUBLIC, true);
        if (CollectionTools.isEmpty(listGetMethod))
            return;
        if (CollectionTools.isEmpty(listSetMethod))
            return;
        for (Method method : listGetMethod) {
            if (!isPublic(method))
                continue;
            Object returnValue;
            try {
                returnValue = method.invoke(srcObj);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
                throw new ReflectException(e);
            }
            String propertyName = method.getName().substring(3);
            Method setMethod;
            if (!CommonTools.isEmpty(mapPropertyMapping)) {
                String setMethodName = mapPropertyMapping
                        .get(propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1));
                if (StringTools.isEmpty(setMethodName))
                    setMethod = findMethodByName(listSetMethod, "set" + propertyName, null);
                else
                    setMethod = findMethodByName(listSetMethod,
                            "set" + setMethodName.substring(0, 1).toUpperCase() + setMethodName.substring(1), null);
            } else
                setMethod = findMethodByName(listSetMethod, "set" + propertyName, null);
            if (setMethod == null)
                continue;
            if (!isPublic(setMethod))
                continue;
            try {
                setMethod.invoke(destObj, returnValue);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
                throw new ReflectException(e);
            }
        }
        if (copyParentClass) {
            if (destObjCla.getSuperclass() != Object.class)
                copyObject(srcObj, srcObjCla, destObj, destObjCla.getSuperclass(), mapPropertyMapping, copyParentClass);
            if (srcObjCla.getSuperclass() != Object.class)
                copyObject(srcObj, srcObjCla.getSuperclass(), destObj, destObjCla, mapPropertyMapping, copyParentClass);
        }
    }

    /**
     * 将一个对象拷贝到另外一个对象,通过public的get和set方法完成
     *
     * @param srcObj             源对象
     * @param destObj            目标对象
     * @param mapPropertyMapping 属性名映射，将首先使用此映射，如果映射中不存在将拷贝同名属性
     * @param copyParentClass    是否拷贝父类中的属性
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws BaseException
     */
    public static void copyObject(Object srcObj, Object destObj, Map<String, String> mapPropertyMapping,
                                  boolean copyParentClass) throws ReflectException {
        copyObject(srcObj, srcObj.getClass(), destObj, destObj.getClass(), mapPropertyMapping, copyParentClass);
    }

    /**
     * 调用指定类的指定方法名的指定参数类型的指定参数的静态方法
     *
     * @param clazz          调用的静态方法所在类
     * @param methodName     方法名称
     * @param parameterTypes 参数类型数组
     * @param params         参数数组
     * @return 调用成功后的返回值
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object callStaticMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object[] params)
            throws ReflectException {
        Method me;
        try {
            me = clazz.getMethod(methodName, parameterTypes);
            return me.invoke(null, params);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            throw new ReflectException(e);
        }
    }

    /**
     * 判断指定类是否是另一个指定类的子类
     *
     * @param fatherClass
     * @param childClass
     * @return
     */
    public static boolean isChildClassOf(Class<?> fatherClass, Class<?> childClass) {
        return fatherClass.isAssignableFrom(childClass);
    }

    /**
     * 判断类是否是枚举类
     */
    public static boolean isEnum(Class<?> cla) {
        return cla.isEnum();
    }

    /**
     * 通过Modifier 获取访问修饰符级别
     */
    public static AccessableLevel getAccessableLevel(int modifier) {
        if (Modifier.isPublic(modifier))
            return AccessableLevel.PUBLIC;
        if (Modifier.isProtected(modifier))
            return AccessableLevel.PROTECTED;
        if (Modifier.isPrivate(modifier))
            return AccessableLevel.PRIVATE;
        return AccessableLevel.DEFAULT;
    }

    /**
     * 判断是否是指定修饰符级别<br>
     * 如果srcLevel的访问权限大于或者等于destLevel时返回true，否则false
     *
     * @param srcLevel  原级别
     * @param destLevel 目标级别
     * @return 如果srcLevel的访问权限大于或者等于destLevel时返回true，否则false
     */
    private static boolean involveModifierLevel(AccessableLevel srcLevel, AccessableLevel destLevel) {
        return srcLevel.getCode() >= destLevel.getCode();
    }

    /**
     * 修饰符的可访问级别
     */
    public static enum AccessableLevel implements BaseEnum {
        /**
         * private
         */
        PRIVATE(1, "private"),
        /**
         * default
         */
        DEFAULT(2, "default"),
        /**
         * protected
         */
        PROTECTED(3, "protected"),
        /**
         * Public
         */
        PUBLIC(4, "public");

        private AccessableLevel(int code, String name) {
            this.code = code;
            this.name = name;
        }

        private int code;
        private String name;

        @Override
        public int getCode() {
            return this.code;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
