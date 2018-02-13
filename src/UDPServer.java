import java.net.*;
import java.io.*;

class UDPServer extends Thread {

  private int port;
  private InetAddress address   = null;
  private DatagramSocket socket = null;
  private DatagramPacket packet = null;
  private byte[] buf;
  volatile boolean runnable = false;

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
  public void register(InetAddress rsHost, int rsPort)
  {
    String msg = "Register;RMI;"+address.getHostAddress()+";"+port+";TAG;";
    buf = msg.getBytes();
    packet = new DatagramPacket(buf, buf.length, rsHost, rsPort);
    try {
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    runnable = true;
    Thread hbThread = new Thread("Heartbeat Thread") {
      public void run() {
        listenHeartBeat();
      }
    };

    if (runnable) {
      hbThread.setPriority(Thread.MAX_PRIORITY);
      hbThread.start();
    } 
  }

  /*
   * Function call to deregister UDPServer with Registry Server.
   * Takes a String argument consisting of 
   * “Deregister;RMI;IP;Port”
   */
  public void deregister(InetAddress rsHost, int rsPort) {
    String msg = "Deregister;RMI;"+address.getHostAddress()+";"+port+";";
    buf = msg.getBytes();
    packet = new DatagramPacket(buf, buf.length, rsHost, rsPort);
    try {
      socket.send(packet);
      runnable = false;
      System.out.println("Deregistering the server from the Registry Server");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * Function call to get a list of all the machines attached to the
   * registry server. The return argument can be assumed to be less than 1024 B
   * “GetList;RMI;IP;Port"
   */
  public void getList(InetAddress rsHost, int rsPort) 
  { 
    String msg = "GetList;RMI;"+address.getHostAddress()+";"+port;
    buf = msg.getBytes();
    packet = new DatagramPacket(buf, buf.length, rsHost, rsPort);
    try {
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
    // try listening to the List of servers connected to the Registry Server.
    buf = new byte[1024];
    packet = new DatagramPacket(buf, buf.length);
    try {
      socket.receive(packet);
    } catch(IOException e) {
      e.printStackTrace();
    }
    msg = new String(packet.getData(), 0, packet.getLength());
    System.out.println("List of servers online are: " + msg);
  }

  /*
   * Listen to the heartbeat message sent by the Registry server to the Group 
   * Server. First HB is during the register and GS responds within 5 sec of 
   * receiving the HB.
   */
  public void listenHeartBeat() {
    // try receiving data from the server
    while(runnable) {
      buf = new byte[1024];
      try {
        Thread.sleep(3500);
        if (!runnable)
          break;
        System.out.println("Listen to the heartbeat");
        DatagramPacket request = new DatagramPacket(buf, buf.length);
        socket.receive(request);
        DatagramPacket reply = new DatagramPacket(request.getData(),
                    request.getLength(), request.getAddress(), request.getPort());
        socket.send(reply);
      } catch(Exception e) {
        e.printStackTrace();
      }
    } // end the while loop
  }

} // End of the UDPServer class

