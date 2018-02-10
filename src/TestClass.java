import java.io.*;
import java.net.*;

public class TestClass {
  public static void main( String args[]) throws SocketException,
  UnknownHostException {
  /*
    UDPClient client;

    System.out.println("Starting the program");
    new UDPServer().start();
    client = new UDPClient();

    String echo = client.sendEcho("hello server");
    assert echo == "hello server";
    echo = client.sendEcho("server is working");
    assert echo != "hello server";

    client.sendEcho("end");
    client.close();
    System.out.println("Ending the program");
  */
    System.out.println("Starting the program");
    UDPServer us = new UDPServer(5000);
    us.register(InetAddress.getByName("128.101.35.147"),5104);
    us.getList(InetAddress.getByName("128.101.35.147"),5105);
  }
}
