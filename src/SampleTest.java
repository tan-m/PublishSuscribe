import java.io.*;
import java.net.*;

class SampleTest {

  public static void main(String args[]) {
    try {
      DatagramSocket socket = new DatagramSocket();
      InetAddress address = InetAddress.getLocalHost();
      String msg = "Register;RMI;"+address.getHostAddress()+";2304;TAG;";
      byte[] buf = msg.getBytes();
      DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 5104);
      socket.send(packet);
      while (true) {
        DatagramPacket request = new DatagramPacket(buf, buf.length);
        socket.receive(request);
        System.out.println(request.toString());
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
