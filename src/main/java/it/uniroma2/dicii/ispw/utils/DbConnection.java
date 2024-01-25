package it.uniroma2.dicii.ispw.utils;

import it.uniroma2.dicii.ispw.exception.DbConnectionException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
    private static Connection conn = null;

    protected DbConnection(){}

    public static Connection getConnection() throws DbConnectionException {
        try (InputStream input = DbConnection.class.getClassLoader().getResourceAsStream("application.properties")){
            if(conn == null || conn.isClosed()){
                if(input == null) throw new DbConnectionException("Configurazione della connessione al database non trovata!");
                else{
                    Properties properties = new Properties();
                    properties.load(input);

                    String dbUrl = properties.getProperty("db.url");
                    String dbUsr = properties.getProperty("db.user");
                    String dbPwd = properties.getProperty("db.password");

                    conn = DriverManager.getConnection(dbUrl, dbUsr, dbPwd);
                }
            }
        } catch (IOException | SQLException e) {
            throw new DbConnectionException("Errore di connessione al db: " + e.getMessage());
        }
        return conn;
    }

    public static void closeConnection() throws SQLException{
        if(conn != null && !conn.isClosed()){
            conn.close();
        }
    }
}