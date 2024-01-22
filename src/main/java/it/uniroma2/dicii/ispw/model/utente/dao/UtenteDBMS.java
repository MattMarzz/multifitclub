package it.uniroma2.dicii.ispw.model.utente.dao;

import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
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
    public String insertUtente(Utente utente) throws DbConnectionException{
        Connection conn = null;
        PreparedStatement statement = null;
        String res = null;
        try {
            conn = DbConnection.getConnection();
            String sql = " insert into utente (cf, nome, cognome, data_nascita, ruolo, email, password)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";

            statement = conn.prepareStatement(sql);
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
            res = "Errore nell'inserimento del nuovo utente";
            LoggerManager.logSevereException("Errore SQL non previsto!", e);
            return res;
        } finally {
            try {
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione", e);
            }
        }
        return res;
    }

    @Override
    public Utente auth(LoginBean loginBean) throws ItemNotFoundException{
        Utente utente = new Utente();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DbConnection.getConnection();

            String query = "SELECT * FROM utente WHERE email = ? AND password = ?";

            statement = conn.prepareStatement(query);
            statement.setString(1, loginBean.getEmail());
            statement.setString(2, loginBean.getPassword());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                utente = setUtenteFromResultSet(resultSet);
            } else throw new ItemNotFoundException("Credenziali errate!");
        } catch (DbConnectionException e) {
            //connection failed
            LoggerManager.logSevereException("Errore di connessione al database.", e);
            return utente;
        } catch (SQLException e){
            //sql exception
            LoggerManager.logSevereException("Errore nel dialogo con il database.", e);
            return utente;
        } finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) statement.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
        return utente;
    }

    @Override
    public Utente getUtenteById(String cf) throws DbConnectionException, ItemNotFoundException {
        Utente utente = new Utente();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM utente WHERE cf=?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, cf);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                utente = setUtenteFromResultSet(resultSet);
            } else
                throw new ItemNotFoundException("Nessun utente trovato con cf: " + cf);
        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore nel dialogo con il database.", e);
            return utente;
        } finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) resultSet.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
        return utente;
    }

    @Override
    public List<Utente> getAllUtenti() throws DbConnectionException, ItemNotFoundException{
        List<Utente> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM utente";

            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();

            if(!resultSet.next())
                throw new ItemNotFoundException("Non è presente nessun utente.");

            do {
                Utente utente = setUtenteFromResultSet(resultSet);
                users.add(utente);
            } while (resultSet.next());


        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore nel dialogo con il database.", e);
            return users;
        }finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) resultSet.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
        return users;
    }

    @Override
    public String editUtente(Utente utente) throws Exception {
        Connection conn = null;
        PreparedStatement statement = null;
        String res = null;
        try {
            conn = DbConnection.getConnection();
            String sql = " update utente set nome=?, cognome=?, data_nascita=?, ruolo=?, email=?"
                    + " where cf=?";

            statement = conn.prepareStatement(sql);
            statement.setString(1, utente.getName());
            statement.setString(2, utente.getSurname());
            statement.setDate(3, new java.sql.Date(utente.getBirthDate().getTime()));
            statement.setInt(4, utente.getRuolo().ordinal());
            statement.setString(5, utente.getEmail());
            statement.setString(6, utente.getCf());

            statement.execute();

            res = "L'Utente" + utente.getName() + "è stato modificato correttamente!";

        } catch (SQLException e) {
            res = "Errore nell'inserimento del nuovo utente";
            LoggerManager.logSevereException("Errore SQL non previsto!", e);
            return res;
        } finally {
            try {
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione", e);
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
            case 0:
                utente.setRuolo(Ruolo.UTENTE);
                break;
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