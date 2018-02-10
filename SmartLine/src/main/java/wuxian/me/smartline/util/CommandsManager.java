package wuxian.me.smartline.util;

import com.google.common.reflect.ClassPath;
import wuxian.me.smartline.annotation.SmartLineFunc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Created by wuxian on 10/2/2018.
 */
public class CommandsManager {

    static Set<ClassPath.ClassInfo> classInfoSet = null;

    static Set<Class> classSet = new HashSet<>();

    private CommandsManager() {
    }

    public static void loadCurrentComamands(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            classInfoSet = ClassPath.from(classLoader).getAllClasses();
            Iterator<ClassPath.ClassInfo> iter = classInfoSet.iterator();

            while (iter.hasNext()) {
                ClassPath.ClassInfo info = iter.next();

                if (packageName != null && packageName.length() != 0) {
                    if (!info.getPackageName().contains(packageName)) {
                        continue;
                    }
                }
                try {
                    Class clz = info.load(); //may throw exception
                    if (isValidSmartLineClass(clz)) {
                        classSet.add(clz);
                    }
                } catch (NoClassDefFoundError e) {

                } catch (Exception e) {

                }
            }

        } catch (IOException e) {

        }
    }


    public static void loadCurrentComamands() {
        loadCurrentComamands("");
    }

    //has some bugs,do not use it
    @Deprecated
    public static void loadCommands(String[] urls) {
        List<URL> urlList = new ArrayList<>();
        for (String url : urls) {
            try {
                URL u = new URL("file:" + url);
                urlList.add(u);
            } catch (Exception e) {
            }
        }

        URL[] urlFinal = new URL[urlList.size()];
        URLClassLoader classLoader = new URLClassLoader(urlList.toArray(urlFinal), null);
        try {
            classInfoSet = ClassPath.from(classLoader).getAllClasses();
            Iterator<ClassPath.ClassInfo> iter = classInfoSet.iterator();

            while (iter.hasNext()) {
                ClassPath.ClassInfo info = iter.next();
                try {
                    Class clz = info.load(); //may throw exception
                    if (isValidSmartLineClass(clz)) {  //FIXME: not working here.....
                        classSet.add(clz);
                    }
                } catch (Exception e) {

                }

            }

        } catch (IOException e) {

        }
    }


    public static boolean isValidSmartLineClass(Class clz) {
        if (clz == null) {
            return false;
        }

        boolean find = false;

        for (Method m : clz.getDeclaredMethods()) {
            if (m.getDeclaredAnnotation(SmartLineFunc.class) != null) {
                find = true;
                break;
            }
        }

        if (!find) {
            return false;
        }


        try {
            Reflect.on(clz).create();
        } catch (ReflectException e) {
            return false;
        }
        return true;
    }

    private static Map<Info, ObjAndMethod> cachedMethodMap = new HashMap<>();


    public static ObjAndMethod findCommandBy(String funcName) {
        return findCommandBy(funcName, new String[]{});
    }

    public static ObjAndMethod findCommandBy(String funcName, String[] argNames) {

        if (funcName == null || funcName.length() == 0) {
            return null;
        }
        Info info = new Info(funcName, argNames);
        if (cachedMethodMap.containsKey(info)) {
            return cachedMethodMap.get(info);
        }

        Iterator<Class> iter = classSet.iterator();
        while (iter.hasNext()) {

            Class clz = iter.next();
            for (Method method : clz.getMethods()) {
                if (method.getAnnotation(SmartLineFunc.class) != null) {

                    if (method.getName().equals(funcName) && argNames.length == method.getParameterCount()) {

                        Parameter[] parameters = method.getParameters();
                        List<String> cmpNames = new ArrayList<>(parameters.length);
                        for (Parameter p : parameters) {
                            cmpNames.add(p.getName());
                        }

                        for (String n : argNames) {
                            if (!cmpNames.contains(n)) {
                                break;
                            }
                        }

                        ObjAndMethod om = ObjAndMethod.create(clz, method);
                        cachedMethodMap.put(info, om);
                        return om;
                    }
                }
            }
        }
        return null;
    }

    public static Set<Class> getClassSet() {
        return classSet;
    }


    private static class Info {
        public String funcName;
        public String[] argNames;

        public Info(String funcName, String[] argNames) {
            this.funcName = funcName;
            this.argNames = argNames;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Info)) return false;

            Info info = (Info) o;

            if (!funcName.equals(info.funcName)) return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(argNames, info.argNames);

        }

        @Override
        public int hashCode() {
            int result = funcName.hashCode();
            result = 31 * result + Arrays.hashCode(argNames);
            return result;
        }
    }

    public static class ObjAndMethod {
        private Class clz;
        private Method method;
        private Object obj;

        public Method getMethod() {
            return method;
        }

        public Object getObj() {
            return obj;
        }

        public static ObjAndMethod create(Class clz, Method method) {
            ObjAndMethod objAndMethod = new ObjAndMethod();
            objAndMethod.clz = clz;
            objAndMethod.method = method;
            try {
                objAndMethod.obj = clz.getConstructor().newInstance(null);
            } catch (Exception e) {
                //System.out.println(e.toString());
            }
            return objAndMethod;
        }

        @Override
        public String toString() {
            return "ObjAndMethod{" +
                    "clz=" + clz +
                    ", method=" + method +
                    '}';
        }
    }

}
