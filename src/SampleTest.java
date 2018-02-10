import java.io.*;
import java.net.*;

class SampleTest {

  public static void main(String args[]) {
    try {
      DatagramSocket socket = new DatagramSocket();
      InetAddress address = InetAddress.getLocalHost();
      String msg = "Register;RMI;";
      byte[] buf = msg.getBytes();
      System.out.println(socket.getPort());
      DatagramPacket packet = new DatagramPacket(buf, buf.length,address, 5104);
      socket.send(packet);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
