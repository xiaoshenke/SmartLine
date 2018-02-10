package wuxian.me.smartline;

import org.junit.Test;
import wuxian.me.smartline.annotation.SmartLineFunc;
import wuxian.me.smartline.util.CommandsManager;

import static org.junit.Assert.*;

/**
 * Created by wuxian on 10/2/2018.
 */
public class CommandsTest {

    @SmartLineFunc(funcName = "hi")
    public void hi() {
        System.out.println("hi");
    }

    @Test
    public void testCmd() {
        CommandsManager.loadCurrentComamands("wuxian.me");
        System.out.println(CommandsManager.getClassSet());

        Commands cmd = new Commands(null);
        cmd.command("hi");
    }

}