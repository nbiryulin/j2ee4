package service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import model.Transport;
import utils.Utils;

public class Server {
  public static void main(String[] args) throws ClassNotFoundException {
    try (ServerSocket server = new ServerSocket(7779)) {
      while (true) {

        Socket client = server.accept();

        try (DataOutputStream out = new DataOutputStream(client.getOutputStream());
            InputStream inputStream = client.getInputStream()) {
          ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
          Transport[] transports = (Transport[]) objectInputStream.readObject();
          out.writeDouble(Utils.countAverage(transports));
          out.flush();
          out.close();
          client.close();

        } catch (IOException e) {
          e.printStackTrace();
          server.close();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
