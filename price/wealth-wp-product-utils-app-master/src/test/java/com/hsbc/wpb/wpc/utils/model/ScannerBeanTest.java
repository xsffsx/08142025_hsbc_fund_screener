package com.dummy.wpb.wpc.utils.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

/**
 * scanner bean to coverage test
 */
public class ScannerBeanTest {
    //define package path
    private static final String PACKAGE_NAME = "com.dummy.wpb.wpc.utils.model";
    //define need to be filtered special class methods
    private static final List<String> filterClazzMethodList = new ArrayList<String>();
    //define need to be filtered special class attributes
    private static final List<String> filterClazzFieldList = new ArrayList<String>();
    //define need to be filtered special class
    private static final List<String> filterClazzList = new ArrayList<String>();
    static {
        filterClazzFieldList.add("");

        filterClazzMethodList.add("");

        filterClazzList.add("");
    }

    @Test
    public void test() {
        String[] packageNames = PACKAGE_NAME.split(",");
        for(String packageName : packageNames) {
            List<Class<?>> allClass = getClasses(packageName);
            if (null != allClass) {
                for (Class classes : allClass) {//loop reflection executes all classes
                    try {
                        boolean isAbstract = Modifier.isAbstract(classes.getModifiers());
                        if (classes.isInterface() || isAbstract) {//skip interfaces or abstract classes
                            continue;
                        }

                        if(classes.getName().equalsIgnoreCase(this.getClass().getName())) {
                            continue;
                        }
                        Constructor[] constructorArr = classes.getConstructors();
                        Object clazzObj = newConstructor(constructorArr, classes);

                        fieldTest(classes, clazzObj);

                        methodInvoke(classes, clazzObj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void fieldTest(Class<?> classes, Object clazzObj)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (null == clazzObj) {
            return;
        }

        String clazzName = classes.getName();
        Field[] fields = classes.getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                String fieldName = field.getName();
                if (filterClazzFieldList.contains(clazzName + "." + fieldName)) {
                    continue;
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Object fieldGetObj = field.get(clazzObj);
                if (!Modifier.isFinal(field.getModifiers()) || null == fieldGetObj) {
                    field.set(clazzObj, adaptorGenObj(field.getType()));
                }
            }
        }
    }

    private void methodInvoke(Class<?> classes, Object clazzObj)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        String clazzName = classes.getName();
        Method[] methods = classes.getDeclaredMethods();
        if (null != methods && methods.length > 0) {
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.equalsIgnoreCase("hashCode")
                        || methodName.equalsIgnoreCase("equals")) {
                    continue;
                }
                String clazzMethodName = clazzName + "." + methodName;
                //exclude unable to execute methods
                if (filterClazzMethodList.contains(clazzMethodName)) {
                    continue;
                }
                //open method permissions
                method.setAccessible(true);
                Class<?>[] paramClassArrs = method.getParameterTypes();

                //execute get/set method
                if (methodName.startsWith("set") && null != clazzObj) {
                    methodInvokeGetSet(classes, clazzObj, method, paramClassArrs, clazzMethodName, methodName);
                    continue;
                }
                //handle static method
                if (Modifier.isStatic(method.getModifiers()) && !classes.isEnum()) {
                    if (null == paramClassArrs || paramClassArrs.length == 0) {
                        method.invoke(null, null);
                    } else if (paramClassArrs.length == 1) {
                        System.out.println("clazzMethodName:" + clazzMethodName + "," + classes.isEnum());
                        method.invoke(null, adaptorGenObj(paramClassArrs[0]));
                    } else if (paramClassArrs.length == 2) {
                        method.invoke(null, adaptorGenObj(paramClassArrs[0]), adaptorGenObj(paramClassArrs[1]));
                    } else if (paramClassArrs.length == 3) {
                        method.invoke(null, adaptorGenObj(paramClassArrs[0]), adaptorGenObj(paramClassArrs[1]),
                                adaptorGenObj(paramClassArrs[2]));
                    } else if (paramClassArrs.length == 4) {
                        method.invoke(null, adaptorGenObj(paramClassArrs[0]), adaptorGenObj(paramClassArrs[1]),
                                adaptorGenObj(paramClassArrs[2]), adaptorGenObj(paramClassArrs[3]));
                    }
                    continue;
                }
                if (null == clazzObj) {
                    continue;
                }
                //execute toString method
                if ("toString".equals(methodName)) {
                    try {
                        Method toStringMethod = classes.getDeclaredMethod(methodName, null);
                        toStringMethod.invoke(clazzObj, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                //other method
                if (null == paramClassArrs || paramClassArrs.length == 0) {
                    method.invoke(clazzObj, null);
                } else if (paramClassArrs.length == 1) {
                    method.invoke(clazzObj, adaptorGenObj(paramClassArrs[0]));
                } else if (paramClassArrs.length == 2) {
                    method.invoke(clazzObj, adaptorGenObj(paramClassArrs[0]), adaptorGenObj(paramClassArrs[1]));
                } else if (paramClassArrs.length == 3) {
                    method.invoke(clazzObj, adaptorGenObj(paramClassArrs[0]), adaptorGenObj(paramClassArrs[1]),
                            adaptorGenObj(paramClassArrs[2]));
                } else if (paramClassArrs.length == 4) {
                    method.invoke(clazzObj, adaptorGenObj(paramClassArrs[0]), adaptorGenObj(paramClassArrs[1]),
                            adaptorGenObj(paramClassArrs[2]), adaptorGenObj(paramClassArrs[3]));
                }
            }
        }
    }

    private void methodInvokeGetSet(Class<?> classes, Object clazzObj, Method method, Class<?>[] paramClassArrs,
                                    String clazzMethodName, String methodName)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object getObj = null;
        String methodNameSuffix = methodName.substring(3, methodName.length());
        Method getMethod = null;
        try {
            getMethod = classes.getDeclaredMethod("get" + methodNameSuffix, null);
        } catch (NoSuchMethodException e) {
            //handle get method not found case
            if (null == getMethod) {
                Character firstChar = methodNameSuffix.charAt(0);
                String firstLowerStr = firstChar.toString().toLowerCase();
                try {
                    getMethod = classes.getDeclaredMethod(
                            firstLowerStr + methodNameSuffix.substring(1, methodNameSuffix.length()), null);
                } catch (NoSuchMethodException e2) {
                    //direct return if finally can't find get method
                    if (null == getMethod) {
                        return;
                    }
                }
            }
        }
        //only can execute when get method return result equals set method parameter
        Class<?> returnClass = getMethod.getReturnType();
        if (paramClassArrs.length == 1 && paramClassArrs[0].toString().equals(returnClass.toString())) {
            getObj = getMethod.invoke(clazzObj, null);
            method.invoke(clazzObj, getObj);
        }

    }

    @SuppressWarnings("rawtypes")
    private Object newConstructor(Constructor[] constructorArr, Class<?> classes)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (null == constructorArr || constructorArr.length < 1) {
            return null;
        }
        Object clazzObj = null;
        boolean isExitNoParamConstruct = false;
        for (Constructor constructor : constructorArr) {
            Class[] constructParamClazzArr = constructor.getParameterTypes();
            if (null == constructParamClazzArr || constructParamClazzArr.length == 0) {
                constructor.setAccessible(true);
                clazzObj = classes.newInstance();
                isExitNoParamConstruct = true;
                break;
            }
        }
        if (!isExitNoParamConstruct) {
            boolean isContinueFor = false;
            Class[] constructParamClazzArr = constructorArr[0].getParameterTypes();
            Object[] construParamObjArr = new Object[constructParamClazzArr.length];
            for (int i = 0; i < constructParamClazzArr.length; i++) {
                Class constructParamClazz = constructParamClazzArr[i];
                construParamObjArr[i] = adaptorGenObj(constructParamClazz);
                if (null == construParamObjArr[i]) {
                    isContinueFor = true;
                }
            }
            if (!isContinueFor) {
                clazzObj = constructorArr[0].newInstance(construParamObjArr);
            }
        }
        return clazzObj;
    }

    private Object adaptorGenObj(Class<?> clazz)
            throws IllegalArgumentException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (null == clazz) {
            return null;
        }
        if ("int".equals(clazz.getName())) {
            return 1;
        } else if ("char".equals(clazz.getName())) {
            return 'x';
        } else if ("boolean".equals(clazz.getName())) {
            return true;
        } else if ("double".equals(clazz.getName())) {
            return 1.0;
        } else if ("float".equals(clazz.getName())) {
            return 1.0f;
        } else if ("long".equals(clazz.getName())) {
            return 1l;
        } else if ("byte".equals(clazz.getName())) {
            return 0xFFFFFFFF;
        } else if ("java.lang.Class".equals(clazz.getName())) {
            return this.getClass();
        } else if ("java.math.BigDecimal".equals(clazz.getName())) {
            return new BigDecimal(1);
        } else if ("java.lang.String".equals(clazz.getName())) {
            return "333";
        } else if ("java.util.Hashtable".equals(clazz.getName())) {
            return new Hashtable();
        } else if ("java.util.Hashtable".equals(clazz.getName())) {
            return new Hashtable();
        } else if ("java.util.List".equals(clazz.getName())) {
            return new ArrayList();
        } else if ("java.util.Date".equals(clazz.getName())){
            return new Date();
        } else {
            //skip interfaces or abstract classes
            boolean paramIsAbstract = Modifier.isAbstract(clazz.getModifiers());
            boolean paramIsInterface = Modifier.isInterface(clazz.getModifiers());
            if (paramIsInterface || paramIsAbstract) {
                return null;
            }
            Constructor<?>[] constructorArrs = clazz.getConstructors();
            return newConstructor(constructorArrs, clazz);
        }
    }

    private List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        //if loop iteration
        boolean recursive = true;
        //get package name and replace '.' to '/'
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    private void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
                                                  List<Class<?>> classes) {
        //get package directory and new a file object
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //loop all file
        for (File file : dirfiles) {
            //continue scanning recursively if directory
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                //remove '.class' if java class
                String className = file.getName().substring(0, file.getName().length() - 6);
                String pakClazzName = packageName + '.' + className;
                if (filterClazzList.contains(pakClazzName)) {
                    continue;
                }
                try {
                    classes.add(Class.forName(pakClazzName));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}