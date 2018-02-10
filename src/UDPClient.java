import java.net.*;
import java.io.*;

public class UDPClient {
  private DatagramSocket socket;
  private InetAddress address;

  private byte[] buf;

  public UDPClient() throws SocketException, UnknownHostException{
    socket = new DatagramSocket();
    address = InetAddress.getByName("localhost");
  }

  public String sendEcho(String msg) {
    buf = msg.getBytes();
    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
    try {
      socket.send(packet);
    } catch (IOException e) {
          System.out.println("Failed receiving data");
    }
    packet = new DatagramPacket(buf, buf.length);
    try {
      socket.receive(packet);
    } catch(IOException e) {
          System.out.println("Failed receiving data");
    }
    String received = new String(
    packet.getData(), 0, packet.getLength());
    return received;
  }

  public void close() {
    socket.close();
  }
}

