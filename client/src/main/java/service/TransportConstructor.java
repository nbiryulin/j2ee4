package service;

import exception.ServerKillException;
import java.io.BufferedReader;
import java.io.IOException;
import model.Car;
import model.Transport;

public class TransportConstructor {

  public static Transport[] create(BufferedReader reader) throws ServerKillException {

    try {
      int count = Integer.parseInt(reader.readLine());
      Transport[] transports = new Transport[count];
      String mark = reader.readLine();
      switch (mark) {
        case "kill": {
          throw new ServerKillException();
        }
        case "mark": {
          System.out.println("Write model count");
          int modelCount = Integer.parseInt(reader.readLine());
          System.out.println("Write model name");
          String modelName = reader.readLine();
          Car car = new Car(modelName, modelCount);

        }
        case "model": {

        }
        case "send": {
          return transports;
        }
        default: {

        }
      }
    } catch (IOException e) {
      System.out.println("Cannot");
    } catch (NumberFormatException e) {

    }
    return null;
  }
}
