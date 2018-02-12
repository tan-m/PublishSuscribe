import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GroupServer extends Remote {

    boolean join (String ip, int port) throws RemoteException;
    boolean leave (String ip, int port) throws RemoteException;
    boolean subscribe (String ip, int port, String article) throws RemoteException;
    boolean unsubscribe (String ip, int port, String article) throws RemoteException;
    boolean publish (String article, String ip, int port) throws RemoteException;
    String ping() throws RemoteException;

}