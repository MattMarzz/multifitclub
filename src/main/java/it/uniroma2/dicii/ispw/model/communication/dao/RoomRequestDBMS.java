package it.uniroma2.dicii.ispw.model.communication.dao;

import it.uniroma2.dicii.ispw.enums.RoomRequestStatus;
import it.uniroma2.dicii.ispw.exception.DbConnectionException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.communication.RoomRequest;
import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.dicii.ispw.utils.ConstantMsg.ERROR_OPENING_DB;
import static it.uniroma2.dicii.ispw.utils.ConstantMsg.ERROR_SQL;

public class RoomRequestDBMS implements RoomRequestDAO{
    @Override
    public String insertRoomRequest(RoomRequest roomRequest) throws ItemAlreadyExistsException {
        String res = "Impossibile inserire prenotazione";
        PreparedStatement statement = null;
        try {
            String sql = "insert into richiesta(titolo, testo, data, quando, status, utente, sala)" +
                    "values (?, ?, ?, ?, ?, ?, ?)";
            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, roomRequest.getTitle());
            statement.setString(2, roomRequest.getMsg());
            statement.setTimestamp(3, roomRequest.getDate());
            statement.setTimestamp(4, roomRequest.getWhen());
            statement.setInt(5, roomRequest.getStatus().ordinal());
            statement.setString(6, roomRequest.getSender());
            statement.setString(7, roomRequest.getRoom());
            statement.executeUpdate();
            res = "Prenotazione inviato correttamente";
        } catch (SQLException e) {
            if(e.getErrorCode() == 1062)
                throw new ItemAlreadyExistsException("Prenotazione esistente");

            LoggerManager.logSevereException(ERROR_SQL, e);
            return res;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return res;
        } finally {
            DbConnection.closeEverything(statement, null, true);
        }

        return res;
    }

    @Override
    public List<RoomRequest> getAllRequest() {
        List<RoomRequest> requestList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from richiesta";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            resultSet = statement.executeQuery();

            if (!resultSet.next()) return requestList;
            do {
                RoomRequest rr = setRoomRequestFromResultSet(resultSet);
                requestList.add(rr);
            } while (resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return requestList;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return requestList;
        } finally {
            DbConnection.closeEverything(statement, resultSet, true);
        }

        return requestList;
    }

    @Override
    public String requestResponse(RoomRequest roomRequest) throws ItemNotFoundException {
        RoomRequest rr = getRoomRequestById(roomRequest.getReqId());
        rr.setStatus(roomRequest.getStatus());
        PreparedStatement statement = null;
        try {
            String sql = "update richiesta set status=? where id=?";
            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);

            statement.setInt(1, rr.getStatus().ordinal());
            statement.setInt(2, rr.getReqId());

            statement.execute();
        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return null;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return null;
        } finally {
            DbConnection.closeEverything(statement, null, true);
        }
        return rr.getSender();
    }

    @Override
    public RoomRequest getRoomRequestById(int id) throws ItemNotFoundException {
        RoomRequest rr;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from richiesta where id = ?";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (!resultSet.next()) throw new ItemNotFoundException("Richiesta non trovata");

            rr = setRoomRequestFromResultSet(resultSet);

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return null;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return null;
        } finally {
            DbConnection.closeEverything(statement, resultSet, false);
        }
        return rr;
    }

    @Override
    public List<RoomRequest> getRoomRequestByUtente(String cf) {
        List<RoomRequest> requestList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from richiesta where utente=?";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, cf);
            resultSet = statement.executeQuery();

            if (!resultSet.next()) return requestList;
            do {
                RoomRequest rr = setRoomRequestFromResultSet(resultSet);
                requestList.add(rr);
            } while (resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return requestList;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return requestList;
        } finally {
            DbConnection.closeEverything(statement, resultSet, true);
        }
        return requestList;
    }

    public RoomRequest setRoomRequestFromResultSet(ResultSet resultSet) throws SQLException{
        RoomRequest rr = new RoomRequest();
        rr.setReqId(resultSet.getInt("id"));
        rr.setDate(resultSet.getTimestamp("data"));
        rr.setTitle(resultSet.getString("titolo"));
        rr.setMsg(resultSet.getString("testo"));
        rr.setSender(resultSet.getString("utente"));
        rr.setRoom(resultSet.getString("sala"));
        rr.setWhen(resultSet.getTimestamp("quando"));
        rr.setStatus(RoomRequestStatus.getStatus(resultSet.getInt("status")));
        return rr;
    }

}
