package fr.hyriode.hyriproxy.proxy;

public class ProxyServer {

    private String name;
    private String hostname;
    private int port;

    public ProxyServer(String name, String hostname, int port) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }
}
