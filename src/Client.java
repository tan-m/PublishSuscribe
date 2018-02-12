import java.rmi.Naming;

public class Client {
    public static void main (String args[]) {

        if (args.length != 2) {
            System.err.println("usage java <ip> <client port>");
        }

        try {
            // host/args[0] is the registry location
            GroupServer gs = (GroupServer) Naming.lookup ("//"+ args[0] + ":5000" + "/GroupServer");


            String ip = "127.0.0.1";
            int port = Integer.parseInt(args[1]);

            new ClientReceiveSubscription(port).start();

            new ClientPingThread(gs).start();

            boolean joinstatus = gs.join(ip, port);
//            boolean leavestatus = gs.leave(ip, port);
            System.out.println("join status :" + joinstatus);

            boolean subscribeStatus = gs.subscribe(ip, port, "Science;;UMN;");
            System.out.println("subscribe status : " + subscribeStatus);

            subscribeStatus = gs.subscribe(ip, port, "Science;;;");
            System.out.println("subscribe status : " + subscribeStatus);

            boolean publishStatus = gs.publish( "Science;;;somecontent", ip, port);
            System.out.println("publish status is " + publishStatus);

            publishStatus = gs.publish( "Science;;UMN;some more content", ip, port);
            System.out.println("publish status is " + publishStatus);

            Thread.sleep(300);

            boolean unsubscribeStatus = gs.unsubscribe(ip, port, "Science;;;");
            System.out.println("unsubscribe status : " + unsubscribeStatus);


        } catch (Exception e) {
            System.out.println("Client bug: " + e);
        }

    }
}