package it.uniroma2.dicii.ispw.notification;


import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// each instance executed by different thread
public class ClientHandler implements Runnable{

    protected static final List<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientCf;
    private Ruolo role;
    private static final String NEW_ANN = "Nuovo annuncio da";
    private static final String NEW_REQ = "Nuova richiesta da";
    private static final String REQ_RESPONSE = "Hai ricevuto una risposta!";
    private static final String NEW_ACT = "Nuove attività inserite";
    private static final String EXIT = "Exit";

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //waiting for user cf and role then the user is "registered"
            String init =  bufferedReader.readLine();
            this.clientCf = init.substring(0, 16);
            this.role = Ruolo.getRuolo(Integer.parseInt(init.substring(17)));

            clientHandlers.add(this);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    @Override
    public void run() {
        //waiting for client input
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                //read notification
                messageFromClient = bufferedReader.readLine();

                if(messageFromClient.startsWith(NEW_ANN)) {
                    broadcastNotification(messageFromClient);
                } else if (messageFromClient.startsWith(NEW_REQ)) {
                    secretariatNotification(messageFromClient);
                } else if (messageFromClient.startsWith(REQ_RESPONSE)) {
                    String recipient = messageFromClient.substring(REQ_RESPONSE.length() + 1);
                    personalNotification(recipient, messageFromClient.substring(0, REQ_RESPONSE.length()));
                } else if (messageFromClient.startsWith(NEW_ACT)) {
                    reloadProg();
                } else if (messageFromClient.startsWith(EXIT)) {
                    exit();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void personalNotification(String recipient, String messageToSend) {
        for(ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientCf.equals(clientCf) && clientHandler.clientCf.equals(recipient)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }  else if (clientHandler.clientCf.equals(clientCf)) {
                    clientHandler.bufferedWriter.write("Risposta inviata!");
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                LoggerManager.logSevere(e.getMessage());
            }
        }
    }

    public void secretariatNotification(String messageToSend) {
        for(ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientCf.equals(clientCf)) {
                    //first of all need to check if the user is a secretariat member
                    if(clientHandler.role.equals(Ruolo.SEGRETERIA)) {
                        clientHandler.bufferedWriter.write(messageToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                }  else {
                    clientHandler.bufferedWriter.write("Richiesta prenotazione inviata!");
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void broadcastNotification(String messageToSend) {
        for(ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientCf.equals(clientCf)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                } else {
                    clientHandler.bufferedWriter.write("Annuncio pubblicato correttamente!");
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void reloadProg() {
        for(ClientHandler clientHandler : clientHandlers) {
            try {

                 clientHandler.bufferedWriter.write("Reload");
                 clientHandler.bufferedWriter.newLine();
                 clientHandler.bufferedWriter.flush();

            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void exit() {
        ServerNotification.decreaseConn();
        closeEverything(socket, bufferedReader, bufferedWriter);
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            LoggerManager.logSevere(e.getMessage());
        }
    }
}
