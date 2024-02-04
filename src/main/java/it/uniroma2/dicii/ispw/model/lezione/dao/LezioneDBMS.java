package it.uniroma2.dicii.ispw.model.lezione.dao;

import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.exception.DbConnectionException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.lezione.Lezione;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.dicii.ispw.utils.ConstantMsg.ERROR_OPENING_DB;
import static it.uniroma2.dicii.ispw.utils.ConstantMsg.ERROR_SQL;

public class LezioneDBMS implements LezioneDAO{
    @Override
    public List<Lezione> getLezioniByCourseId(String nomeCorso) throws ItemNotFoundException {
        List<Lezione> lezioniList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM lezione WHERE corso=?";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, nomeCorso);
            resultSet = statement.executeQuery();

            if(!resultSet.next())
                return lezioniList;

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
            LoggerManager.logSevereException(ERROR_SQL, e);
            return lezioniList;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return lezioniList;
        } finally {
            DbConnection.closeEverything(statement, resultSet);
        }
        return lezioniList;
    }
}
