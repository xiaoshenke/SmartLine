package wuxian.me.smartline;

import org.junit.Test;

public class SmartLineOptsTest {

    @Test
    public void testInit() {

        SmartLine SmartLine = new SmartLine();
        SmartLineOpts opts = new SmartLineOpts(SmartLine, System.getProperties());

        System.out.println(opts.saveDir()); // /Users/dashu/.SmartLine

        //System.out.println(System.getProperties());
    }

}