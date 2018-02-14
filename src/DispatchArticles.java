import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.Set;

public class DispatchArticles implements Runnable {

    Set<IP_And_Port> clientList;
    Map<ArticleInfo, Set<IP_And_Port>> subcribeMap;
    ArticleInfo articleInfo;
    String articleContent;

    public DispatchArticles(Set<IP_And_Port> clientList, Map<ArticleInfo, 
                            Set<IP_And_Port>> subcribeMap, ArticleInfo articleInfo,
                            String articleContent) {
        this.clientList = clientList;
        this.subcribeMap = subcribeMap;
        this.articleInfo = articleInfo;
        this.articleContent = articleContent;
    }

    @Override
    public void run() {

        Set<IP_And_Port> subscribers = subcribeMap.get(articleInfo);

        byte[] buf = articleContent.getBytes();

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        InetAddress address = null;
        for (IP_And_Port subscriber : subscribers) {
            if (!clientList.contains(subscriber)) continue;
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
    }
}
