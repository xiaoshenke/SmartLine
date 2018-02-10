package wuxian.me.smartline.example;

import org.junit.Test;
import wuxian.me.smartline.util.CommandsManager;

import static org.junit.Assert.*;

/**
 * Created by wuxian on 10/2/2018.
 */
public class HelloworldTest {

    @Test
    public void testObjAndMethod() {
        CommandsManager.ObjAndMethod om = CommandsManager.ObjAndMethod.create(Helloworld.class, null);
        System.out.println(om.getObj());
    }

}