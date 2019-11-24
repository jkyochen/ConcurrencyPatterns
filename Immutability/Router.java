package Immutability;

public final class Router {

    final String ip;
    final Integer port;
    final String iface;

    public Router(String ip, Integer port, String iface) {
        this.ip = ip;
        this.port = port;
        this.iface = iface;
    }
}
