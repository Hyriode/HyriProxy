package fr.hyriode.hyriproxy.proxy;

import fr.hyriode.hyriproxy.HyriProxy;
import fr.hyriode.hyriproxy.utils.logger.LogType;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class ProxyCheckAlive extends Thread {

    private static ProxyListener proxyListener;

    public ProxyCheckAlive(ProxyListener proxyListener) {
        ProxyCheckAlive.proxyListener = proxyListener;

        HyriProxy.get().getLogger().log("Starting ProxyCheckAlive...", LogType.INFO);
    }

    public void run() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // Check if proxy is alive
                if (!ProxyCheckAlive.proxyListener.isAlive()) {
                    // Proxy is no longer alive, so we stop the proxy
                    HyriProxy.get().getLogger().log("HyriProxy is no longer responding !", LogType.ERROR);
                    HyriProxy.get().stop();
                }
            }
        }, 0, 2000);
    }
}
