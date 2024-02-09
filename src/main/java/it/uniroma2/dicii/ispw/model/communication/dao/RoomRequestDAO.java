package it.uniroma2.dicii.ispw.model.communication.dao;

import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.communication.RoomRequest;

import java.util.List;

public interface RoomRequestDAO {
     public String insertRoomRequest(RoomRequest roomRequest) throws ItemAlreadyExistsException;
     public List<RoomRequest> getAllRequest();
     public String requestResponse(RoomRequest roomRequest) throws ItemNotFoundException;
     public RoomRequest getRoomRequestById(int id) throws ItemNotFoundException;
     public List<RoomRequest> getRoomRequestByUtente(String cf);
     public List<RoomRequest> getAllAcceptedRequest();
}
