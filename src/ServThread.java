import java.net.*;
import java.io.*;

class UDPServer extends Thread {

  private DatagramSocket socket;
  private boolean running;
  private byte[] buf = new byte[1024];

  // Constructor call, creates the Sample Server Port
  public UDPServer() throws SocketException {
    socket = new DatagramSocket(4445);
  }

  /*
   * Function call to register UDPServer with Registry Server.
   * Takes a String argument consisting of 
   * “Register;RMI;IP;Port;BindingName;Port for RMI”
   */
  public void Register(String regString) {

  }

  /*
   * Function call to deregister UDPServer with Registry Server.
   * Takes a String argument consisting of 
   * “Deregister;RMI;IP;Port”
   */
  public void Deregister(String dregString) {

  }

  /*
   * Function call to get a list of all the machines attached to the
   * registry server. The return argument can be assumed to be less than 1024 B
   * “GetList;RMI;IP;Port"
   */
  public String GetList(String glString) {

  }

  /*
   * Implement the thread interface
   */
  public void run() {
    running = true;

    while (running) {
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      try {
        socket.receive(packet);
      } catch(IOException e) {
          System.out.println("Failed receiving data");
      }

      InetAddress address = packet.getAddress();
      int port = packet.getPort();
      packet = new DatagramPacket(buf, buf.length, address, port);
      String received = new String(packet.getData(), 0, packet.getLength());

      if (received.equals("end")) {
        running = false;
        continue;
      }
      try {
        socket.send(packet);
      } catch (IOException e) {
          System.out.println("Failed receiving data");
      }
    }
    socket.close();
  }
}
