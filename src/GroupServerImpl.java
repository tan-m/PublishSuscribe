import java.rmi.RemoteException;
import java.rmi.server.*;
import java.rmi.server.UnicastRemoteObject;

public class GroupServerImpl extends UnicastRemoteObject implements GroupServer {

    protected GroupServerImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean join(String ip, int port) throws RemoteException {
        System.out.println("in server");
        return false;
    }
}
