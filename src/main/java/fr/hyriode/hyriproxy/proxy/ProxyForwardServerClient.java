package fr.hyriode.hyriproxy.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ProxyForwardServerClient extends Thread {

    private Socket serverSocket;
    private boolean forwardingActive = false;

    private final ProxyManager proxyManager;
    private final Socket clientSocket;

    public ProxyForwardServerClient(ProxyManager proxyManager, Socket clientSocket) {
        this.proxyManager = proxyManager;
        this.clientSocket = clientSocket;
    }

    public void run() {

        InputStream clientIn;
        OutputStream clientOut;
        InputStream serverIn;
        OutputStream serverOut;
        try {
            // Connect to the destination server : current proxy server (bungeecord with not many players)
            this.serverSocket = new Socket(this.proxyManager.getCurrentProxyServer().getHostname(), this.proxyManager.getCurrentProxyServer().getPort());

            this.serverSocket.setKeepAlive(true);
            this.clientSocket.setKeepAlive(true);

            // Save input and output from client and server
            clientIn = this.clientSocket.getInputStream();
            clientOut = this.clientSocket.getOutputStream();
            serverIn = this.serverSocket.getInputStream();
            serverOut = this.serverSocket.getOutputStream();

        } catch (IOException e) {
            this.connectionBroken();
            return;
        }

        // Call forward method
        this.forward(clientIn, clientOut, serverIn, serverOut);
    }

    public synchronized void forward(InputStream clientIn, OutputStream clientOut, InputStream serverIn, OutputStream serverOut) {
        // Start forwarding between client and server
        this.forwardingActive = true;
        final ProxyForward clientForward = new ProxyForward(this, clientIn, serverOut);
        clientForward.start();
        final ProxyForward serverForward = new ProxyForward(this, serverIn, clientOut);
        serverForward.start();
    }

    public synchronized void connectionBroken() {
        // Close sockets
        try {
            this.serverSocket.close();
        } catch (IOException ignored) {}
        try {
            this.clientSocket.close();
        } catch (IOException ignored) {}

        if (this.forwardingActive) {
            forwardingActive = false;
        }
    }
}
