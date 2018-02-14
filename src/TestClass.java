import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.*;

public class TestClass {
    public static void main (String args[]) throws Exception {


        GroupServerImpl groupServer = new GroupServerImpl(2304);
        groupServer.register(InetAddress.getByName("localhost"), 5104);
        //for( int i = 0 ; i < 100; i++) {
          groupServer.getList(InetAddress.getByName("localhost"), 5104);
        //  Thread.sleep(250);
        //}
        groupServer.deregister(InetAddress.getByName("localhost"), 5104);
/*
        System.out.println("\nRunning on the main server");
        GroupServerImpl groupServer1 = new GroupServerImpl(2304);
        groupServer1.register(InetAddress.getByName("dio.cs.umn.edu"),5105);
        groupServer1.getList(InetAddress.getByName("dio.cs.umn.edu"), 5105);
        for( int i = 0 ; i < 100; i++) {
        //  groupServer1.getList(InetAddress.getByName("dio.cs.umn.edu"), 5105);
          Thread.sleep(1500);
        }
        groupServer1.deregister(InetAddress.getByName("dio.cs.umn.edu"), 5105);
*/
        Registry localRegistry = LocateRegistry.createRegistry( 5000);
        localRegistry.bind ("GroupServer", groupServer);
    }
}
