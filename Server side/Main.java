import java.io.IOException;
import java.net.ServerSocket;
import java.sql.*;

/* Starts up the server. */
public class Main {
    public static void main(String[] args) throws IOException {
        try{
            ServerSocket serverSocket = new ServerSocket(3000);
            Server server = new Server(serverSocket);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("server closed");
    }
}