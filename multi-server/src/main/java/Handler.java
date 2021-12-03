import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import model.Transport;
import utils.Utils;

public class Handler implements Runnable {

  private final Socket client;

  public Handler(Socket client) {
    this.client = client;
  }


  @Override
  public void run() {
    try (DataOutputStream out = new DataOutputStream(client.getOutputStream());
        InputStream inputStream = client.getInputStream()
    ) {
      ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
      Transport[] transports = (Transport[]) objectInputStream.readObject();
      out.writeDouble(Utils.countAverage(transports));
      out.flush();
      out.close();
      client.close();

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("");
    }
  }
}
