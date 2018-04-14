import java.rmi.RemoteException;

public class ClientPingThread extends Thread {

    GroupServer gs;
    ClientPingThread(GroupServer gs) {
        this.gs = gs;
    }

    public void run() {
        while (true) {

            try {
                String str = gs.ping();
                System.out.println("in ping " + str);
                if (!str.equals("Hi")) {
                    break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("error!");
        return;
    }
}
