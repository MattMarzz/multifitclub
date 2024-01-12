package it.uniroma2.dicii.ispw.models.corso.dao;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.utils.DbConnection;
import it.uniroma2.dicii.ispw.enums.TypersOfPersistenceLayer;
import it.uniroma2.dicii.ispw.enums.UserRoleInCourse;
import it.uniroma2.dicii.ispw.exceptions.DbConnectionException;
import it.uniroma2.dicii.ispw.exceptions.ItemNotFoundException;
import it.uniroma2.dicii.ispw.models.corso.Corso;
import it.uniroma2.dicii.ispw.models.utente.Utente;
import it.uniroma2.dicii.ispw.models.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.models.utente.dao.UtenteDBMS;

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
        if(App.getPersistenceLayer().equals(TypersOfPersistenceLayer.JDBC)){
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
            conn = DbConnection.getInstance().getConnection();
            String sql;
            sql = userRoleInCourse.equals(UserRoleInCourse.ENROLLMENT) ? "SELECT * FROM iscrizione WHERE utente=?" : "SELECT * FROM insegnato WHERE utente=?";

            statement = conn.prepareStatement(sql);
            statement.setString(1, cf);
            resultSet = statement.executeQuery();

            if(!resultSet.first())
                throw new ItemNotFoundException("Non esiste alcun corso per l'utente con c.f.: " + cf);

            do{
                courses.add(getCourseById(resultSet.getString("corso")));
            }while(resultSet.next());

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
        return courses;
    }

    @Override
    public List<Utente> getUsersByCourseId(String nomeCorso, UserRoleInCourse userRoleInCourse) throws Exception {
        List<Utente> utenteList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            conn = DbConnection.getInstance().getConnection();
            String sql;
            sql = userRoleInCourse.equals(UserRoleInCourse.ENROLLMENT) ? "SELECT * FROM iscrizione WHERE corso=?" : "SELECT * FROM insegnato WHERE corso=?";

            statement = conn.prepareStatement(sql);
            statement.setString(1, nomeCorso);
            resultSet = statement.executeQuery();

            if(!resultSet.first())
                throw new ItemNotFoundException("Non esiste alcun utente per il corso: " + nomeCorso);

            do{
                utenteList.add(utenteDAO.getUtenteById(resultSet.getString("utente")));
            }while(resultSet.next());

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
        return utenteList;
    }

    public Corso getCourseById(String nomeCorso) throws DbConnectionException, ItemNotFoundException{
        Corso corso = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            conn = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM corso WHERE nome=?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, nomeCorso);

            resultSet = statement.executeQuery();
            if (resultSet.next())
                corso = new Corso(resultSet.getString("nome"), resultSet.getDate("data_inizio"));
            else
                throw new ItemNotFoundException("Nessun corso trovato con nome: " + nomeCorso);
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
        return corso;
    }
}