import java.rmi.Naming;

public class Client {
    public static void main (String args[]) {

        try {
            // host/args[0] is the registry location
            GroupServer gs = (GroupServer) Naming.lookup ("//"+ args[0] + ":5000" + "/GroupServer");

            new ClientReceiveSubscription().start();

            boolean joinstatus = gs.join("127.0.0.1", 4000);
//            boolean leavestatus = gs.leave("127.0.0.1", 4000);
            System.out.println("join status :" + joinstatus);

            boolean subscribeStatus = gs.subscribe("127.0.0.1", 4000, "Science;;UMN;");
            System.out.println("subscribe status : " + subscribeStatus);

            subscribeStatus = gs.subscribe("127.0.0.1", 4000, "Science;;;");
            System.out.println("subscribe status : " + subscribeStatus);


            boolean publishStatus = gs.publish( "Science;;;somecontent", "127.0.0.1", 4000);
            System.out.println("publish status is " + publishStatus);

            publishStatus = gs.publish( "Science;;UMN;some more content", "127.0.0.1", 4000);
            System.out.println("publish status is " + publishStatus);


        } catch (Exception e) {
            System.out.println("Client bug: " + e);
        }

    }
}