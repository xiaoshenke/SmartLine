package wuxian.me.smartline;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang3.tuple.Pair;
import wuxian.me.smartline.parser.*;
import wuxian.me.smartline.parser.node.ASTNode;
import wuxian.me.smartline.parser.node.MyTreeAdaptor;
import wuxian.me.smartline.util.CommandsManager;

import java.lang.reflect.Method;
import java.util.*;

import static java.lang.Math.toIntExact;

/**
 * Created by wuxian on 5/1/2018.
 * 封装命令
 */
public class Commands {

    private SmartLine smartLine;

    public Commands(SmartLine smartLine) {
        this.smartLine = smartLine;
    }

    public boolean command(String cmd) {
        return execute(cmd, true, true);
    }

    private boolean execute(String line, boolean call, boolean entireLineAsCommand) {
        if (line == null || line.length() == 0) {
            return false; // ???
        }

        CharStream input = new ANTLRStringStream(line);
        CallLexer lexer = new CallLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CallParser parser = new CallParser(tokens);
        parser.setTreeAdaptor(new MyTreeAdaptor());

        CallParser.call_return r = null;

        try {
            r = parser.call();
        } catch (RecognitionException e) {
            return true;
        }

        ASTNode t = (ASTNode) r.getTree();

        if (t.getToken().getType() != CallParser.ID) {
            return false;
        }

        String cmd = t.getText().toLowerCase();

        Map<String, String> map = getParams(t);

        Set<String> argNameSet = map.keySet();
        String[] argNames = new String[argNameSet.size()];
        argNames = argNameSet.toArray(argNames);

        CommandsManager.ObjAndMethod om = CommandsManager.findCommandBy(cmd, argNames);
        if (om == null) {
            smartLine.info("command: " + cmd + " not supported");
            return false;
        }

        Method method = om.getMethod();
        Class<?>[] types = method.getParameterTypes();
        Object[] objs = new Object[types.length];

        String[] values = new String[types.length];
        values = map.values().toArray(values);

        for (int i = 0; i < values.length; i++) {
            Class clz = types[i];
            if (clz.equals(Integer.class) || clz.equals(int.class)) {
                objs[i] = Integer.parseInt(values[i]);
            } else if (clz.equals(Double.class) || clz.equals(double.class)) {
                objs[i] = Double.parseDouble(values[i]);
            } else {
                objs[i] = values[i];
            }
        }
        try {
            method.invoke(om.getObj(), objs);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    //private static final String CMD_UPLOAD = "upload";
    //private static final String CMD_EXPORT_WORDS = "exportwords";
    //private static final String CMD_CONSILIENCE = "consilience";
    //private static final String CMD_FREEDOM = "freedom";

    private Map<String, String> getParams(ASTNode t) {

        Map<String, String> params = new HashMap<>();
        if (t.getChildCount() == 0) {
            return params;
        }

        for (int i = 0; i < t.getChildCount(); i++) {
            ASTNode child = (ASTNode) t.getChild(i);
            if (child.getToken().getType() != CallParser.ID) {
                continue;
            }
            if (child.getChildCount() == 0) {
                return null;
            }
            params.put(child.getText(), child.getChild(0).getText());
        }
        return params;
    }


    private void info(String content) {
        if (smartLine != null) {
            smartLine.info(content);
        }
    }

}
