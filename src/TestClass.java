import java.io.*;
import java.net.*;

public class TestClass {
  public static void main( String args[]) throws SocketException,
  UnknownHostException {
    System.out.println("Starting the program");
    UDPServer us = new UDPServer(4445);
    us.register(InetAddress.getByName("128.107.35.147"),5104);
    //us.getList(InetAddress.getByName("128.101.35.147"),5105);
  }
}
