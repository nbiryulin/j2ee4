import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiServer {

  static ExecutorService executeIt = Executors.newFixedThreadPool(2);

  public static void main(String[] args) {
    try (ServerSocket server = new ServerSocket(7777)) {
      while (true) {
        Socket client = server.accept();
        executeIt.execute(new Handler(client));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (RuntimeException e) {
      e.printStackTrace();
      executeIt.shutdown();
    }
  }
}
