package it.uniroma2.dicii.ispw;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
    private Connection conn = null;
    private static DbConnection instance = null;

    protected DbConnection(){
        try (InputStream input = DbConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if(input == null){
                System.out.println("Sorry, unable to find config.properties");
            }else{
                Properties properties = new Properties();
                properties.load(input);

                String dbUrl = properties.getProperty("db.url");
                String dbUsr = properties.getProperty("db.user");
                String dbPwd = properties.getProperty("db.password");

                this.conn = DriverManager.getConnection(dbUrl, dbUsr, dbPwd);
            }

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeDbConnection() throws SQLException {
        if(this.conn != null) conn.close();
    }

    public Connection getConn() {
        return conn;
    }

    public synchronized static DbConnection getDbConnectionInstance(){
        if(DbConnection.instance == null)
            DbConnection.instance = new DbConnection();
        return instance;
    }

}
