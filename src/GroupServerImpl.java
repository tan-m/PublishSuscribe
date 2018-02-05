import java.rmi.RemoteException;
import java.rmi.server.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GroupServerImpl extends UnicastRemoteObject implements GroupServer {

    List<IP_And_Port> clientList = new ArrayList<>();
    int MAXCLIENT = 10;

    protected GroupServerImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean join(String ip, int port) throws RemoteException {
        System.out.println("in server");
        if (clientList.size() == MAXCLIENT) return false;
        IP_And_Port obj = new IP_And_Port(ip, port);
        clientList.add(obj);
        System.out.println(clientList);
        return true;
    }

    @Override
    public boolean leave(String ip, int port) throws RemoteException {
        boolean res = clientList.remove(new IP_And_Port(ip, port));
        System.out.println(clientList);
        return res;

    }
}
