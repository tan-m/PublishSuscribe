public class ClientDriver {
    public static void main (String args[]) {
        if (args.length != 1) {
            System.err.println("This class is for making many clients");
            System.err.println("usage java <rmiregistry ip>");
        }
        String rmiRegistryIP =  args[0];

        for (int i=0; i<11; i++) {
            Client c = new Client();
            c.startClient(rmiRegistryIP, 4000+i);
        }
    }
}
