import java.rmi.Naming;

public class Client {
    public static void main (String args[]) {
        System.out.println("Hello World");

        try {
            // host/args[0] is the registry location
            GroupServer gs = (GroupServer) Naming.lookup ("//"+ args[0] +"/GroupServer");
            gs.join("127.0.0.1", 5000);

        } catch (Exception e) {
            System.out.println("HelloClient bug: " + e);
        }

    }
}
