package fr.hyriode.hyriproxy.proxy;

import fr.hyriode.hyriproxy.HyriProxy;
import fr.hyriode.hyriproxy.utils.logger.LogType;

import java.util.ArrayList;
import java.util.List;

public class ProxyManager {

    private ProxyServer currentProxyServer;

    private final List<ProxyServer> proxyServers;

    private final int port;

    public ProxyManager(int port) {
        this.proxyServers = new ArrayList<>();
        this.port = port;

        HyriProxy.get().getLogger().log("Starting ProxyManager...", LogType.INFO);

        // Start listening
        final ProxyListener proxyListener = new ProxyListener(this, this.port);
        proxyListener.start();

        // Init the ProxyCheckAlive and the ProxyBalancer
        this.initProxyCheckAlive(proxyListener);
        this.initProxyBalancer();
    }

    private void initProxyCheckAlive(ProxyListener proxyListener) {
        final ProxyCheckAlive proxyCheckAlive = new ProxyCheckAlive(proxyListener);
        proxyCheckAlive.setDaemon(true);
        proxyCheckAlive.start();
    }

    private void initProxyBalancer() {
        final ProxyBalancer proxyBalancer = new ProxyBalancer(this);
        proxyBalancer.setDaemon(true);
        proxyBalancer.start();
    }

    public void addProxyServer(ProxyServer proxyServer) {
        this.proxyServers.add(proxyServer);
    }

    public List<ProxyServer> getProxyServers() {
        return proxyServers;
    }

    public void setCurrentProxyServer(ProxyServer proxyServer) {
        this.currentProxyServer = proxyServer;
        HyriProxy.get().getLogger().log("Set current ProxyServer to " + proxyServer.getName() + " -> " + proxyServer.getHostname() + ":" + proxyServer.getPort(), LogType.INFO);
    }

    public ProxyServer getCurrentProxyServer() {
        return currentProxyServer;
    }

    public int getPort() {
        return port;
    }
}
