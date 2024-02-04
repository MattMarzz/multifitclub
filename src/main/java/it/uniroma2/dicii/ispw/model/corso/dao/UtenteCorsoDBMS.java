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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.dicii.ispw.utils.ConstantMsg.*;

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
    public List<Corso> getCoursesByUserId(String cf, UserRoleInCourse userRoleInCourse) throws  ItemNotFoundException {
        List<Corso> courses = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            String sql = userRoleInCourse.equals(UserRoleInCourse.ENROLLMENT) ? "SELECT * FROM iscrizione WHERE utente=?" : "SELECT * FROM insegnato WHERE utente=?";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, cf);
            resultSet = statement.executeQuery();

            if(!resultSet.next())
                return courses;

            do{
                courses.add(getCourseById(resultSet.getString("corso")));
            }while(resultSet.next());

        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return courses;
        } catch (SQLException e){
            LoggerManager.logSevereException(ERROR_SQL, e);;
            return courses;
        } finally {
            DbConnection.closeEverything(statement, resultSet);
        }
        return courses;
    }

    @Override
    public List<Utente> getUsersByCourseId(String nomeCorso, UserRoleInCourse userRoleInCourse) throws ItemNotFoundException {
        List<Utente> utenteList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            String sql;
            sql = userRoleInCourse.equals(UserRoleInCourse.ENROLLMENT) ? "SELECT * FROM iscrizione WHERE corso=?" : "SELECT * FROM insegnato WHERE corso=?";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, nomeCorso);
            resultSet = statement.executeQuery();

            if(!resultSet.first())
                return utenteList;

            do{
                utenteList.add(utenteDAO.getUtenteByCf(resultSet.getString("utente")));
            }while(resultSet.next());

        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
            return utenteList;
        } catch (SQLException e){
            LoggerManager.logSevereException(ERROR_SQL, e);;
            return utenteList;
        } finally {
            DbConnection.closeEverything(statement, resultSet);
        }
        return utenteList;
    }

    @Override
    public void removeEnrollmentByUtente(Utente utente, Corso corso) {
        PreparedStatement statement = null;
        try{
            String sql = "delete from iscrizione WHERE corso=? and utente=?";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, corso.getName());
            statement.setString(2, utente.getCf());
            statement.executeUpdate();

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
        } finally {
            DbConnection.closeEverything(statement, null);
        }
    }

    @Override
    public void addEnrollmentToUtente(Utente utente, Corso corso){
        PreparedStatement statement = null;
        try{
            String sql = "insert into iscrizione(corso, utente) values(?, ?)";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, corso.getName());
            statement.setString(2, utente.getCf());
            statement.executeUpdate();

        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
        } catch (DbConnectionException e) {
            LoggerManager.logSevereException(ERROR_OPENING_DB, e);
        } finally {
            DbConnection.closeEverything(statement, null);
        }
    }

    public Corso getCourseById(String nomeCorso) throws DbConnectionException, ItemNotFoundException{
        Corso corso = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            String sql = "SELECT * FROM corso WHERE nome=?";

            statement = DbConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, nomeCorso);

            resultSet = statement.executeQuery();
            if (resultSet.next())
                corso = new Corso(resultSet.getString("nome"), resultSet.getDate("data_inizio"));
            else
                throw new ItemNotFoundException(ERROR_OPENING_DB + nomeCorso);
        } catch (SQLException e) {
            LoggerManager.logSevereException(ERROR_SQL, e);
            return corso;
        } finally {
//            DbConnection.closeEverything(statement, resulSet);
        }
        return corso;
    }
}