package fr.hyriode.hyriproxy.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProxyForward extends Thread {

    private static final int BUFFER_SIZE = 8192;

    private ProxyForwardServerClient parent;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ProxyForward(ProxyForwardServerClient parent, InputStream inputStream, OutputStream outputStream) {
        this.parent = parent;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            while (true) {
                int bytesRead = this.inputStream.read(buffer);
                if (bytesRead == -1)
                    break; // Reached the end of the stream
                this.outputStream.write(buffer, 0, bytesRead);
                this.outputStream.flush();
            }
        } catch (IOException ignored) {} // Connection broken

        // Notify parent that the connection is broken
        this.parent.connectionBroken();
    }
}
