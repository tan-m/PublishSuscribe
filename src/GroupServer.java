import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GroupServer extends Remote {
    boolean join (String ip, int port) throws RemoteException;
}