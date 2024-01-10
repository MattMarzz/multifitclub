package it.uniroma2.dicii.ispw.utente.dao;

import it.uniroma2.dicii.ispw.DbConnection;
import it.uniroma2.dicii.ispw.exceptions.DbConnectionException;
import it.uniroma2.dicii.ispw.utente.Utente;

import java.sql.*;

public class UtenteDBMS implements UtenteDAO{

    @Override
    public void insertUtente(Utente utente) {

        try {
            DbConnection dbConnection = DbConnection.getDbConnectionInstance();
            Connection conn = dbConnection.getConn();

            String sql = " insert into utente (cf, nome, cognome, data_nascita)"
                    + " values (?, ?, ?, ?)";

            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setString (1, utente.getCf());
            preparedStmt.setString (2, utente.getName());
            preparedStmt.setString   (3, utente.getSurname());
            preparedStmt.setDate(4, new java.sql.Date(utente.getBirthDate().getTime()));

            preparedStmt.execute();

        } catch (SQLException | DbConnectionException e) {
            //TODO: return message
            throw new RuntimeException(e);
        }
    }

}
