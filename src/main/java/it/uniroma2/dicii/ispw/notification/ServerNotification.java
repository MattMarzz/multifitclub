package it.uniroma2.dicii.ispw.notification;

import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNotification {
    private final ServerSocket serverSocket;
    private static final int PORT = 1234;
    private int connections = 0;
    private static final int MAX_CONNECTONS = 1000;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        ServerNotification server = new ServerNotification(serverSocket);
        server.startServer();
    }

    public ServerNotification(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {

        try {
            while (!serverSocket.isClosed()) {
                //waiting for clients
                Socket socket = serverSocket.accept();
                LoggerManager.logFine("Nuovo client connesso!");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
                connections++;
                if(connections >= MAX_CONNECTONS) break;
            }
        } catch (IOException e) {
            LoggerManager.logSevere(e.getMessage());
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LoggerManager.logSevere(e.getMessage());
        }
    }

}
