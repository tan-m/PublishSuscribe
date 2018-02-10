import java.net.*;
import java.io.*;

class UDPServer {

  private int port;
  private InetAddress address   = null;
  private DatagramSocket socket = null;
  private DatagramPacket packet = null;
  private byte[] buf;

  // Constructor call, creates the Sample Server Port
  public UDPServer(int port) {
    this.port = port;
    try{
      socket = new DatagramSocket(port);
      address = InetAddress.getLocalHost();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * Function call to register UDPServer with Registry Server.
   * Takes a String argument consisting of IP address and the
   * port to connect. The register function builds the string -
   * “Register;RMI;IP;Port;BindingName;Port for RMI”
   */
  public void register(InetAddress rsIP, int rsPort)
  {
    String msg = "Register;RMI;"+address.getHostAddress()+";"+port+";TAG;1099;";
    buf = msg.getBytes();
    packet = new DatagramPacket(buf, buf.length, rsIP, rsPort);
    try {
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
    listenHeartBeat();
  }

  /*
   * Function call to deregister UDPServer with Registry Server.
   * Takes a String argument consisting of 
   * “Deregister;RMI;IP;Port”
   */
  public void deregister() {

  }

  /*
   * Function call to get a list of all the machines attached to the
   * registry server. The return argument can be assumed to be less than 1024 B
   * “GetList;RMI;IP;Port"
   */
  public void getList(InetAddress rsIP, int rsPort) 
  { 
    String msg = "GetList;RMI;"+address.getHostAddress()+";"+port;
    buf = msg.getBytes();
    packet = new DatagramPacket(buf, buf.length, rsIP, rsPort);
    try {
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
    // try listening to the List of servers connected to the Registry Server.
    packet = new DatagramPacket(buf, buf.length);
    try {
      socket.receive(packet);
    } catch(IOException e) {
      e.printStackTrace();
    }
    msg = new String(packet.getData(), 0, packet.getLength());
    System.out.println(msg);
  }

  /*
   * Listen to the heartbeat message sent by the Registry server to the Group 
   * Server. First HB is during the register and GS responds within 5 sec of 
   * receiving the HB.
   */
  public void listenHeartBeat() {
    byte[] buf = new byte[1024];
    packet = new DatagramPacket(buf, buf.length);
    try {
      socket = new DatagramSocket(5105);
      socket.receive(packet);
    } catch(IOException e) {
      e.printStackTrace();
    }
    String received = new String(packet.getData(), 0, packet.getLength());
    System.out.println(received);
  }
}


    /*
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    try {
      socket.receive(packet);
    } catch(IOException e) {
      e.printStackTrace();
    }

    InetAddress address = packet.getAddress();
    int port = packet.getPort();
    packet = new DatagramPacket(buf, buf.length, address, port);
    String received = new String(packet.getData(), 0, packet.getLength());

    try {
      socket.send(packet);
    } catch (IOException e) {
        System.out.println("Failed receiving data");
    }
    */
