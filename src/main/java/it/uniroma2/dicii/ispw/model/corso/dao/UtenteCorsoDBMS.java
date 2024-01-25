package it.uniroma2.dicii.ispw.model.corso.dao;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.enums.UserRoleInCourse;
import it.uniroma2.dicii.ispw.exception.DbConnectionException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDBMS;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtenteCorsoDBMS implements UtenteCorsoDAO{
    private UtenteDAO utenteDAO;
    private CorsoDAO corsoDAO;

    public UtenteCorsoDBMS(){
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)){
            utenteDAO = new UtenteDBMS();
            //corsoDAO = new CorsoDBMS();
        }

//        else
//            utenteDAO = new UtentoFS();
    }


    @Override
    public List<Corso> getCoursesByUserId(String cf, UserRoleInCourse userRoleInCourse) throws DbConnectionException, ItemNotFoundException {
        List<Corso> courses = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            conn = DbConnection.getConnection();
            String sql;
            sql = userRoleInCourse.equals(UserRoleInCourse.ENROLLMENT) ? "SELECT * FROM iscrizione WHERE utente=?" : "SELECT * FROM insegnato WHERE utente=?";

            statement = conn.prepareStatement(sql);
            statement.setString(1, cf);
            resultSet = statement.executeQuery();

            if(!resultSet.next())
                return courses;

            do{
                courses.add(getCourseById(resultSet.getString("corso")));
            }while(resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore nel dialogo con il database.", e);
            return courses;
        } finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) resultSet.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
        return courses;
    }

    @Override
    public List<Utente> getUsersByCourseId(String nomeCorso, UserRoleInCourse userRoleInCourse) throws Exception {
        List<Utente> utenteList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            conn = DbConnection.getConnection();
            String sql;
            sql = userRoleInCourse.equals(UserRoleInCourse.ENROLLMENT) ? "SELECT * FROM iscrizione WHERE corso=?" : "SELECT * FROM insegnato WHERE corso=?";

            statement = conn.prepareStatement(sql);
            statement.setString(1, nomeCorso);
            resultSet = statement.executeQuery();

            if(!resultSet.first())
                return utenteList;

            do{
                utenteList.add(utenteDAO.getUtenteByCf(resultSet.getString("utente")));
            }while(resultSet.next());

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore nel dialogo con il database.", e);
            return utenteList;
        } finally {
            try {
                if(statement != null) statement.close();
                if(resultSet != null) resultSet.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
        return utenteList;
    }

    @Override
    public void removeEnrollmentByUtente(Utente utente, Corso corso) {
        PreparedStatement statement = null;
        try{
            String sql = "delete from iscrizione WHERE corso=? and utente=?";

            statement = DbConnection.getConnection().prepareStatement(sql);
            statement.setString(1, corso.getName());
            statement.setString(2, utente.getCf());
            statement.executeUpdate();

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore SQL non previsto: ", e);
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException("Impossibile connettersi al db: ", e);
        } finally {
            try {
                if (statement != null) statement.close();
                DbConnection.closeConnection();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
    }

    @Override
    public void addEnrollmentToUtente(Utente utente, Corso corso){
        PreparedStatement statement = null;
        try{
            String sql = "insert into iscrizione(corso, utente) values(?, ?)";

            statement = DbConnection.getConnection().prepareStatement(sql);
            statement.setString(1, corso.getName());
            statement.setString(2, utente.getCf());
            statement.executeUpdate();

        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore SQL non previsto: ", e);
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException("Impossibile connettersi al db: ", e);
        } finally {
            try {
                if (statement != null) statement.close();
                DbConnection.closeConnection();
            } catch (SQLException e) {
                LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            }
        }
    }

    public Corso getCourseById(String nomeCorso) throws DbConnectionException, ItemNotFoundException{
        Corso corso = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM corso WHERE nome=?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, nomeCorso);

            resultSet = statement.executeQuery();
            if (resultSet.next())
                corso = new Corso(resultSet.getString("nome"), resultSet.getDate("data_inizio"));
            else
                throw new ItemNotFoundException("Nessun corso trovato con nome: " + nomeCorso);
        } catch (SQLException e) {
            LoggerManager.logSevereException("Errore nel dialogo con il database.", e);
            return corso;
        } finally {
            //try {
            //    if(statement != null) statement.close();
            //    if(resultSet != null) resultSet.close();
            //    if(conn != null) conn.close();
            //} catch (SQLException e) {
            //    LoggerManager.logSevereException("Errore nella chiusura della connessione.", e);
            //}
        }
        return corso;
    }
}