package payloads;

public class Listener {
    private String ip;
    private int port;

    public Listener(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}
