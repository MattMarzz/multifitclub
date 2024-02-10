package it.uniroma2.dicii.ispw.model.communication.dao;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import it.uniroma2.dicii.ispw.enums.RoomRequestStatus;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.communication.RoomRequest;
import it.uniroma2.dicii.ispw.utils.CSVManager;
import it.uniroma2.dicii.ispw.utils.ConstantMsg;
import it.uniroma2.dicii.ispw.utils.DateParser;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RoomRequestFS implements RoomRequestDAO{

    private static final String CSV_FILE_NAME = CSVManager.getCsvDir() + "richiesta.csv";
    private final File file;
    private static final int INDEX_ID = 0;
    private static final int INDEX_TITOLO = 1;
    private static final int INDEX_TESTO = 2;
    private static final int INDEX_DATA = 3;
    private static final int INDEX_QUANDO = 4;
    private static final int INDEX_STATUS = 5;
    private static final int INDEX_UTENTE = 6;
    private static final int INDEX_SALA = 7;

    public RoomRequestFS() throws IOException {
        this.file = new File(CSV_FILE_NAME);

        if(!file.exists()) {
            boolean isFileCreated = file.createNewFile();
            if(!isFileCreated) throw new IOException("Impossibile dialogare con il file");
        }
    }

    @Override
    public String insertRoomRequest(RoomRequest roomRequest) {
        CSVWriter csvWriter = null;
        String[] rcrd;
        try {
            csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, true)),  ICSVWriter.DEFAULT_SEPARATOR,
                    ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);

            List<RoomRequest> allRequests = getAllRequest();
            int lastId = 0;
            if(!allRequests.isEmpty())
                lastId = allRequests.getLast().getReqId();

            roomRequest.setReqId(lastId + 1);

            rcrd = setRecordFromRRequest(roomRequest);

            csvWriter.writeNext(rcrd);
            csvWriter.flush();

        } catch (IOException e) {
            LoggerManager.logSevereException("Impossibile scrivere file!", e);
            return "Errore in inserimento";

        }finally {
            CSVManager.closeCsvWriter(csvWriter);
        }

        return "Inserimento effettuato";
    }

    @Override
    public List<RoomRequest> getAllRequest() {
        List<RoomRequest> roomRequests = new ArrayList<>();
        CSVReader csvReader = null;

        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd = {};

            while ((rcrd = csvReader.readNext()) != null) {
                RoomRequest rr = setRRequestFromRecord(rcrd);
                roomRequests.add(rr);
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return roomRequests;
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        return roomRequests;
    }

    @Override
    public String requestResponse(RoomRequest roomRequest) throws ItemNotFoundException {
        boolean isExistingRr = false;
        CSVReader csvReader = null;
        CSVWriter csvWriter = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd = {};

            int index = INDEX_ID;
            List<String[]> updatedRecords = new ArrayList<>();
            String originalSender = null;

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(Integer.parseInt(rcrd[index]) == roomRequest.getReqId()) {
                    isExistingRr = true;
                    rcrd[INDEX_STATUS] = String.valueOf(roomRequest.getStatus().ordinal());
                    originalSender = rcrd[INDEX_UTENTE];
                }
                updatedRecords.add(rcrd);
            }

            if(isExistingRr) {
                csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, false)),  ICSVWriter.DEFAULT_SEPARATOR,
                        ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);
                csvWriter.writeAll(updatedRecords);
                csvWriter.flush();

                return originalSender;
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            throw new ItemNotFoundException("Utente non esistente.");
        } finally {
            CSVManager.closeCsvReader(csvReader);
            CSVManager.closeCsvWriter(csvWriter);
        }
        return "Richiesta non trovata";
    }

    @Override
    public RoomRequest getRoomRequestById(int id) throws ItemNotFoundException {
        List<RoomRequest> allRequests = getAllRequest();
        for (RoomRequest rr: allRequests) {
            if(rr.getReqId() == id) {
                return rr;
            }
        }
        throw new ItemNotFoundException("Richiesta non esistente!");
    }

    @Override
    public List<RoomRequest> getRoomRequestByUtente(String cf) {
        List<RoomRequest> allRequests = getAllRequest();
        allRequests.removeIf(rr -> !rr.getSender().equals(cf));
        return allRequests;
    }

    @Override
    public List<RoomRequest> getAllAcceptedRequest() {
        List<RoomRequest> allRequests = getAllRequest();
        allRequests.removeIf(rr -> !rr.getStatus().equals(RoomRequestStatus.ACCEPTED));
        return allRequests;
    }

    private RoomRequest setRRequestFromRecord(String[] rcrd) {
        int id = Integer.parseInt(rcrd[INDEX_ID]);
        String titolo = rcrd[INDEX_TITOLO];
        String testo = rcrd[INDEX_TESTO];
        String dataStr = rcrd[INDEX_DATA];
        String quandoStr = rcrd[INDEX_QUANDO];
        int statusId = Integer.parseInt(rcrd[INDEX_STATUS]);
        String sender = rcrd[INDEX_UTENTE];
        String sala = rcrd[INDEX_SALA];

        Timestamp date = null;
        Timestamp when = null;
        try {
            date = DateParser.parseStringToTimestamp(dataStr);
            when = DateParser.parseStringToTimestamp(quandoStr);
        } catch (InvalidDataException e) {
            LoggerManager.logSevereException("Errore di conversione data: ", e);
        }

        return new RoomRequest(sender, id, titolo, testo, date, sala, when, RoomRequestStatus.getStatus(statusId));
    }

    private String[] setRecordFromRRequest(RoomRequest rr) {
        String[] rcrd = new String[8];

        rcrd[INDEX_ID] = String.valueOf(rr.getReqId());
        rcrd[INDEX_TITOLO] = rr.getTitle();
        rcrd[INDEX_TESTO] = rr.getMsg();
        rcrd[INDEX_UTENTE] = rr.getSender();
        rcrd[INDEX_DATA] = DateParser.parseTimestampToString(rr.getDate());
        rcrd[INDEX_QUANDO] = DateParser.parseTimestampToString(rr.getWhen());
        rcrd[INDEX_SALA] = rr.getRoom();
        rcrd[INDEX_STATUS] = String.valueOf(rr.getStatus().ordinal());

        return rcrd;
    }
    
}
