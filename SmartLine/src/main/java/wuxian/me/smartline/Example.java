package wuxian.me.smartline;

import wuxian.me.smartline.annotation.SmartLineFunc;

/**
 * Created by wuxian on 10/2/2018.
 */
public class Example {

    @SmartLineFunc(funcName = "example")
    public void example_abc() {
        System.out.println("success call example");
    }

    @SmartLineFunc(funcName = "example_with_1_param")
    public void example_with_1_param(String param1) {
        System.out.println("success call example_with_1_param param1: " + param1);
    }

    @SmartLineFunc(funcName = "example_with_2_param")
    public void example_with_2_param(String param1, Integer param2) {
        System.out.println("success call example_with_1_param param1: " + param1 + " param2: " + param2);
    }

    public static void main(String[] args) throws Exception {
        SmartLine smartLine = new SmartLine();
        smartLine.loadCommandByPackage("wuxian.me");

        try {
            int status = smartLine.begin(args, null);
            if (!Boolean.getBoolean(SmartLineOpts.PROPERTY_NAME_EXIT)) {
                System.exit(status);
            }
        } finally {
            smartLine.close();
        }
    }
}
