import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StartServer {
    public static void main (String args[]) throws Exception {
        GroupServer groupServer = new GroupServerImpl();
        System.out.println("server started");
//        Naming.bind("GroupServer", groupServer);

        Registry localRegistry = LocateRegistry.createRegistry( 5000);
        localRegistry.bind ("GroupServer", groupServer);
    }
}
