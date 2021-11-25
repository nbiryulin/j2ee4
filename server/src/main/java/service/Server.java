package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import model.Transport;
import utils.Utils;

public class Server {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    ServerSocket ss = new ServerSocket(7777);

    Socket socket = ss.accept();

    InputStream inputStream = socket.getInputStream();
    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
    Transport[] transports = (Transport[]) objectInputStream.readObject();
    Utils.countAverage()
    ss.close();
    socket.close();
  }
}
