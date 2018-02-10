package wuxian.me.smartline.util;

import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.Assert.*;

/**
 * Created by wuxian on 10/2/2018.
 */
public class ReflectTest {

    @Test
    public void testOn() throws Exception {

        URL url = new URL("file:/Users/wuxian/Desktop/SmartLine-Example-1.0.jar");

        URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, null);

        Reflect.on("wuxian.me.smartline.example.Helloworld", classLoader).create().call("helloworld");

    }

}