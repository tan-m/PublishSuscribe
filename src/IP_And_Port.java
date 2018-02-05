public class IP_And_Port {
    String ip;
    int port;
    IP_And_Port (String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return ip+":"+port;
    }

    @Override
    public boolean equals(Object obj1) {
        IP_And_Port obj = (IP_And_Port) obj1;
        return ip.equals(obj.ip) && obj.port == port;
    }
}
