import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupServerImpl extends UnicastRemoteObject implements GroupServer {

    Set<IP_And_Port> clientList = new ConcurrentHashMap().newKeySet();
    Map<ArticleInfo, Set<IP_And_Port>> subcribeMap = new ConcurrentHashMap<>();
    ExecutorService executor;
    int MAXCLIENT = 10;

    private int gsPort;
    private InetAddress address   = null;
    private DatagramSocket socket = null;
    private DatagramPacket packet = null;
    private byte[] buf;
    volatile boolean runnable = false;

  // Constructor call, creates the Sample Server Port

    protected GroupServerImpl(InetAddress rsHost, int rsPort, int gsPort) throws RemoteException {
      super();
      this.gsPort = gsPort;
      try{
        socket = new DatagramSocket(gsPort);
        address = InetAddress.getLocalHost();
        register(rsHost, rsPort);
      } catch(Exception e) {
        e.printStackTrace();
      }
        executor = Executors.newFixedThreadPool(5);
    }

    @Override
    public boolean join(String ip, int port) throws RemoteException {
        if (clientList.size() == MAXCLIENT) return false;
        IP_And_Port obj = new IP_And_Port(ip, port);
        clientList.add(obj);
//        System.out.println(clientList);
        return true;
    }

    @Override
    public boolean leave(String ip, int port) throws RemoteException {
        boolean res = clientList.remove(new IP_And_Port(ip, port));
        return res;
    }

    @Override
    public boolean subscribe(String ip, int port, String article) throws RemoteException {

        IP_And_Port clientAddr = new IP_And_Port(ip, port);
        if (!clientList.contains(clientAddr)) return false;

        String sections[] = {"", "", ""};
        int start = 0;
        for (int i=0, j=0; i<article.length(); i++) {
            if (article.charAt(i) == ';') {
                sections[j++] = article.substring(start, i);
                start = i+1;
            }
        }
        if (start != article.length()) return false; // since there is some content

        ArticleInfo articleInfo = new ArticleInfo(sections[0], sections[1], sections[2]);

        Set<IP_And_Port> list =  subcribeMap.getOrDefault(articleInfo, new HashSet<>());
        list.add(clientAddr);
        subcribeMap.put(articleInfo, list );

        System.out.println(subcribeMap);

        return true;

    }

    @Override
    public boolean unsubscribe(String ip, int port, String article) throws RemoteException {
        IP_And_Port clientAddr = new IP_And_Port(ip, port);
        if (!clientList.contains(clientAddr)) return true; // already left the server

        String sections[] = {"", "", ""};
        int start = 0;
        for (int i=0, j=0; i<article.length(); i++) {
            if (article.charAt(i) == ';') {
                sections[j++] = article.substring(start, i);
                start = i+1;
            }
        }
        if (start != article.length()) return false; // since there is some content

        ArticleInfo articleInfo = new ArticleInfo(sections[0], sections[1], sections[2]);

        if (subcribeMap.containsKey(articleInfo)) {
            Set<IP_And_Port> list =  subcribeMap.get(articleInfo);
            list.remove(clientAddr);
        }


        return true;
    }

    @Override
    public boolean publish(String article, String ip, int port) throws RemoteException {

        String sections[] = {"", "", ""};
        int start = 0;
        for (int i=0, j=0; i<article.length(); i++) {
            if (article.charAt(i) == ';') {
                sections[j++] = article.substring(start, i);
                start = i+1;
            }
        }
        if (start == article.length()) return false; // nothing to publish
        String articleContent = article.substring(start, article.length());

        ArticleInfo articleInfo = new ArticleInfo(sections[0], sections[1], sections[2]);


        Runnable worker = new DispatchArticles( clientList, subcribeMap, articleInfo, articleContent );
        executor.execute(worker);

        return true;

    }

    @Override
    public String ping() throws RemoteException {
        return "Hi";
    }
  /*
   * Function call to register UDPServer with Registry Server.
   * Takes a String argument consisting of IP address and the
   * port to connect. The register function builds the string -
   * “Register;RMI;IP;Port;BindingName;Port for RMI”
   */
  public void register(InetAddress rsHost, int rsPort)
  {
    String msg = "Register;RMI;"+address.getHostAddress()+";"+gsPort+";TAG;";
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
    String msg = "Deregister;RMI;"+address.getHostAddress()+";"+gsPort+";";
    buf = msg.getBytes();
    packet = new DatagramPacket(buf, buf.length, rsHost, rsPort);
    try {
      socket.send(packet);
      runnable = false;
      System.out.println("Deregistering the server from the Registry Server");
      socket.close();
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
    String msg = "GetList;RMI;"+address.getHostAddress()+";"+gsPort;
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

}
