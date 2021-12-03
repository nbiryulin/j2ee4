package service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import model.Transport;

public class ClientRunnable implements Runnable{

  private String name;
  private Transport[] transports;

  public ClientRunnable(String name,Transport[] transports) {
    this.name = name;
    this.transports = transports;
  }

  @Override
  public void run() {
    try (Socket socket = new Socket("localhost", 7777);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        DataInputStream reader = new DataInputStream(inputStream);
    ) {
      objectOutputStream.writeObject(transports);
      System.out.println(name + " " + reader.readDouble());
    } catch (UnknownHostException e) {
      System.out.println("Server is not responding");
    } catch (IOException e) {
      System.out.println("IO exception :( ");
      System.out.println("probably server didn't start");
    }
  }
}
