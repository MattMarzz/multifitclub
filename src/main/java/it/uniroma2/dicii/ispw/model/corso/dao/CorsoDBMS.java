package it.uniroma2.dicii.ispw.model.corso.dao;

import it.uniroma2.dicii.ispw.exception.DbConnectionException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.dicii.ispw.utils.ConstantMsg.*;


public class CorsoDBMS implements CorsoDAO {

    @Override
    public Corso getCorsoByNome(String nome) throws ItemNotFoundException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Corso corso = new Corso();
        try {
            String sql = "select * from corso where nome=?";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, nome);
            resultSet = statement.executeQuery();

            if(!resultSet.next()) throw new ItemNotFoundException("Nessun corso con nome: " + nome);

            corso.setName(resultSet.getString("nome"));
            corso.setStartDate(resultSet.getDate("data_inizio"));

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return null;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return null;
        } finally {
            DbConnection.closeEverything(statement, resultSet, true);
        }
        return corso;
    }

    @Override
    public List<Corso> getAllCorsi() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Corso> corsoList = new ArrayList<>();
        try {
            String sql = "select * from corso";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            resultSet = statement.executeQuery();

            if(!resultSet.next()) return corsoList;

            do {
                Corso c = new Corso(resultSet.getString("nome"), resultSet.getDate("data_inizio"));
                corsoList.add(c);
            } while (resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return corsoList;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return corsoList;
        }
        finally {
            DbConnection.closeEverything(statement, resultSet, true);
        }
        return corsoList;
    }

    @Override
    public void insertCorso(Corso corso) throws ItemAlreadyExistsException {
        PreparedStatement statement = null;
        try {
            String sql = "insert into corso(nome, data_inizio) values(?, ?)";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, corso.getName());
            statement.setDate(2, new java.sql.Date(corso.getStartDate().getTime()));
            statement.executeUpdate();

        }  catch (SQLException e) {
            if(e.getErrorCode() == 1062)
                throw new ItemAlreadyExistsException("L'utente con cf: " + corso.getName() + " esiste già");

            LoggerManager.logSevereException(ERROR_SQL, e);
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
        } finally {
            DbConnection.closeEverything(statement, null, true);
        }
    }

    @Override
    public void removeCorso(Corso corso) {
        PreparedStatement statement = null;

        try {
            String sql = "delete from corso where nome=? and data_inizio=?";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, corso.getName());
            statement.setDate(2, new java.sql.Date(corso.getStartDate().getTime()));
            statement.executeUpdate();

        }  catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
        }
        finally {
            DbConnection.closeEverything(statement, null, true);
        }
    }
}
