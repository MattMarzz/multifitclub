package it.uniroma2.dicii.ispw.models.utente.dao;

import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.exceptions.DbConnectionException;
import it.uniroma2.dicii.ispw.exceptions.ItemNotFoundException;
import it.uniroma2.dicii.ispw.models.utente.Utente;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.*;

public class UtenteDBMS implements UtenteDAO{

    @Override
    public String insertUtente(Utente utente) throws DbConnectionException{
        Connection conn = null;
        PreparedStatement statement = null;
        String res = null;
        try {
            conn = DbConnection.getInstance().getConnection();
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
    public Utente auth(UtenteBean utenteBean) throws ItemNotFoundException{
        Utente utente = new Utente();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DbConnection.getInstance().getConnection();

            String query = "SELECT * FROM utente WHERE email = ? AND password = ?";

            statement = conn.prepareStatement(query);
            statement.setString(1, utenteBean.getEmail());
            statement.setString(2, utenteBean.getPassword());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
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
            } else throw new ItemNotFoundException("Accesso fallito!");
        } catch (DbConnectionException e) {
            //connection failed
            e.printStackTrace();
        } catch (SQLException e){
            //sql exception
            e.printStackTrace();
        } finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) statement.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return utente;
    }

    @Override
    public Utente getUtenteById(String cf) throws DbConnectionException, ItemNotFoundException {
        Utente utente = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            conn = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM utente WHERE cf=?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, cf);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                utente = new Utente();
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
            } else
                throw new ItemNotFoundException("Nessun utente trovato con cf: " + cf);
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
        return utente;
    }

}
