package fr.hyriode.hyriproxy;

import fr.hyriode.hyriproxy.proxy.ProxyManager;
import fr.hyriode.hyriproxy.proxy.ProxyServer;
import fr.hyriode.hyriproxy.utils.logger.LogType;
import fr.hyriode.hyriproxy.utils.logger.Logger;

import java.io.File;

public class HyriProxy {

    private static HyriProxy instance;

    private Logger logger;
    private boolean running;

    public void start() {
        instance = this;
        this.logger = new Logger("[HyriProxy]", new File("logs/latest.log"));
        this.running = true;

        // Start the proxy
        this.startProxy();
    }

    public void startProxy() {
        this.logger.log("Starting HyriProxy...", LogType.INFO);

        // Set the ProxyManager with proxies server
        final ProxyManager proxyManager = new ProxyManager(25565);
        final ProxyServer bungee01 = new ProxyServer("bungee-01", "localhost", 25570);
        final ProxyServer bungee02 = new ProxyServer("bungee-02", "localhost", 25590);
        proxyManager.addProxyServer(bungee01);
        proxyManager.addProxyServer(bungee02);
    }

    public void stop() {
        this.logger.log("Stopping HyriProxy...", LogType.INFO);
        this.running = false;
        System.exit(0);
    }

    public static HyriProxy get() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isRunning() {
        return running;
    }
}
