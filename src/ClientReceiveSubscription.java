import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ClientReceiveSubscription extends Thread {

    int port;
    ClientReceiveSubscription(int port) {
        this.port = port;
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        byte buf[] = new byte[120];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {

            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Article received: " + received);
        }
    }
}
