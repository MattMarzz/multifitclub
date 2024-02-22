package it.uniroma2.dicii.ispw.notification;

import it.uniroma2.dicii.ispw.enums.RoomRequestStatus;
import it.uniroma2.dicii.ispw.model.communication.RoomRequest;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.utils.Subject;

import java.io.*;
import java.net.Socket;

public class Client extends Subject {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Utente utente;
    private static final String NEW_ANN = "Nuovo annuncio da";
    private static final String NEW_REQ = "Nuova richiesta da";
    private static final String REQ_RESPONSE = "Hai ricevuto una risposta!";
    private static final String NEW_ACT = "Nuove attività inserite";
    private static final String EXIT = "Exit";

    public Client(Socket socket, Utente utente) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.utente = utente;

            bufferedWriter.write(utente.getCf() + " " + utente.getRuolo().getId());
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendAnnouncementNotification() {
        try {
            bufferedWriter.write(NEW_ANN + " " + utente.getName());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendRoomRequestNotification(RoomRequest roomRequest) {
        if(roomRequest.getStatus().equals(RoomRequestStatus.PENDING)){
            //need to notify all secretary members
            try {
                bufferedWriter.write(NEW_REQ + " " + utente.getName());
                bufferedWriter.newLine();
                bufferedWriter.flush();

            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        } else {
            //it's a room request response -> only sender need notification
            try {
                //send text and recipient
                bufferedWriter.write(REQ_RESPONSE + " " + roomRequest.getSender());
                bufferedWriter.newLine();
                bufferedWriter.flush();

            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void newActivitiesDetected() {
        try {
            bufferedWriter.write(NEW_ACT);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void exit() {
        try {
            bufferedWriter.write(EXIT + " " + utente.getCf());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        //new thread always listening for server notification
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromServer;

                while(socket.isConnected()) {
                    try {
                        messageFromServer = bufferedReader.readLine();
                        //notify all attached observers
                        notifyChanges(messageFromServer);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
