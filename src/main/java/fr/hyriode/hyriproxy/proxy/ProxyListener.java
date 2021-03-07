package fr.hyriode.hyriproxy.proxy;

import fr.hyriode.hyriproxy.HyriProxy;
import fr.hyriode.hyriproxy.utils.logger.LogType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyListener extends Thread {

    private ProxyManager proxyManager;
    private int port;

    public ProxyListener(ProxyManager proxyManager, int port) {
        this.proxyManager = proxyManager;
        this.port = port;
    }

    public void run() {
        try {
            // Starting listening
            final ServerSocket serverSocket = new ServerSocket(this.port);
            HyriProxy.get().getLogger().log("Starting listening on " + this.port, LogType.INFO);

            while (HyriProxy.get().isRunning()) {
                // Detect if client socket ping server socket
                final Socket clientSocket = serverSocket.accept();

                final ProxyForwardServerClient proxyForwardServerClient = new ProxyForwardServerClient(this.proxyManager, clientSocket);
                proxyForwardServerClient.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
