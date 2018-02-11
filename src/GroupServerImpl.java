import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GroupServerImpl extends UnicastRemoteObject implements GroupServer {

    Set<IP_And_Port> clientList = new HashSet<>();
    Map<ArticleInfo, List<IP_And_Port>> subcribeMap = new ConcurrentHashMap<>();
    int MAXCLIENT = 10;

    protected GroupServerImpl() throws RemoteException {
        super();
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
        System.out.println(clientList);
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

        List<IP_And_Port> list =  subcribeMap.getOrDefault(articleInfo, new ArrayList<>());
        list.add(clientAddr);
        subcribeMap.put(articleInfo, list );

        System.out.println(subcribeMap);

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
        List<IP_And_Port> subscribers = subcribeMap.get(articleInfo);

        byte[] buf = articleContent.getBytes();

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        InetAddress address = null;
        for (IP_And_Port subscriber : subscribers) {
            try {
                address = InetAddress.getByName(subscriber.ip);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, subscriber.port);

            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return true;
    }
}
