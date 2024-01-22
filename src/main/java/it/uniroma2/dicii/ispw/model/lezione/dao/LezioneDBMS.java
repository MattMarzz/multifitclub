package it.uniroma2.dicii.ispw.model.lezione.dao;

import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.exception.DbConnectionException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.lezione.Lezione;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LezioneDBMS implements LezioneDAO{
    @Override
    public List<Lezione> getLezioniByCourseId(String nomeCorso) throws ItemNotFoundException, DbConnectionException {
        List<Lezione> lezioniList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM lezione WHERE corso=?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, nomeCorso);

            resultSet = statement.executeQuery();

            if(!resultSet.next())
                throw new ItemNotFoundException("Non esiste alcuna lezione per il corso: " + nomeCorso);

            do{
                Lezione l = new Lezione();
                l.setCfUtente(resultSet.getString("istruttore"));
                l.setDay(resultSet.getString("giorno"));
                l.setStartTime(resultSet.getTime("ora"));
                l.setCourseName(resultSet.getString("corso"));
                l.setSala(resultSet.getString("sala"));
                lezioniList.add(l);
            }while(resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore nel dialogo con il database.", e);
            return lezioniList;
        } finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) resultSet.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
        return lezioniList;
    }
}
