package it.uniroma2.dicii.ispw.models.lezione.dao;

import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.exceptions.DbConnectionException;
import it.uniroma2.dicii.ispw.exceptions.ItemNotFoundException;
import it.uniroma2.dicii.ispw.models.lezione.Lezione;

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
            conn = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM lezione WHERE corso=?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, nomeCorso);

            resultSet = statement.executeQuery();

            if(!resultSet.first())
                throw new ItemNotFoundException("Non esiste alcuna lezione per il corso: " + nomeCorso);

            do{
                Lezione l = new Lezione();
                l.setCfUtente(resultSet.getString("utente"));
                l.setDay(resultSet.getString("data"));
                l.setStartTime(resultSet.getTime("ora"));
                l.setCourseName(resultSet.getString("corso"));
                lezioniList.add(l);
            }while(resultSet.next());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) resultSet.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lezioniList;
    }
}
