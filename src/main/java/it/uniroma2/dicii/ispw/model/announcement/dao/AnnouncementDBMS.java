package it.uniroma2.dicii.ispw.model.announcement.dao;

import it.uniroma2.dicii.ispw.exception.DbConnectionException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.model.announcement.Announcement;
import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDBMS implements AnnouncementDAO{
    @Override
    public String insertAnnouncement(Announcement announcement) throws ItemAlreadyExistsException {
        String res = "Impossibile inserire annuncio";
        PreparedStatement statement = null;
        try {
            String sql = "insert into annuncio(titolo, testo, data, utente)" +
                    "values (?, ?, ?, ?)";
            statement = DbConnection.getConnection().prepareStatement(sql);
            statement.setString(1, announcement.getTitle());
            statement.setString(2, announcement.getText());
            statement.setTimestamp(3, announcement.getDate());
            statement.setString(4, announcement.getSender());
            statement.executeUpdate();
            res = "Annuncio inviato correttamente";
        } catch (SQLException e) {
            if(e.getErrorCode() == 1062)
                throw new ItemAlreadyExistsException("Annuncio esistente");

            LoggerManager.logSevereException("Errore SQL non previsto: ", e);
            return res;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException("Impossibile connettersi al db: ", e);
            return res;
        } finally {
            try {
                if (statement != null) statement.close();
                DbConnection.closeConnection();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione", e);
            }
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

            statement = DbConnection.getConnection().prepareStatement(sql);
            resultSet = statement.executeQuery();

            if(!resultSet.next()) return announcementList;
            do {
                Announcement a = new Announcement();
                a.setId(resultSet.getInt("id"));
                a.setDate(resultSet.getTimestamp("data"));
                a.setTitle(resultSet.getString("titolo"));
                a.setText(resultSet.getString("testo"));
                a.setSender(resultSet.getString("utente"));

                announcementList.add(a);
            } while (resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore SQL non previsto: ", e);
            return announcementList;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException("Impossibile connettersi al db: ", e);
            return announcementList;
        } finally {
            try {
                if (statement != null) statement.close();
                if(resultSet != null) resultSet.close();
                DbConnection.closeConnection();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione", e);
            }
        }

        return announcementList;
    }
}
