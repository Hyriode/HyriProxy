package fr.hyriode.hyriproxy.proxy;

import fr.hyriode.hyriproxy.HyriProxy;
import fr.hyriode.hyriproxy.utils.logger.LogType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class ProxyBalancer extends Thread {

    private static ProxyManager proxyManager;

    public ProxyBalancer(ProxyManager proxyManager) {
        ProxyBalancer.proxyManager = proxyManager;

        HyriProxy.get().getLogger().log("Starting ProxyBalancer...", LogType.INFO);
    }

    public void run() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // Set a new proxy server
                ProxyBalancer.setCurrentProxyServer();
            }
        }, 0, 2000);
    }

    private static void setCurrentProxyServer() {
        // Map of players by proxy
        final Map<Integer, ProxyServer> playersInProxies = new HashMap<>();

        // Put in the map valid proxies server with their players
        ProxyBalancer.proxyManager.getProxyServers().forEach(proxyServer -> {
            final int playersInProxy = ProxyBalancer.getOnlinePlayersOnProxyServer(proxyServer);
            if (playersInProxy != -1) {
                playersInProxies.put(playersInProxy, proxyServer);
            }
        });

        // Check if the map is empty : there is no proxy server to set
        if (!playersInProxies.isEmpty()) {
            final ProxyServer proxyServer = Collections.min(playersInProxies.entrySet(), Map.Entry.comparingByKey()).getValue();
            if (ProxyBalancer.proxyManager.getCurrentProxyServer() != proxyServer)
                ProxyBalancer.proxyManager.setCurrentProxyServer(proxyServer);
            playersInProxies.clear();
        }
    }

    private static int getOnlinePlayersOnProxyServer(ProxyServer proxyServer) {
        try {
            final Socket socket = new Socket(proxyServer.getHostname(), proxyServer.getPort());
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            final DataInputStream in = new DataInputStream(socket.getInputStream());

            // Send request to get motd, onlinePlayers and maxPlayers
            out.write(0xFE);

            // Append a StringBuilder with response
            int b;
            final StringBuilder str = new StringBuilder();
            while ((b = in.read()) != -1) {
                if (b > 16 && b != 255 && b != 23 && b != 24) {
                    str.append((char) b);
                }
            }

            socket.close();

            // Return onlinePlayers
            return Integer.parseInt(str.toString().split("§")[1]);

        } catch (IOException e) {
            HyriProxy.get().getLogger().log(proxyServer.getName() + " -> " + proxyServer.getHostname() + ":" + proxyServer.getPort() + " is not responding.", LogType.WARN);
        }
        return -1;
    }
}
