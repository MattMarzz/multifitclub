package it.uniroma2.dicii.ispw.model.utente.dao;

import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.exception.DbConnectionException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDBMS implements UtenteDAO{

    @Override
    public String insertUtente(Utente utente) throws ItemAlreadyExistsException {
        PreparedStatement statement = null;
        String res = "Errore imprevisto in inserimento.";
        try {
            String sql = " insert into utente (cf, nome, cognome, data_nascita, ruolo, email, password)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";

            statement = DbConnection.getConnection().prepareStatement(sql);
            statement.setString(1, utente.getCf());
            statement.setString(2, utente.getName());
            statement.setString(3, utente.getSurname());
            statement.setDate(4, new java.sql.Date(utente.getBirthDate().getTime()));
            statement.setInt(5, utente.getRuolo().ordinal());
            statement.setString(6, utente.getEmail());
            statement.setString(7, utente.getPassword());

            statement.execute();

            res = "Utente inserito correttamente!";

        } catch (SQLException e) {
            if(e.getErrorCode() == 1062)
                throw new ItemAlreadyExistsException("L'utente con cf: " + utente.getCf() + " esiste già");

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
    public Utente auth(LoginBean loginBean) throws ItemNotFoundException{
        Utente utente = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM utente WHERE email = ? AND password = ?";

            statement = DbConnection.getConnection().prepareStatement(query);
            statement.setString(1, loginBean.getEmail());
            statement.setString(2, loginBean.getPassword());

            resultSet = statement.executeQuery();
            if(!resultSet.next()) throw new ItemNotFoundException("Credenziali errate!");
            utente = setUtenteFromResultSet(resultSet);

        } catch (DbConnectionException e) {
            LoggerManager.logSevereException("Impossibile connettersi al db: ", e);
            return utente;
        } catch (SQLException e){
            LoggerManager.logSevereException("Errore SQL non previsto: ", e);;
            return utente;
        } finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) statement.close();
                DbConnection.closeConnection();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
        return utente;
    }

    @Override
    public Utente getUtenteByCf(String cf) throws ItemNotFoundException {
        Utente utente;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM utente WHERE cf=?";

            statement = DbConnection.getConnection().prepareStatement(sql);
            statement.setString(1, cf);
            resultSet = statement.executeQuery();

            if(!resultSet.next())
                throw new ItemNotFoundException("Non esiste utente con cf: " + cf);

            utente = setUtenteFromResultSet(resultSet);
        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore SQL non previsto. ", e);
            return null;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException("Impossibile connettersi al db: ", e);
            return null;
        } finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) resultSet.close();
                DbConnection.closeConnection();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
        return utente;
    }

    @Override
    public List<Utente> getAllUtenti() {
        List<Utente> users = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "SELECT * FROM utente";

            statement = DbConnection.getConnection().prepareStatement(sql);
            resultSet = statement.executeQuery();

            if(!resultSet.next())
                return users;

            do {
                Utente utente = setUtenteFromResultSet(resultSet);
                users.add(utente);
            } while (resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore nel reperire utenti. ", e);
            return users;
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException("Impossibile connettersi al db: ", e);
            return users;
        }
        finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) resultSet.close();
                DbConnection.closeConnection();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
        return users;
    }

    @Override
    public String editUtente(Utente utente) {
        PreparedStatement statement = null;
        String res = "Impossibile modificare l'utente.";
        try {
            String sql = " update utente set nome=?, cognome=?, data_nascita=?, ruolo=?, email=?"
                    + " where cf=?";

            statement = DbConnection.getConnection().prepareStatement(sql);
            statement.setString(1, utente.getName());
            statement.setString(2, utente.getSurname());
            statement.setDate(3, new java.sql.Date(utente.getBirthDate().getTime()));
            statement.setInt(4, utente.getRuolo().ordinal());
            statement.setString(5, utente.getEmail());
            statement.setString(6, utente.getCf());

            statement.execute();

            res = "L'Utente " + utente.getName() + " è stato modificato correttamente!";

        } catch (SQLException e) {
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
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
        return res;
    }

    private Utente setUtenteFromResultSet(ResultSet resultSet) throws SQLException {
        Utente utente = new Utente();
        utente.setName(resultSet.getString("nome"));
        utente.setCf(resultSet.getString("cf"));
        utente.setSurname(resultSet.getString("cognome"));
        utente.setBirthDate(resultSet.getDate("data_nascita"));
        utente.setEmail(resultSet.getString("email"));
        utente.setPassword(resultSet.getString("password"));
        switch (resultSet.getInt("ruolo")) {
            case 1:
                utente.setRuolo(Ruolo.ISTRUTTORE);
                break;
            case 2:
                utente.setRuolo(Ruolo.SEGRETERIA);
                break;
            default:
                utente.setRuolo(Ruolo.UTENTE);
                break;
        }
        return utente;
    }

}