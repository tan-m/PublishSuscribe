import java.rmi.Naming;
import java.rmi.RemoteException;

public class StartServer {
    public static void main (String args[]) throws Exception {
        GroupServer groupServer = new GroupServerImpl();
        Naming.bind("GroupServer", groupServer);
    }
}
