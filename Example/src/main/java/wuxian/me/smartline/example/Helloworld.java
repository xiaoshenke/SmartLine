package wuxian.me.smartline.example;

import wuxian.me.smartline.annotation.SmartLineFunc;
import wuxian.me.smartline.util.CommandsManager;

/**
 * Created by wuxian on 9/2/2018.
 */
public class Helloworld {


    public Helloworld() {
        System.out.println("hello world in constructor");
    }

    @SmartLineFunc(funcName = "helloworld")
    public void helloworld() {
        System.out.println("hello world in helloworld func");
    }

    @SmartLineFunc(funcName = "helloworld2")
    public void helloworld2() {

    }

    public static void main(String[] args) throws Exception {

        System.out.println(CommandsManager.isValidSmartLineClass(Helloworld.class));

    }


}
