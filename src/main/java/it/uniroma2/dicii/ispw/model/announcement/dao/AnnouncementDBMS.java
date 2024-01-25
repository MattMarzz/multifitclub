package it.uniroma2.dicii.ispw.model.announcement.dao;

import it.uniroma2.dicii.ispw.exception.DbConnectionException;
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
    public String insertAnnouncement(Announcement announcement) throws DbConnectionException {
        String res = "Impossibile inserire annuncio";
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "insert into annuncio(titolo, testo, data, utente)" +
                    "values (?, ?, ?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, announcement.getTitle());
            statement.setString(2, announcement.getText());
            statement.setTimestamp(3, announcement.getDate());
            statement.setString(4, announcement.getSender());
            statement.executeUpdate();
            res = "Annuncio inviato correttamente";
        } catch (SQLException e) {
            LoggerManager.logInfoException("Impossibile inserire annuncio", e);
            return res;
        }

        return res;
    }

    @Override
    public List<Announcement> getAllAnnouncement() throws DbConnectionException {
        List<Announcement> announcementList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "select * from annuncio";
            statement = conn.prepareStatement(sql);
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
            LoggerManager.logSevereException("Errore nel reperire la lista degli annunci ", e);
            return announcementList;
        }

        return announcementList;
    }
}
