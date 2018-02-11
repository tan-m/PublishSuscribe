import java.io.*;
import java.net.*;

public class TestClass {
  public static void main( String args[]) throws SocketException,
  UnknownHostException {
  
    System.out.println("Running on the local host with port 5104");
    UDPServer us = new UDPServer(2304);
   
    us.register(InetAddress.getByName("localhost"),5104);
    us.getList(InetAddress.getByName("localhost"),5104);
    us.deregister(InetAddress.getByName("localhost"),5104);
  
    System.out.println("\nRunning on the main server");
    us.register(InetAddress.getByName("dio.cs.umn.edu"), 5105);
    us.getList(InetAddress.getByName("dio.cs.umn.edu"), 5105);
    us.deregister(InetAddress.getByName("dio.cs.umn.edu"), 5105);
  }
}
