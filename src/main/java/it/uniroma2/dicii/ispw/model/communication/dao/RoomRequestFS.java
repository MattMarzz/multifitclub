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

            int index = RoomRequestAttributesOrder.getIndexId();
            List<String[]> updatedRecords = new ArrayList<>();
            String originalSender = null;

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(Integer.parseInt(rcrd[index]) == roomRequest.getReqId()) {
                    isExistingRr = true;
                    rcrd[RoomRequestAttributesOrder.getIndexStatus()] = String.valueOf(roomRequest.getStatus().ordinal());
                    originalSender = rcrd[RoomRequestAttributesOrder.getIndexUtente()];
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
        int id = Integer.parseInt(rcrd[RoomRequestAttributesOrder.getIndexId()]);
        String titolo = rcrd[RoomRequestAttributesOrder.getIndexTitolo()];
        String testo = rcrd[RoomRequestAttributesOrder.getIndexTesto()];
        String dataStr = rcrd[RoomRequestAttributesOrder.getIndexData()];
        String quandoStr = rcrd[RoomRequestAttributesOrder.getIndexQuando()];
        int statusId = Integer.parseInt(rcrd[RoomRequestAttributesOrder.getIndexStatus()]);
        String sender = rcrd[RoomRequestAttributesOrder.getIndexUtente()];
        String sala = rcrd[RoomRequestAttributesOrder.getIndexSala()];

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

        rcrd[RoomRequestAttributesOrder.getIndexId()] = String.valueOf(rr.getReqId());
        rcrd[RoomRequestAttributesOrder.getIndexTitolo()] = rr.getTitle();
        rcrd[RoomRequestAttributesOrder.getIndexTesto()] = rr.getMsg();
        rcrd[RoomRequestAttributesOrder.getIndexUtente()] = rr.getSender();
        rcrd[RoomRequestAttributesOrder.getIndexData()] = DateParser.parseTimestampToString(rr.getDate());
        rcrd[RoomRequestAttributesOrder.getIndexQuando()] = DateParser.parseTimestampToString(rr.getWhen());
        rcrd[RoomRequestAttributesOrder.getIndexSala()] = rr.getRoom();
        rcrd[RoomRequestAttributesOrder.getIndexStatus()] = String.valueOf(rr.getStatus().ordinal());

        return rcrd;
    }

    private static class RoomRequestAttributesOrder {
        public static int getIndexId() {
            return 0;
        }
        public static int getIndexTitolo() {
            return 1;
        }
        public static int getIndexTesto() {
            return 2;
        }
        public static int getIndexData() {
            return 3;
        }
        public static int getIndexQuando() {
            return 4;
        }
        public static int getIndexStatus() {
            return 5;
        }
        public static int getIndexUtente() {
            return 6;
        }
        public static int getIndexSala() {
            return 7;
        }
    }
}
