package wuxian.me.smartline;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

/**
 * Created by wuxian on 9/2/2018.
 */
public class LoadJarTest {

    @Test
    public void testLoad() throws Exception {

        URL url = new URL("file:/Users/dashu/Desktop/Playgroud/bigdata/SmartLine/Example/target/SmartLine-Example-1.0.jar");

        URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, null);

        ImmutableSet<ClassPath.ClassInfo> set = ClassPath.from(classLoader).getAllClasses();

        Iterator<ClassPath.ClassInfo> iter = set.iterator();
        while (iter.hasNext()) {

            ClassPath.ClassInfo info = iter.next();

            System.out.println("className: " + info.getName());
            Class clz = info.load();
            System.out.println("clz: " + clz.toGenericString());
        }

        set = ClassPath.from(Thread.currentThread().getContextClassLoader()).getAllClasses();
        iter = set.iterator();
        while (iter.hasNext()) {
            ClassPath.ClassInfo info = iter.next();
            //System.out.println("className: " + info.getName());
            Class clz = info.load();
            System.out.println("clz: " + clz.toGenericString());
        }
    }

}
