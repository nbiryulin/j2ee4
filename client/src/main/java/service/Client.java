package service;

import exceptions.DuplicateModelNameException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import model.Car;
import model.Transport;

public class Client {


  public static void main(String[] args) throws InterruptedException, DuplicateModelNameException {

    Transport[] transports = new Transport[2];

    Transport transport1 = new Car("mark 1", 2);
    transport1.addModel("model 1", 1);
    transport1.addModel("model 2", 2);

    Transport transport2 = new Car("mark 2", 2);
    transport2.addModel("model 3", 3);
    transport2.addModel("model 4", 4);

    transports[0] = transport1;
    transports[1] = transport2;

    try (Socket socket = new Socket("localhost", 7777);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        //BufferedReader reader = new DataInputStream()r(new InputStreamReader(inputStream));
        DataInputStream reader = new DataInputStream(inputStream);
    ) {
      objectOutputStream.writeObject(transports);
      System.out.println(reader.readDouble());
    } catch (UnknownHostException e) {
      System.out.println("Server is not responding");
    } catch (IOException e) {
      System.out.println("IO exception :( ");
      System.out.println("probably server didn't start");
    }
  }
}