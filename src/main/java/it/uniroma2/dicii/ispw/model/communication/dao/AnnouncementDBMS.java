package it.uniroma2.dicii.ispw.model.communication.dao;

import it.uniroma2.dicii.ispw.exception.DbConnectionException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.model.communication.Announcement;
import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.dicii.ispw.utils.ConstantMsg.ERROR_OPENING_DB;
import static it.uniroma2.dicii.ispw.utils.ConstantMsg.ERROR_SQL;

public class AnnouncementDBMS implements AnnouncementDAO{
    @Override
    public String insertAnnouncement(Announcement announcement) throws ItemAlreadyExistsException {
        String res = "Impossibile inserire annuncio";
        PreparedStatement statement = null;
        try {
            String sql = "insert into annuncio(titolo, testo, data, utente)" +
                    "values (?, ?, ?, ?)";
            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, announcement.getTitle());
            statement.setString(2, announcement.getMsg());
            statement.setTimestamp(3, announcement.getDate());
            statement.setString(4, announcement.getSender());
            statement.executeUpdate();
            res = "Annuncio inviato correttamente";
        } catch (SQLException e) {
            if(e.getErrorCode() == 1062)
                throw new ItemAlreadyExistsException("Annuncio esistente");

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
    public List<Announcement> getAllAnnouncement() {
        List<Announcement> announcementList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from annuncio";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            resultSet = statement.executeQuery();

            if (!resultSet.next()) return announcementList;
            do {
                Announcement a = new Announcement();
                a.setAnnId(resultSet.getInt("id"));
                a.setDate(resultSet.getTimestamp("data"));
                a.setTitle(resultSet.getString("titolo"));
                a.setMsg(resultSet.getString("testo"));
                a.setSender(resultSet.getString("utente"));

                announcementList.add(a);
            } while (resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return announcementList;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return announcementList;
        } finally {
            DbConnection.closeEverything(statement, resultSet, true);
        }

        return announcementList;
    }

}
