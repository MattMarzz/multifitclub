package it.uniroma2.dicii.ispw.utils;

import it.uniroma2.dicii.ispw.exception.DbConnectionException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static it.uniroma2.dicii.ispw.utils.ConstantMsg.ERROR_CLOSING_DB;

public class DbConnection {

    private static DbConnection instance = null;
    private Connection conn = null;

    private DbConnection(){}

    public Connection getConnection() throws DbConnectionException {
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

    public static synchronized DbConnection getInstance() {
        if (instance == null)
            instance = new DbConnection();
        return instance;
    }

    public void closeConnection() throws SQLException{
        if(conn != null && !conn.isClosed()){
            conn.close();
        }
    }

    public static void closeEverything(Statement st, ResultSet rs, boolean wantToCloseConn) {
        try {
            if (st != null) st.close();
            if (rs != null) rs.close();
            if(wantToCloseConn)
                DbConnection.getInstance().closeConnection();
        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_CLOSING_DB, e);
        }
    }

}