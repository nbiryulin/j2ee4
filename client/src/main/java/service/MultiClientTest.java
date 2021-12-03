package service;

import exceptions.DuplicateModelNameException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.Car;
import model.Transport;

public class MultiClientTest {

  public static void main(String[] args) throws DuplicateModelNameException {
    ExecutorService exec = Executors.newFixedThreadPool(10);

    for (int i = 1; i <= 10 ; i++) {
      List<Transport> transports = new ArrayList<>();
      for (int j = 0; j < i; j++) {
        Transport transport = new Car("mark " + j, i);
        for (int k = 0; k <= j ; k++) {//почти О(1)
          transport.addModel("model " + k, k);
          transports.add(transport);
        }
      }
      System.out.println(transports);
      exec.execute(new ClientRunnable("client " + i, transports.toArray(new Transport[i])));
    }
    exec.shutdown();
  }

}
