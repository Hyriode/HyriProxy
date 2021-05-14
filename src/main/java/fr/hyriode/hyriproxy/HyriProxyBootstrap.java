package fr.hyriode.hyriproxy;

public class HyriProxyBootstrap {

    public static void main(String[] args) {
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52.0D) {
            System.err.println("*** ERROR *** Hyggdrasil requires Java >= 8 to function!");
            return;
        }

        final HyriProxy hyriProxy = new HyriProxy();
        hyriProxy.start();
    }

}
