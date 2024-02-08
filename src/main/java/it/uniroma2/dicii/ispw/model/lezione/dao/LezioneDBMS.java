package it.uniroma2.dicii.ispw.model.lezione.dao;

import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.model.utente.Utente;
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
    public List<Lezione> getLezioniByCourseId(String nomeCorso) {
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
                Lezione l = setLezioneFromResultSet(resultSet);
                lezioniList.add(l);
            }while(resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return lezioniList;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return lezioniList;
        } finally {
            DbConnection.closeEverything(statement, resultSet, true);
        }
        return lezioniList;
    }

    @Override
    public List<Lezione> getAllLezioniForDay(String giorno) {
        List<Lezione> lezioniList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM lezione WHERE giorno=?";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, giorno);
            resultSet = statement.executeQuery();

            if(!resultSet.next())
                return lezioniList;

            do{
               Lezione l = setLezioneFromResultSet(resultSet);
               lezioniList.add(l);
            }while(resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return lezioniList;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return lezioniList;
        } finally {
            DbConnection.closeEverything(statement, resultSet, true);
        }
        return lezioniList;
    }

    @Override
    public String insertLezioni(List<Lezione> lezioneList) throws ItemAlreadyExistsException{
        PreparedStatement statement = null;
        try{
            String sql = "INSERT INTO lezione(giorno, ora, sala, corso, istruttore) VALUES(?,?,?,?,?)";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);

            for (Lezione l: lezioneList) {
                statement.setString(1, l.getDay());
                statement.setTime(2, l.getStartTime());
                statement.setString(3, l.getSala());
                statement.setString(4, l.getCourseName());
                statement.setString(5, l.getCfUtente());
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException e) {
            if(e.getErrorCode() == 1062)
                throw new ItemAlreadyExistsException("Stai tentando di inserire una lezione già esistente!");
            LoggerManager.logSevereException(ERROR_SQL, e);
            return "Impossibile inserire attività. ";
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return "Impossibile inserire attività. ";
        } finally {
            DbConnection.closeEverything(statement, null, true);
        }
        return "Attività ierite correttamente!";
    }

    @Override
    public List<Lezione> getAllLezioni() {
        List<Lezione> lezioniList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM lezione ORDER BY corso";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            resultSet = statement.executeQuery();

            if(!resultSet.next())
                return lezioniList;

            do{
                Lezione l = setLezioneFromResultSet(resultSet);
                lezioniList.add(l);
            }while(resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return lezioniList;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return lezioniList;
        } finally {
            DbConnection.closeEverything(statement, resultSet, true);
        }
        return lezioniList;
    }


    private Lezione setLezioneFromResultSet(ResultSet resultSet) throws SQLException {
        Lezione lezione = new Lezione();
        lezione.setCfUtente(resultSet.getString("istruttore"));
        lezione.setDay(resultSet.getString("giorno"));
        lezione.setStartTime(resultSet.getTime("ora"));
        lezione.setCourseName(resultSet.getString("corso"));
        lezione.setSala(resultSet.getString("sala"));
        return lezione;
    }
}
